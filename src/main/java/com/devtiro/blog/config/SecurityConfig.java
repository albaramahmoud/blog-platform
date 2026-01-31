package com.devtiro.blog.config;

import com.devtiro.blog.entities.User;
import com.devtiro.blog.repositories.UserRepo;
import com.devtiro.blog.security.BlogUserDetailsService;
import com.devtiro.blog.security.JwtAuthenticationFilter;
import com.devtiro.blog.services.AuthenticationService;
import org.aspectj.asm.AsmManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(AuthenticationService authenticationService) {
        return new JwtAuthenticationFilter(authenticationService);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http
            ,JwtAuthenticationFilter jwtAuthenticationFilter)throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                            .requestMatchers(HttpMethod.POST,"/api/v1/auth/**").permitAll()
                            .requestMatchers(HttpMethod.GET,"/api/v1/posts/**").permitAll()
                            .requestMatchers(HttpMethod.GET,"/api/v1/categories/**").permitAll()
                            .requestMatchers(HttpMethod.GET,"/api/v1/tags/**").permitAll()
                            .anyRequest().authenticated()
                )
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter,UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    UserDetailsService userDetailsService(UserRepo userRepo){
        BlogUserDetailsService blogUserDetailsService = new BlogUserDetailsService(userRepo);

        String email = "user@test.com";
        userRepo.findUserByEmail(email).orElseGet(() -> {
            User newUser = User.builder()
                    .name("Test User")
                    .email(email)
                    .password(passwordEncoder().encode("1234"))
                    .build();
            return userRepo.save(newUser);
        });

        return blogUserDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
