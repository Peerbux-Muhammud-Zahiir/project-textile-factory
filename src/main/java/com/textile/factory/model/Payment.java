package com.textile.factory.model;

import java.time.LocalDate;

public class Payment {
    private int paymentId;
    private int orderId;
    private String paymentMethod;
    private double amount;
    private LocalDate paymentDate;
    private String creditCardNumber;
    private String expirationDate;
    private String bankNumber;
    private String cardHolderName;

    // Constructor
    public Payment(int paymentId, int orderId, String paymentMethod, double amount, LocalDate paymentDate, String creditCardNumber, String expirationDate, String bankNumber, String cardHolderName) {
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.paymentMethod = paymentMethod;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.creditCardNumber = creditCardNumber;
        this.expirationDate = expirationDate;
        this.bankNumber = bankNumber;
        this.cardHolderName = cardHolderName;
    }

    // Getters and Setters
    public int getPaymentId() { return paymentId; }
    public int getOrderId() { return orderId; }
    public String getPaymentMethod() { return paymentMethod; }
    public double getAmount() { return amount; }
    public LocalDate getPaymentDate() { return paymentDate; }
    public String getCreditCardNumber() { return creditCardNumber; }
    public String getExpirationDate() { return expirationDate; }
    public String getBankNumber() { return bankNumber; }
    public String getCardHolderName() { return cardHolderName; }
}