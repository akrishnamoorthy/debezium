package com.notification;

public class CustomerOrder {
    private int id;
    private String customerId;
    private String dealerDetails;
    private String orderCity;
    private String invoiceNo;

    CustomerOrder(int id, String dealerDetails){
        this.id = id;
        this.dealerDetails = dealerDetails;
    }
    CustomerOrder(){

    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getDealerDetails() {
        return dealerDetails;
    }

    public void setDealerDetails(String dealerDetails) {
        this.dealerDetails = dealerDetails;
    }

    public String getOrderCity() {
        return orderCity;
    }

    public void setOrderCity(String orderCity) {
        this.orderCity = orderCity;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }
}