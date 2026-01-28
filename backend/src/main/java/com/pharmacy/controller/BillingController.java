package com.pharmacy.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pharmacy.dto.BillRequest;
import com.pharmacy.model.Bill;
import com.pharmacy.model.SaleItem;
import com.pharmacy.repository.BillRepository;
import com.pharmacy.service.AuditService;
import com.pharmacy.service.BillingService;

@RestController
@RequestMapping("/api/billing")
@CrossOrigin(origins = "*")
public class BillingController {

    @Autowired
    private BillingService billingService;

    @Autowired
    private BillRepository billRepo;

    @Autowired
    private AuditService auditService;

    @Autowired
    private HttpServletRequest request;

    // ✅ SELL MEDICINE
    @PostMapping("/sell")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PHARMACIST')")
    public Bill sell(@RequestBody BillRequest billRequest) {

        String username = getCurrentUsername();
        Bill bill = billingService.processSale(billRequest);

        auditService.logActionWithRequest(
            username,
            "SELL_MEDICINE",
            "Sold medicines. Bill No: " + bill.getId() + ", Total: " + bill.getTotalAmount(),
            "Bill",
            String.valueOf(bill.getId()),
            request
        );

        return bill;
    }

    // ✅ SALES HISTORY
    @GetMapping("/history")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PHARMACIST')")
    public List<SaleItem> history() {
        return billingService.getSalesHistory();
   }

    // ✅ BILL LIST
    @GetMapping("/bills")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PHARMACIST')")
    public List<Bill> getBills() {
        return billRepo.findAllByOrderBySaleDateDesc();
    }

    // 🔐 COMMON METHOD (same as MedicineController)
    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null ? authentication.getName() : "system";
    }
}
