package com.ecommerce.admin.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new EcommerceUserDetailsService();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());

        return new ProviderManager(authenticationProvider);
    }

    @Bean
    RememberMeServices rememberMeServices(UserDetailsService userDetailsService) {
        TokenBasedRememberMeServices.RememberMeTokenAlgorithm encodingAlgorithm = TokenBasedRememberMeServices.RememberMeTokenAlgorithm.SHA256;
        TokenBasedRememberMeServices rememberMe = new TokenBasedRememberMeServices("AbcDefgHijKlmnOpqrs_1234567890", userDetailsService, encodingAlgorithm);
        rememberMe.setMatchingAlgorithm(TokenBasedRememberMeServices.RememberMeTokenAlgorithm.MD5);
        rememberMe.setTokenValiditySeconds(7 * 24 * 60 * 60);
        return rememberMe;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, RememberMeServices rememberMeServices) throws Exception {
        http
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/users/**").hasAuthority("Admin")
                        .requestMatchers("/categories/**","/brands/**").hasAnyAuthority("Admin", "Editor")
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage("/login")
                        .usernameParameter("email")
                        .permitAll()
                )
                .logout(LogoutConfigurer::permitAll)
                .rememberMe(rememberMe -> rememberMe
                        .rememberMeServices(rememberMeServices))
        ;


        return http.build();
    }


    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/images/**",
                "/js/**",
                "/webjars/**"
        );
    }
}
