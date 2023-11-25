package com.sayas.filmhub.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


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
        http.authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers(mvc.pattern("/admin/**")).hasAnyRole(EDITOR_ROLE, ADMIN_ROLE)
                        .requestMatchers(mvc.pattern("/ocen-film")).authenticated()
                        .anyRequest().permitAll()
                )
                .formLogin(login -> login
                        .loginPage("/login")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout/**", HttpMethod.GET.name()))
                        .logoutSuccessUrl("/login?logout").permitAll()
                );
        http.csrf().ignoringRequestMatchers(new AntPathRequestMatcher("/h2-console/**"));
        http.headers().frameOptions().sameOrigin();
        return http.build();

    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(MvcRequestMatcher.Builder mvc) {
        return web -> web.ignoring().requestMatchers(
                mvc.pattern("/img/**"),
                mvc.pattern("/scripts/**"),
                mvc.pattern("/styles/**")
        );
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}