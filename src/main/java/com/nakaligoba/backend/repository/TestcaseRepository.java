package com.nakaligoba.backend.repository;

import com.nakaligoba.backend.domain.Testcase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestcaseRepository extends JpaRepository<Testcase, Long> {
}
