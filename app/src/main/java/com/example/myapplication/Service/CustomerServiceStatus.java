package com.example.myapplication.Service;

import androidx.lifecycle.MutableLiveData;

import com.example.myapplication.Model.Driver;
import com.example.myapplication.Model.Order;
import com.example.myapplication.Model.OrderPrice;

public class CustomerServiceStatus {
    public static final int FOUND_DRIVER = 2;
    public static final int DRIVER_COMPLETE = 3;
    private static CustomerServiceStatus instance;
    private MutableLiveData<Integer> orderStatus;
    private Order order;

    private Driver driver;

    private OrderPrice orderPrice;

    private CustomerServiceStatus() {
        orderStatus = new MutableLiveData<>();
    }

    public static synchronized CustomerServiceStatus getInstance() {
        if (instance == null) {
            instance = new CustomerServiceStatus();
        }
        return instance;
    }

    public MutableLiveData<Integer> getStatus() {
        return orderStatus;
    }
    public void setOrder(Order order) {
        this.order = order;
    }
    public Order getOrder(){return  order;}

    public Driver getDriver() {
        return driver;
    }
    public void setDriver(Driver driver) {
        this.driver = driver;
    }
    public OrderPrice getOrderPrice() {
        return orderPrice;
    }
    public void setOrderPrice(OrderPrice orderPrice) {
        this.orderPrice = orderPrice;
    }

}


