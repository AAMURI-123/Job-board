package com.umerscode.jobboard.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.*;
import static jakarta.persistence.GenerationType.*;

@Entity
@Table(name = "company")
@NoArgsConstructor
@Getter @Setter
@ToString
public class Company {

    public Company(Long id,String name, String email, String phoneNo, String address) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phoneNo = phoneNo;
        this.address = address;
    }

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String name;
    @Column(unique = true)
    private String email;
    private String phoneNo;
    private String address;

    @OneToMany(cascade = ALL,mappedBy = "company",fetch = FetchType.EAGER)
    private List<Job> jobs  = new ArrayList<>();


}
