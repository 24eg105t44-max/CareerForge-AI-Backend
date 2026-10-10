package com.careerforge.ai.controller;

import com.careerforge.ai.dto.JobApplicationDto;
import com.careerforge.ai.service.JobApplicationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/applications")
public class JobApplicationController {

    private final JobApplicationService jobApplicationService;

    public JobApplicationController(JobApplicationService jobApplicationService) {
        this.jobApplicationService = jobApplicationService;
    }

    @PostMapping("/apply/{jobId}")
    public ResponseEntity<JobApplicationDto> applyForJob(@PathVariable Long jobId, @RequestBody(required = false) Map<String, String> body) {
        String coverLetter = (body != null) ? body.get("coverLetter") : "";
        return ResponseEntity.ok(jobApplicationService.applyForJob(jobId, coverLetter));
    }

    @GetMapping("/my-applications")
    public ResponseEntity<List<JobApplicationDto>> getMyApplications() {
        return ResponseEntity.ok(jobApplicationService.getMyApplications());
    }
}
