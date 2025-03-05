package com.textile.factory.model;

public class Customer {
    private int customerId;
    private String name;
    private String address;
    private String contactDetails;

    public Customer(int customerId, String name, String address, String contactDetails) {
        this.customerId = customerId;
        this.name = name;
        this.address = address;
        this.contactDetails = contactDetails;
    }

    public int getCustomerId() { return customerId; }
    public String getName() { return name; }
    public String getAddress() { return address; }
    public String getContactDetails() { return contactDetails; }
}