package com.nakaligoba.backend.repository;

import com.nakaligoba.backend.entity.MemberEntity;
import com.nakaligoba.backend.entity.MemberProjectEntity;
import com.nakaligoba.backend.entity.ProjectEntity;
import com.nakaligoba.backend.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberProjectRepository extends JpaRepository<MemberProjectEntity, Long> {
    MemberProjectEntity findByMemberAndProjectId(MemberEntity member, Long projectId);
    List<MemberProjectEntity> findByMember(MemberEntity member);
    Optional<MemberProjectEntity> findByProjectAndMemberAndRole(ProjectEntity project, MemberEntity member, Role role);
}
