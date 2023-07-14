package com.umerscode.jobboard.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.*;
import static jakarta.persistence.GenerationType.*;

@Entity
@Table(name = "employees")
@NoArgsConstructor
@Data
@ToString
public class Employee {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    @Column(unique = true)
    private String employeeCode;
    private String firstName;
    private String lastName;
    @Column(unique = true)
    private String email;
    private String phoneNo;
    private String jobType;
    private String address;
    private int yearsOfExperience;
    private String educationLevel;

    @OneToMany(cascade = ALL, mappedBy = "employee")
    private List<SubmittedApplication> submittedApplications = new ArrayList<>();

    public Employee(Long id, String employeeCode, String firstName, String lastName, String email, String phoneNo, String jobType, String address, int yearsOfExperience, String educationLevel) {
        this.id = id;
        this.employeeCode = employeeCode;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNo = phoneNo;
        this.jobType = jobType;
        this.address = address;
        this.yearsOfExperience = yearsOfExperience;
        this.educationLevel = educationLevel;
    }
}
