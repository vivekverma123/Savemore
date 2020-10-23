package com.example.model;

public class Account {

    private String name;
    private int balance,debit,credit;

    public Account(String name, int balance, int debit, int credit) {
        this.name = name;
        this.balance = balance;
        this.debit = debit;
        this.credit = credit;
    }

    public Account() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public int getDebit() {
        return debit;
    }

    public void setDebit(int debit) {
        this.debit = debit;
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }
}
