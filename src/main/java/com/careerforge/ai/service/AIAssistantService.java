package com.careerforge.ai.service;

import com.careerforge.ai.dto.AIAssistantDto.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AIAssistantService {

    @Value("${app.ai.openai-key:}")
    private String apiKey;

    public ChatResponse processChatQuery(ChatRequest request) {
        String msg = request.getMessage() != null ? request.getMessage().trim().toLowerCase() : "";
        String roleContext = request.getContextRole() != null ? request.getContextRole() : "Software Engineer";

        // If OpenAI API Key is provided, real OpenAI integration could be called here.
        // Otherwise, use our intelligent rule engine.

        String reply;
        List<String> followups = new ArrayList<>();
        List<String> resources = new ArrayList<>();

        if (msg.contains("resume") || msg.contains("ats")) {
            reply = "To make your resume ATS-friendly and stand out to recruiters:\n" +
                    "1. Use standard section headers (Experience, Education, Skills, Projects).\n" +
                    "2. Include exact keywords from the job description (e.g., 'Spring Boot', 'REST API', 'MySQL').\n" +
                    "3. Format achievements with numbers: 'Reduced query execution time by 40% using database indexing'.\n" +
                    "4. Avoid tables, images, or non-standard fonts that ATS parsers fail to extract.";
            followups.add("How do I write impactful bullet points?");
            followups.add("Analyze my saved resume for ATS score");
            followups.add("What skills are missing for a Senior Java Developer?");
            resources.add("CareerForge Resume Builder Tool");
            resources.add("ATS Formatting Checklist Guide");
        } else if (msg.contains("interview") || msg.contains("question")) {
            reply = "Here are top strategy tips for technical and behavioral interviews:\n" +
                    "1. **STAR Method**: Frame answers as Situation, Task, Action, and Result.\n" +
                    "2. **System Design**: For mid to senior roles, focus on scalability, load balancing, caching (Redis), and data persistence.\n" +
                    "3. **Coding Rounds**: Always talk out loud while solving problems. Explain time & space complexity (Big O).";
            followups.add("Practice Mock Java Technical Interview");
            followups.add("Common behavioral questions at top tech companies");
            resources.add("CareerForge Mock Interview Simulator");
            resources.add("System Design Primer");
        } else if (msg.contains("roadmap") || msg.contains("skills") || msg.contains("learn")) {
            reply = "Here is a high-level roadmap for " + roleContext + ":\n" +
                    "• **Phase 1: Core Fundamentals**: Deep dive into Java 21, Data Structures & Algorithms, Git.\n" +
                    "• **Phase 2: Backend Mastery**: Spring Boot 3, Hibernate JPA, RESTful API design, PostgreSQL/MySQL.\n" +
                    "• **Phase 3: Modern Frontend**: React, Tailwind CSS, State Management.\n" +
                    "• **Phase 4: Cloud & DevOps**: Docker containerization, CI/CD with GitHub Actions, AWS basics.";
            followups.add("Show my interactive Skill Roadmap");
            followups.add("What projects should I build for my portfolio?");
            resources.add("CareerForge Skill Roadmap Engine");
        } else if (msg.contains("salary") || msg.contains("negotiat")) {
            reply = "When negotiating salary offers:\n" +
                    "1. Research market rates using glassdoor, levels.fyi, or ambitionbox for your location and experience tier.\n" +
                    "2. Always anchor higher than your minimum target, highlighting unique project contributions and certifications.\n" +
                    "3. Consider the total compensation package: base pay, performance bonuses, remote work flexibility, and learning stipends.";
            followups.add("What is the average salary for Java Full Stack Developers?");
            followups.add("How to politely ask for a salary review?");
            resources.add("Tech Salary Benchmark Report 2026");
        } else {
            reply = "Welcome to CareerForge AI! I can assist you with:\n" +
                    "• ATS Resume Optimization & Keyword Analysis\n" +
                    "• Technical & HR Mock Interview Preparation\n" +
                    "• Custom Skill Roadmaps for " + roleContext + "\n" +
                    "• Job Application Strategies & Portfolio Projects\n\n" +
                    "How can I help advance your career today?";
            followups.add("Review my resume score");
            followups.add("Give me mock interview questions for Spring Boot");
            followups.add("Show job vacancies matching my profile");
            resources.add("CareerForge Platform Overview");
        }

        String engineType = (apiKey != null && !apiKey.isBlank()) ? "OPENAI_API" : "RULE_ENGINE";
        return new ChatResponse(reply, followups, resources, engineType);
    }
}
