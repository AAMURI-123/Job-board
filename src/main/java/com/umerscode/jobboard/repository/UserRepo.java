package com.umerscode.jobboard.repository;

import com.umerscode.jobboard.Entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<AppUser,Long> {
    Optional<AppUser> findUserByEmail(String email);
}
