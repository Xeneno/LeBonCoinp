package com.example.LeBonCoinp.Authentication;
import com.example.LeBonCoinp.Users.User;
import com.example.LeBonCoinp.Users.UserDTO.UserResponseDTO;
import com.example.LeBonCoinp.Users.UserRepository;
import com.example.LeBonCoinp.Security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import static com.example.LeBonCoinp.Authentication.AuthDtos.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
  private final UserRepository users;
  private final PasswordEncoder encoder;
  private final AuthenticationManager authMgr;
  private final JwtService jwt;

  public AuthController(UserRepository users, PasswordEncoder encoder,
                        AuthenticationManager authMgr, JwtService jwt) {
    this.users = users; this.encoder = encoder; this.authMgr = authMgr; this.jwt = jwt;
  }

  @PostMapping("/register")
  public ResponseEntity<AuthResponse> register(@RequestBody @Valid RegisterRequest req) {
    if (users.findByEmail(req.email()).isPresent() || users.findByUsername(req.username()).isPresent()) {
      return ResponseEntity.badRequest().build();
    }
    var u = new User();
    u.setUsername(req.username());
    u.setEmail(req.email());
    u.setPassword(encoder.encode(req.password()));
    users.save(u);

    var token = jwt.generate(u.getEmail(), java.util.Map.of("role","ROLE_USER","username",u.getUsername()));
    return ResponseEntity.ok(new AuthResponse(token));
  }

  @PostMapping("/login")
  public ResponseEntity<AuthResponse> login(@RequestBody @Valid LoginRequest req) {
    authMgr.authenticate(new UsernamePasswordAuthenticationToken(req.email(), req.password()));
    var u = users.findByEmail(req.email()).orElseThrow();
    var token = jwt.generate(u.getEmail(), java.util.Map.of("role","ROLE_USER","username",u.getUsername()));
    return ResponseEntity.ok(new AuthResponse(token));
  }

  @GetMapping("/me")
  public ResponseEntity<UserResponseDTO> me(@RequestHeader(name="Authorization", required=false) String authz) {
    if (authz == null || !authz.startsWith("Bearer ")) return ResponseEntity.status(401).build();
    var email = jwt.subject(authz.substring(7));
    var u = users.findByEmail(email).orElse(null);
    if (u == null) return ResponseEntity.status(401).build();
    return ResponseEntity.ok(new UserResponseDTO(u.getId(), u.getUsername(), u.getEmail()));
  }
}
