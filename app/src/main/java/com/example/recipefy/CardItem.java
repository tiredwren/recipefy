package com.example.recipefy;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.Date;

public class CardItem implements Parcelable {

    private String itemName;
    private Date expiryDate;
    private boolean isSelected;

    public CardItem(String itemName, Date expiryDate) {
        this.itemName = itemName;
        this.expiryDate = expiryDate;
        this.isSelected = false;
    }

    protected CardItem(Parcel in) {
        itemName = in.readString();
        expiryDate = new Date(in.readLong());
        isSelected = in.readByte() != 0;
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

    public Date getExpiryDate() {
        return expiryDate;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(itemName);
        dest.writeLong(expiryDate.getTime());
        dest.writeByte((byte) (isSelected ? 1 : 0));
    }
}
