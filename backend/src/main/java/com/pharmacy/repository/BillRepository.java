package com.pharmacy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pharmacy.model.Bill;

public interface BillRepository extends JpaRepository<Bill, Integer> {
    List<Bill> findAllByOrderBySaleDateDesc();
}

