package com.securetrade.controller;

import com.securetrade.model.User;
import com.securetrade.repository.UserRepository;
import com.securetrade.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

// handles signup and login

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // signup - save user with encoded password
    @PostMapping("/signup")
    public String signup(@RequestBody User user) {

        // encode password before saving (never save plain text password)
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        userRepo.save(user);

        return "User registered successfully";
    }

    // login - check password and return JWT token
    @PostMapping("/login")
    public String login(@RequestBody User user) {

        // find user by username
        User existingUser = userRepo.findByUsername(user.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // check if password matches
        boolean passwordMatch = passwordEncoder.matches(user.getPassword(), existingUser.getPassword());

        if (passwordMatch) {
            String token = jwtUtil.generateToken(user.getUsername());
            return token;
        }

        throw new RuntimeException("Invalid credentials");
    }
}