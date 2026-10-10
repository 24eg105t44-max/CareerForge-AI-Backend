package com.careerforge.ai.controller;

import com.careerforge.ai.dto.ResumeAnalysisDto;
import com.careerforge.ai.dto.ResumeDto;
import com.careerforge.ai.service.ResumeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/resumes")
public class ResumeController {

    private final ResumeService resumeService;

    public ResumeController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    @GetMapping
    public ResponseEntity<ResumeDto> getLatestResume() {
        return ResponseEntity.ok(resumeService.getLatestResume());
    }

    @PostMapping
    public ResponseEntity<ResumeDto> saveResume(@RequestBody ResumeDto dto) {
        return ResponseEntity.ok(resumeService.saveResume(dto));
    }

    @GetMapping("/analyze")
    public ResponseEntity<ResumeAnalysisDto> analyzeCurrentResume() {
        return ResponseEntity.ok(resumeService.analyzeCurrentResume());
    }
}
