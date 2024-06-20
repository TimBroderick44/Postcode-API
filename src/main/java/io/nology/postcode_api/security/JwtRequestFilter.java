package io.nology.postcode_api.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
// OncePerRequestFilter is a Spring Security class that ensures it jumps in once per request
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    // This method is called for every request that comes in
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        // The authorization header is where the JWT token is stored
        final String authorizationHeader = request.getHeader("Authorization");

        
        String username = null;
        String jwt = null;

        // If the authorization header is not null and starts with "Bearer ", we extract the JWT token
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            username = jwtUtil.extractUsername(jwt);
        }

        // If the username is not null and the user is not already authenticated, we authenticate the user
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // We get the user details from the userDetailsService
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            // If the token is valid, we create an authentication token and set the security context
            if (jwtUtil.validateToken(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        // We then pass the request and response to the next filter in the chain (varies but probably the UsernamePasswordAuthenticationFilter)
        chain.doFilter(request, response);
    }
}
