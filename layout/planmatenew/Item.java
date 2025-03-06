package com.example.planmatenew;

public class Item {

    private int id;
    private String itemName;
    private double itemPrice;
    private byte[] itemImage;

    public Item(int id, String itemName, double itemPrice, byte[] itemImage) {
        this.id = id;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.itemImage = itemImage;
    }

    public int getId() { return id; }
    public String getItemName() { return itemName; }
    public double getItemPrice() { return itemPrice; }
    public byte[] getItemImage() { return itemImage; }
}
