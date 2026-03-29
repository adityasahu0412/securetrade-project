package com.securetrade.config;

import com.securetrade.security.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// Main security configuration for the app
// Using JWT so we don't need session or form login

@Configuration
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

    // using BCrypt to hash passwords before saving to DB
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable()) // disabled because we are using JWT not cookies
                .cors(cors -> {})
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/login", "/auth/signup").permitAll() // these routes are public
                        .anyRequest().authenticated() // everything else needs token
                )
                .httpBasic(httpBasic -> httpBasic.disable())
                .formLogin(form -> form.disable()); // not using form login

        // add JWT filter before the default username/password filter
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}