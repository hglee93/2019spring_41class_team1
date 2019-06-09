package com.team.gajimarket.item;

import android.graphics.Bitmap;

public class RecyclerItem {
    public String name;
    public String price;
    public String size;
    public Bitmap drawableID;

    public RecyclerItem() { }

    public RecyclerItem(String name, String price, String size, Bitmap drawableID) {
        this.name = name;
        this.price = price;
        this.size = size;
        this.drawableID = drawableID;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getSize() { return size; }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setSize(String size) { this.size = size; }

    public void setPic(Bitmap bitmap) { this.drawableID = bitmap; }
}
