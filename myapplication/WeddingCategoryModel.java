package com.example.myapplication;

public class WeddingCategoryModel {

    int image;
    String name;
    String category;

    public WeddingCategoryModel(int image, String name, String category) {
        this.image = image;
        this.name = name;
        this.category = category;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

}
