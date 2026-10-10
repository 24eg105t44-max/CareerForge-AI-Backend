package com.careerforge.ai.controller;

import com.careerforge.ai.dto.SkillRoadmapDto;
import com.careerforge.ai.service.SkillRoadmapService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/skills")
public class SkillRoadmapController {

    private final SkillRoadmapService skillRoadmapService;

    public SkillRoadmapController(SkillRoadmapService skillRoadmapService) {
        this.skillRoadmapService = skillRoadmapService;
    }

    @GetMapping("/roadmap")
    public ResponseEntity<SkillRoadmapDto> getRoadmap(@RequestParam(required = false, defaultValue = "Java Full Stack Engineer") String role) {
        return ResponseEntity.ok(skillRoadmapService.getRoadmapForRole(role));
    }

    @PostMapping("/roadmap")
    public ResponseEntity<SkillRoadmapDto> saveRoadmap(@RequestBody SkillRoadmapDto dto) {
        return ResponseEntity.ok(skillRoadmapService.saveRoadmap(dto));
    }
}
