package com.example.myapplication;

import android.os.Parcel;

import org.junit.Test;

import static org.junit.Assert.*;

public class WordsPairsTest {

    @Test
    public void getENG() {
        String eng1 = "English_Word1";
        String span1 = "Spanish_Word1";
        int w = 0;
        WordsPairs pair1 = new WordsPairs(eng1, span1, w);
        assertEquals("English_Word1", pair1.getENG());
    }

    @Test
    public void setENG() {
        String eng1 = "English_Word1";
        String span1 = "Spanish_Word1";
        int w = 0;
        WordsPairs pair1 = new WordsPairs(eng1, span1, w);
        String eng2 = "English_Word2";
        pair1.setENG(eng2);
        assertEquals("English_Word2", pair1.getENG());
        String eng3 = "English_Word3";
        pair1.setENG(eng3);
        assertEquals("English_Word3", pair1.getENG());
        String eng4 = "English_Word4";
        pair1.setENG(eng4);
        assertEquals("English_Word4", pair1.getENG());
    }

    @Test
    public void getSPAN() {
        String eng1 = "English_Word1";
        String span1 = "Spanish_Word1";
        int w = 0;
        WordsPairs pair1 = new WordsPairs(eng1, span1, w);
        assertEquals("Spanish_Word1", pair1.getSPAN());
    }

    @Test
    public void setSPAN() {
        String eng1 = "English_Word1";
        String span1 = "Spanish_Word1";
        int w = 0;
        WordsPairs pair1 = new WordsPairs(eng1, span1, w);
        String span2 = "Spanish_Word2";
        pair1.setSPAN(span2);
        assertEquals("Spanish_Word2", pair1.getSPAN());
        String span3 = "Spanish_Word3";
        pair1.setSPAN(span3);
        assertEquals("Spanish_Word3", pair1.getSPAN());
        String span4 = "Spanish_Word4";
        pair1.setSPAN(span4);
        assertEquals("Spanish_Word4", pair1.getSPAN());
    }

    @Test
    public void getTotal() {
        String eng1 = "English_Word1";
        String span1 = "Spanish_Word1";
        int w = 100;
        WordsPairs pair1 = new WordsPairs(eng1, span1, w);
        assertEquals(100, pair1.getTotal());
    }

    @Test
    public void setTotal() {
        String eng1 = "English_Word1";
        String span1 = "Spanish_Word1";
        int w = 100;
        WordsPairs pair1 = new WordsPairs(eng1, span1, w);
        int w2 = 17;
        pair1.setTotal(w2);
        assertEquals(17, pair1.getTotal());
        int w3 = 27;
        pair1.setTotal(w3);
        assertEquals(27, pair1.getTotal());
        int w4 = 44;
        pair1.setTotal(w4);
        assertEquals(44, pair1.getTotal());
    }

    @Test
    public void describeContents() {
        WordsPairs pair1 = new WordsPairs();
        assertEquals(0, pair1.describeContents());
    }

}