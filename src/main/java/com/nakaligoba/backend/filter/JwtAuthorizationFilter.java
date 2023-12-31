package com.nakaligoba.backend.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.nakaligoba.backend.domain.JwtDetails;
import com.nakaligoba.backend.domain.Member;
import com.nakaligoba.backend.service.component.jwt.JwtProperties;
import com.nakaligoba.backend.repository.MemberRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.persistence.EntityNotFoundException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final MemberRepository memberRepository;
    private final JwtProperties jwtProperties;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, MemberRepository memberRepository, JwtProperties jwtProperties) {
        super(authenticationManager);

        this.memberRepository = memberRepository;
        this.jwtProperties = jwtProperties;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String jwtHeader = request.getHeader(jwtProperties.getHEADER_STRING());
        String email = "";

        if (jwtHeader == null || !jwtHeader.startsWith(jwtProperties.getTOKEN_PREFIX())) {
            chain.doFilter(request, response);
            return;
        }

        String jwt = request.getHeader(jwtProperties.getHEADER_STRING()).replace(jwtProperties.getTOKEN_PREFIX(), "");

        try {
            email = JWT.require(Algorithm.HMAC512(jwtProperties.getSECRET_KEY())).build().verify(jwt).getClaim(jwtProperties.getCLAIM()).asString();
        } catch (TokenExpiredException e) {
            request.setAttribute(jwtProperties.getHEADER_STRING(), JwtAuthenticationEntryPoint.EXPIRATION_JWT);
        } catch (JWTVerificationException e) {
            request.setAttribute(jwtProperties.getHEADER_STRING(), JwtAuthenticationEntryPoint.NOT_VALIDATE_JWT);
        }

        if (email != null) {
            Member memberEntity = memberRepository.findByEmail(email)
                    .orElseThrow(EntityNotFoundException::new);

            JwtDetails jwtDetails = new JwtDetails(memberEntity);
            Authentication authentication = new UsernamePasswordAuthenticationToken(jwtDetails, null, jwtDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        chain.doFilter(request, response);
    }
}
