package com.umerscode.jobboard.Controllers;

import com.umerscode.jobboard.Dto.AppSubmitDto;
import com.umerscode.jobboard.Dto.RegisterEmployeeDto;
import com.umerscode.jobboard.Entity.AppUser;
import com.umerscode.jobboard.Entity.Employee;
import com.umerscode.jobboard.Entity.Job;
import com.umerscode.jobboard.Entity.SubmittedApplication;
import com.umerscode.jobboard.repository.EmployeeRepo;
import com.umerscode.jobboard.service.EmployeeServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/v1/employee")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeRepo employeeRepo;
    private final EmployeeServiceImpl employeeService;

    @GetMapping("/profile")
    @PreAuthorize("hasAuthority('EMPLOYEE')")
    public ResponseEntity<Employee> getEmployeeProfile(){
        AppUser currentUser = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    return ResponseEntity.ok().body(employeeRepo.findByEmail(currentUser.getEmail()));
    }

    @GetMapping("/jobs")
   @PreAuthorize("hasAuthority('EMPLOYEE')")
    public ResponseEntity<List<Job>> getAllJobs(){
        return ResponseEntity.ok().body(employeeService.getAllJobs());
    }

    @GetMapping("/job/{type}")
    @PreAuthorize("hasAuthority('EMPLOYEE')")
    public ResponseEntity<List<Job>> getJobsByType(@PathVariable("type") String type){
        return ResponseEntity.ok().body(employeeService.getJobByType(type));
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('EMPLOYEE')")
    public ResponseEntity<Employee> updateEmployeeById(@PathVariable("id") Long id,
                                                       @RequestBody Employee employee){
        return ResponseEntity.ok().body(employeeService.updateEmployee(id, employee));
    }

    @PostMapping("/register")
    public ResponseEntity<Employee> registerEmployee(@RequestBody RegisterEmployeeDto registerDto){
        return new ResponseEntity<>(employeeService.registerEmployee(registerDto), CREATED);
    }

    @PostMapping("/application-submit")
    @PreAuthorize("hasAuthority('EMPLOYEE')")
    public ResponseEntity<SubmittedApplication> submitApplication(@RequestBody SubmittedApplication submittedApplication){

        return new ResponseEntity<>(employeeService.submitApplication(submittedApplication), CREATED);
    }
}
