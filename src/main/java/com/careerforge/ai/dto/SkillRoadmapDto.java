package com.careerforge.ai.dto;

import java.util.List;

public class SkillRoadmapDto {
    
    public static class SkillItem {
        private String id;
        private String name;
        private String category;
        private Boolean isCompleted;
        private String level; // Required, Optional, Advanced
        private String resourceUrl;

        public SkillItem() {}
        public SkillItem(String id, String name, String category, Boolean isCompleted, String level, String resourceUrl) {
            this.id = id;
            this.name = name;
            this.category = category;
            this.isCompleted = isCompleted;
            this.level = level;
            this.resourceUrl = resourceUrl;
        }

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }
        public Boolean getIsCompleted() { return isCompleted; }
        public void setIsCompleted(Boolean isCompleted) { this.isCompleted = isCompleted; }
        public String getLevel() { return level; }
        public void setLevel(String level) { this.level = level; }
        public String getResourceUrl() { return resourceUrl; }
        public void setResourceUrl(String resourceUrl) { this.resourceUrl = resourceUrl; }
    }

    private Long id;
    private String targetRole;
    private String currentLevel;
    private Integer progressPercentage;
    private List<SkillItem> skills;

    public SkillRoadmapDto() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTargetRole() { return targetRole; }
    public void setTargetRole(String targetRole) { this.targetRole = targetRole; }

    public String getCurrentLevel() { return currentLevel; }
    public void setCurrentLevel(String currentLevel) { this.currentLevel = currentLevel; }

    public Integer getProgressPercentage() { return progressPercentage; }
    public void setProgressPercentage(Integer progressPercentage) { this.progressPercentage = progressPercentage; }

    public List<SkillItem> getSkills() { return skills; }
    public void setSkills(List<SkillItem> skills) { this.skills = skills; }
}
