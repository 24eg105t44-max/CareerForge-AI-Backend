package com.careerforge.ai.service;

import com.careerforge.ai.dto.UserProfileDto;
import com.careerforge.ai.entity.User;
import com.careerforge.ai.entity.UserProfile;
import com.careerforge.ai.repository.UserProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final AuthService authService;

    public UserProfileService(UserProfileRepository userProfileRepository, AuthService authService) {
        this.userProfileRepository = userProfileRepository;
        this.authService = authService;
    }

    public UserProfileDto getCurrentUserProfile() {
        User currentUser = authService.getCurrentUser();
        UserProfile profile = userProfileRepository.findByUser(currentUser)
                .orElseGet(() -> {
                    UserProfile newProfile = new UserProfile(currentUser);
                    return userProfileRepository.save(newProfile);
                });

        return mapToDto(profile);
    }

    @Transactional
    public UserProfileDto updateProfile(UserProfileDto dto) {
        User currentUser = authService.getCurrentUser();
        UserProfile profile = userProfileRepository.findByUser(currentUser)
                .orElseGet(() -> new UserProfile(currentUser));

        if (dto.getTitle() != null) profile.setTitle(dto.getTitle());
        if (dto.getBio() != null) profile.setBio(dto.getBio());
        if (dto.getLocation() != null) profile.setLocation(dto.getLocation());
        if (dto.getPhone() != null) profile.setPhone(dto.getPhone());
        if (dto.getLinkedinUrl() != null) profile.setLinkedinUrl(dto.getLinkedinUrl());
        if (dto.getGithubUrl() != null) profile.setGithubUrl(dto.getGithubUrl());
        if (dto.getExperienceLevel() != null) profile.setExperienceLevel(dto.getExperienceLevel());
        if (dto.getDesiredRole() != null) profile.setDesiredRole(dto.getDesiredRole());
        if (dto.getResumeUrl() != null) profile.setResumeUrl(dto.getResumeUrl());

        // Calculate profile completion score dynamically
        int score = 20; // Base score for having an account
        if (profile.getTitle() != null && !profile.getTitle().isBlank()) score += 15;
        if (profile.getBio() != null && !profile.getBio().isBlank()) score += 15;
        if (profile.getLocation() != null && !profile.getLocation().isBlank()) score += 10;
        if (profile.getPhone() != null && !profile.getPhone().isBlank()) score += 10;
        if (profile.getLinkedinUrl() != null && !profile.getLinkedinUrl().isBlank()) score += 15;
        if (profile.getDesiredRole() != null && !profile.getDesiredRole().isBlank()) score += 15;

        profile.setProfileCompletion(Math.min(score, 100));

        UserProfile updated = userProfileRepository.save(profile);
        return mapToDto(updated);
    }

    private UserProfileDto mapToDto(UserProfile profile) {
        UserProfileDto dto = new UserProfileDto();
        dto.setId(profile.getId());
        dto.setUserId(profile.getUser().getId());
        dto.setName(profile.getUser().getName());
        dto.setEmail(profile.getUser().getEmail());
        dto.setTitle(profile.getTitle());
        dto.setBio(profile.getBio());
        dto.setLocation(profile.getLocation());
        dto.setPhone(profile.getPhone());
        dto.setLinkedinUrl(profile.getLinkedinUrl());
        dto.setGithubUrl(profile.getGithubUrl());
        dto.setExperienceLevel(profile.getExperienceLevel());
        dto.setDesiredRole(profile.getDesiredRole());
        dto.setProfileCompletion(profile.getProfileCompletion());
        dto.setResumeUrl(profile.getResumeUrl());
        return dto;
    }
}
