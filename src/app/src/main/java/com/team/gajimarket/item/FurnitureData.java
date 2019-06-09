package com.team.gajimarket.item;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class FurnitureData {
    private String sellerName, sellerEmail;
    private String furnitureName;
    private String price;
    private String width, depth, height;

    public FurnitureData() {}

    public FurnitureData(String sellerName, String sellerEmail, String furnitureName, String price, String width, String depth, String height) {
        //this.sellerID = sellerID;
        this.sellerName = sellerName;
        this.sellerEmail = sellerEmail;
        this.furnitureName = furnitureName;
        this.price = price;
        this.width = width;
        this.depth = depth;
        this.height = height;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        //result.put("sellerID", sellerID);
        result.put("sellerName", sellerName);
        result.put("sellerEmail", sellerEmail);
        result.put("furnitureName", furnitureName);
        result.put("price", price);
        result.put("width", width);
        result.put("depth", depth);
        result.put("height", height);

        return result;
    }

    //public String getSellerID() { return sellerID; }
    public String getSellerName() { return sellerName; }
    public String getSellerEmail() { return sellerEmail; }
    public String getFurnitureName() {
        return furnitureName;
    }
    public String getPrice() {
        return price;
    }
    public String getWidth() { return width; }
    public String getDepth() { return depth; }
    public String getHeight() { return height; }
}
