package madalin.newsreader;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import madalin.newsreader.models.NewsItem;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by madalin2 on 04.07.2016.
 */
public class SavedNewsActivity extends AppCompatActivity {

    SavedNewsAdapter adapter;
    RecyclerView recyclerView;
    OkHttpClient okHttpClient;
    ProgressBar progressBar;
    SharedPreferences preferences;
    TextView nothingSaved;
    String url;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.saved_news);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SavedNewsActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        preferences = getApplicationContext().getSharedPreferences(NewsRecyclerAdapter.PREF_FILE, Context.MODE_PRIVATE);
        adapter = new SavedNewsAdapter(new SavedNewsAdapter.OnItemClickedListener() {

            @Override
            public void onItemClicked(NewsItem item) {
                //Toast.makeText(getApplicationContext(), item.getTitle() , Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SavedNewsActivity.this, ArticleActivity.class);
                intent.putExtra(ArticleActivity.EXTRA_ITEM, item);
                startActivity(intent);
            }
        });
        okHttpClient = new OkHttpClient();
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setAdapter(adapter);

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);

        nothingSaved = (TextView) findViewById(R.id.nothing_saved);
        nothingSaved.setVisibility(View.GONE);
        loadSavedItems();

/*        ImageView ivFavorite = adapter.holder.ivFavorite;
        if (ivFavorite != null) {
            ivFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    preferences.edit().remove("ids").apply();
                    adapter.notifyDataSetChanged();
                    loadSavedItems();
                }
            });
        }*/

    }

    private void loadSavedItems(){

        //preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());


        Request request = new Request.Builder().url("https://hacker-news.firebaseio.com/v0/topstories.json").build();
        okHttpClient.newCall(request).enqueue(new Callback() { //enqueue ne face pe un alt thread, puteam execute, dar sunt pe main thread si imi bloca tot
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("OkHttp", e.getMessage(), e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = preferences.getString("ids","");
                Gson gson = new GsonBuilder().create();

                Type typeToken = new TypeToken<List<Integer>>() {}.getType();
                List<Integer> storyIds = gson.fromJson(string, typeToken);

                final List<NewsItem> stories = new ArrayList<>();
                int size;
                if (storyIds != null) {
                    size = Math.min(storyIds.size(), 20);
                }
                else{
                    size = 0;
                }
                for (int i = 0; i < size; i++) {
                    Integer storyId = storyIds.get(i);

                    Request storyRequest = new Request.Builder().url(String.format("https://hacker-news.firebaseio.com/v0/item/%d.json?print=pretty",storyId)).build();
                    Response storyResponse = okHttpClient.newCall(storyRequest).execute();
                    String storyJsonString = storyResponse.body().string();
                    storyResponse.body().close();

                    NewsItem newsItem = gson.fromJson(storyJsonString, NewsItem.class);
                    if (!TextUtils.isEmpty(newsItem.getContentUrl())){
                        stories.add(newsItem);
                    }



                }

                runOnUiThread(new Runnable() { //vrem sa facem update pe threadul principal, iar noi suntem pe cel secundar, deci folosim metoda asta
                    @Override
                    public void run() {
                        if (stories.isEmpty()){
                            progressBar.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.GONE);
                            nothingSaved.setVisibility(View.VISIBLE);
                        }else {
                            progressBar.setVisibility(View.GONE);
                            nothingSaved.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            adapter.updateItems(stories);
                        }


                    }
                });

            }
        });
    }

    public SharedPreferences getPreferences() {
        return preferences;
    }

    public void removeItems(){

    }


    private void addDummyItems(){
        List<NewsItem> dummyItems = new ArrayList<>();

        for (int i = 0; i < 100; i++){
            NewsItem item = new NewsItem();
            item.setId(i);
            item.setTitle("My Title" + i);
            item.setDescription("Ceva descriere" + i);
            item.setContentUrl("http://google.com");
            dummyItems.add(item);
        }
        adapter.updateItems(dummyItems);
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.saved_news_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.delete_saved_news:
                preferences.edit().remove("ids").apply();
                Toast.makeText(this, "All saved news have been deleted", Toast.LENGTH_SHORT).show();
                loadSavedItems();
                break;

        }
        return super.onOptionsItemSelected(item);
    }
/*
    public void showAlert(String alert){
        Dialog dialog = new Dialog(SavedNewsActivity.this);
        dialog.setTitle("News link");
        dialog.setContentView(R.layout.alert_layout);
        TextView alertText = (TextView) findViewById(R.id.alertText);
        if (alertText != null) {
            alertText.setText(alert);
        }
        dialog.show();
    }*/

}
