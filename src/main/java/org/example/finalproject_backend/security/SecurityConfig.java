package org.example.finalproject_backend.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsService customUserDetailsService;
    private final JwtRequestFilter jwtRequestFilter;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/book/saveBookStandard").permitAll()  // Allow public access
                        .requestMatchers("/api/book/getBooksDto").permitAll()
                        .requestMatchers("/api/book/saveBookAsAdmin").hasRole("ADMIN")
                        .requestMatchers("/api/book/deleteBookAsAdmin").hasRole("ADMIN")
                        .requestMatchers("/api/review/getReviewsByBook").permitAll()
                        .requestMatchers("/api/review/getAllReviewsAsAdmin").hasRole("ADMIN") ////
                        .requestMatchers("/api/review/saveReview").hasRole("USER")
                        .requestMatchers("/api/review/deleteReviewAsAdmin").hasRole("ADMIN")
                        .requestMatchers("/api/review/deleteReviewAsUser").hasRole("USER")
                        .requestMatchers("/api/review/updateReviewAsUser").hasRole("USER")
                        .requestMatchers("/api/review/getUserReviews").hasRole("USER")
                        .requestMatchers("/api/review/getReviewById").hasRole("USER")
                        .requestMatchers("/api/user/deleteUserAsAdmin").hasRole("ADMIN")
                        .requestMatchers("/api/user/getAllUsersAsAdmin").hasRole("ADMIN")
                        .requestMatchers("/api/user/delete").hasRole("USER")
                        .anyRequest().authenticated()   // Other requests require authentication
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}