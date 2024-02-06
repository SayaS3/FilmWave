package com.sayas.filmhub.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;


@Configuration
public class CustomSecurityConfig {
    private static final String USER_ROLE = "USER";
    private static final String EDITOR_ROLE = "EDITOR";
    private static final String ADMIN_ROLE = "ADMIN";

    @Bean
    MvcRequestMatcher.Builder mvc(HandlerMappingIntrospector introspector) {
        return new MvcRequestMatcher.Builder(introspector);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, MvcRequestMatcher.Builder mvc) throws Exception {
        http.
                authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers(mvc.pattern("/admin/**")).hasAnyRole(EDITOR_ROLE, ADMIN_ROLE)
                        .requestMatchers(mvc.pattern("/delete-comment/**")).hasAnyRole(EDITOR_ROLE, ADMIN_ROLE)
                        .requestMatchers(mvc.pattern("/shadow-ban/**")).hasAnyRole(EDITOR_ROLE, ADMIN_ROLE)
                        .requestMatchers(mvc.pattern("/rate-movie")).authenticated()
                        .requestMatchers(mvc.pattern("/add-comment")).hasAnyRole(EDITOR_ROLE, ADMIN_ROLE, USER_ROLE)
                        .requestMatchers(mvc.pattern("/submit-movie")).hasAnyRole(EDITOR_ROLE, ADMIN_ROLE, USER_ROLE)
                        .requestMatchers(mvc.pattern("/report-error")).hasAnyRole(EDITOR_ROLE, ADMIN_ROLE, USER_ROLE)
                        .requestMatchers(toH2Console()).hasAnyRole(ADMIN_ROLE)
                        .anyRequest().permitAll()
                )
                .headers(headers -> headers.frameOptions().disable())
                .formLogin(login -> login
                        .loginPage("/login")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout/**", HttpMethod.GET.name()))
                        .logoutSuccessUrl("/login?logout").permitAll()
                )
                .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .ignoringRequestMatchers(toH2Console()));


        return http.build();


    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}