package com.example.myapplication;

import java.io.Serializable;

public class WordsPairs implements Serializable {

    private String eng = null;
    private String span = null;
    private int totalWrong = 0;

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
}
