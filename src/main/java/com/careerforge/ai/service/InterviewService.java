package com.careerforge.ai.service;

import com.careerforge.ai.dto.InterviewDto.*;
import com.careerforge.ai.entity.InterviewSession;
import com.careerforge.ai.entity.User;
import com.careerforge.ai.repository.InterviewSessionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class InterviewService {

    private final InterviewSessionRepository interviewSessionRepository;
    private final AuthService authService;
    private final ObjectMapper objectMapper;

    private static final List<Question> QUESTION_BANK = Arrays.asList(
            // Technical - Java & Spring
            new Question(1L, "Technical", "Easy", "What is the difference between JVM, JRE, and JDK?",
                    "JDK is the development kit containing tools and JRE. JRE provides the execution environment containing JVM and core libraries. JVM executes Java bytecode.",
                    Arrays.asList("jvm", "jre", "jdk", "bytecode", "development", "runtime")),
            new Question(2L, "Technical", "Medium", "How does Spring Boot handle Dependency Injection and Bean Lifecycle?",
                    "Spring Boot uses ApplicationContext to manage bean lifecycles. DI occurs via constructor or setter injection using @Autowired or @Component annotations.",
                    Arrays.asList("dependency injection", "bean", "autowired", "applicationcontext", "lifecycle", "inversion of control")),
            new Question(3L, "Technical", "Hard", "Explain the internal workings of HashMap and how hash collisions are resolved in Java 8+.",
                    "HashMap uses an array of Node buckets. Hash code determines bucket index. Collisions are handled via linked list, which upgrades to a Red-Black Tree when bucket size exceeds 8 (TREEIFY_THRESHOLD).",
                    Arrays.asList("hashcode", "bucket", "collision", "linked list", "red-black tree", "treeify", "equals")),

            // Technical - React & Frontend
            new Question(4L, "Technical", "Easy", "What are React Hooks and why were they introduced?",
                    "Hooks like useState and useEffect allow functional components to manage state and lifecycle features without writing ES6 class components.",
                    Arrays.asList("usestate", "useeffect", "functional component", "state", "lifecycle", "class")),
            new Question(5L, "Technical", "Medium", "Explain Virtual DOM and React Reconciliation process.",
                    "React maintains a Virtual DOM in memory. On state changes, it creates a new Virtual DOM tree, diffs it with the previous tree using the Diffing algorithm, and efficiently updates only changed real DOM nodes.",
                    Arrays.asList("virtual dom", "diffing", "reconciliation", "re-render", "performance", "real dom")),

            // Behavioral
            new Question(6L, "Behavioral", "Medium", "Tell me about a time you faced a tough technical challenge and how you resolved it.",
                    "Focus on using the STAR method (Situation, Task, Action, Result). Highlight problem diagnosis, team communication, and measurable outcomes.",
                    Arrays.asList("star", "challenge", "communication", "solution", "result", "teamwork")),

            // HR
            new Question(7L, "HR", "Easy", "Where do you see yourself in 3 to 5 years as a Software Engineer?",
                    "Express enthusiasm for continuous learning, mastering architectural design, taking on leadership responsibility, and adding high business value.",
                    Arrays.asList("learning", "growth", "leadership", "architecture", "impact", "skills"))
    );

    public InterviewService(InterviewSessionRepository interviewSessionRepository, AuthService authService) {
        this.interviewSessionRepository = interviewSessionRepository;
        this.authService = authService;
        this.objectMapper = new ObjectMapper();
    }

    public List<Question> getQuestions(String category, String difficulty) {
        return QUESTION_BANK.stream()
                .filter(q -> category == null || category.equalsIgnoreCase("All") || q.getCategory().equalsIgnoreCase(category))
                .filter(q -> difficulty == null || difficulty.equalsIgnoreCase("All") || q.getDifficulty().equalsIgnoreCase(difficulty))
                .collect(Collectors.toList());
    }

    @Transactional
    public SubmissionFeedback evaluateAnswer(SubmissionRequest request) {
        Question matchingQ = QUESTION_BANK.stream()
                .filter(q -> q.getId() != null && q.getId().equals(request.getQuestionId()))
                .findFirst()
                .orElse(null);

        String answerText = request.getUserAnswer() != null ? request.getUserAnswer().trim().toLowerCase() : "";
        List<String> keywords = matchingQ != null ? matchingQ.getKeyKeywords() : Arrays.asList("solution", "logic", "design");

        long matchedCount = keywords.stream().filter(kw -> answerText.contains(kw.toLowerCase())).count();
        int baseScore = (int) Math.round(((double) matchedCount / keywords.size()) * 70);
        int lengthBonus = Math.min(30, answerText.length() / 15);

        int totalScore = Math.min(100, Math.max(35, baseScore + lengthBonus));

        List<String> strengths = new ArrayList<>();
        List<String> areasToImprove = new ArrayList<>();

        if (answerText.length() > 100) {
            strengths.add("Provided a detailed explanation with proper context.");
        }
        if (matchedCount > 0) {
            strengths.add("Successfully incorporated key technical terms into your response.");
        }

        if (matchedCount < keywords.size() / 2) {
            areasToImprove.add("Try incorporating more specific terms like: " + String.join(", ", keywords));
        }
        if (answerText.length() < 60) {
            areasToImprove.add("Elaborate slightly more on practical implementation details and trade-offs.");
        }

        SubmissionFeedback feedback = new SubmissionFeedback();
        feedback.setScore(totalScore);
        feedback.setStrengths(strengths.isEmpty() ? List.of("Clear concise structure") : strengths);
        feedback.setAreasToImprove(areasToImprove.isEmpty() ? List.of("Add a real-world code example") : areasToImprove);
        feedback.setSuggestedAnswer(matchingQ != null ? matchingQ.getSampleAnswer() : "Be clear, structured, and mention key algorithms or principles.");
        feedback.setFeedback(totalScore >= 80 ? "Excellent answer! Demonstrates strong conceptual understanding." :
                totalScore >= 60 ? "Good response. Include deeper technical terminology for senior roles." :
                "Needs improvement. Review the sample answer and try emphasizing core mechanisms.");

        // Save session in DB
        try {
            User currentUser = authService.getCurrentUser();
            InterviewSession session = new InterviewSession();
            session.setUser(currentUser);
            session.setTargetRole(request.getTargetRole() != null ? request.getTargetRole() : "Software Engineer");
            session.setCategory(request.getCategory());
            session.setDifficulty(request.getDifficulty());
            session.setTotalQuestions(1);
            session.setOverallScore(totalScore);
            session.setQuestionsAndAnswersJson(objectMapper.writeValueAsString(request));
            session.setFeedbackSummary(feedback.getFeedback());
            interviewSessionRepository.save(session);
        } catch (Exception ignored) {}

        return feedback;
    }

    public List<InterviewSession> getPracticeHistory() {
        User currentUser = authService.getCurrentUser();
        return interviewSessionRepository.findTop5ByUserIdOrderByCompletedAtDesc(currentUser.getId());
    }
}
