package com.careerforge.ai.service;

import com.careerforge.ai.dto.JobDto;
import com.careerforge.ai.entity.Job;
import com.careerforge.ai.entity.SavedJob;
import com.careerforge.ai.entity.User;
import com.careerforge.ai.exception.ResourceNotFoundException;
import com.careerforge.ai.repository.JobApplicationRepository;
import com.careerforge.ai.repository.JobRepository;
import com.careerforge.ai.repository.SavedJobRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobService {

    private final JobRepository jobRepository;
    private final SavedJobRepository savedJobRepository;
    private final JobApplicationRepository jobApplicationRepository;
    private final AuthService authService;

    public JobService(JobRepository jobRepository,
                      SavedJobRepository savedJobRepository,
                      JobApplicationRepository jobApplicationRepository,
                      AuthService authService) {
        this.jobRepository = jobRepository;
        this.savedJobRepository = savedJobRepository;
        this.jobApplicationRepository = jobApplicationRepository;
        this.authService = authService;
    }

    public List<JobDto> getAllJobs(String search, String location, String type, String experience) {
        List<Job> jobs;
        if (search != null && !search.trim().isEmpty()) {
            jobs = jobRepository.searchJobs(search.trim());
        } else {
            jobs = jobRepository.findByStatus(Job.JobStatus.ACTIVE);
        }

        // Apply filters
        return jobs.stream()
                .filter(job -> location == null || location.isEmpty() || job.getLocation().equalsIgnoreCase(location))
                .filter(job -> type == null || type.isEmpty() || job.getType().name().equalsIgnoreCase(type))
                .filter(job -> experience == null || experience.isEmpty() || (job.getExperienceLevel() != null && job.getExperienceLevel().equalsIgnoreCase(experience)))
                .map(this::mapToDtoWithUserStatus)
                .collect(Collectors.toList());
    }

    public JobDto getJobById(Long id) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + id));
        return mapToDtoWithUserStatus(job);
    }

    @Transactional
    public JobDto createJob(JobDto dto) {
        Job job = new Job();
        mapDtoToJob(dto, job);
        Job saved = jobRepository.save(job);
        return mapToDto(saved);
    }

    @Transactional
    public JobDto updateJob(Long id, JobDto dto) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + id));
        mapDtoToJob(dto, job);
        Job updated = jobRepository.save(job);
        return mapToDto(updated);
    }

    @Transactional
    public void deleteJob(Long id) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + id));
        jobRepository.delete(job);
    }

    @Transactional
    public boolean toggleSaveJob(Long jobId) {
        User currentUser = authService.getCurrentUser();
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + jobId));

        boolean alreadySaved = savedJobRepository.existsByUserAndJob(currentUser, job);
        if (alreadySaved) {
            savedJobRepository.deleteByUserAndJob(currentUser, job);
            return false; // Unsaved
        } else {
            SavedJob savedJob = new SavedJob(currentUser, job);
            savedJobRepository.save(savedJob);
            return true; // Saved
        }
    }

    public List<JobDto> getSavedJobs() {
        User currentUser = authService.getCurrentUser();
        List<SavedJob> savedJobs = savedJobRepository.findByUser(currentUser);
        return savedJobs.stream()
                .map(sj -> {
                    JobDto dto = mapToDto(sj.getJob());
                    dto.setIsSaved(true);
                    dto.setIsApplied(jobApplicationRepository.existsByUserAndJob(currentUser, sj.getJob()));
                    return dto;
                })
                .collect(Collectors.toList());
    }

    private JobDto mapToDtoWithUserStatus(Job job) {
        JobDto dto = mapToDto(job);
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
                User currentUser = authService.getCurrentUser();
                dto.setIsSaved(savedJobRepository.existsByUserAndJob(currentUser, job));
                dto.setIsApplied(jobApplicationRepository.existsByUserAndJob(currentUser, job));
            }
        } catch (Exception ignored) {}
        return dto;
    }

    private JobDto mapToDto(Job job) {
        JobDto dto = new JobDto();
        dto.setId(job.getId());
        dto.setTitle(job.getTitle());
        dto.setCompany(job.getCompany());
        dto.setLocation(job.getLocation());
        dto.setType(job.getType());
        dto.setExperienceLevel(job.getExperienceLevel());
        dto.setSalaryRange(job.getSalaryRange());
        dto.setDescription(job.getDescription());
        dto.setRequirements(job.getRequirements());
        dto.setResponsibilities(job.getResponsibilities());
        dto.setCategory(job.getCategory());
        dto.setRequiredSkills(job.getRequiredSkills());
        dto.setStatus(job.getStatus());
        dto.setPostedBy(job.getPostedBy());
        dto.setCreatedAt(job.getCreatedAt());
        return dto;
    }

    private void mapDtoToJob(JobDto dto, Job job) {
        job.setTitle(dto.getTitle());
        job.setCompany(dto.getCompany());
        job.setLocation(dto.getLocation());
        if (dto.getType() != null) job.setType(dto.getType());
        job.setExperienceLevel(dto.getExperienceLevel());
        job.setSalaryRange(dto.getSalaryRange());
        job.setDescription(dto.getDescription());
        job.setRequirements(dto.getRequirements());
        job.setResponsibilities(dto.getResponsibilities());
        job.setCategory(dto.getCategory());
        job.setRequiredSkills(dto.getRequiredSkills());
        if (dto.getStatus() != null) job.setStatus(dto.getStatus());
        if (dto.getPostedBy() != null) job.setPostedBy(dto.getPostedBy());
    }
}
