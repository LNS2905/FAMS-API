package com.fams.fams.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fams.fams.security.JwtAuthenticationEntryPoint;
import com.fams.fams.security.JwtAuthenticationFilter;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
@SecurityScheme(name = "Bear Authentication", type = SecuritySchemeType.HTTP, bearerFormat = "JWT", scheme = "bearer")
public class SecurityConfig {
    private UserDetailsService userDetailsService;
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    public SecurityConfig(UserDetailsService userDetailsService,
            JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
            JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.userDetailsService = userDetailsService;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // http.csrf((csrf) -> csrf.disable())
        // .authorizeHttpRequests((authorize) ->
        // authorize.requestMatchers(HttpMethod.GET, "/api/v1/**").permitAll()
        // .requestMatchers("/api/v1/auth/**").permitAll()
        // .requestMatchers("/swagger-ui/**").permitAll()
        // .requestMatchers("/v3/api-docs/**").permitAll()
        // .requestMatchers("/api/v1/auth/user/info").authenticated()
        // .anyRequest()
        // //.permitAll()
        // .authenticated()
        // )
        //
        // .exceptionHandling((exception) -> exception
        // .authenticationEntryPoint(this.jwtAuthenticationEntryPoint)
        // ).sessionManagement((session) -> session
        // .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        // );
        //
        // http.addFilterBefore(this.jwtAuthenticationFilter,
        // UsernamePasswordAuthenticationFilter.class);
        //
        // return http.build();

        http.addFilterBefore(this.jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        http.csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }
}
