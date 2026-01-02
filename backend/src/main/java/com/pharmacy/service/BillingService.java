package com.pharmacy.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pharmacy.dto.SaleRequest;
import com.pharmacy.model.Bill;
import com.pharmacy.model.Medicine;
import com.pharmacy.model.Sale;
import com.pharmacy.repository.BillRepository;
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

    public Bill processSale(List<SaleRequest> items) {

        Bill bill = new Bill();
        bill.setBillNo("BILL-" + System.currentTimeMillis());
        billRepo.save(bill);

        for (SaleRequest req : items) {

            Medicine med = medicineRepo.findById(req.getMedicineId())
                    .orElseThrow(() -> new RuntimeException("Medicine not found"));

            // 🔽 reduce stock
            med.setQuantity(med.getQuantity() - req.getQuantity());
            medicineRepo.save(med);

            // 🔥 SAVE INTO SALES TABLE (THIS WAS MISSING)
            Sale sale = new Sale();
            sale.setBillNo(bill.getBillNo());
            sale.setMedicineId(med.getId());
            sale.setMedicineName(med.getName());
            sale.setBatchNo(med.getBatchNo());
            sale.setQuantity(req.getQuantity());
            sale.setPricePerUnit(req.getPrice());
            sale.setTotalAmount(req.getQuantity() * req.getPrice());

            saleRepo.save(sale);
        }

        return bill;
    }

    public List<Sale> getSalesHistory() {
        return saleRepo.findAll();
    }
}

