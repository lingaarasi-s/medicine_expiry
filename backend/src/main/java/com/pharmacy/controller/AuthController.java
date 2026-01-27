package com.pharmacy.controller;

import com.pharmacy.dto.JwtResponse;
import com.pharmacy.dto.LoginRequest;
import com.pharmacy.dto.SignupRequest;
import com.pharmacy.model.User;
import com.pharmacy.service.AuthService;
import com.pharmacy.service.AuditService;
import com.pharmacy.service.JwtUtils;
import com.pharmacy.service.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    AuthService authService;

    @Autowired
    AuditService auditService;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest,
                                            HttpServletRequest request) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        // Log login action
        auditService.logActionWithRequest(loginRequest.getUsername(), "LOGIN",
                "User logged in successfully", "User", userDetails.getId().toString(), request);

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                userDetails.getFullName(),
                roles));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest,
                                        HttpServletRequest request) {

        User user = authService.registerUser(signUpRequest);

        // Log user registration
        auditService.logActionWithRequest(signUpRequest.getUsername(), "USER_REGISTRATION",
                "New user registered: " + signUpRequest.getFullName(), "User", user.getId().toString(), request);

        return ResponseEntity.ok("User registered successfully!");
    }
}