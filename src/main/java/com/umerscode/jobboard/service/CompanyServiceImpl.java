package com.umerscode.jobboard.service;

import com.umerscode.jobboard.Dto.RegisterCompanyDto;
import com.umerscode.jobboard.Entity.AppUser;
import com.umerscode.jobboard.Entity.Company;
import com.umerscode.jobboard.Entity.Employee;
import com.umerscode.jobboard.Entity.Job;
import com.umerscode.jobboard.repository.CompanyRepo;
import com.umerscode.jobboard.repository.EmployeeRepo;
import com.umerscode.jobboard.repository.JobRepo;
import com.umerscode.jobboard.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static com.umerscode.jobboard.Entity.Role.EMPLOYEE;
import static com.umerscode.jobboard.Entity.Role.MANAGER;

@Service
@RequiredArgsConstructor
@Transactional
public class CompanyServiceImpl implements CompanyServices{

    private final CompanyRepo companyRepo;
    private final EmployeeRepo employeeRepo;
    private final JobRepo jobRepo;
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<Employee> getEmployeeByJobType(String jobType) {

        if(employeeRepo.findByJobType(jobType).isEmpty())
        throw  new IllegalStateException("No Employee for "+ jobType+" job.");

        return employeeRepo.findByJobType(jobType);
    }

    @Override
    public List<Job> getAllJobs() {
        return jobRepo.findAll();
    }

    @Override
    public void deleteJobById(Long id) {
        jobRepo.findById(id).orElseThrow(()-> new IllegalStateException("Job with id "+ id+" not found"));
        jobRepo.deleteById(id);
    }

    @Override
    public Job updateJob(Long id, Job job) {
    Job jobFromDb = jobRepo.findById(id).orElseThrow(()-> new IllegalStateException("Job with id "+ id+" not found"));
        jobFromDb.setJobType(job.getJobType());
        jobFromDb.setPayPerHour(job.getPayPerHour());
        jobFromDb.setEducationLevel(job.getEducationLevel());
        jobFromDb.setYearsOfExperience(job.getYearsOfExperience());

        return jobFromDb;
    }

    @Override
    public Job createNewJob(Job job) {
        String uuid = UUID.randomUUID().toString().substring(0,8);
        while(jobRepo.findByJobNumber(uuid).isPresent())
        { uuid = UUID.randomUUID().toString().substring(0,8);}
        AppUser currentUser = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Company company = companyRepo.findByEmail(currentUser.getEmail());
        job.setCompany(company);

        job.setJobNumber(uuid);
        return jobRepo.save(job);
    }

    @Override
    public Company registerCompany(RegisterCompanyDto registerDto) {
        if(userRepo.findUserByEmail(registerDto.getUser().getEmail()).isPresent())
            throw new IllegalStateException("User email is taken");

        AppUser user = registerDto.getUser();
        user.setPassword(passwordEncoder.encode(registerDto.getUser().getPassword()));
        user.setRole(MANAGER);
        userRepo.save(user);

        Company company = registerDto.getCompany();
        return companyRepo.save(company);
    }


}
