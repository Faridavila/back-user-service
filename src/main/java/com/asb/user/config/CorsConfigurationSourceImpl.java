package com.asb.user.config;

import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.List;

@Configuration
public class CorsConfigurationSourceImpl implements CorsConfigurationSource {

    @Override
    public CorsConfiguration getCorsConfiguration(@NonNull HttpServletRequest request) {
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        // Headers permitidos
        corsConfiguration.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type"));

        // IMPORTANTE: Permite TODOS los orígenes de tu red local
        // Esto incluye localhost Y las IPs de la red (como 192.168.1.40)
        corsConfiguration.setAllowedOriginPatterns(List.of(
                "http://localhost:*",      // Localhost en cualquier puerto
                "http://127.0.0.1:*",      // Loopback
                "http://192.168.*:*",      // Toda tu red local 192.168.x.x
                "http://10.0.*:*"          // Otras redes locales comunes
        ));

        // Métodos HTTP permitidos
        corsConfiguration.setAllowedMethods(List.of(
                "GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"
        ));

        // Permite credenciales (cookies, headers de autorización)
        corsConfiguration.setAllowCredentials(true);

        // Headers expuestos en la respuesta
        corsConfiguration.setExposedHeaders(List.of(
                "Authorization",
                "Access-Control-Allow-Origin",
                "Access-Control-Allow-Credentials"
        ));

        return corsConfiguration;
    }
}