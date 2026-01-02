package com.pharmacy.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "sale_item")
public class SaleItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "bill_id")
    private Bill bill;


    private String medicineName;
    private String batchNo;
    private int quantity;
    private double pricePerUnit;
    private double totalAmount;

    // ===== GETTERS =====

    public int getId() {
        return id;
    }

    public Bill getBill() {
        return bill;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPricePerUnit() {
        return pricePerUnit;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    // ===== SETTERS =====

    public void setId(int id) {
        this.id = id;
    }

    public void setBill(Bill bill) {
        this.bill = bill;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setPricePerUnit(double pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
}
