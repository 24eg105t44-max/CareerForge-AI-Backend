package com.careerforge.ai.service;

import com.careerforge.ai.dto.ResumeAnalysisDto;
import com.careerforge.ai.dto.ResumeDto;
import com.careerforge.ai.entity.Resume;
import com.careerforge.ai.entity.ResumeAnalysis;
import com.careerforge.ai.entity.User;
import com.careerforge.ai.repository.ResumeAnalysisRepository;
import com.careerforge.ai.repository.ResumeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ResumeService {

    private final ResumeRepository resumeRepository;
    private final ResumeAnalysisRepository resumeAnalysisRepository;
    private final AuthService authService;
    private final ObjectMapper objectMapper;

    private static final List<String> HIGH_DEMAND_KEYWORDS = Arrays.asList(
            "Java", "Spring Boot", "React", "JavaScript", "TypeScript", "REST API",
            "SQL", "MySQL", "Git", "Microservices", "Docker", "Kubernetes",
            "AWS", "CI/CD", "Tailwind CSS", "Data Structures", "System Design", "Unit Testing"
    );

    public ResumeService(ResumeRepository resumeRepository,
                         ResumeAnalysisRepository resumeAnalysisRepository,
                         AuthService authService) {
        this.resumeRepository = resumeRepository;
        this.resumeAnalysisRepository = resumeAnalysisRepository;
        this.authService = authService;
        this.objectMapper = new ObjectMapper();
    }

    public ResumeDto getLatestResume() {
        User currentUser = authService.getCurrentUser();
        return resumeRepository.findTopByUserIdOrderByUpdatedAtDesc(currentUser.getId())
                .map(this::mapToDto)
                .orElseGet(() -> {
                    ResumeDto empty = new ResumeDto();
                    empty.setTitle("Software Engineer Resume");
                    empty.setSummary("Passionate full-stack developer with expertise in Java, Spring Boot, and modern web applications.");
                    empty.setEducationJson("[{\"institution\":\"State Technological University\",\"degree\":\"B.Tech Computer Science\",\"year\":\"2022-2026\",\"gpa\":\"8.8/10\"}]");
                    empty.setSkillsJson("[\"Java 21\", \"Spring Boot\", \"React\", \"REST APIs\", \"MySQL\", \"Tailwind CSS\", \"Git\"]");
                    empty.setExperienceJson("[{\"company\":\"Tech Innovators Inc.\",\"role\":\"Software Engineering Intern\",\"duration\":\"Jun 2025 - Aug 2025\",\"details\":\"Developed microservices using Java Spring Boot and built responsive dashboards in React.\"}]");
                    empty.setProjectsJson("[{\"name\":\"CareerForge AI\",\"description\":\"Intelligent career development platform using Spring Boot and React.\",\"techStack\":\"Java, Spring Boot, React, Tailwind, MySQL\"}]");
                    empty.setCertificationsJson("[\"AWS Certified Cloud Practitioner\", \"Oracle Certified Professional: Java SE 21\"]");
                    empty.setScore(85);
                    return empty;
                });
    }

    @Transactional
    public ResumeDto saveResume(ResumeDto dto) {
        User currentUser = authService.getCurrentUser();
        Resume resume = resumeRepository.findTopByUserIdOrderByUpdatedAtDesc(currentUser.getId())
                .orElseGet(() -> {
                    Resume newR = new Resume();
                    newR.setUser(currentUser);
                    return newR;
                });

        resume.setTitle(dto.getTitle() != null ? dto.getTitle() : "My Professional Resume");
        resume.setSummary(dto.getSummary());
        resume.setEducationJson(dto.getEducationJson());
        resume.setExperienceJson(dto.getExperienceJson());
        resume.setSkillsJson(dto.getSkillsJson());
        resume.setProjectsJson(dto.getProjectsJson());
        resume.setCertificationsJson(dto.getCertificationsJson());

        // Analyze and set score
        ResumeAnalysisDto analysis = analyzeResumeContent(resume);
        resume.setScore(analysis.getAtsScore());

        Resume saved = resumeRepository.save(resume);

        // Save analysis entity
        try {
            ResumeAnalysis ra = new ResumeAnalysis();
            ra.setUser(currentUser);
            ra.setAtsScore(analysis.getAtsScore());
            ra.setFormattingScore(analysis.getFormattingScore());
            ra.setKeywordMatchesJson(objectMapper.writeValueAsString(analysis.getKeywordMatches()));
            ra.setMissingSkillsJson(objectMapper.writeValueAsString(analysis.getMissingSkills()));
            ra.setRecommendationsJson(objectMapper.writeValueAsString(analysis.getRecommendations()));
            resumeAnalysisRepository.save(ra);
        } catch (Exception ignored) {}

        return mapToDto(saved);
    }

    public ResumeAnalysisDto analyzeCurrentResume() {
        User currentUser = authService.getCurrentUser();
        Optional<Resume> resumeOpt = resumeRepository.findTopByUserIdOrderByUpdatedAtDesc(currentUser.getId());
        if (resumeOpt.isPresent()) {
            return analyzeResumeContent(resumeOpt.get());
        } else {
            // Default sample analysis
            Resume defaultResume = new Resume();
            defaultResume.setSummary("Entry-level developer skilled in Java, SQL, and React");
            defaultResume.setSkillsJson("[\"Java\", \"Spring Boot\", \"React\", \"SQL\"]");
            return analyzeResumeContent(defaultResume);
        }
    }

    public ResumeAnalysisDto analyzeResumeContent(Resume resume) {
        String fullContent = String.format("%s %s %s %s %s %s",
                resume.getSummary() != null ? resume.getSummary() : "",
                resume.getEducationJson() != null ? resume.getEducationJson() : "",
                resume.getExperienceJson() != null ? resume.getExperienceJson() : "",
                resume.getSkillsJson() != null ? resume.getSkillsJson() : "",
                resume.getProjectsJson() != null ? resume.getProjectsJson() : "",
                resume.getCertificationsJson() != null ? resume.getCertificationsJson() : ""
        ).toLowerCase();

        List<String> matchedKeywords = new ArrayList<>();
        List<String> missingSkills = new ArrayList<>();

        for (String kw : HIGH_DEMAND_KEYWORDS) {
            if (fullContent.contains(kw.toLowerCase())) {
                matchedKeywords.add(kw);
            } else {
                missingSkills.add(kw);
            }
        }

        int keywordScore = (int) Math.round(((double) matchedKeywords.size() / HIGH_DEMAND_KEYWORDS.size()) * 60);

        int structureScore = 0;
        if (resume.getSummary() != null && resume.getSummary().length() > 50) structureScore += 10;
        if (resume.getEducationJson() != null && !resume.getEducationJson().isBlank()) structureScore += 10;
        if (resume.getExperienceJson() != null && !resume.getExperienceJson().isBlank()) structureScore += 10;
        if (resume.getProjectsJson() != null && !resume.getProjectsJson().isBlank()) structureScore += 10;

        int totalAtsScore = Math.min(100, Math.max(40, keywordScore + structureScore));
        int formattingScore = Math.min(98, 80 + (structureScore / 2));

        List<String> recommendations = new ArrayList<>();
        if (matchedKeywords.size() < 5) {
            recommendations.add("Add more core technical keywords like Spring Boot, REST API, or Cloud Services to boost your ATS match percentage.");
        }
        if (resume.getSummary() == null || resume.getSummary().length() < 60) {
            recommendations.add("Expand your summary into 3-4 impactful sentences highlighting quantifiable metrics (e.g., 'Improved API response speed by 35%').");
        }
        if (missingSkills.contains("Docker") || missingSkills.contains("CI/CD")) {
            recommendations.add("Including modern DevOps tools (Docker, GitHub Actions, CI/CD pipelines) significantly increases shortlist rates.");
        }
        recommendations.add("Use strong action verbs like 'Engineered', 'Optimized', 'Architected' instead of 'Worked on' or 'Helped with'.");

        ResumeAnalysisDto dto = new ResumeAnalysisDto();
        dto.setAtsScore(totalAtsScore);
        dto.setFormattingScore(formattingScore);
        dto.setKeywordMatches(matchedKeywords);
        dto.setMissingSkills(missingSkills);
        dto.setRecommendations(recommendations);
        return dto;
    }

    private ResumeDto mapToDto(Resume resume) {
        ResumeDto dto = new ResumeDto();
        dto.setId(resume.getId());
        dto.setTitle(resume.getTitle());
        dto.setSummary(resume.getSummary());
        dto.setEducationJson(resume.getEducationJson());
        dto.setExperienceJson(resume.getExperienceJson());
        dto.setSkillsJson(resume.getSkillsJson());
        dto.setProjectsJson(resume.getProjectsJson());
        dto.setCertificationsJson(resume.getCertificationsJson());
        dto.setScore(resume.getScore());
        return dto;
    }
}
