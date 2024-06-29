package com.example.myapplication.Model;

public class OrderPrice {
    Double totalPrice;
    boolean isPaper;
    boolean isPlastic;
    Double weightPaper;
    Double weightPlastic;
    public OrderPrice(){}
    public OrderPrice(Double price, boolean isPaper, boolean isPlastic, Double weightPaper, Double weightPlastic){
        this.totalPrice = price;
        this.isPaper = isPaper;
        this.isPlastic = isPlastic;
        this.weightPaper = weightPaper;
        this.weightPlastic = weightPlastic;
    }
    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
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
