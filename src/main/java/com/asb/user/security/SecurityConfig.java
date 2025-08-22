package com.asb.user.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity()
public class SecurityConfig {


    // config
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry ->
                        authorizationManagerRequestMatcherRegistry

                                .requestMatchers(HttpMethod.POST, "/api/v1/back-user-service/user/login").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/v1/back-user-service/user/create").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/v1/back-user-service/version").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/v1/back-user-service/user").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/v1/back-user-service/user/**").permitAll()

                                .requestMatchers(HttpMethod.GET, "/api/v1/back-user-service/swagger-ui").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/v1/back-user-service/swagger-ui/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/v1/back-user-service/swagger-ui/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/v1/back-user-service/swagger-ui**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/v1/back-user-service/swagger-resources/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/v1/back-user-service/swagger-ui").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/v1/back-user-service/swagger-ui-custom.html").permitAll()

                                .requestMatchers("/version/**").permitAll()
                                //.requestMatchers(HttpMethod.DELETE).hasRole("ADMIN")
                                //.requestMatchers("/admin/**").hasAnyRole("ADMIN")
                                //.requestMatchers("/user/**").hasAnyRole("USER", "ADMIN")
                                //.requestMatchers("/login/**").permitAll()
                                .anyRequest().permitAll()
                )
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

}