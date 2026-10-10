package com.careerforge.ai.service;

import com.careerforge.ai.dto.AdminStatsDto;
import com.careerforge.ai.entity.Job;
import com.careerforge.ai.entity.JobApplication.ApplicationStatus;
import com.careerforge.ai.repository.*;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AdminService {

    private final UserRepository userRepository;
    private final JobRepository jobRepository;
    private final JobApplicationRepository jobApplicationRepository;
    private final ResumeRepository resumeRepository;
    private final InterviewSessionRepository interviewSessionRepository;

    public AdminService(UserRepository userRepository,
                        JobRepository jobRepository,
                        JobApplicationRepository jobApplicationRepository,
                        ResumeRepository resumeRepository,
                        InterviewSessionRepository interviewSessionRepository) {
        this.userRepository = userRepository;
        this.jobRepository = jobRepository;
        this.jobApplicationRepository = jobApplicationRepository;
        this.resumeRepository = resumeRepository;
        this.interviewSessionRepository = interviewSessionRepository;
    }

    public AdminStatsDto getAdminStats() {
        AdminStatsDto stats = new AdminStatsDto();
        stats.setTotalUsers(userRepository.count());
        stats.setTotalJobs(jobRepository.count());
        stats.setActiveJobs((long) jobRepository.findByStatus(Job.JobStatus.ACTIVE).size());
        stats.setTotalApplications(jobApplicationRepository.count());
        stats.setTotalResumes(resumeRepository.count());
        stats.setTotalInterviews(interviewSessionRepository.count());

        Map<String, Long> statusMap = new HashMap<>();
        for (ApplicationStatus status : ApplicationStatus.values()) {
            statusMap.put(status.name(), jobApplicationRepository.countByStatus(status));
        }
        stats.setApplicationsByStatus(statusMap);

        return stats;
    }
}
