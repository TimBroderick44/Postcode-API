package io.nology.postcode_api.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.web.access.AccessDeniedHandler;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    // When BCryptPasswordEncoder was included, a circular dependency was created (???)
    // Importing resolved this issue. 
    private final BCryptPasswordEncoder passwordEncoder;
    private final AccessDeniedHandler accessDeniedHandler;

    public SecurityConfig(UserDetailsService userDetailsService, BCryptPasswordEncoder passwordEncoder, AccessDeniedHandler accessDeniedHandler) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.accessDeniedHandler = accessDeniedHandler;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .httpBasic(withDefaults())
            .exceptionHandling(exceptionHandling -> exceptionHandling.accessDeniedHandler(accessDeniedHandler)
            );
        return http.build();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        auth.authenticationProvider(authProvider);
    }
}
