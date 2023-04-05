package tn.esprit.gateway;

import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.result.method.annotation.ResponseBodyResultHandler;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
public class LoggingGatewayFilterFactory extends AbstractGatewayFilterFactory<LoggingGatewayFilterFactory.Config> {
    final Logger logger = LoggerFactory.getLogger(LoggingGatewayFilterFactory.class);
    public LoggingGatewayFilterFactory(){
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            // Pre-processing
            if (!config.isAuthenticated()) {
                logger.info("Pre GatewayFilter logging: "
                        + config.getBaseMessage());
                return chain.filter(exchange)
                        .then(Mono.fromRunnable(() -> {
                            // Post-processing
                            if (config.isPostLogger()) {
                                logger.info("Post GatewayFilter logging: "
                                        + config.getBaseMessage());
                            }
                        }));
            }

            else {
                var authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
                logger.info("Auth header is: {}", authHeader);
                if(authHeader != null){
                    var client = HttpClient.newHttpClient();
                    var request = HttpRequest.newBuilder(
                            URI.create("http://localhost:8083/api"))
                            .header("Authorization", authHeader)
                            .build();
                    try{
                        var res = client.send(request, new JsonBodyHandler<>(Response.class)).body().get();
                        logger.info(res.getEmail());
                        var req = exchange.getRequest();
                        exchange.getRequest().mutate().headers(h->h.add("id", res.getId().toString()));
                        exchange.getRequest().mutate().headers(h->h.add("email", res.getEmail().toString()));
                        exchange.getRequest().mutate().headers(h -> h.add("authorities", res.getUserRoles().toString()));
                        return chain.filter(exchange);
                    }catch (Exception e){
                        logger.info("Error: {}", e.getMessage());
                    }
                }else{
                    logger.info("Not Executed");
                }
            }
            logger.info("skipped to default no authed response");
            exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
            return exchange.getResponse().setComplete();

        };
    }



    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Config {
        private String baseMessage;
        private boolean postLogger;
        private boolean authenticated;

    }
}
