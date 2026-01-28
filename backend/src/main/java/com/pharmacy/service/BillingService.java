package com.pharmacy.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pharmacy.dto.BillRequest;
import com.pharmacy.dto.SaleRequest;
import com.pharmacy.model.Bill;
import com.pharmacy.model.Customer;
import com.pharmacy.model.Medicine;
import com.pharmacy.model.SaleItem;
import com.pharmacy.repository.BillRepository;
import com.pharmacy.repository.CustomerRepository;
import com.pharmacy.repository.MedicineRepository;
import com.pharmacy.repository.SaleItemRepository;

@Service
public class BillingService {

    @Autowired
    private BillRepository billRepo;

    @Autowired
    private MedicineRepository medicineRepo;

    @Autowired
    private SaleItemRepository saleRepo;

    @Autowired
    private CustomerRepository customerRepo;

    public Bill processSale(BillRequest request) {

        // Handle customer
        Customer customer = customerRepo.findByNameAndPhone(request.getCustomerName(), request.getCustomerPhone())
                .orElseGet(() -> {
                    Customer newCust = new Customer();
                    newCust.setName(request.getCustomerName());
                    newCust.setPhone(request.getCustomerPhone());
                    return customerRepo.save(newCust);
                });

        Bill bill = new Bill();
        bill.setBillNo("BILL-" + System.currentTimeMillis());
        bill.setCustomerId(customer.getId());
        bill.setCustomerName(customer.getName());
        billRepo.save(bill);

        double total = 0;

        for (SaleRequest req : request.getItems()) {

          Medicine med = medicineRepo.findById(req.getMedicineId())
            .orElseThrow(() -> new RuntimeException("Medicine not found"));

        if (med.getQuantity() < req.getQuantity()) {
            throw new RuntimeException("Insufficient stock for " + med.getName());
        }

    // reduce stock
        med.setQuantity(med.getQuantity() - req.getQuantity());
        medicineRepo.save(med);

        double price = med.getSellingPrice();
        double itemTotal = price * req.getQuantity();
        total += itemTotal;

    // ✅ SAVE INTO sale_item
    SaleItem item = new SaleItem();
    item.setBill(bill);
    item.setMedicineName(med.getName());
    item.setBatchNo(med.getBatchNo());
    item.setQuantity(req.getQuantity());
    item.setPricePerUnit(price);
    item.setTotalAmount(itemTotal);

    saleRepo.save(item);
}


        bill.setTotalAmount(total);
        billRepo.save(bill);

        return bill;
    }

    public List<SaleItem> getSalesHistory() {
        return saleRepo.findAll();
    }
}

