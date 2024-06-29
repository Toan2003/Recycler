package com.example.myapplication.Service;

import androidx.lifecycle.MutableLiveData;

import com.example.myapplication.Model.Customer;
import com.example.myapplication.Model.Order;
import com.example.myapplication.Model.OrderPrice;

public class DriverServiceStatus {
    public static final int FOUND_ORDER = 0;
    private static DriverServiceStatus instance;
    private MutableLiveData<Integer> orderStatus;
    private Order order;

    private Customer customer;

    private OrderPrice orderPrice;

    private DriverServiceStatus() {
        orderStatus = new MutableLiveData<>();
    }

    public static synchronized DriverServiceStatus getInstance() {
        if (instance == null) {
            instance = new DriverServiceStatus();
        }
        return instance;
    }

    public MutableLiveData<Integer> getStatus() {
        return orderStatus;
    }
    public void setOrder(Order order) {
        this.order = order;
    }
    public Order getOrder(){
        return  order;
    }
    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    public OrderPrice getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(OrderPrice orderPrice) {
        this.orderPrice = orderPrice;
    }


}


