package com.pharmacy.controller;

import java.time.LocalDate;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pharmacy.model.Medicine;
import com.pharmacy.repository.MedicineRepository;
import com.pharmacy.service.AuditService;
import com.pharmacy.service.MedicineService;

@RestController
@RequestMapping("/api/medicine")
@CrossOrigin(origins = "*")
public class MedicineController {

    @Autowired
    private MedicineService service;

    @Autowired
    private MedicineRepository repo;

    @Autowired
    private AuditService auditService;

    @Autowired
    private HttpServletRequest request;

    // All medicines
    @GetMapping("/all")
    public List<Medicine> getAll() {
        repo.deleteZeroStock();
        return repo.findAll();
    }

    // Expire soon (next 30 days)
    @GetMapping("/expire-soon")
    public List<Medicine> getExpireSoon() {
        LocalDate today = LocalDate.now();
        LocalDate futureDate = today.plusMonths(2); // ✅ 2 months

        return repo.findExpireSoon(today, futureDate);
    }

    // Expired
    @GetMapping("/expired")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PHARMACIST')")
    public List<Medicine> expired() {
        return repo.findExpired();
    }

    // Low stock
    @GetMapping("/low-stock")
    public List<Medicine> lowStock() {
        return repo.findByQuantityLessThan(100);
    }


    // Delete
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PHARMACIST')")
    public void delete(@PathVariable int id) {
        String username = getCurrentUsername();
        Medicine medicine = repo.findById(id).orElse(null);
        if (medicine != null) {
            auditService.logActionWithRequest(username, "DELETE_MEDICINE",
                "Deleted medicine: " + medicine.getName() + " (Batch: " + medicine.getBatchNo() + ")",
                "Medicine", String.valueOf(id), request);
        }
        repo.deleteById(id);
    }

    // Manual entry
    @PostMapping("/manual")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PHARMACIST')")
    public Medicine manual(@RequestBody Medicine med) {
        String username = getCurrentUsername();
        Medicine saved = service.addOrUpdate(med);
        auditService.logActionWithRequest(username, "ADD_MEDICINE",
            "Added medicine manually: " + saved.getName() + " (Batch: " + saved.getBatchNo() + ")",
            "Medicine", String.valueOf(saved.getId()), request);
        return saved;
    }


    // ✅ Scanner add
    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PHARMACIST')")
    public Medicine addByBarcode(
            @RequestParam String barcode,
            @RequestParam int quantity) {

        String username = getCurrentUsername();
        Medicine medicine = service.addByBarcode(barcode, quantity);
        auditService.logActionWithRequest(username, "ADD_MEDICINE",
            "Added medicine by barcode: " + medicine.getName() + " (Quantity: " + quantity + ")",
            "Medicine", String.valueOf(medicine.getId()), request);
        return medicine;
    }

    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null ? authentication.getName() : "system";
    }
}
