package com.example.myapplication;

public class WeddingShopModel {
    private int regNo;
    private String shopCategory;
    private String shopName;
    private String shopImage;
    private String eventType;

    public WeddingShopModel(int regNo, String shopCategory, String shopName, String shopImage, String eventType) {
        this.regNo = regNo;
        this.shopCategory = shopCategory;
        this.shopName = shopName;
        this.shopImage = shopImage;
        this.eventType = eventType;
    }

    // Getters
    public int getRegNo() { return regNo; }
    public String getShopCategory() { return shopCategory; }
    public String getShopName() { return shopName; }
    public String getShopImage() { return shopImage; }
    public String getEventType() { return eventType; }
}