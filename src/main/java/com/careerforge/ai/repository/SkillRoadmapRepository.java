package com.careerforge.ai.repository;

import com.careerforge.ai.entity.SkillRoadmap;
import com.careerforge.ai.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SkillRoadmapRepository extends JpaRepository<SkillRoadmap, Long> {
    Optional<SkillRoadmap> findTopByUserIdOrderByUpdatedAtDesc(Long userId);
}
