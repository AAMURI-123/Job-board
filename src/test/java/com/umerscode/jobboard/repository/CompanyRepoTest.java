package com.umerscode.jobboard.repository;

import com.umerscode.jobboard.Entity.Company;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest(properties =
        {"spring.datasource.url = jdbc:h2://mem:db;DB_CLOSE_DELAY=-1",
                "spring.datasource.username = sa",
                "spring.datasource.password = sa",
                "spring.jpa.properties.hibernate.dialect = org.hibernate.dialect.H2Dialect",
                "spring.jpa.hibernate.ddl-auto = create-drop"})

class CompanyRepoTest {

    @Autowired
    private CompanyRepo companyRepo;


    @Test
    void itShouldGetCompanyByEmail(){
        Company company = new Company(1l,"medtronic","medtronic@gmail.com","321184790","12 canv fridley,MN,55123");

        companyRepo.save(company);

        //when
        Company response = companyRepo.findByEmail(company.getEmail());

        //verify
        assertThat(response).isNotNull();
       //assertThat(response).isEqualTo(company);
    }
}