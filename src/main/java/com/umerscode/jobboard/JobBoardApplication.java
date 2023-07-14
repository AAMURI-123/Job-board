package com.umerscode.jobboard;

import com.umerscode.jobboard.Entity.*;
import com.umerscode.jobboard.repository.CompanyRepo;
import com.umerscode.jobboard.repository.EmployeeRepo;
import com.umerscode.jobboard.repository.JobRepo;
import com.umerscode.jobboard.repository.UserRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.UUID;

import static com.umerscode.jobboard.Entity.Role.*;

@SpringBootApplication
public class JobBoardApplication {


	public static void main(String[] args) {
		SpringApplication.run(JobBoardApplication.class, args);
	}


//	@Bean
//	CommandLineRunner commandLineRunner(UserRepo userRepo, EmployeeRepo employeeRepo
//									,PasswordEncoder passwordEncoder,CompanyRepo companyRepo, JobRepo jobRepo){
//		return args -> {
//			AppUser umer = new AppUser(null,"umer@gmail.com",passwordEncoder.encode("pass"), EMPLOYEE);
//			AppUser aamuri = new AppUser(null,"medtronic@gmail.com",passwordEncoder.encode("pass"), MANAGER);
//			userRepo.saveAll(List.of(umer,aamuri));
//
//			Employee employee1 = new Employee(null, UUID.randomUUID().toString(),
//					"umer","ali","umer@gmail.com","123786030","Medical Assembly","2390 ave ln blaine,MN,55124",
//					1,"Bachelor's Degree");
//
//			Employee employee2 = new Employee(null, UUID.randomUUID().toString(),
//					"aamuri","ali","aamuri@gmail.com","123786022","Mechanical","14 ave Fridley,MN,55444",
//					1,"Bachelor's Degree");
//			employeeRepo.saveAll(List.of(employee1,employee2));
//
//			Company medtronic = new Company("Medtronic","medtronic@gmail.com","332786907","1300 commic ave Fridley,MN,55443");
//			Company boston = new Company("Boston","boston@gmail.com","332786907","47 ave new brighton,MN,55403");
//			companyRepo.saveAll(List.of(medtronic,boston));
//
//			Job job1 = new Job(UUID.randomUUID().toString(),"Medical Assembly",20,"High school",0,medtronic);
//			Job job2 = new Job(UUID.randomUUID().toString(),"Mechanical",21,"High school",1,boston);
//			jobRepo.saveAll(List.of(job1,job2));
//		};
//	}
}
