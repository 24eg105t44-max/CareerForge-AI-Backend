package com.careerforge.ai.repository;

import com.careerforge.ai.entity.SavedJob;
import com.careerforge.ai.entity.User;
import com.careerforge.ai.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SavedJobRepository extends JpaRepository<SavedJob, Long> {
    List<SavedJob> findByUser(User user);
    List<SavedJob> findByUserId(Long userId);
    Optional<SavedJob> findByUserAndJob(User user, Job job);
    Boolean existsByUserAndJob(User user, Job job);
    void deleteByUserAndJob(User user, Job job);
}
