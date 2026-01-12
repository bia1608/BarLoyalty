package com.barloyalty.gateway.controller;

import com.barloyalty.gateway.model.User;
import com.barloyalty.gateway.repository.UserRepository;
import com.barloyalty.gateway.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * REGISTER - Înregistrare client nou
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {

        // Validări input
        if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Username is required"));
        }

        if (request.getPassword() == null || request.getPassword().length() < 6) {
            return ResponseEntity.badRequest().body(Map.of("error", "Password must be at least 6 characters"));
        }

        if (request.getEmail() == null || !request.getEmail().contains("@")) {
            return ResponseEntity.badRequest().body(Map.of("error", "Valid email is required"));
        }

        // Verifică duplicate
        if (userRepository.findByUsername(request.getUsername()) != null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Username already exists"));
        }

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email deja folosit!");
        }

        // Creează utilizator nou
        User newUser = new User();
        newUser.setUsername(request.getUsername());
        newUser.setEmail(request.getEmail());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setName(request.getName());
        newUser.setRole("CLIENT"); // ROL HARDCODAT - securitate!
        newUser.setLoyaltyPoints(0);

        userRepository.save(newUser);

        // Generează JWT automat după register
        String token = jwtUtil.generateToken(newUser.getUsername(), newUser.getRole());

        Map<String, Object> response = new HashMap<>();
        response.put("message", "User registered successfully");
        response.put("username", newUser.getUsername());
        response.put("token", token);
        response.put("role", newUser.getRole());

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * LOGIN - Autentificare
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        try {
            // Încearcă autentificarea
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );

            // Dacă ajunge aici, autentificarea a reușit
            User user = userRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> new RuntimeException("Utilizator negasit"));
            // Generează JWT
            String token = jwtUtil.generateToken(user.getUsername(), user.getRole());

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Login successful");
            response.put("username", user.getUsername());
            response.put("token", token);
            response.put("role", user.getRole());
            response.put("loyaltyPoints", user.getLoyaltyPoints());

            return ResponseEntity.ok(response);

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid username or password"));
        }
    }
}

// DTO pentru Register
class RegisterRequest {
    private String username;
    private String email;
    private String password;
    private String name;

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}

// DTO pentru Login
class LoginRequest {
    private String username;
    private String password;

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}