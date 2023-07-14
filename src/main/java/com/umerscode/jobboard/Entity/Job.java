package com.umerscode.jobboard.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.GenerationType.*;


@Entity
@Table(name = "jobs")
 @NoArgsConstructor
@Setter
public class Job {
    public Job(String jobNumber, String jobType, int payPerHour,
               String educationLevel, int yearsOfExperience, Company company) {
        this.jobNumber = jobNumber;
        this.jobType = jobType;
        this.payPerHour = payPerHour;
        this.educationLevel = educationLevel;
        this.yearsOfExperience = yearsOfExperience;
        this.company = company;
    }

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Getter
    private Long id;
    @Getter
    private String jobNumber;
    @Getter
    private String jobType;
    @Getter
    private int payPerHour;
    @Getter
    private String educationLevel;
    @Getter
    private int yearsOfExperience;

    @ManyToOne    //(fetch = FetchType.EAGER)
    @JoinColumn(name = "company_id",referencedColumnName = "id")
    private Company company;

    @OneToMany(cascade = ALL, mappedBy = "job")
    private List<SubmittedApplication> submittedApplications = new ArrayList<>();
}
