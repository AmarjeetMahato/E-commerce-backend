package com.springboot.SpringSecurityConfig;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final   JwtFilter jwtAuthFilter;
    private  final JwtService jwtService;
    private final AuthenticationProvider authenticationProvider;


    @Bean
     public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws  Exception{

         httpSecurity.authorizeHttpRequests(req -> req
                 .requestMatchers(
                         "/swagger-ui/index.html",  // Swagger UI access
                         "/swagger-ui/**",          // Swagger UI access
                         "/v3/api-docs/**",         // OpenAPI docs
                         "/swagger-ui.html",        // Swagger UI main page
                         "/api/v1/auth/**"          // Allow authentication APIs
                 ).permitAll()
                 .anyRequest().authenticated()
         );
         httpSecurity.sessionManagement(session-> session.sessionCreationPolicy(
                SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                 .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

    httpSecurity.csrf(AbstractHttpConfigurer::disable);
       return httpSecurity.build();
      }
}
