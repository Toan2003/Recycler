package com.example.myapplication.Model;

public class TypeAmount {
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getAmount() {
        return Amount;
    }

    public void setAmount(Double amount) {
        Amount = amount;
    }

    public TypeAmount(String type, Double amount, String hintString) {
        this.type = type;
        Amount = amount;
    }

    private String type;
    private Double Amount;
    private String hintString;

    public String getHintString() {
        return hintString;
    }

    public void setHintString(String hintString) {
        this.hintString = hintString;
    }
}
