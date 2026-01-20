package com.pharmacy.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pharmacy.model.Medicine;

public interface MedicineRepository extends JpaRepository<Medicine, Integer> {

    @Query("DELETE FROM Medicine m WHERE m.quantity <= 0")
    @Modifying
    @Transactional
    void deleteZeroStock();

    Optional<Medicine> 
    findByNameAndBatchNoAndExpiryDate(
        String name,
        String batchNo,
        LocalDate expiryDate
    );



    @Query("""
SELECT m FROM Medicine m
WHERE m.expiryDate BETWEEN :today AND :futureDate
""")
    List<Medicine> findExpireSoon(
        @Param("today") LocalDate today,
        @Param("futureDate") LocalDate futureDate
    );

    // ✅ Already expired
    @Query(
      value = "SELECT * FROM medicine WHERE expiry_date < CURDATE()",
      nativeQuery = true
    )
    List<Medicine> findExpired();

    // ✅ Low stock (<100)
    List<Medicine> findByQuantityLessThan(int qty);

}
