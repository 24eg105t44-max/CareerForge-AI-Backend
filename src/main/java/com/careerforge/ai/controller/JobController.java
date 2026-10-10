package com.careerforge.ai.controller;

import com.careerforge.ai.dto.JobDto;
import com.careerforge.ai.service.JobService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @GetMapping
    public ResponseEntity<List<JobDto>> getAllJobs(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String experience) {
        return ResponseEntity.ok(jobService.getAllJobs(search, location, type, experience));
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobDto> getJobById(@PathVariable Long id) {
        return ResponseEntity.ok(jobService.getJobById(id));
    }

    @PostMapping("/{id}/save")
    public ResponseEntity<Map<String, Object>> toggleSaveJob(@PathVariable Long id) {
        boolean saved = jobService.toggleSaveJob(id);
        return ResponseEntity.ok(Map.of("saved", saved, "message", saved ? "Job saved successfully" : "Job unsaved"));
    }

    @GetMapping("/saved")
    public ResponseEntity<List<JobDto>> getSavedJobs() {
        return ResponseEntity.ok(jobService.getSavedJobs());
    }
}
