package com.example.recipefy;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CardItem implements Parcelable, Serializable {

    private String itemName;
    private String expiryDate;
    private String id;
    private boolean isSelected;

    public CardItem() {
        //empty for deserialization
    }

    public CardItem(String itemName, String expiryDate) {
        this.itemName = itemName;
        this.expiryDate = expiryDate;
        this.isSelected = false;
    }

    protected CardItem(Parcel in) {
        itemName = in.readString();
        expiryDate = String.valueOf(new Date(in.readLong()));
        isSelected = in.readByte() != 0;
    }

    private String userId;

    public CardItem(String userId) {
        // Assign other parameters...
        this.userId = userId;
    }

    public static final Creator<CardItem> CREATOR = new Creator<CardItem>() {
        @Override
        public CardItem createFromParcel(Parcel in) {
            return new CardItem(in);
        }

        @Override
        public CardItem[] newArray(int size) {
            return new CardItem[size];
        }
    };

    public String getItemName() {
        return itemName;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setTitle(String itemName) {
        this.itemName = itemName;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getItemId() {
        return id;
    }

    public void setItemId(String itemId) {
        this.id = itemId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(itemName);
        dest.writeLong(Long.parseLong(expiryDate));
        dest.writeByte((byte) (isSelected ? 1 : 0));
    }

    public Date getExpiryDateAsDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            return dateFormat.parse(expiryDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}


