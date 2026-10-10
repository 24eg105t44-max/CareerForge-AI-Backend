package com.careerforge.ai.dto;

import com.careerforge.ai.entity.JobApplication.ApplicationStatus;

public class ApplicationStatusUpdateDto {
    private ApplicationStatus status;
    private String adminNotes;

    public ApplicationStatusUpdateDto() {}

    public ApplicationStatus getStatus() { return status; }
    public void setStatus(ApplicationStatus status) { this.status = status; }

    public String getAdminNotes() { return adminNotes; }
    public void setAdminNotes(String adminNotes) { this.adminNotes = adminNotes; }
}
