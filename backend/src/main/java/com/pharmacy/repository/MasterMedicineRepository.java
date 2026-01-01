package com.pharmacy.repository;

import com.pharmacy.model.MasterMedicine;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MasterMedicineRepository
        extends JpaRepository<MasterMedicine, Integer> {

    Optional<MasterMedicine> findByBarcode(String barcode);
}
