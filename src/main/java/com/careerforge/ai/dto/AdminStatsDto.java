package com.careerforge.ai.dto;

import java.util.Map;

public class AdminStatsDto {
    private Long totalUsers;
    private Long totalJobs;
    private Long activeJobs;
    private Long totalApplications;
    private Long totalResumes;
    private Long totalInterviews;
    private Map<String, Long> applicationsByStatus;

    public AdminStatsDto() {}

    public Long getTotalUsers() { return totalUsers; }
    public void setTotalUsers(Long totalUsers) { this.totalUsers = totalUsers; }

    public Long getTotalJobs() { return totalJobs; }
    public void setTotalJobs(Long totalJobs) { this.totalJobs = totalJobs; }

    public Long getActiveJobs() { return activeJobs; }
    public void setActiveJobs(Long activeJobs) { this.activeJobs = activeJobs; }

    public Long getTotalApplications() { return totalApplications; }
    public void setTotalApplications(Long totalApplications) { this.totalApplications = totalApplications; }

    public Long getTotalResumes() { return totalResumes; }
    public void setTotalResumes(Long totalResumes) { this.totalResumes = totalResumes; }

    public Long getTotalInterviews() { return totalInterviews; }
    public void setTotalInterviews(Long totalInterviews) { this.totalInterviews = totalInterviews; }

    public Map<String, Long> getApplicationsByStatus() { return applicationsByStatus; }
    public void setApplicationsByStatus(Map<String, Long> applicationsByStatus) { this.applicationsByStatus = applicationsByStatus; }
}
