package com.careerforge.ai.service;

import com.careerforge.ai.dto.ApplicationStatusUpdateDto;
import com.careerforge.ai.dto.JobApplicationDto;
import com.careerforge.ai.entity.Job;
import com.careerforge.ai.entity.JobApplication;
import com.careerforge.ai.entity.Notification;
import com.careerforge.ai.entity.User;
import com.careerforge.ai.exception.BadRequestException;
import com.careerforge.ai.exception.ResourceNotFoundException;
import com.careerforge.ai.repository.JobApplicationRepository;
import com.careerforge.ai.repository.JobRepository;
import com.careerforge.ai.repository.NotificationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobApplicationService {

    private final JobApplicationRepository jobApplicationRepository;
    private final JobRepository jobRepository;
    private final NotificationRepository notificationRepository;
    private final AuthService authService;

    public JobApplicationService(JobApplicationRepository jobApplicationRepository,
                                 JobRepository jobRepository,
                                 NotificationRepository notificationRepository,
                                 AuthService authService) {
        this.jobApplicationRepository = jobApplicationRepository;
        this.jobRepository = jobRepository;
        this.notificationRepository = notificationRepository;
        this.authService = authService;
    }

    @Transactional
    public JobApplicationDto applyForJob(Long jobId, String coverLetter) {
        User currentUser = authService.getCurrentUser();
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + jobId));

        if (jobApplicationRepository.existsByUserAndJob(currentUser, job)) {
            throw new BadRequestException("You have already applied for this position!");
        }

        JobApplication application = new JobApplication(currentUser, job, coverLetter);
        JobApplication saved = jobApplicationRepository.save(application);

        // Send confirmation notification
        notificationRepository.save(new Notification(
                currentUser,
                "Application Submitted",
                "Your application for " + job.getTitle() + " at " + job.getCompany() + " has been received successfully.",
                "JOB_UPDATE"
        ));

        return mapToDto(saved);
    }

    public List<JobApplicationDto> getMyApplications() {
        User currentUser = authService.getCurrentUser();
        return jobApplicationRepository.findByUser(currentUser)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<JobApplicationDto> getAllApplicationsForAdmin() {
        return jobApplicationRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public JobApplicationDto updateApplicationStatus(Long applicationId, ApplicationStatusUpdateDto dto) {
        JobApplication application = jobApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found with id: " + applicationId));

        if (dto.getStatus() != null) {
            application.setStatus(dto.getStatus());
        }
        if (dto.getAdminNotes() != null) {
            application.setAdminNotes(dto.getAdminNotes());
        }

        JobApplication updated = jobApplicationRepository.save(application);

        // Notify applicant of status update
        notificationRepository.save(new Notification(
                updated.getUser(),
                "Application Status Updated",
                "Your application status for " + updated.getJob().getTitle() + " is now: " + updated.getStatus().name(),
                "JOB_UPDATE"
        ));

        return mapToDto(updated);
    }

    private JobApplicationDto mapToDto(JobApplication app) {
        JobApplicationDto dto = new JobApplicationDto();
        dto.setId(app.getId());
        dto.setJobId(app.getJob().getId());
        dto.setJobTitle(app.getJob().getTitle());
        dto.setCompany(app.getJob().getCompany());
        dto.setLocation(app.getJob().getLocation());
        dto.setSalaryRange(app.getJob().getSalaryRange());
        dto.setUserId(app.getUser().getId());
        dto.setUserName(app.getUser().getName());
        dto.setUserEmail(app.getUser().getEmail());
        dto.setStatus(app.getStatus());
        dto.setCoverLetter(app.getCoverLetter());
        dto.setAdminNotes(app.getAdminNotes());
        dto.setAppliedAt(app.getAppliedAt());
        dto.setUpdatedAt(app.getUpdatedAt());
        return dto;
    }
}
