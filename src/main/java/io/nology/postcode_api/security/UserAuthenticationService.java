package io.nology.postcode_api.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import io.nology.postcode_api.user.User;
import io.nology.postcode_api.user.UserRepository;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserAuthenticationService implements UserDetailsService {

    // This service is needed to integrate our custom User entity with Spring Security.
    // It converts the User entity into a UserDetails object, which Spring Security uses for authentication and authorization.
    // This service fetches user details from the database and provides the username, password, and roles to Spring Security.
    
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        Set<SimpleGrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toSet());

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }
}
