package project.voting.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsUtils;
import project.voting.security.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> {})
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()

                        // ✅ Public endpoints
                        .requestMatchers("/api/voters/login", "/api/voters/register").permitAll()
                        .requestMatchers("/api/voters/settings/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/candidates/**").permitAll()

                        // 🔓 Admin endpoints (Token is still verified by jwtAuthFilter)
                        .requestMatchers("/api/admin/**", "/api/voters/admin/**").permitAll()

                        // 🔒 Secure everything else
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}