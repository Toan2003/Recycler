package com.example.myapplication.UI;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapplication.Model.Post;
import com.example.myapplication.R;

import java.util.List;

public class SocialCustomAdapter extends ArrayAdapter<Post> {
    Context context;
    List<Post> post;
    public SocialCustomAdapter(Context context, int layoutToBeInflated, List<Post> post) {
        super(context, R.layout.social_list_view, post);
        this.context = context;
        this.post = post;
    }
    static class ViewHolder {
        ImageView imagesocial;
        TextView titlesocial, contentsocial;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if (convertView ==null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.social_list_view, null);

            holder = new ViewHolder();
            holder.imagesocial =(ImageView) convertView.findViewById(R.id.imagesocial);
            holder.titlesocial = (TextView) convertView.findViewById(R.id.titlesocial);
            holder.contentsocial = (TextView) convertView.findViewById(R.id.contentSocial);

            Post cur = post.get(position);

            holder.titlesocial.setText(cur.getTitle());
            holder.contentsocial.setText(cur.getContent());
            holder.imagesocial.setImageResource(cur.getImage());

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        return convertView;
    }
}


