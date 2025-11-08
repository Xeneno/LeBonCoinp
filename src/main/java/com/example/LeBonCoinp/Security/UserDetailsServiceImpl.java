package com.example.LeBonCoinp.Security;

import com.example.LeBonCoinp.Users.UserRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
  private final UserRepository repo;
  public UserDetailsServiceImpl(UserRepository repo) { this.repo = repo; }

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    var u = repo.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    return org.springframework.security.core.userdetails.User
        .withUsername(u.getEmail())
        .password(u.getPassword())
        .roles("USER")
        .build();
        // we do this because spring security only knows about UserDetails, not our User entity
  }
}
