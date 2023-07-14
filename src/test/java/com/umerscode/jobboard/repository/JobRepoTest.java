package com.umerscode.jobboard.repository;

import com.umerscode.jobboard.Entity.Job;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest(properties = {
        "spring.datasource.url = jdbc:h2://mem:db;DB_CLOSE_DELAY=-1",
        "spring.datasource.username = sa",
        "spring.datasource.password = sa",
        "spring.jpa.properties.hibernate.dialect = org.hibernate.dialect.H2Dialect",
        "spring.jpa.hibernate.ddl-auto = create-drop"
})

class JobRepoTest {

    @Autowired
    private JobRepo jobRepo;

    @Test
    void itShouldFindJobByType(){
        //given
        Job job1 = new Job(UUID.randomUUID().toString(),"Team Leader",20,
                "HS",1,null);

        Job job2 = new Job(UUID.randomUUID().toString(),"Team Manager",30,
                "HS",5,null);

        jobRepo.saveAll(List.of(job1,job2));

        //when
        List<Job> actualResponse = jobRepo.findByJobType(job1.getJobType()).get();

        //then
        assertThat(actualResponse).isNotNull();
        assertThat(actualResponse.size()).isEqualTo(1);
        assertThat(actualResponse.get(0)).isEqualTo(job1);
    }

    @Test
    void itShouldFindJobByNumber(){
        //given
        Job job1 = new Job(UUID.randomUUID().toString(),"Team Leader",20,
                "HS",1,null);

        Job job2 = new Job(UUID.randomUUID().toString(),"Team Manager",30,
                "HS",5,null);

        jobRepo.saveAll(List.of(job1,job2));

        //when
        Optional<Job> actualResponse = jobRepo.findByJobNumber(job1.getJobNumber());

        //then
        assertThat(actualResponse).isNotEmpty();
        assertThat(actualResponse.get()).isEqualTo(job1);
    }
}