package com.example.LeBonCoinp.Authentication;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class AuthDtos {
  public record RegisterRequest(@NotBlank String username, @Email String email, @NotBlank String password) {}
  public record LoginRequest(@Email String email, @NotBlank String password) {}
  public record AuthResponse(String accessToken) {}
}
