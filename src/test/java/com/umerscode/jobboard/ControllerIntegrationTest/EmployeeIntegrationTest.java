package com.umerscode.jobboard.ControllerIntegrationTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.umerscode.jobboard.Dto.RegisterEmployeeDto;
import com.umerscode.jobboard.Entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static com.umerscode.jobboard.Entity.Role.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
class EmployeeIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        // initializing the mockMvc
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }

    @Test
    @WithMockUser(username = "umer123@gmail.com",authorities = "EMPLOYEE")
    void itShouldGetAllJobs() throws Exception {
         mockMvc.perform(get("/api/v1/employee/jobs"))
                .andExpect(status().isOk());

    }

    @Test
    @WithMockUser(username = "umer123@gmail.com",authorities = "EMPLOYEE")
    void itShouldGetJobByType() throws Exception {
        mockMvc.perform(get("/api/v1/employee/job/{type}","Medical Assembly"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "umer123@gmail.com",authorities = "EMPLOYEE")
    void itShouldUpdateEmployeeById() throws Exception {
        Employee employee = new Employee(4l,"5e300eed-5991-45fd-a035-d918459d0cc9 ",
                "mariam","hassan","mariam@gmail.com","847-920-4738","Mechanical Engineer",
                "11 camd ave ardenhills,MN,55012 ",3,"BD");

        mockMvc.perform(put("/api/v1/employee/update/{id}",employee.getId())
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.phoneNo").value("847-920-4738"))
                .andExpect(jsonPath("$.yearsOfExperience").value(3))
                .andExpect(jsonPath("$.educationLevel").value("BD"))
                .andExpect(jsonPath("$.jobType").value("Mechanical Engineer"));

    }

    @Test
    void itShouldRegisterNewEmployee() throws Exception {
        Employee employee = new Employee(null, null,
                "amir","mohamed","amir@gmail.com","847-110-4538","Software Developer",
                "3535 ave ardenhills,MN,55012 ",1,"BD");
        AppUser user = new AppUser(null,"amir@gmail.com","pass",EMPLOYEE);

        RegisterEmployeeDto registerEmployee = new RegisterEmployeeDto(user,employee);

        mockMvc.perform(post("/api/v1/employee/register")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerEmployee)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.employeeCode").isNotEmpty())
                .andExpect(jsonPath("$.email").value("amir@gmail.com"))
                .andExpect(jsonPath("$.phoneNo").value("847-110-4538"))
                .andExpect(jsonPath("$.yearsOfExperience").value(1))
                .andExpect(jsonPath("$.educationLevel").value("BD"));
    }

    @Test
    void EmployeeShouldBeAbleToSubmitApplication() throws Exception {

       Company company = new Company(2l,"Boston", "boston@gmail.com", "332786907","47 ave new brighton,MN,55403");
        Job job = new Job("eefeaf37-d495-4a3f-9c24-e61429e8ec93","Medical Assembly",
                20,"HS",0,company);

        SubmittedApplication submitApp = new SubmittedApplication(null,job);

        mockMvc.perform(post("/api/v1/employee/application-submit")
                .with(httpBasic("ahmed@gmail.com","pass"))
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(submitApp)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.applicationNumber").isNotEmpty());
    }

    @Test
    void itShouldGetEmployeeDetailsAfterAuthentication() throws Exception {

        mockMvc.perform(get("/api/v1/employee/profile")
                .with(httpBasic("umer@gmail.com","pass")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("umer@gmail.com"))
                .andExpect(jsonPath("$.employeeCode").isNotEmpty());
    }
}