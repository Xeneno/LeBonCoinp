package com.example.LeBonCoinp.Security;

import com.example.LeBonCoinp.Users.User;
import com.example.LeBonCoinp.Users.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
  private final JwtService jwt;
  private final UserRepository users;

  public JwtAuthFilter(JwtService jwt, UserRepository users) {
    this.jwt = jwt; this.users = users;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
      throws ServletException, IOException {

    String header = req.getHeader(HttpHeaders.AUTHORIZATION);
    if (header != null && header.startsWith("Bearer ")) {
      String token = header.substring(7); // remove bearer part
      if (jwt.isValid(token)) {
        String email = jwt.subject(token);
        User u = users.findByEmail(email).orElse(null);
        // SecurityContextHolder.getContext().getAuthentication() == null to avoid overwriting existing auth
        if (u != null && SecurityContextHolder.getContext().getAuthentication() == null) {
          var auth = new UsernamePasswordAuthenticationToken(
              u.getEmail(), null, List.of(new SimpleGrantedAuthority("ROLE_USER")));
          auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
          SecurityContextHolder.getContext().setAuthentication(auth);
        }
      }
    }
    chain.doFilter(req, res);
  }
}
