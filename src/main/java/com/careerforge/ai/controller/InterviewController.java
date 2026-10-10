package com.careerforge.ai.controller;

import com.careerforge.ai.dto.InterviewDto.*;
import com.careerforge.ai.entity.InterviewSession;
import com.careerforge.ai.service.InterviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/interviews")
public class InterviewController {

    private final InterviewService interviewService;

    public InterviewController(InterviewService interviewService) {
        this.interviewService = interviewService;
    }

    @GetMapping("/questions")
    public ResponseEntity<List<Question>> getQuestions(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String difficulty) {
        return ResponseEntity.ok(interviewService.getQuestions(category, difficulty));
    }

    @PostMapping("/submit")
    public ResponseEntity<SubmissionFeedback> evaluateAnswer(@RequestBody SubmissionRequest request) {
        return ResponseEntity.ok(interviewService.evaluateAnswer(request));
    }

    @GetMapping("/history")
    public ResponseEntity<List<InterviewSession>> getPracticeHistory() {
        return ResponseEntity.ok(interviewService.getPracticeHistory());
    }
}
