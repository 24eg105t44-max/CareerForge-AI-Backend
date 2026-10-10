package com.careerforge.ai.dto;

import java.util.List;

public class AIAssistantDto {

    public static class ChatRequest {
        private String message;
        private String contextRole;

        public ChatRequest() {}

        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }

        public String getContextRole() { return contextRole; }
        public void setContextRole(String contextRole) { this.contextRole = contextRole; }
    }

    public static class ChatResponse {
        private String reply;
        private List<String> suggestedFollowups;
        private List<String> recommendedResources;
        private String engineType; // "AI_MODELS" or "RULE_ENGINE"

        public ChatResponse() {}
        public ChatResponse(String reply, List<String> suggestedFollowups, List<String> recommendedResources, String engineType) {
            this.reply = reply;
            this.suggestedFollowups = suggestedFollowups;
            this.recommendedResources = recommendedResources;
            this.engineType = engineType;
        }

        public String getReply() { return reply; }
        public void setReply(String reply) { this.reply = reply; }

        public List<String> getSuggestedFollowups() { return suggestedFollowups; }
        public void setSuggestedFollowups(List<String> suggestedFollowups) { this.suggestedFollowups = suggestedFollowups; }

        public List<String> getRecommendedResources() { return recommendedResources; }
        public void setRecommendedResources(List<String> recommendedResources) { this.recommendedResources = recommendedResources; }

        public String getEngineType() { return engineType; }
        public void setEngineType(String engineType) { this.engineType = engineType; }
    }
}
