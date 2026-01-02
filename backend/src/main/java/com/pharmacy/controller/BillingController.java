package com.pharmacy.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pharmacy.dto.SaleRequest;
import com.pharmacy.model.Bill;
import com.pharmacy.model.Sale;
import com.pharmacy.service.BillingService;

@RestController
@RequestMapping("/api/billing")
@CrossOrigin
public class BillingController {

    @Autowired
    private BillingService billingService;

    @PostMapping("/sell")
    public Bill sell(@RequestBody List<SaleRequest> items) {
        return billingService.processSale(items);
    }

    @GetMapping("/history")
    public List<Sale> history() {
        return billingService.getSalesHistory();
    }
}
