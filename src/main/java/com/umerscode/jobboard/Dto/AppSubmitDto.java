package com.umerscode.jobboard.Dto;

import com.umerscode.jobboard.Entity.Employee;
import com.umerscode.jobboard.Entity.Job;
import lombok.Data;

@Data
public class AppSubmitDto {
    private Employee employee;
    private Job job;
}
