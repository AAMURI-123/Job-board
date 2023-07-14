package com.umerscode.jobboard.repository;

import com.umerscode.jobboard.Entity.Employee;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest(properties =
        {"spring.datasource.url = jdbc:h2://mem:db;DB_CLOSE_DELAY=-1",
                "spring.datasource.username = sa",
                "spring.datasource.password = sa",
                "spring.jpa.properties.hibernate.dialect = org.hibernate.dialect.H2Dialect",
                "spring.jpa.hibernate.ddl-auto = create-drop"})

class EmployeeRepoTest {


    private EmployeeRepo employeeRepo;

    @Autowired
    public EmployeeRepoTest(EmployeeRepo employeeRepo) {
        this.employeeRepo = employeeRepo;
    }

    private Employee employee1;
    private Employee employee2;



    @BeforeEach
    void setUp() {
        //given
     employee1 = new Employee(null, UUID.randomUUID().toString(),"umer","ali",
                "umerali@gmail.com","123455667","Software engineer","12 ave ,MN",0,"Bsc");

     employee2 = new Employee(null, UUID.randomUUID().toString(),"sarah","alex",
                "sarah@gmail.com","120015667","Data Entry","11 ave ,MN",1,"HS");

        employeeRepo.saveAll(List.of(employee1,employee2));

    }

    @AfterEach
    void tearDown() {
        employeeRepo.deleteAll();
    }

    @Test
    void itShouldFindEmployeeByEmail(){
        //when
        Employee actualEmployee = employeeRepo.findByEmail(employee1.getEmail());

        //then
        assertThat(actualEmployee).isNotNull();
        assertEquals(employee1,actualEmployee);
    }



    @Test
    void itShouldFindEmployeeByCode(){
        //when
        Optional<Employee> actualEmployee = employeeRepo.findByEmployeeCode(employee1.getEmployeeCode());

        //then
        assertThat(actualEmployee).isNotEmpty();
        assertEquals(employee1,actualEmployee.get());
    }

    @Test
    void itShouldFindEmployeeByJobType(){
        //when
        List<Employee> actualEmployee = employeeRepo.findByJobType(employee1.getJobType());

        //then
        assertThat(actualEmployee).isNotNull();
        assertThat(actualEmployee.size()).isEqualTo(1);
        assertEquals(employee1,actualEmployee.get(0));
    }

}