package com.example.brandnewsuitecase.Models;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Product  {
    private String itemName,price,description;
    private String image;
    private  String key;


    public Product() {
    }

    public Product(String itemName, String price, String description, String image, String key) {
        this.itemName = itemName;
        this.price = price;
        this.description = description;
        this.image = image;
        this.key = key;

    }

    protected Product(Parcel in) {
        itemName = in.readString();
        price = in.readString();
        description = in.readString();
        image = in.readString();//to load the image
    }


    @Override
    public String toString() {
        return "Product{" +
                "itemName='" + itemName + '\'' +
                ", price='" + price + '\'' +
                ", description='" + description + '\'' +
                ", image='" + image + '\'' +
                ", key='" + key + '\'' +
                '}';
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }





}
