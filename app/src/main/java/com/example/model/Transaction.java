package com.example.model;

import java.util.*;

public class Transaction {

    private long time;
    private int type,amount,bal;

    /* 0 for debit
       1 for credit */

    private String category;
    private String id;

    /*only for credit transactions will be user defined strings to group expenses as per his need */

    public Transaction(long time, int type, int amount, int bal, String category, String id) {
        this.time = time;
        this.type = type;
        this.amount = amount;
        this.bal = bal;
        this.category = category;
        this.id = id;
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

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getBal() {
        return bal;
    }

    public void setBal(int bal) {
        this.bal = bal;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
