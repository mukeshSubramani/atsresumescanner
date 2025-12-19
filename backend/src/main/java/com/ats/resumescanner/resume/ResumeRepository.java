package com.ats.resumescanner.resume;

import com.ats.resumescanner.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResumeRepository extends JpaRepository<Resume, Long> {
    List<Resume> findAllByUser(User user);
}
