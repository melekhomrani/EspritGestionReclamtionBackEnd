package tn.esprit.authService.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${server.trustedPort:null}")
    private String trustedPort;

    @Value("${server.trustedPathPrefix:null}")
    private String trustedPathPrefix;

    @Bean
    public FilterRegistrationBean<TrustedEndpointFilter> trustedEndpointsFilter() {
        return new FilterRegistrationBean<>(new TrustedEndpointFilter(trustedPort, trustedPathPrefix));
    }
}
