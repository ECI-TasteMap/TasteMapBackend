package com.eci.edu.ieti.tastemap.user.config;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.source.JWKSourceBuilder;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.net.URL;
import java.util.Arrays;
import java.util.List;

@Configuration
public class SecurityConfig {

    @Value("${app.cors.allowed-origins}")
    private String[] allowedOrigins;

    @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
    private String jwkSetUri;

    @Bean
    public JwtDecoder jwtDecoder() {
        try {
            var jwkSource = JWKSourceBuilder
                    .create(new URL(jwkSetUri))
                    .build();

            var jwtProcessor = new DefaultJWTProcessor<SecurityContext>();
            jwtProcessor.setJWSKeySelector(
                    new JWSVerificationKeySelector<>(JWSAlgorithm.ES256, jwkSource));

            return new NimbusJwtDecoder(jwtProcessor);
        } catch (Exception e) {
            throw new RuntimeException("Error configurando JWT decoder", e);
        }
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/users").permitAll()
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/v3/api-docs/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/ai/history").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/ai/history").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/v1/users/*/chat").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/v1/restaurants", "/api/v1/restaurants/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/v1/reviews", "/api/v1/reviews/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/v1/restaurants").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/v1/restaurants/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/restaurants/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/v1/reviews").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/v1/reviews/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/reviews/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/v1/users/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/users/**").authenticated()
                        .anyRequest().authenticated())
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.decoder(jwtDecoder())));

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Arrays.asList(allowedOrigins));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}