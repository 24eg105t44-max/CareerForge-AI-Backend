package com.careerforge.ai.repository;

import com.careerforge.ai.entity.InterviewSession;
import com.careerforge.ai.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InterviewSessionRepository extends JpaRepository<InterviewSession, Long> {
    List<InterviewSession> findByUserId(Long userId);
    List<InterviewSession> findTop5ByUserIdOrderByCompletedAtDesc(Long userId);
}
