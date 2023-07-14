package com.umerscode.jobboard.repository;

import com.umerscode.jobboard.Entity.AppUser;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static com.umerscode.jobboard.Entity.Role.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest(properties =
        {"spring.datasource.url = jdbc:h2://mem:db;DB_CLOSE_DELAY=-1",
         "spring.datasource.username = sa",
         "spring.datasource.password = sa",
         "spring.jpa.properties.hibernate.dialect = org.hibernate.dialect.H2Dialect",
         "spring.jpa.hibernate.ddl-auto = create-drop"})

class UserRepoTest {

    @Autowired
    private UserRepo userRepo;

        @Test
        void itShouldFindUserByEmail(){
            //given
            AppUser user1 = new AppUser(1l,"alexes@gmail.com","password", MANAGER);
            AppUser user2 = new AppUser(2l,"dembele@gmail.com","password", EMPLOYEE);
            userRepo.saveAll(List.of(user1,user2));

            //when - calling method that is being testing
            Optional<AppUser> actualUser = userRepo.findUserByEmail(user1.getEmail());

            //then - verify
            assertThat(actualUser).isNotEmpty();
            assertThat(actualUser.get()).isEqualTo(user1);

        }
}