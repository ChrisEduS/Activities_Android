package com.example.project5;

import java.util.Date;

public class Expense {
    private Type type;
    private String detail;
    private double amount;
    private String date;

    public Expense(Type type, String detail, double amount, String date) {
        this.type = type;
        this.detail = detail;
        this.amount = amount;
        this.date = date;
    }

    public Type getType() {
        return type;
    }
    public String getDetail(){
        return detail;
    }

    public double getAmount() {
        return amount;
    }

    public String getDate() {
        return date;
    }

    public void setType(Type type) {
        this.type = type;
    }
    public void setDetail(String detail){
        this.detail = detail;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setDate(String date) {
        this.date = date;
    }
    @Override
    public String toString(){
        String str;
        str = this.type.getTypeName()+"-"+this.detail+"-"+this.date+
                "-"+this.amount;
        return str;
    }
}
