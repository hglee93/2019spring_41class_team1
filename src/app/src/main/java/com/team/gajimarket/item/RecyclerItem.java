package com.team.gajimarket.item;

public class RecyclerItem {
    String name;
    String price;
    String size;

    public RecyclerItem() { }

    public RecyclerItem(String name, String price, String size) {
        this.name = name;
        this.price = price;
        this.size = size;
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
}
