package com.example.myapplication.Service;

import androidx.lifecycle.MutableLiveData;

import com.example.myapplication.Model.TypeAmount;

import java.util.List;

public class TotalLiveData {
    private static TotalLiveData instance;
    private MutableLiveData<Boolean> isEdit;

    private List<TypeAmount> list;


    private TotalLiveData() {
        isEdit = new MutableLiveData<>();
    }

    public static synchronized TotalLiveData getInstance() {
        if (instance == null) {
            instance = new TotalLiveData();
        }
        return instance;
    }

    public List<TypeAmount> getList() {
        return list;
    }

    public void setList(List<TypeAmount> list) {
        this.list = list;
    }

    public MutableLiveData<Boolean> getIsEdit() {
        return isEdit;
    }

}
