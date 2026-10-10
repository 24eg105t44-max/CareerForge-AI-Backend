package com.careerforge.ai.repository;

import com.careerforge.ai.entity.JobApplication;
import com.careerforge.ai.entity.User;
import com.careerforge.ai.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {
    List<JobApplication> findByUser(User user);
    List<JobApplication> findByUserId(Long userId);
    List<JobApplication> findByJobId(Long jobId);
    Optional<JobApplication> findByUserAndJob(User user, Job job);
    Boolean existsByUserAndJob(User user, Job job);
    Long countByUserId(Long userId);
    Long countByStatus(JobApplication.ApplicationStatus status);
}
