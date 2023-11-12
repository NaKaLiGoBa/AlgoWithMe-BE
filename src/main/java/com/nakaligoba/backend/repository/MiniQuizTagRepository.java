package com.nakaligoba.backend.repository;

import com.nakaligoba.backend.domain.MiniQuizTag;
import com.nakaligoba.backend.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MiniQuizTagRepository extends JpaRepository<MiniQuizTag, Long> {
    List<MiniQuizTag> findAllByTagIn(List<Tag> tags);
}
