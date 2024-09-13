package config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity serverHttpSecurity){
        serverHttpSecurity
                .csrf(ServerHttpSecurity.CsrfSpec::disable)  // Updated way to disable CSRF
                .authorizeExchange(exchange -> exchange
                        .pathMatchers("/eureka/**").permitAll()  // Permit access to Eureka endpoints
                        .anyExchange().authenticated())  // All other endpoints need to be authenticated
                .oauth2ResourceServer(ServerHttpSecurity.OAuth2ResourceServerSpec::jwt);  // Configure JWT as the resource server

        return serverHttpSecurity.build();
    }
}

