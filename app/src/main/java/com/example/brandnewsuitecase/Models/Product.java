package com.example.brandnewsuitecase.Models;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Product implements Parcelable {
    private String itemName,price,description,itemID;
    private String image;

    public Product() {
    }

    public Product(String courseName, String price, String description, String image, String itemID) {
        this.itemName = courseName;
        this.price = price;
        this.description = description;
        this.image = image;
        this.itemID = itemID;
    }

    protected Product(Parcel in) {
        itemName = in.readString();
        price = in.readString();
        description = in.readString();
        image = in.readString();//to load the image

        itemID = in.readString();
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    @Override
    public String toString() {
        return
                " courseName:" + itemName + "\n"+
                        " price:" + price  +"\n"+
                        " description:" + description + "\n"
                ;
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



    public String getItemID() {
        return itemID;
    }

    public void setItemID(String itemID) {
        this.itemID = itemID;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int flags) {
        parcel.writeString(itemName);
        parcel.writeString(price);
        parcel.writeString(description);
        parcel.writeString(image);//writing parcelable object

        parcel.writeString(itemID);
    }



}
