package com.nakaligoba.backend.repository;

import com.nakaligoba.backend.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByEmail(String email);

    Member findByEmail(String email);
}
