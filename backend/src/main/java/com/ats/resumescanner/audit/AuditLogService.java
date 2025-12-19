package com.ats.resumescanner.audit;

import com.ats.resumescanner.auth.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public void log(User user, String action, String metaJson) {
        AuditLog log = AuditLog.builder()
                .user(user)
                .action(action)
                .metaJson(metaJson)
                .build();
        auditLogRepository.save(log);
    }
}
