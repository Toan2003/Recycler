package com.example.myapplication.Models;

public class OrderPrice {
    Double price;
    boolean isPaper;
    boolean isPlastic;
    Double weightPaper;
    Double weightPlastic;
    public OrderPrice(){}
    public OrderPrice(Double price, boolean isPaper, boolean isPlastic, Double weightPaper, Double weightPlastic){
        this.price = price;
        this.isPaper = isPaper;
        this.isPlastic = isPlastic;
        this.weightPaper = weightPaper;
        this.weightPlastic = weightPlastic;
    }
    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public boolean isPaper() {
        return isPaper;
    }

    public void setPaper(boolean paper) {
        isPaper = paper;
    }

    public boolean isPlastic() {
        return isPlastic;
    }

    public void setPlastic(boolean plastic) {
        isPlastic = plastic;
    }

    public Double getWeightPaper() {
        return weightPaper;
    }

    public void setWeightPaper(Double weightPaper) {
        this.weightPaper = weightPaper;
    }

    public Double getWeightPlastic() {
        return weightPlastic;
    }

    public void setWeightPlastic(Double weightPlastic) {
        this.weightPlastic = weightPlastic;
    }

}