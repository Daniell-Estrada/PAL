package com.example.pal.config;

import java.util.Arrays;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.cors(cors -> cors.configurationSource(corsConfigurationSource()))
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(
            auth ->
                auth
                    // Endpoints públicos
                    .requestMatchers(
                        "/api-docs/**",
                        "/swagger-ui.html",
                        "/swagger-ui/**",
                        "/v3/api-docs/**",
                        "/auth/**")
                    .permitAll()

                    // Endpoints para administradores
                    .requestMatchers("/api/users/**")
                    // .hasRole("ADMIN")
                    .permitAll()

                    // Endpoints para instructores
                    .requestMatchers("/api/courses/**", "/api/content/**", "/api/reports/**")
                    //            .hasAnyRole("ADMIN", "INSTRUCTOR")
                    .permitAll()

                    // Endpoints para estudiantes
                    .requestMatchers(
                        "/api/enrollments/**",
                        "/api/exams/submit/**",
                        "/api/exams/results/**",
                        "/api/certificates/**")
                    // .hasAnyRole("ADMIN", "INSTRUCTOR", "ESTUDIANTE")
                    .permitAll()

                    // Endpoints de lectura generales
                    .requestMatchers(HttpMethod.GET, "/api/courses/**", "/api/categories/**")
                    .permitAll()

                    // Por defecto, requerir autenticación
                    .anyRequest()
                    .authenticated())
        .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()));

    return http.build();
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    // Allow specific origin instead of wildcard
    configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173"));
    configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    configuration.setAllowedHeaders(Arrays.asList("*"));
    configuration.setAllowCredentials(true); // Allow credentials
    configuration.setExposedHeaders(Arrays.asList("x-auth-token"));

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }

  @Bean
  public InMemoryUserDetailsManager userDetailsService(PasswordEncoder passwordEncoder) {
    UserDetails admin =
        User.builder()
            .username("admin")
            .password(passwordEncoder.encode("admin123"))
            .roles("ADMIN")
            .build();

    UserDetails instructor =
        User.builder()
            .username("instructor")
            .password(passwordEncoder.encode("instructor123"))
            .roles("INSTRUCTOR")
            .build();

    UserDetails student =
        User.builder()
            .username("student")
            .password(passwordEncoder.encode("student123"))
            .roles("ESTUDIANTE")
            .build();

    return new InMemoryUserDetailsManager(admin, instructor, student);
  }
}
