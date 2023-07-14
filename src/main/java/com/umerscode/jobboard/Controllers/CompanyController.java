package com.umerscode.jobboard.Controllers;

import com.umerscode.jobboard.Dto.RegisterCompanyDto;
import com.umerscode.jobboard.Entity.AppUser;
import com.umerscode.jobboard.Entity.Company;
import com.umerscode.jobboard.Entity.Employee;
import com.umerscode.jobboard.Entity.Job;
import com.umerscode.jobboard.repository.CompanyRepo;
import com.umerscode.jobboard.service.CompanyServiceImpl;
import com.umerscode.jobboard.service.CompanyServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/v1/company")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyServiceImpl companyService;
    private final CompanyRepo companyRepo;

    @GetMapping("/findEmployeeByJob/{jobType}")
    @PreAuthorize("hasAuthority('MANAGER')")
    public List<Employee> getEmployeeByJob(@PathVariable("jobType") String jobType){
        return companyService.getEmployeeByJobType(jobType);
    }

    @GetMapping("/jobs")
    @PreAuthorize("hasAuthority('MANAGER')")
    public ResponseEntity<List<Job>> getAllJobs(){
        return ResponseEntity.ok().body(companyService.getAllJobs());
    }

    @DeleteMapping("/job/{id}")
    @PreAuthorize("hasAuthority('MANAGER')")
    public void deleteJobById(@PathVariable("id") Long id){
        companyService.deleteJobById(id);
    }

    @PutMapping("/job/update/{id}")
    @PreAuthorize("hasAuthority('MANAGER')")
    public ResponseEntity<Job> updateJob(@PathVariable("id") Long id,
                                         @RequestBody Job job){
        return ResponseEntity.ok().body(companyService.updateJob(id,job));
    }

    @PostMapping("/create/job")
    @PreAuthorize("hasAuthority('MANAGER')")
    public ResponseEntity<Job> createNewJob(@RequestBody Job job){
        return new ResponseEntity<>(companyService.createNewJob(job), CREATED);
    }

    @GetMapping("/profile")
    @PreAuthorize("hasAuthority('MANAGER')")
    public ResponseEntity<Company> getCompanyProfile() {
        AppUser currentUser = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok().body(companyRepo.findByEmail(currentUser.getEmail()));
    }

    @PostMapping("/register")
    public ResponseEntity<Company> registerCompany(@RequestBody RegisterCompanyDto registerDto){
        return new ResponseEntity<>(companyService.registerCompany(registerDto), CREATED);
    }
}
