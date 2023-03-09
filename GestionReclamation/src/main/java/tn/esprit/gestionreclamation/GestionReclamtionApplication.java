package tn.esprit.gestionreclamation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class GestionReclamtionApplication {

    public static void main(String[] args) {

        SpringApplication.run(GestionReclamtionApplication.class, args);
    }

    @Bean
    public WebMvcConfigurer corsConfig() {
        return new WebMvcConfigurer() {
            protected void configure(HttpSecurity http) throws Exception {
                http.csrf().disable();
            }

            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**");
            }
        };
    }

}
