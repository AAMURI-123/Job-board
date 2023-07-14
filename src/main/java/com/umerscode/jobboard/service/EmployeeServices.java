package com.umerscode.jobboard.service;

import com.umerscode.jobboard.Dto.AppSubmitDto;
import com.umerscode.jobboard.Dto.RegisterEmployeeDto;
import com.umerscode.jobboard.Entity.Company;
import com.umerscode.jobboard.Entity.Employee;
import com.umerscode.jobboard.Entity.Job;
import com.umerscode.jobboard.Entity.SubmittedApplication;

import java.util.List;

public interface EmployeeServices {

     List<Job> getAllJobs();
     List<Job> getJobByType(String jobType);
     Employee updateEmployee(Long id, Employee employee);
     Employee registerEmployee(RegisterEmployeeDto registerDto);
     SubmittedApplication submitApplication(SubmittedApplication submitApp);

}
