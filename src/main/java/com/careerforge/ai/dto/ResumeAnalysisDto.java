package com.careerforge.ai.dto;

import java.util.List;

public class ResumeAnalysisDto {
    private Integer atsScore;
    private Integer formattingScore;
    private List<String> keywordMatches;
    private List<String> missingSkills;
    private List<String> recommendations;

    public ResumeAnalysisDto() {}

    public Integer getAtsScore() { return atsScore; }
    public void setAtsScore(Integer atsScore) { this.atsScore = atsScore; }

    public Integer getFormattingScore() { return formattingScore; }
    public void setFormattingScore(Integer formattingScore) { this.formattingScore = formattingScore; }

    public List<String> getKeywordMatches() { return keywordMatches; }
    public void setKeywordMatches(List<String> keywordMatches) { this.keywordMatches = keywordMatches; }

    public List<String> getMissingSkills() { return missingSkills; }
    public void setMissingSkills(List<String> missingSkills) { this.missingSkills = missingSkills; }

    public List<String> getRecommendations() { return recommendations; }
    public void setRecommendations(List<String> recommendations) { this.recommendations = recommendations; }
}
