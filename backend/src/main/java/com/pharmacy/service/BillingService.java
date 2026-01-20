package com.pharmacy.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pharmacy.dto.BillRequest;
import com.pharmacy.dto.SaleRequest;
import com.pharmacy.model.Bill;
import com.pharmacy.model.Customer;
import com.pharmacy.model.Medicine;
import com.pharmacy.model.Sale;
import com.pharmacy.repository.BillRepository;
import com.pharmacy.repository.CustomerRepository;
import com.pharmacy.repository.MedicineRepository;
import com.pharmacy.repository.SaleRepository;

@Service
public class BillingService {

    @Autowired
    private BillRepository billRepo;

    @Autowired
    private MedicineRepository medicineRepo;

    @Autowired
    private SaleRepository saleRepo;

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

            // 🔽 reduce stock
            med.setQuantity(med.getQuantity() - req.getQuantity());
            medicineRepo.save(med);

            // Use selling price from medicine
            double price = med.getSellingPrice();
            double itemTotal = price * req.getQuantity();
            total += itemTotal;

            // 🔥 SAVE INTO SALES TABLE
            Sale sale = new Sale();
            sale.setBillNo(bill.getBillNo());
            sale.setMedicineId(med.getId());
            sale.setMedicineName(med.getName());
            sale.setBatchNo(med.getBatchNo());
            sale.setQuantity(req.getQuantity());
            sale.setPricePerUnit(price);
            sale.setTotalAmount(itemTotal);
            sale.setCustomerId(customer.getId());

            saleRepo.save(sale);
        }

        bill.setTotalAmount(total);
        billRepo.save(bill);

        return bill;
    }

    public List<Sale> getSalesHistory() {
        return saleRepo.findAll();
    }
}

