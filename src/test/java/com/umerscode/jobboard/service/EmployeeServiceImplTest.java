package com.umerscode.jobboard.service;

import com.umerscode.jobboard.Dto.RegisterEmployeeDto;
import com.umerscode.jobboard.Entity.*;
import com.umerscode.jobboard.repository.EmployeeRepo;
import com.umerscode.jobboard.repository.JobRepo;
import com.umerscode.jobboard.repository.SubmitAppRepo;
import com.umerscode.jobboard.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.hibernate.sql.results.graph.instantiation.internal.ArgumentReader;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.*;

import static com.umerscode.jobboard.Entity.Role.*;
import static java.util.Optional.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepo employeeRepo;
    @Mock
    private JobRepo jobRepo;
    @Mock
    private UserRepo userRepo;
    @Mock
    private SubmitAppRepo submitAppRepo;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private EmployeeServiceImpl employeeService;



    @Test
    void itShouldGetAllJobs(){

        //given
        Job job1 = new Job(UUID.randomUUID().toString(),"Team Leader",20,
                "HS",1,null);
        Job job2 = new Job(UUID.randomUUID().toString(),"Team Manager",30,
                "HS",5,null);
        List<Job> jobs = new ArrayList<>(Arrays.asList(job1,job2));

         when(jobRepo.findAll()).thenReturn(jobs);

         //when
        List<Job> actualJobs = employeeService.getAllJobs();

        //then
        assertThat(actualJobs).isNotNull();
        assertThat(actualJobs.size()).isEqualTo(jobs.size());
        assertEquals(jobs,actualJobs);

    }

    @Test
    void itShouldGetJobsByType(){

        //given
        Job job1 = new Job(UUID.randomUUID().toString(),"Team Leader",20,
                "HS",1,null);

        Job job2 = new Job(UUID.randomUUID().toString(),"Team Manager",30,
                "HS",5,null);

        List<Job> jobs = new ArrayList<>();
        jobs.add(job1);

        when(jobRepo.findByJobType(job1.getJobType())).thenReturn(of(jobs));

        //when
        List<Job> actualJobs = employeeService.getJobByType(job1.getJobType());

        //then
        assertThat(actualJobs).isNotNull();
        assertThat(actualJobs.size()).isEqualTo(jobs.size());
        assertEquals(jobs,actualJobs);

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(jobRepo).findByJobType(argumentCaptor.capture());
        String capturedValue = argumentCaptor.getValue();
        assertEquals(job1.getJobType(),capturedValue);

    }

    @Test
    void itShouldUpdateEmployee(){
      Employee employee1 = new Employee(1l, UUID.randomUUID().toString(),"umer","ali",
                "umerali@gmail.com","123455667","Software engineer","12 ave ,MN",0,"Bsc");
        when(employeeRepo.findById(employee1.getId())).thenReturn(Optional.of(employee1));
        employee1.setYearsOfExperience(1);
        employee1.setEducationLevel("HS");

        //when
        Employee response = employeeService.updateEmployee(employee1.getId(), employee1);

        //then
        assertEquals(employee1,response);

        ArgumentCaptor<Long> argumentCaptor = ArgumentCaptor.forClass(Long.class);
        verify(employeeRepo).findById(argumentCaptor.capture());
        Long captorValue = argumentCaptor.getValue();
        assertEquals(employee1.getId(),captorValue);
    }

    @Test
    void itShouldThrowIfEmployeeWithIdDoesNotExist(){

        Employee employee1 = new Employee(1l, UUID.randomUUID().toString(),"umer","ali",
                "umerali@gmail.com","123455667","Software engineer","12 ave ,MN",0,"Bsc");

        //when & then
        assertThatThrownBy(()->employeeService.updateEmployee(2l,employee1))
                            .isInstanceOf(IllegalStateException.class)
                            .hasMessage("employee with id "+2+" not found");
    }

    @Test
    void itShouldRegisterEmployee(){
        //given
        AppUser user = new AppUser(1l,"user@gmail.com","password", EMPLOYEE);
        Employee employee1 = new Employee(1l, UUID.randomUUID().toString(),"umer","ali",
                "umerali@gmail.com","123455667","Software engineer","12 ave ,MN",0,"Bsc");
        RegisterEmployeeDto registerDto = new RegisterEmployeeDto(user,employee1);

        when(passwordEncoder.encode(user.getPassword())).thenReturn("abc12%a8klSC#nmBFOa0#$v");
        when(userRepo.save(user)).thenReturn(user);
        when(employeeRepo.save(employee1)).thenReturn(employee1);

        //when
        Employee registeredEmployee = employeeService.registerEmployee(registerDto);

        //then
        assertThat(registeredEmployee).isNotNull();
        assertThat(registeredEmployee.getEmail()).isEqualTo(employee1.getEmail());



    }


    @Test
    void itShouldThrowIfUserWithEmailDoesNotExist(){

        Employee employee1 = new Employee(1l, UUID.randomUUID().toString(),"umer","ali",
                "umerali@gmail.com","123455667","Software engineer","12 ave ,MN",0,"Bsc");
        AppUser user = new AppUser(1l,"user@gmail.com","password", EMPLOYEE);
        RegisterEmployeeDto registerDto = new RegisterEmployeeDto(user,employee1);

        when(userRepo.findUserByEmail(user.getEmail())).thenReturn(Optional.of(user));

        //when & then
        assertThatThrownBy(()->employeeService.registerEmployee(registerDto))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("User email is taken");
    }

}