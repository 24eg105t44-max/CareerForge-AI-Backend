package com.careerforge.ai.dto;

import java.util.List;

public class InterviewDto {
    
    public static class Question {
        private Long id;
        private String category;
        private String difficulty;
        private String questionText;
        private String sampleAnswer;
        private List<String> keyKeywords;

        public Question() {}
        public Question(Long id, String category, String difficulty, String questionText, String sampleAnswer, List<String> keyKeywords) {
            this.id = id;
            this.category = category;
            this.difficulty = difficulty;
            this.questionText = questionText;
            this.sampleAnswer = sampleAnswer;
            this.keyKeywords = keyKeywords;
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }
        public String getDifficulty() { return difficulty; }
        public void setDifficulty(String difficulty) { this.difficulty = difficulty; }
        public String getQuestionText() { return questionText; }
        public void setQuestionText(String questionText) { this.questionText = questionText; }
        public String getSampleAnswer() { return sampleAnswer; }
        public void setSampleAnswer(String sampleAnswer) { this.sampleAnswer = sampleAnswer; }
        public List<String> getKeyKeywords() { return keyKeywords; }
        public void setKeyKeywords(List<String> keyKeywords) { this.keyKeywords = keyKeywords; }
    }

    public static class SubmissionRequest {
        private String targetRole;
        private String category;
        private String difficulty;
        private Long questionId;
        private String userQuestion;
        private String userAnswer;

        public SubmissionRequest() {}

        public String getTargetRole() { return targetRole; }
        public void setTargetRole(String targetRole) { this.targetRole = targetRole; }
        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }
        public String getDifficulty() { return difficulty; }
        public void setDifficulty(String difficulty) { this.difficulty = difficulty; }
        public Long getQuestionId() { return questionId; }
        public void setQuestionId(Long questionId) { this.questionId = questionId; }
        public String getUserQuestion() { return userQuestion; }
        public void setUserQuestion(String userQuestion) { this.userQuestion = userQuestion; }
        public String getUserAnswer() { return userAnswer; }
        public void setUserAnswer(String userAnswer) { this.userAnswer = userAnswer; }
    }

    public static class SubmissionFeedback {
        private Integer score;
        private String feedback;
        private List<String> strengths;
        private List<String> areasToImprove;
        private String suggestedAnswer;

        public SubmissionFeedback() {}

        public Integer getScore() { return score; }
        public void setScore(Integer score) { this.score = score; }
        public String getFeedback() { return feedback; }
        public void setFeedback(String feedback) { this.feedback = feedback; }
        public List<String> getStrengths() { return strengths; }
        public void setStrengths(List<String> strengths) { this.strengths = strengths; }
        public List<String> getAreasToImprove() { return areasToImprove; }
        public void setAreasToImprove(List<String> areasToImprove) { this.areasToImprove = areasToImprove; }
        public String getSuggestedAnswer() { return suggestedAnswer; }
        public void setSuggestedAnswer(String suggestedAnswer) { this.suggestedAnswer = suggestedAnswer; }
    }
}
