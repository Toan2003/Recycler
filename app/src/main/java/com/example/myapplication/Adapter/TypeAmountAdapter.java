package com.example.myapplication.Adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Model.TypeAmount;
import com.example.myapplication.R;
import com.example.myapplication.Service.TotalLiveData;
import com.example.myapplication.UI.ClientOrderFragment2;

import java.util.List;

public class TypeAmountAdapter extends  RecyclerView.Adapter<TypeAmountAdapter.TypeAmountViewHolder> {
    private Context context;
    private List<TypeAmount> typeAmountList;

    public void setTypeAmountList(List<TypeAmount> typeAmountList) {
        this.typeAmountList = typeAmountList;
        notifyDataSetChanged();
    }
    public TypeAmountAdapter(Context context) {
        this.context = context;
    }

    public List<TypeAmount> getTypeAmountList() {
        return typeAmountList;
    }
    @NonNull
    @Override
    public TypeAmountViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pop_up_card, parent, false);
        return new TypeAmountViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TypeAmountViewHolder holder, int position) {
        TypeAmount typeAmount = typeAmountList.get(position);
        if (typeAmount == null) {
            Log.d("TypeAmountAdapter", "onBindViewHolder: typeAmount is null");
            return;
        }
        Log.d("TypeAmountAdapter", "onBindViewHolder: typeAmount is not null");
        holder.type.setText(typeAmount.getType());
//        holder.amount.setText(String.valueOf(typeAmount.getAmount()));
        holder.amount.setHint(typeAmount.getHintString());
        holder.amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                typeAmount.setIsHave(true);
                try {
                    typeAmount.setAmount(Double.valueOf(s.toString()));
                } catch (Exception e){
                    typeAmount.setAmount(0.0);
                }
                TotalLiveData.getInstance().setList(typeAmountList);
                TotalLiveData.getInstance().getIsEdit().setValue(Boolean.TRUE);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (typeAmountList != null) {
            return typeAmountList.size();
        }
        return 0;
    }

    public class TypeAmountViewHolder extends RecyclerView.ViewHolder {
        private EditText amount;
        private TextView type;

        public TypeAmountViewHolder(@NonNull View itemView) {
            super(itemView);
            amount = itemView.findViewById(R.id.amount12);
            type = itemView.findViewById(R.id.type12);
        }
    }
}
