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

    public Medicine addByBarcode(String barcode, int quantity) {

        MasterMedicine master = masterRepo.findByBarcode(barcode)
            .orElseThrow(() ->
                new RuntimeException("Barcode not found in master medicine"));

        Medicine med = new Medicine();
        med.setName(master.getName());
        med.setBarcode(master.getBarcode());
        med.setBatchNo(master.getBatchNo());
        med.setManufacturer(master.getManufacturer());
        med.setCategory(master.getCategory());
        med.setExpiryDate(master.getExpiryDate());
        med.setQuantity(quantity);

        return medicineRepo.save(med);
    }
}
