package com.nakaligoba.backend.config;

import com.nakaligoba.backend.filter.JwtAuthenticationEntryPoint;
import com.nakaligoba.backend.filter.JwtAuthenticationFilter;
import com.nakaligoba.backend.filter.JwtAuthorizationFilter;
import com.nakaligoba.backend.jwt.JwtProperties;
import com.nakaligoba.backend.jwt.JwtProvider;
import com.nakaligoba.backend.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtProvider jwtProvider;
    private final JwtProperties jwtProperties;
    private final MemberRepository memberRepository;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager(), jwtProvider);
        jwtAuthenticationFilter.setFilterProcessesUrl("/api/v1/auth/signin");

        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .formLogin().disable()
                .httpBasic().disable()
                .addFilter(jwtAuthenticationFilter)
                .addFilter(new JwtAuthorizationFilter(authenticationManager(), memberRepository, jwtProperties))
                .authorizeRequests()
                .antMatchers("/api/v1/auth/**", "/api/test").permitAll()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new JwtAuthenticationEntryPoint());
    }
}
