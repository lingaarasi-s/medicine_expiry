package com.pharmacy.service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import com.pharmacy.model.Medicine;

@Service
public class DiscountService {

    public double calculateDiscount(Medicine med, int monthlySales) {

        boolean nearExpiry =
            med.getExpiryDate().isBefore(LocalDate.now().plusMonths(2));

        boolean lowSales = monthlySales < 20;

        if (nearExpiry && lowSales) return 30; // 30% discount
        if (nearExpiry) return 15;
        if (lowSales) return 10;

        return 0;
    }
}
