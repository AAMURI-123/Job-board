package com.umerscode.jobboard.service;

import com.umerscode.jobboard.Dto.RegisterCompanyDto;
import com.umerscode.jobboard.Entity.Company;
import com.umerscode.jobboard.Entity.Employee;
import com.umerscode.jobboard.Entity.Job;

import java.util.List;

public interface CompanyServices {

    List<Employee> getEmployeeByJobType(String jobType);
    List<Job> getAllJobs();
    void deleteJobById(Long id);
    Job updateJob(Long id, Job job);
    Job createNewJob(Job job);
    Company registerCompany(RegisterCompanyDto registerDto);
}
