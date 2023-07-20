package com.umerscode.jobboard.service;

import com.umerscode.jobboard.Dto.AppSubmitDto;
import com.umerscode.jobboard.Dto.RegisterEmployeeDto;
import com.umerscode.jobboard.Entity.*;
import com.umerscode.jobboard.repository.EmployeeRepo;
import com.umerscode.jobboard.repository.JobRepo;
import com.umerscode.jobboard.repository.SubmitAppRepo;
import com.umerscode.jobboard.repository.UserRepo;
import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static com.umerscode.jobboard.Entity.Role.*;

@Service
@RequiredArgsConstructor
@Transactional
public class EmployeeServiceImpl implements EmployeeServices {

    private final JobRepo jobRepo;
    private final EmployeeRepo employeeRepo;
    private final UserRepo userRepo;
    private final SubmitAppRepo submitAppRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<Job> getAllJobs() {
        return jobRepo.findAll();
    }

    @Override
    public List<Job> getJobByType(String jobType) {
        return jobRepo.findByJobType(jobType).orElseThrow(()->new IllegalStateException("No "+jobType+" job found"));
    }

    @Override
    public Employee updateEmployee(Long id, Employee employee) {
        Employee employeeFromDb = employeeRepo.findById(id)
                .orElseThrow(()-> new IllegalStateException("employee with id "+id+" not found"));
       employeeFromDb.setPhoneNo(employee.getPhoneNo());
       employeeFromDb.setJobType(employee.getJobType());
       employeeFromDb.setAddress(employee.getAddress());
       employeeFromDb.setYearsOfExperience(employee.getYearsOfExperience());
       employeeFromDb.setEducationLevel(employee.getEducationLevel());
        return employeeFromDb;
    }

    @Override
    public Employee registerEmployee(@NotNull RegisterEmployeeDto registerDto) {

      String uuid = UUID.randomUUID().toString().substring(0,8);
//        while(employeeRepo.findByEmployeeCode(uuid).isPresent())
//        { uuid = UUID.randomUUID().toString().substring(0,8);}

        if(userRepo.findUserByEmail(registerDto.getUser().getEmail()).isPresent())
           throw new IllegalStateException("User email is taken");

        AppUser user = registerDto.getUser();
        user.setPassword(passwordEncoder.encode(registerDto.getUser().getPassword()));
        user.setRole(EMPLOYEE);
       userRepo.save(user);

        Employee employee = registerDto.getEmployee();
            employee.setEmployeeCode(uuid);
        return employeeRepo.save(employee);
    }

    @Override
    public SubmittedApplication submitApplication(SubmittedApplication submittedApp) {
        String uuid  = UUID.randomUUID().toString().substring(0,8);
        AppUser activeUser = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        submittedApp.setApplicationNumber(uuid);
        submittedApp.setEmployee(employeeRepo.findByEmail(activeUser.getEmail()));

        return submitAppRepo.save(submittedApp);
    }
}
