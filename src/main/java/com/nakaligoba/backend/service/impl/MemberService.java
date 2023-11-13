package com.nakaligoba.backend.service.impl;

import com.nakaligoba.backend.domain.Member;
import com.nakaligoba.backend.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public Member getMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.info("Member not found by: {}", email);
                    return new UsernameNotFoundException("해당 이메일로 회원정보를 찾을 수 없습니다: "+ email);
                });
    }


}
