package com.careerforge.ai.service;

import com.careerforge.ai.dto.SkillRoadmapDto;
import com.careerforge.ai.dto.SkillRoadmapDto.SkillItem;
import com.careerforge.ai.entity.SkillRoadmap;
import com.careerforge.ai.entity.User;
import com.careerforge.ai.repository.SkillRoadmapRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class SkillRoadmapService {

    private final SkillRoadmapRepository skillRoadmapRepository;
    private final AuthService authService;
    private final ObjectMapper objectMapper;

    public SkillRoadmapService(SkillRoadmapRepository skillRoadmapRepository, AuthService authService) {
        this.skillRoadmapRepository = skillRoadmapRepository;
        this.authService = authService;
        this.objectMapper = new ObjectMapper();
    }

    public SkillRoadmapDto getRoadmapForRole(String targetRole) {
        User currentUser = authService.getCurrentUser();
        String roleKey = (targetRole != null && !targetRole.isBlank()) ? targetRole : "Java Full Stack Engineer";

        Optional<SkillRoadmap> existing = skillRoadmapRepository.findTopByUserIdOrderByUpdatedAtDesc(currentUser.getId());
        if (existing.isPresent() && existing.get().getTargetRole().equalsIgnoreCase(roleKey)) {
            return mapToDto(existing.get());
        }

        // Generate default roadmap for target role
        SkillRoadmapDto defaultDto = generateDefaultRoadmap(roleKey);
        saveRoadmap(defaultDto);
        return defaultDto;
    }

    @Transactional
    public SkillRoadmapDto saveRoadmap(SkillRoadmapDto dto) {
        User currentUser = authService.getCurrentUser();
        SkillRoadmap roadmap = skillRoadmapRepository.findTopByUserIdOrderByUpdatedAtDesc(currentUser.getId())
                .orElseGet(() -> {
                    SkillRoadmap sr = new SkillRoadmap();
                    sr.setUser(currentUser);
                    return sr;
                });

        roadmap.setTargetRole(dto.getTargetRole());
        roadmap.setCurrentLevel(dto.getCurrentLevel() != null ? dto.getCurrentLevel() : "Intermediate");

        long completedCount = dto.getSkills().stream().filter(s -> Boolean.TRUE.equals(s.getIsCompleted())).count();
        int progress = dto.getSkills().isEmpty() ? 0 : (int) Math.round(((double) completedCount / dto.getSkills().size()) * 100);

        roadmap.setProgressPercentage(progress);

        try {
            roadmap.setRequiredSkillsJson(objectMapper.writeValueAsString(dto.getSkills()));
        } catch (Exception e) {
            roadmap.setRequiredSkillsJson("[]");
        }

        SkillRoadmap saved = skillRoadmapRepository.save(roadmap);
        return mapToDto(saved);
    }

    private SkillRoadmapDto mapToDto(SkillRoadmap entity) {
        SkillRoadmapDto dto = new SkillRoadmapDto();
        dto.setId(entity.getId());
        dto.setTargetRole(entity.getTargetRole());
        dto.setCurrentLevel(entity.getCurrentLevel());
        dto.setProgressPercentage(entity.getProgressPercentage());

        try {
            List<SkillItem> items = objectMapper.readValue(entity.getRequiredSkillsJson(), new TypeReference<List<SkillItem>>() {});
            dto.setSkills(items);
        } catch (Exception e) {
            dto.setSkills(Collections.emptyList());
        }
        return dto;
    }

    private SkillRoadmapDto generateDefaultRoadmap(String role) {
        SkillRoadmapDto dto = new SkillRoadmapDto();
        dto.setTargetRole(role);
        dto.setCurrentLevel("Intermediate");
        dto.setProgressPercentage(45);

        List<SkillItem> skills = new ArrayList<>();
        if (role.toLowerCase().contains("frontend") || role.toLowerCase().contains("react")) {
            skills.add(new SkillItem("1", "HTML5 & Modern CSS / Tailwind", "Core", true, "Required", "https://tailwindcss.com"));
            skills.add(new SkillItem("2", "JavaScript ES6+ & Async/Await", "Core", true, "Required", "https://developer.mozilla.org"));
            skills.add(new SkillItem("3", "React 18 & State Management", "Framework", true, "Required", "https://react.dev"));
            skills.add(new SkillItem("4", "TypeScript for React Developers", "Language", false, "Required", "https://www.typescriptlang.org"));
            skills.add(new SkillItem("5", "Next.js SSR & Server Components", "Advanced", false, "Recommended", "https://nextjs.org"));
            skills.add(new SkillItem("6", "Jest & React Testing Library", "Testing", false, "Recommended", "https://testing-library.com"));
        } else {
            // Default Java Full Stack
            skills.add(new SkillItem("1", "Java 21 Core Syntax & OOP Principles", "Core", true, "Required", "https://docs.oracle.com/en/java/"));
            skills.add(new SkillItem("2", "Spring Boot 3 REST APIs & Dependency Injection", "Framework", true, "Required", "https://spring.io/projects/spring-boot"));
            skills.add(new SkillItem("3", "Spring Data JPA & MySQL Database Design", "Database", true, "Required", "https://spring.io/projects/spring-data-jpa"));
            skills.add(new SkillItem("4", "Spring Security with JWT & OAuth2", "Security", false, "Required", "https://spring.io/projects/spring-security"));
            skills.add(new SkillItem("5", "React & Modern Front-End Integration", "Frontend", true, "Required", "https://react.dev"));
            skills.add(new SkillItem("6", "Docker & Microservices Architecture", "DevOps", false, "Recommended", "https://www.docker.com"));
            skills.add(new SkillItem("7", "System Design & Distributed Caching (Redis)", "Architecture", false, "Advanced", "https://redis.io"));
        }

        dto.setSkills(skills);
        return dto;
    }
}
