package com.umerscode.jobboard.service;

import com.umerscode.jobboard.Dto.RegisterCompanyDto;
import com.umerscode.jobboard.Dto.RegisterEmployeeDto;
import com.umerscode.jobboard.Entity.AppUser;
import com.umerscode.jobboard.Entity.Company;
import com.umerscode.jobboard.Entity.Employee;
import com.umerscode.jobboard.Entity.Job;
import com.umerscode.jobboard.repository.CompanyRepo;
import com.umerscode.jobboard.repository.EmployeeRepo;
import com.umerscode.jobboard.repository.JobRepo;
import com.umerscode.jobboard.repository.UserRepo;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.umerscode.jobboard.Entity.Role.EMPLOYEE;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompanyServiceImplTest {

    @Mock
    private CompanyRepo companyRepo;
    @Mock
    private JobRepo jobRepo;
    @Mock
    private EmployeeRepo employeeRepo;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserRepo userRepo;

    @InjectMocks
    private CompanyServiceImpl companyService;

    @Test
    void itShouldGetEmployeeByJobType(){
       Employee employee1 = new Employee(1l, UUID.randomUUID().toString(),"umer","ali",
                "umerali@gmail.com","123455667","Software engineer","12 ave ,MN",0,"Bsc");

        when(employeeRepo.findByJobType(employee1.getJobType())).thenReturn(List.of(employee1));

        //when
        List<Employee> employeesByJobType = companyService.getEmployeeByJobType(employee1.getJobType());

        //verify
        assertThat(employeesByJobType).isNotNull();
        assertEquals(employeesByJobType.get(0),employee1);

    }

    @Test
    void itShouldThrowIfCanNotFindEmployeeByJobType(){
        Employee employee1 = new Employee(1l, UUID.randomUUID().toString(),"umer","ali",
                "umerali@gmail.com","123455667","Software engineer","12 ave ,MN",0,"Bsc");

        //when & then
        assertThatThrownBy(()-> companyService.getEmployeeByJobType(employee1.getJobType()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("No Employee for "+ employee1.getJobType()+" job.");
    }

    @Test
    void itShouldGetAllJobs(){
        //given
        Job job1 = new Job(UUID.randomUUID().toString(),"Team Leader",20,
                "HS",1,null);

        Job job2 = new Job(UUID.randomUUID().toString(),"Team Manager",30,
                "HS",5,null);

        when(jobRepo.findAll()).thenReturn(List.of(job1,job2));

        //when
        List<Job> allJobs = companyService.getAllJobs();

        //verify
        assertThat(allJobs).isNotNull();
        assertThat(allJobs.size()).isEqualTo(2);
        assertEquals(allJobs.get(0),job1);
        assertEquals(allJobs.get(1),job2);
    }

    @Test
    void itShouldDeleteJobById(){

        Job job1 = new Job(UUID.randomUUID().toString(),"Team Leader",20,
                "HS",1,null);
        job1.setId(1l);

        when(jobRepo.findById(job1.getId())).thenReturn(Optional.of(job1));
        doNothing().when(jobRepo).deleteById(job1.getId());

        companyService.deleteJobById(job1.getId());
    }

    @Test
    void itShouldThrowIfCanNotFindJobById(){
        Job job1 = new Job(UUID.randomUUID().toString(),"Team Leader",20,
                "HS",1,null);
        job1.setId(1l);

        //when & then
        assertThatThrownBy(()-> companyService.deleteJobById(job1.getId()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Job with id "+ job1.getId()+" not found");
    }

    @Test
    void itShouldUpdateJob(){
        //given
        Job job1 = new Job(UUID.randomUUID().toString(),"Team Leader",20,
                "HS",1,null);
        job1.setId(1l);

        when(jobRepo.findById(job1.getId())).thenReturn(Optional.of(job1));

        job1.setPayPerHour(10);
        job1.setYearsOfExperience(0);
        job1.setEducationLevel("BD");

        //when
        Job updatedJob = companyService.updateJob(job1.getId(), job1);

        //verify
        assertThat(updatedJob).isNotNull();
        assertThat(updatedJob).isEqualTo(job1);
    }

    @Test
    void itShouldRegisterNewCompany(){
        AppUser user = new AppUser(1l,"user@gmail.com","password", EMPLOYEE);
        Company company = new Company(1l,"medtronic","medtronic@gmail.com","321184790","12 canv fridley,MN,55123");
        RegisterCompanyDto registerDto = new RegisterCompanyDto(user,company);

        when(passwordEncoder.encode(user.getPassword())).thenReturn("abc12%a8klSC#nmBFOa0#$v");
        when(userRepo.save(user)).thenReturn(user);
        when(companyRepo.save(company)).thenReturn(company);

        //when
        Company registeredCompany = companyService.registerCompany(registerDto);

        //then
        assertThat(registeredCompany).isNotNull();
        assertThat(registeredCompany).isEqualTo(company);


    }

    @Test
    void itShouldThrowIfUserAlreadyExist(){

        AppUser user = new AppUser(1l,"user@gmail.com","password", EMPLOYEE);
        Company company = new Company(1l,"medtronic","medtronic@gmail.com","321184790","12 canv fridley,MN,55123");
        RegisterCompanyDto registerDto = new RegisterCompanyDto(user,company);

        when(userRepo.findUserByEmail(user.getEmail())).thenReturn(Optional.of(user));
        //when and then
        assertThatThrownBy(()-> companyService.registerCompany(registerDto))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("User email is taken");
    }

    @Test
    @Disabled
        // TODO- needs to authenticate a user to run a test case
    void itShouldCreateNewJob() {

    }
}
