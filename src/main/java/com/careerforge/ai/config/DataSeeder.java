package com.careerforge.ai.config;

import com.careerforge.ai.entity.Job;
import com.careerforge.ai.entity.Job.JobStatus;
import com.careerforge.ai.entity.Job.JobType;
import com.careerforge.ai.entity.Notification;
import com.careerforge.ai.entity.User;
import com.careerforge.ai.entity.User.Role;
import com.careerforge.ai.entity.UserProfile;
import com.careerforge.ai.repository.JobRepository;
import com.careerforge.ai.repository.NotificationRepository;
import com.careerforge.ai.repository.UserProfileRepository;
import com.careerforge.ai.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final JobRepository jobRepository;
    private final NotificationRepository notificationRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(UserRepository userRepository,
                      UserProfileRepository userProfileRepository,
                      JobRepository jobRepository,
                      NotificationRepository notificationRepository,
                      PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userProfileRepository = userProfileRepository;
        this.jobRepository = jobRepository;
        this.notificationRepository = notificationRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            // Seed Admin
            User admin = new User("System Admin", "admin@careerforge.ai", passwordEncoder.encode("Admin@123"), Role.ROLE_ADMIN);
            userRepository.save(admin);
            UserProfile adminProfile = new UserProfile(admin);
            adminProfile.setTitle("Lead Platform Administrator");
            userProfileRepository.save(adminProfile);

            // Seed User
            User user = new User("Alex Morgan", "alex@careerforge.ai", passwordEncoder.encode("User@123"), Role.ROLE_USER);
            User savedUser = userRepository.save(user);

            UserProfile profile = new UserProfile(savedUser);
            profile.setTitle("Full Stack Java Engineer");
            profile.setBio("Passionate B.Tech CSE student specializing in Spring Boot, React, and microservices architecture.");
            profile.setLocation("Bangalore, India");
            profile.setExperienceLevel("0-2 Years");
            profile.setDesiredRole("Java Full Stack Developer");
            profile.setProfileCompletion(85);
            userProfileRepository.save(profile);

            // Notifications
            notificationRepository.save(new Notification(savedUser, "Welcome to CareerForge AI", "Your profile is 85% complete. Upload your resume to unlock AI ATS scoring!", "GENERAL"));
            notificationRepository.save(new Notification(savedUser, "Job Alert: Java Developer", "New position matching your profile: Senior Java Developer at CloudScale Systems.", "JOB_UPDATE"));
        }

        if (jobRepository.count() == 0) {
            Job job1 = new Job();
            job1.setTitle("Java Full Stack Engineer");
            job1.setCompany("Nexus Software Labs");
            job1.setLocation("Bangalore / Remote");
            job1.setType(JobType.FULL_TIME);
            job1.setExperienceLevel("Fresher / 0-2 Years");
            job1.setSalaryRange("₹8,00,000 - ₹14,00,000 PA");
            job1.setCategory("Software Development");
            job1.setRequiredSkills("Java 21, Spring Boot, React, MySQL, REST API");
            job1.setDescription("We are seeking an ambitious Java Full Stack Engineer to design scalable cloud services and modern React user interfaces.");
            job1.setRequirements("• Strong background in Java 17/21 and Spring Framework\n• Proficiency in React and Tailwind CSS\n• Understanding of SQL databases and REST APIs");
            job1.setResponsibilities("• Build robust RESTful APIs using Spring Boot\n• Develop responsive frontend components in React\n• Write clean, testable code with unit tests");
            job1.setStatus(JobStatus.ACTIVE);
            job1.setPostedBy("Nexus Talent Team");

            Job job2 = new Job();
            job2.setTitle("Senior React & Frontend Architect");
            job2.setCompany("Apex Digital Platforms");
            job2.setLocation("Hyderabad, India");
            job2.setType(JobType.FULL_TIME);
            job2.setExperienceLevel("3-5 Years");
            job2.setSalaryRange("₹18,00,000 - ₹28,00,000 PA");
            job2.setCategory("Frontend Development");
            job2.setRequiredSkills("React 18, TypeScript, Redux Toolkit, Tailwind CSS, Vite");
            job2.setDescription("Lead front-end architecture for enterprise SaaS web applications using modern React, TypeScript, and micro-frontends.");
            job2.setRequirements("• 3+ years experience building complex React SPA applications\n• Deep understanding of DOM performance optimization and Web Vitals");
            job2.setResponsibilities("• Architect component libraries and design tokens\n• Mentor junior developers and enforce clean code standards");
            job2.setStatus(JobStatus.ACTIVE);
            job2.setPostedBy("Apex HR");

            Job job3 = new Job();
            job3.setTitle("AI / ML Pipeline Specialist");
            job3.setCompany("Cognitive Tech Solutions");
            job3.setLocation("Pune / Hybrid");
            job3.setType(JobType.REMOTE);
            job3.setExperienceLevel("1-3 Years");
            job3.setSalaryRange("₹12,00,000 - ₹20,00,000 PA");
            job3.setCategory("Data & Artificial Intelligence");
            job3.setRequiredSkills("Python, PyTorch, LangChain, OpenAI APIs, Spring Boot");
            job3.setDescription("Build and integrate Generative AI capabilities, LLM fine-tuning pipelines, and intelligent AI agents into enterprise products.");
            job3.setRequirements("• Hands-on experience with LLMs, RAG architecture, and Vector Databases (Pinecone/Milvus)\n• Knowledge of backend API integration");
            job3.setResponsibilities("• Develop AI agentic workflows\n• Optimize prompt engineering and embedding search latency");
            job3.setStatus(JobStatus.ACTIVE);
            job3.setPostedBy("Cognitive AI Team");

            Job job4 = new Job();
            job4.setTitle("DevOps & Cloud Systems Engineer");
            job4.setCompany("CloudScale Infrastructure");
            job4.setLocation("Chennai, India");
            job4.setType(JobType.FULL_TIME);
            job4.setExperienceLevel("2-4 Years");
            job4.setSalaryRange("₹15,00,000 - ₹22,00,000 PA");
            job4.setCategory("DevOps & Cloud");
            job4.setRequiredSkills("AWS, Docker, Kubernetes, Terraform, GitHub Actions");
            job4.setDescription("Manage multi-cloud Kubernetes clusters, automated CI/CD pipelines, and high-availability infrastructure for microservices.");
            job4.setRequirements("• AWS Certification (Solutions Architect or SysOps)\n• Hands-on Kubernetes deployment experience");
            job4.setResponsibilities("• Maintain zero-downtime CI/CD deployment pipelines\n• Optimize cloud spending and infrastructure security");
            job4.setStatus(JobStatus.ACTIVE);
            job4.setPostedBy("CloudScale Hiring");

            jobRepository.saveAll(Arrays.asList(job1, job2, job3, job4));
        }
    }
}
