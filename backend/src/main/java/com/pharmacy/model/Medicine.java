package com.pharmacy.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(
  name = "medicine",
  uniqueConstraints = {
    @UniqueConstraint(
      columnNames = {"name", "batch_no", "expiry_date"}
    )
  }
)

public class Medicine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String name;

    @Column(name = "batch_no", nullable = false)
    private String batchNo;

    @Column(name = "expiry_date", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate expiryDate;

    @Column(nullable = false)
    private String manufacturer;

    @Column(nullable = false)
    private String category;

    @Column(name = "return_policy")
    private String returnPolicy;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private double sellingPrice;

    @Column(name = "added_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate addedDate;

    @PrePersist
    public void onCreate() {
        this.addedDate = LocalDate.now();
    }

    // ===== Getters & Setters =====
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getBatchNo() { return batchNo; }
    public void setBatchNo(String batchNo) { this.batchNo = batchNo; }

    public LocalDate getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }

    public String getManufacturer() { return manufacturer; }
    public void setManufacturer(String manufacturer) { this.manufacturer = manufacturer; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getReturnPolicy() { return returnPolicy; }
    public void setReturnPolicy(String returnPolicy) { this.returnPolicy = returnPolicy; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getSellingPrice() { return sellingPrice; }
    public void setSellingPrice(double sellingPrice) { this.sellingPrice = sellingPrice; }

    public LocalDate getAddedDate() { return addedDate; }

}
