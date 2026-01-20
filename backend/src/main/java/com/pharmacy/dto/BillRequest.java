package com.pharmacy.dto;

import java.util.List;

public class BillRequest {

    private String customerName;
    private String customerPhone;
    private List<SaleRequest> items;

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public List<SaleRequest> getItems() {
        return items;
    }

    public void setItems(List<SaleRequest> items) {
        this.items = items;
    }
}