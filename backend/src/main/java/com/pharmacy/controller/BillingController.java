package com.pharmacy.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pharmacy.model.Medicine;
import com.pharmacy.model.Sale;
import com.pharmacy.repository.MedicineRepository;
import com.pharmacy.repository.SaleRepository;

@RestController
@RequestMapping("/api/billing")
@CrossOrigin
public class BillingController {

    @Autowired
    private MedicineRepository medicineRepo;

    @Autowired
    private SaleRepository saleRepo;

    // 👉 New Sale
    @PostMapping("/sell")
    public Sale sellMedicine(
            @RequestParam int medicineId,
            @RequestParam int quantity,
            @RequestParam double price) {

        Medicine med = medicineRepo.findById(medicineId)
                .orElseThrow(() -> new RuntimeException("Medicine not found"));

        if (med.getQuantity() < quantity) {
            throw new RuntimeException("Insufficient stock");
        }

        // reduce stock
        med.setQuantity(med.getQuantity() - quantity);
        medicineRepo.save(med);

        // save sale
        Sale sale = new Sale();
        sale.setMedicineId(med.getId());
        sale.setMedicineName(med.getName());
        sale.setBatchNo(med.getBatchNo());
        sale.setQuantity(quantity);
        sale.setPricePerUnit(price);
        sale.setTotalAmount(quantity * price);

        return saleRepo.save(sale);
    }

    // 👉 Sales History
    @GetMapping("/history")
    public List<Sale> history() {
        return saleRepo.findAll();
    }
}
