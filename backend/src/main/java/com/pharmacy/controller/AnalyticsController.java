package com.pharmacy.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pharmacy.repository.SaleRepository;

@RestController
@RequestMapping("/api/analytics")
@CrossOrigin
public class AnalyticsController {

    @Autowired
    private SaleRepository saleRepo;

    @GetMapping("/monthly-sales")
    public List<Map<String, Object>> monthlySales() {
        List<Object[]> rows = saleRepo.monthlySales();

        return rows.stream().map(r -> {
            Map<String, Object> m = new HashMap<>();
            m.put("medicineName", r[0]);
            m.put("quantitySold", r[1]);
            m.put("totalAmount", r[2]);
            return m;
        }).toList();
    }
}
