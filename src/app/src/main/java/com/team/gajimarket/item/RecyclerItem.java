package com.team.gajimarket.item;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class RecyclerItem implements Parcelable{
    public String key;
    public String name;
    public String price;
    public String size;
    public Bitmap drawableID;

    @Override
    public int describeContents() { return 0; }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(key);
        dest.writeString(name);
        dest.writeString(price);
        dest.writeString(size);
    }

    public static final Parcelable.Creator<RecyclerItem> CREATOR = new Parcelable.Creator<RecyclerItem>() {
        public RecyclerItem createFromParcel(Parcel in) {
            return new RecyclerItem(in);
        }

        public RecyclerItem[] newArray(int size) {
            return new RecyclerItem[size];
        }
    };

    public RecyclerItem(Parcel src) {
        readFromParcel(src);
    }

    public void readFromParcel(Parcel src) {
        key = src.readString();
        name = src.readString();
        price = src.readString();
        size = src.readString();
    }

    public RecyclerItem() { }

    public RecyclerItem (String key, String name, String price, String size, Bitmap drawableID) {
        this.key = key;
        this.name = name;
        this.price = price;
        this.size = size;
        this.drawableID = drawableID;
    }

    public String getKey() { return key; }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getSize() { return size; }

    public Bitmap getDrawableID() { return drawableID; }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setSize(String size) { this.size = size; }

    public void setPic(Bitmap bitmap) { this.drawableID = bitmap; }
}
