package com.careerforge.ai.controller;

import com.careerforge.ai.dto.AdminStatsDto;
import com.careerforge.ai.dto.ApplicationStatusUpdateDto;
import com.careerforge.ai.dto.JobApplicationDto;
import com.careerforge.ai.dto.JobDto;
import com.careerforge.ai.entity.User;
import com.careerforge.ai.repository.UserRepository;
import com.careerforge.ai.service.AdminService;
import com.careerforge.ai.service.JobApplicationService;
import com.careerforge.ai.service.JobService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;
    private final JobService jobService;
    private final JobApplicationService jobApplicationService;
    private final UserRepository userRepository;

    public AdminController(AdminService adminService,
                           JobService jobService,
                           JobApplicationService jobApplicationService,
                           UserRepository userRepository) {
        this.adminService = adminService;
        this.jobService = jobService;
        this.jobApplicationService = jobApplicationService;
        this.userRepository = userRepository;
    }

    @GetMapping("/stats")
    public ResponseEntity<AdminStatsDto> getAdminStats() {
        return ResponseEntity.ok(adminService.getAdminStats());
    }

    @PostMapping("/jobs")
    public ResponseEntity<JobDto> createJob(@RequestBody JobDto jobDto) {
        return ResponseEntity.ok(jobService.createJob(jobDto));
    }

    @PutMapping("/jobs/{id}")
    public ResponseEntity<JobDto> updateJob(@PathVariable Long id, @RequestBody JobDto jobDto) {
        return ResponseEntity.ok(jobService.updateJob(id, jobDto));
    }

    @DeleteMapping("/jobs/{id}")
    public ResponseEntity<Map<String, String>> deleteJob(@PathVariable Long id) {
        jobService.deleteJob(id);
        return ResponseEntity.ok(Map.of("message", "Job deleted successfully"));
    }

    @GetMapping("/applications")
    public ResponseEntity<List<JobApplicationDto>> getAllApplications() {
        return ResponseEntity.ok(jobApplicationService.getAllApplicationsForAdmin());
    }

    @PutMapping("/applications/{id}/status")
    public ResponseEntity<JobApplicationDto> updateApplicationStatus(
            @PathVariable Long id,
            @RequestBody ApplicationStatusUpdateDto updateDto) {
        return ResponseEntity.ok(jobApplicationService.updateApplicationStatus(id, updateDto));
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userRepository.findAll();
        users.forEach(u -> u.setPassword("[PROTECTED]"));
        return ResponseEntity.ok(users);
    }
}
