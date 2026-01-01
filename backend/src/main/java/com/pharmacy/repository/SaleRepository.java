package com.pharmacy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.pharmacy.model.Sale;

public interface SaleRepository extends JpaRepository<Sale, Integer> {

    // Monthly sales summary (by medicine name)
    @Query("""
        SELECT s.medicineName, SUM(s.quantity), SUM(s.totalAmount)
        FROM Sale s
        WHERE MONTH(s.saleDate) = MONTH(CURRENT_DATE)
          AND YEAR(s.saleDate) = YEAR(CURRENT_DATE)
        GROUP BY s.medicineName
    """)
    List<Object[]> monthlySales();
}
