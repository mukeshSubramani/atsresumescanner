package com.ats.resumescanner.scan;

import com.ats.resumescanner.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScanRepository extends JpaRepository<Scan, Long> {
    List<Scan> findAllByUser(User user);
}
