package com.umerscode.jobboard.repository;

import com.umerscode.jobboard.Entity.SubmittedApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubmitAppRepo  extends JpaRepository<SubmittedApplication,Long> {
}
