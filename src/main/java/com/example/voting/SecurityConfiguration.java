package com.example.voting;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder encoder) {
        InMemoryUserDetailsManager man = new InMemoryUserDetailsManager();

        UserDetails ud1 = User
                .withUsername("karel")
                .password(encoder.encode("appel"))
                .roles("VOTER")
                .build();

        UserDetails ud2 = User
                .withUsername("ans")
                .password(encoder.encode("peer"))
                .roles("ADMIN")
                .build();

        man.createUser(ud1);
        man.createUser(ud2);

        return man;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(HttpMethod.GET,"/votes").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/votes").hasAnyRole("ADMIN", "VOTER")
                        .requestMatchers("/**").permitAll()
                        .anyRequest().denyAll()
                )
                .sessionManagement(sess -> sess
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(csrf -> csrf.disable());

        return http.build();


    }

}
