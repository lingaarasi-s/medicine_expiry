package com.pharmacy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pharmacy.model.MasterMedicine;
import com.pharmacy.model.Medicine;
import com.pharmacy.repository.MasterMedicineRepository;
import com.pharmacy.repository.MedicineRepository;

@Service
public class MedicineService {

    @Autowired
    private MedicineRepository medicineRepo;

    @Autowired
    private MasterMedicineRepository masterRepo;

    /* =============================
       MANUAL ENTRY (NO DUPLICATES)
    ============================== */
    public Medicine addOrUpdate(Medicine med) {

        return medicineRepo
            .findByNameAndBatchNoAndExpiryDate(
                med.getName(),
                med.getBatchNo(),
                med.getExpiryDate()
            )
            .map(existing -> {
                // ✅ already exists → update quantity
                existing.setQuantity(
                    existing.getQuantity() + med.getQuantity()
                );
                return medicineRepo.save(existing);
            })
            .orElseGet(() -> {
                // ✅ new medicine → insert
                return medicineRepo.save(med);
            });
    }

    /* =============================
       BARCODE ENTRY (NO DUPLICATES)
    ============================== */
    public Medicine addByBarcode(String barcode, int quantity) {

        MasterMedicine master = masterRepo.findByBarcode(barcode)
            .orElseThrow(() ->
                new RuntimeException("Barcode not found in master medicine"));

        return medicineRepo
            .findByNameAndBatchNoAndExpiryDate(
                master.getName(),
                master.getBatchNo(),
                master.getExpiryDate()
            )
            .map(existing -> {
                existing.setQuantity(existing.getQuantity() + quantity);
                return medicineRepo.save(existing);
            })
            .orElseGet(() -> {
                Medicine med = new Medicine();
                med.setName(master.getName());
                med.setBatchNo(master.getBatchNo());
                med.setManufacturer(master.getManufacturer());
                med.setCategory(master.getCategory());
                med.setExpiryDate(master.getExpiryDate());
                med.setSellingPrice(master.getSellingPrice());
                med.setQuantity(quantity);
                return medicineRepo.save(med);
            });
    }
}
