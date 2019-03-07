package com.example.myapplication;

import android.os.Parcel;
import android.os.Parcelable;


public class WordsPairs implements Parcelable {

    private String eng = null;
    private String span = null;
    private int totalWrong = 0;

    public WordsPairs() {
    }
    public WordsPairs(String eng, String span) {
        this.eng = eng;
        this.span = span;
    }
    public WordsPairs(String eng, String span, int wrong) {
        this.eng = eng;
        this.span = span;
        this.totalWrong = wrong;
    }

    public String getENG() {
        return eng;
    }

    public void setENG(String eng) {
        this.eng = eng;
    }

    public String getSPAN() {
        return span;
    }

    public void setSPAN(String span) {
        this.span = span;
    }

    public int getTotal() {
        return totalWrong;
    }

    public void setTotal(int total) {
        totalWrong = total;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(eng);
        parcel.writeString(span);
    }

    public static final Parcelable.Creator<WordsPairs> CREATOR = new Parcelable.Creator<WordsPairs>() {
        @Override
        public WordsPairs createFromParcel(Parcel parcel) {
            WordsPairs mPairs = new WordsPairs();
            mPairs.eng = parcel.readString();
            mPairs.span = parcel.readString();
            return mPairs;
        }

        @Override
        public WordsPairs[] newArray(int i) {
            return new WordsPairs[i];
        }
    };

}