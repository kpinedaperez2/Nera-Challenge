package com.local.accounts.router;

import com.local.accounts.infrastructure.config.auth.JwtUtil;
import com.local.accounts.router.dto.AuthResponse;
import com.local.accounts.router.dto.LoginRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class LoginController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;


    public LoginController(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;

    }

    /**
     * Authenticates the user and generates a JWT token.
     *
     * @param loginRequest the login request containing username and password
     * @return a response entity containing the authentication token
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        String token = jwtUtil.generateToken(loginRequest.getUsername());
        return ResponseEntity.ok(new AuthResponse(token));
    }
}
