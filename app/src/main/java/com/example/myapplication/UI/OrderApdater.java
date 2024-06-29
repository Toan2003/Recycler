package com.example.myapplication.UI;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Model.Order;
import com.example.myapplication.R;

import java.util.ArrayList;

public class OrderApdater extends RecyclerView.Adapter<OrderApdater.ViewHolder> {
    Context context;
    ArrayList<Order> orderList;

    public OrderApdater(Context context, ArrayList<Order> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // gán view
        View view = LayoutInflater.from(context).inflate(R.layout.order_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Gán dữ liêuk
        Order sanPham = orderList.get(position);
    }

    @Override
    public int getItemCount() {
        return orderList.size(); // trả item tại vị trí postion
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Ánh xạ view
//            imgAvatar = itemView.findViewById(R.id.imgAvatar);
//            txtGiaSanPham = itemView.findViewById(R.id.txtGiaSanPham);
//            txtTenSanPham = itemView.findViewById(R.id.txtTenSanPham);

        }
    }
}
