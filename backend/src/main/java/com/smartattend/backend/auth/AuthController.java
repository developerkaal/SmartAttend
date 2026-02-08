package com.smartattend.backend.auth;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserAccountRepository userAccountRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthController(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        if (userAccountRepository.existsByEmail(request.email())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new AuthResponse(null, "Email already registered"));
        }
        UserAccount account = new UserAccount(
            request.fullName(),
            request.email(),
            passwordEncoder.encode(request.password())
        );
        UserAccount saved = userAccountRepository.save(account);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(new AuthResponse(UserDto.from(saved), "Registered successfully"));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return userAccountRepository.findByEmail(request.email())
            .filter(user -> passwordEncoder.matches(request.password(), user.getPasswordHash()))
            .map(user -> ResponseEntity.ok(new AuthResponse(UserDto.from(user), "Login successful")))
            .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new AuthResponse(null, "Invalid credentials")));
    }

    public record RegisterRequest(
        @NotBlank String fullName,
        @Email @NotBlank String email,
        @NotBlank String password
    ) {}

    public record LoginRequest(
        @Email @NotBlank String email,
        @NotBlank String password
    ) {}

    public record AuthResponse(UserDto user, String message) {}

    public record UserDto(Long id, String fullName, String email) {
        public static UserDto from(UserAccount account) {
            return new UserDto(account.getId(), account.getFullName(), account.getEmail());
        }
    }
}
