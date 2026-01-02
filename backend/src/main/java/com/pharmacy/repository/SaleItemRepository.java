package com.pharmacy.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pharmacy.model.SaleItem;

public interface SaleItemRepository extends JpaRepository<SaleItem, Integer> {
}
