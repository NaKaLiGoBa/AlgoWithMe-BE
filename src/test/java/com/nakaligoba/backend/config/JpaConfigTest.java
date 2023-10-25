package com.nakaligoba.backend.config;

import com.nakaligoba.backend.entity.ProjectEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
public class JpaConfigTest {

    @Autowired
    EntityManager em;

    @Transactional
    @Test
    void givenEntity_whenSave_thenJpaAudit() {
        ProjectEntity entity = ProjectEntity.builder()
                .name("test")
                .description("test")
                .build();

        em.persist(entity);

        assertThat(entity.getCreatedAt()).isNotNull();
        assertThat(entity.getUpdatedAt()).isNotNull();
    }
}
