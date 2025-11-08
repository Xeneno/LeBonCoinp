package com.example.LeBonCoinp.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {
  private final JwtAuthFilter jwtFilter;
  private final UserDetailsService uds;

  public SecurityConfig(JwtAuthFilter jwtFilter, UserDetailsService uds) {
    this.jwtFilter = jwtFilter; this.uds = uds;
  }

  @Bean PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }

  // Spring builds AuthenticationManager from AuthenticationConfiguration using your UDS + encoder
  @Bean AuthenticationManager authenticationManager(AuthenticationConfiguration cfg) throws Exception {
    return cfg.getAuthenticationManager();
  }

  @Bean
  SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
      .csrf(csrf -> csrf.disable()) // we're not using cookies for session
      .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // so the server doesn't store anything about sessions
      .authorizeHttpRequests(auth -> auth
        .requestMatchers("/api/auth/**", "/h2-console/**").permitAll() // auth endpoints and h2-console are public
        .anyRequest().authenticated() // all other endpoints require authentication
      )
      .headers(h -> h.frameOptions(f -> f.sameOrigin())) // allow framing from same origin (for h2-console)
     
      .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class); // make sure JWT filter is applied before username/password filter
    return http.build();
  }
}
