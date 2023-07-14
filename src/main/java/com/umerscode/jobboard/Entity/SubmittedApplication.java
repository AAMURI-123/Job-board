package com.umerscode.jobboard.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static jakarta.persistence.GenerationType.*;

@Entity
@Table(name = "submitted_applications")
 @Data
@NoArgsConstructor
public class SubmittedApplication {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(unique = true)
   // @Getter
    private String applicationNumber;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "job_id")
    private Job job;


}
