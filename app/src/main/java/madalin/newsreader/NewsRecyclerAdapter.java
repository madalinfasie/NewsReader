package madalin.newsreader;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.StringDef;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import madalin.newsreader.db.NewsDataBaseHelper;
import madalin.newsreader.models.NewsItem;

/**
 * Created by madalin2 on 11.06.2016.
 */

public class NewsRecyclerAdapter extends RecyclerView.Adapter<NewsRecyclerAdapter.ViewHolder> {

    protected static final String PREF_FILE = "favorite.pref";

    interface OnItemClickedListener{
        void onItemClicked(NewsItem item);
    }

    private final List<NewsItem> items = new ArrayList<>();
    protected final List<Long> favoriteItemIds = new ArrayList<>();
    private final OnItemClickedListener listener;
    private final SharedPreferences preferences;
    private final Gson gson;



    public NewsRecyclerAdapter(Context context, OnItemClickedListener listener){
        this.listener = listener;

        gson = new GsonBuilder().create();
        preferences = context.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);

        loadFavoriteIds();
    }


    public  void loadFavoriteIds(){
        String json = preferences.getString("ids", "[]");
        Type typeToken = new TypeToken<List<Long>>() {}.getType();
        List<Long> ids = gson.fromJson(json, typeToken);

        favoriteItemIds.clear();
        favoriteItemIds.addAll(ids);
        notifyDataSetChanged();


    }

    private void saveFavoriteIds(){

        preferences.edit().putString("ids",gson.toJson(favoriteItemIds)).apply();
        //commit scrie instant pe disk (ce ai scris tu) sau .apply care nu le scrie instant, ci mai tarziu pe alt thread ca sa nu blocheze sau ceva
        //daca am nevoie acum pe loc, pun commit, daca le folosesc mai tarziu, pun apply
    }

    public void updateItems(List<NewsItem> newsItems){
        items.clear();
        items.addAll(newsItems);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_layout, parent, false);
        final ViewHolder holder = new ViewHolder(itemView);

        itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                listener.onItemClicked(holder.item);
            }
        });
        holder.ivFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(favoriteItemIds.contains(holder.item.getId())){
                    favoriteItemIds.remove(holder.item.getId());
                    holder.ivFavorite.setActivated(false);
                }else{
                    favoriteItemIds.add(holder.item.getId());
                    holder.ivFavorite.setActivated(true);
                }
                saveFavoriteIds(); //in mod normal as fi pus save si load in alta parte, adaptorul sa nu se ocupe de asta pentru ca dureaza
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

        if(favoriteItemIds.contains(item.getId())){
            holder.ivFavorite.setActivated(true);
        }else {
            holder.ivFavorite.setActivated(false);
        }

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
        ImageView ivFavorite;
        ImageView share;
        NewsItem item;

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
