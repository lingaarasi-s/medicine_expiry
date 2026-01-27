package com.pharmacy.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pharmacy.model.AuditLog;
import com.pharmacy.service.AuditService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/audit")
public class AuditController {

    @Autowired
    private AuditService auditService;

    @GetMapping("/logs")
    @PreAuthorize("hasRole('ADMIN')")
    public List<AuditLog> getAllAuditLogs() {
        return auditService.getAllAuditLogs();
    }

    @GetMapping("/logs/user/{username}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PHARMACIST')")
    public List<AuditLog> getAuditLogsByUsername(@PathVariable String username) {
        return auditService.getAuditLogsByUsername(username);
    }

    @GetMapping("/logs/action/{action}")
    @PreAuthorize("hasRole('ADMIN')")
    public List<AuditLog> getAuditLogsByAction(@PathVariable String action) {
        return auditService.getAuditLogsByAction(action);
    }

    @GetMapping("/logs/date-range")
    @PreAuthorize("hasRole('ADMIN')")
    public List<AuditLog> getAuditLogsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return auditService.getAuditLogsByDateRange(startDate, endDate);
    }
}