package com.textile.factory.model;

public class Item {
    private int itemId;
    private String itemNumber;
    private String category;
    private String size;
    private double costPrice;
    private int stockLevel;
    private int outOfStockThreshold;

    public Item(int itemId, String itemNumber, String category, String size, double costPrice, int stockLevel, int outOfStockThreshold) {
        this.itemId = itemId;
        this.itemNumber = itemNumber;
        this.category = category;
        this.size = size;
        this.costPrice = costPrice;
        this.stockLevel = stockLevel;
        this.outOfStockThreshold = outOfStockThreshold;
    }

    public int getItemId() { return itemId; }
    public String getItemNumber() { return itemNumber; }
    public String getCategory() { return category; }
    public String getSize() { return size; }
    public double getCostPrice() { return costPrice; }
    public int getStockLevel() { return stockLevel; }
    public int getOutOfStockThreshold() { return outOfStockThreshold; }
}