package com.pharmacy.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.pharmacy.service.MedicineService;

@RestController
@RequestMapping("/api/medicine")
@CrossOrigin(origins = "*")
public class MedicineController {

    @Autowired
    private MedicineService service;

    @Autowired
    private MedicineRepository repo;

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
    public void delete(@PathVariable int id) {
        repo.deleteById(id);
    }

    // Manual entry
    @PostMapping("/manual")
    public Medicine manual(@RequestBody Medicine med) {
        return service.addOrUpdate(med);
    }


    // ✅ Scanner add
    @PostMapping("/add")
    public Medicine addByBarcode(
            @RequestParam String barcode,
            @RequestParam int quantity) {

        return service.addByBarcode(barcode, quantity);
    }
}
