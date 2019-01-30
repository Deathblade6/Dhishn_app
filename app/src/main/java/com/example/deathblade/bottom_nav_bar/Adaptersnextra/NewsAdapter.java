package com.example.deathblade.bottom_nav_bar.Adaptersnextra;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.deathblade.bottom_nav_bar.R;

import java.util.ArrayList;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsHolder>{

    private ArrayList<News> data;

    public NewsAdapter(ArrayList<News> news){
        data = news;
    }

    public class NewsHolder extends RecyclerView.ViewHolder{

        private TextView titleTextView, messageTextView;

        public View getItemView(){
            return itemView;
        }

        public NewsHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.title_text_view);
            messageTextView = itemView.findViewById(R.id.message_text_view);
        }
    }

    @NonNull
    @Override
    public NewsHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.news_list_item, viewGroup, false);
        NewsHolder holder = new NewsHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull NewsHolder newsHolder, int i) {
        News news = data.get(i);
        newsHolder.titleTextView.setText(news.getmTitle());
        newsHolder.messageTextView.setText(news.getmMessage());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(ArrayList<News> data) {
        this.data = data;
    }

}
