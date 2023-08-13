package com.example.quick_food.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class DishModel implements Parcelable {
    public String imageURL;
    public String name;
    public String description;
    public double price;

    public DishModel (String imageURL, String name, String description) {
        this.imageURL = imageURL;
        this.name = name;
        this.description = description;
        price = 10.99;
    }

    protected DishModel(Parcel in) {
        imageURL = in.readString();
        name = in.readString();
        description = in.readString();
        price = in.readDouble();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(imageURL);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeDouble(price);
    }

    public static final Creator<DishModel> CREATOR = new Creator<DishModel>() {
        @Override
        public DishModel createFromParcel(Parcel in) {
            return new DishModel(in);
        }

        @Override
        public DishModel[] newArray(int size) {
            return new DishModel[size];
        }
    };
}
