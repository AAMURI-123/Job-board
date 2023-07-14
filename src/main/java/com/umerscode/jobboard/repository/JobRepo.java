package com.umerscode.jobboard.repository;

import com.umerscode.jobboard.Entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobRepo extends JpaRepository<Job,Long> {
   Optional<List<Job>> findByJobType(String jobType);
    Optional<Job> findByJobNumber(String number);
}
