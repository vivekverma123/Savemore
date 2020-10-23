package com.example.model;

import java.util.*;

public class Transaction {

    private long time;
    private int type;

    /* 0 for debit
       1 for credit */

    private String category;

    /*only for credit transactions will be user defined strings to group expenses as per his need */

    public Transaction(long time, int type, String category) {
        this.time = time;
        this.type = type;
        this.category = category;
    }

    public Transaction() {
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Date giveDate()
    {
        Date d1 = new Date(time);
        return d1;
    }
}
