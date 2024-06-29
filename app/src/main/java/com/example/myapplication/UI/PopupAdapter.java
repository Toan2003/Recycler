package com.example.myapplication.UI;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class PopupAdapter extends FragmentStateAdapter {

    public PopupAdapter(@NonNull DriverFragment fragmentActivity) {
        super(fragmentActivity);
    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new PopupPage1Fragment(); // Your first page content
            case 1:
                return new PopupPage1Fragment(); // Your second page content
            default:
                return null;
        }

    }
    @Override
    public int getItemCount() {
        return 2; // Số lượng trang bạn muốn hiển thị
    }
}
