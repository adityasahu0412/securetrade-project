package com.securetrade.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

// this filter runs on every request
// it checks if the request has a valid JWT token in the Authorization header
// if valid, it sets the user as authenticated in Spring Security context

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");
        System.out.println("Authorization header: " + header);

        // check if header exists and starts with Bearer
        if (header != null && header.startsWith("Bearer ")) {

            // remove "Bearer " prefix to get the actual token
            String token = header.substring(7);

            try {
                String username = jwtUtil.extractUsername(token);
                System.out.println("Extracted username: " + username);

                // only set authentication if not already set
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                    UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(
                                    username,
                                    null,
                                    Collections.singletonList(() -> "ROLE_USER")
                            );

                    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // set user as authenticated
                    SecurityContextHolder.getContext().setAuthentication(auth);
                    System.out.println("Authentication set for user: " + username);
                }

            } catch (Exception e) {
                // token is invalid or expired
                System.out.println("JWT error: " + e.getMessage());
            }
        }

        // pass request to next filter
        filterChain.doFilter(request, response);
    }
}