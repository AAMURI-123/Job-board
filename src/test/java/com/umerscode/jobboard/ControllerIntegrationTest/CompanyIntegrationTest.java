package com.umerscode.jobboard.ControllerIntegrationTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.umerscode.jobboard.Dto.RegisterCompanyDto;
import com.umerscode.jobboard.Entity.AppUser;
import com.umerscode.jobboard.Entity.Company;
import com.umerscode.jobboard.Entity.Job;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.UUID;

import static com.umerscode.jobboard.Entity.Role.MANAGER;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class CompanyIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }

    @Test
    @WithMockUser(username = "medtronic@gmail.com",authorities = "MANAGER")
    void itShouldGetAllJobs() throws Exception {
        mockMvc.perform(get("/api/v1/company/jobs"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "medtronic@gmail.com",authorities = "MANAGER")
    void itShouldGetEmployeeByTheirJob() throws Exception {

        mockMvc.perform(get("/api/v1/company/findEmployeeByJob/{jobType}", "Technician"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..email").isNotEmpty());

    }

    @Test
    @WithMockUser(username = "medtronic@gmail.com",authorities = "MANAGER")
    void itShouldDeleteJobById() throws Exception {

        mockMvc.perform(delete("/api/v1/company/job/{id}", 2))
                .andExpect(status().isOk());

    }

    @Test
    @WithMockUser(username = "medtronic@gmail.com",authorities = "MANAGER")
    void itShouldUpdateJobById() throws Exception {

        Job job = new Job("03e1ab15-9bbe-4bd4-963e-2a57670614a0","Technician",23,"HS",1,
                null);
            job.setId(2l);

        mockMvc.perform(put("/api/v1/company/job/update/{id}", job.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(job)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jobType").value("Technician"))
                .andExpect(jsonPath("$.payPerHour").value(23))
                .andExpect(jsonPath("$.educationLevel").value("HS"));

    }

    @Test
    void itShouldCreateNewJob() throws Exception {
        Job job = new Job(UUID.randomUUID().toString(),"Project Manager",27,
                "BD",2,null);

        mockMvc.perform(post("/api/v1/company/create/job")
                .with(httpBasic("medtronic@gmail.com","pass"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(job)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.jobNumber").isNotEmpty())
                .andExpect(jsonPath("$.id").isNotEmpty());

    }

    @Test
    void itShouldGetCompanyDetailsAfterAuthentication() throws Exception {

        mockMvc.perform(get("/api/v1/company/profile")
                .with(httpBasic("medtronic@gmail.com","pass")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("medtronic@gmail.com"));
    }

    @Test
    void itShouldRegisterNewCompany() throws Exception {
        Company company = new Company(null,"Google","google@gmail.com",
                "321-184-118","2214 aven Fridley,MN,55123");

        AppUser user = new AppUser(null,"google@gmail.com","pass",MANAGER);

        RegisterCompanyDto registerCompany = new RegisterCompanyDto(user,company);

        mockMvc.perform(post("/api/v1/company/register")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerCompany)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.email").value("google@gmail.com"));
    }
}