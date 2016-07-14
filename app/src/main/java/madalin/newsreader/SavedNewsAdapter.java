package madalin.newsreader;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.StringDef;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import madalin.newsreader.models.NewsItem;

/**
 * Created by madalin2 on 11.06.2016.
 */

public class SavedNewsAdapter extends RecyclerView.Adapter<SavedNewsAdapter.ViewHolder> {
    ViewHolder holder;

    private final List<NewsItem> items = new ArrayList<>();

    interface OnItemClickedListener{
        void onItemClicked(NewsItem item);
    }

    OnItemClickedListener listener;
    public SavedNewsAdapter(OnItemClickedListener listener){
        this.listener = listener;
    }

    public void updateItems(List<NewsItem> newsItems){
        items.clear();
        items.addAll(newsItems);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.saved_news_content, parent, false);
        holder = new ViewHolder(itemView);

        itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                listener.onItemClicked(holder.item);
            }

        });


        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //efectueaza apeluri la numarul specificat
//                Uri number = Uri.parse("tel:654897213");
//                Intent callIntent = new Intent(Intent.ACTION_VIEW, number);
//                v.getContext().startActivity(callIntent);

                Intent urlIntent = new Intent();
                urlIntent.setAction(Intent.ACTION_SEND);
                urlIntent.putExtra(Intent.EXTRA_TEXT, holder.item.getContentUrl());
                urlIntent.setType("text/plain");

                //v.getContext().startActivity(urlIntent); //daca ar fi ceva default, nu mi-ar conveni

                v.getContext().startActivity(Intent.createChooser(urlIntent, "Share with..."));
            }
        });
        return holder;


    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        NewsItem item = items.get(position);
        holder.item = item;
        holder.tvTitle.setText(item.getTitle());
        holder.description.setText(item.getDescription());

        //pot face onClick pe imageView, dar creeaza la fiecare view asa ca preferabil e sa fac in onCreate
    }

    @Override
    public int getItemCount() {

        return items.size();
    }
    //obligatoriu cream ViewHolder si il implementam - regula RecycleView

    static class ViewHolder extends  RecyclerView.ViewHolder{
        ImageView image;
        TextView tvTitle;
        TextView description;
        NewsItem item;
        ImageView ivFavorite;
        ImageView share;
        public ViewHolder(View itemView){
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
            tvTitle = (TextView) itemView.findViewById(R.id.title);
            description = (TextView) itemView.findViewById(R.id.newsText);
            ivFavorite = (ImageView) itemView.findViewById(R.id.iv_fav);
            share = (ImageView) itemView.findViewById(R.id.btn_share);
        }
    }


}
