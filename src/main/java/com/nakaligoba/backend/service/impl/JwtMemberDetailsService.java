package com.nakaligoba.backend.service.impl;

import com.nakaligoba.backend.domain.JwtDetails;
import com.nakaligoba.backend.domain.Member;
import com.nakaligoba.backend.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Slf4j
@RequiredArgsConstructor
@Service
public class JwtMemberDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member memberEntity = memberRepository.findByEmail(email)
                .orElseThrow(EntityNotFoundException::new);
        log.info("email : " + memberEntity.getEmail());

        return new JwtDetails(memberEntity);
    }
}
