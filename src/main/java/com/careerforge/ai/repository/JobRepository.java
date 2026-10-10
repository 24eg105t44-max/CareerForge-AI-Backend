package com.careerforge.ai.repository;

import com.careerforge.ai.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long>, JpaSpecificationExecutor<Job> {
    List<Job> findByStatus(Job.JobStatus status);

    @Query("SELECT j FROM Job j WHERE j.status = 'ACTIVE' AND " +
           "(LOWER(j.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           " LOWER(j.company) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           " LOWER(j.location) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           " LOWER(j.requiredSkills) LIKE LOWER(CONCAT('%', :query, '%')))")
    List<Job> searchJobs(@Param("query") String query);
}
