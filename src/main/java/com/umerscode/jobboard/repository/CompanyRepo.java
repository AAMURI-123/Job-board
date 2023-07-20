package com.umerscode.jobboard.repository;

import com.umerscode.jobboard.Entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepo extends JpaRepository<Company,Long> {
    Company findByEmail(String email);
}
