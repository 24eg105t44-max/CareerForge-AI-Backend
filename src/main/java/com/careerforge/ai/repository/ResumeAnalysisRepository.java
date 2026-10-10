package com.careerforge.ai.repository;

import com.careerforge.ai.entity.ResumeAnalysis;
import com.careerforge.ai.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResumeAnalysisRepository extends JpaRepository<ResumeAnalysis, Long> {
    Optional<ResumeAnalysis> findTopByUserIdOrderByAnalyzedAtDesc(Long userId);
}
