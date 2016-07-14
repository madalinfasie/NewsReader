package madalin.newsreader;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crash.FirebaseCrash;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import madalin.newsreader.db.NewsDataBaseHelper;
import madalin.newsreader.models.NewsItem;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by madalin2 on 11.06.2016.
 */
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    NewsRecyclerAdapter adapter;
    private OkHttpClient okHttpClient;
    ProgressBar progressBar;
    RecyclerView recyclerView;
    ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        okHttpClient = new OkHttpClient();

//        NewsDataBaseHelper dbHelper = new NewsDataBaseHelper(this);
//        dbHelper.getAll();
//        dbHelper.getWritableDatabase().insert();




        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ((TextView) findViewById(R.id.toolbar_title)).setText("News Reader");


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Dau un crash facut de mine fara ca aplicatia sa dea efectiv crash. Utila ca sa urmaresc cand intra pe catch (gaseste o exceptie)
        //FirebaseCrash.report(new Exception("My first Android non-fatal error"));

        adapter = new NewsRecyclerAdapter(this, new NewsRecyclerAdapter.OnItemClickedListener(){

            @Override
            public void onItemClicked(NewsItem item) {
                //Toast.makeText(getApplicationContext(), item.getTitle() , Toast.LENGTH_SHORT).show();
                FirebaseAnalytics analytics = FirebaseAnalytics.getInstance(MainActivity.this);
                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, String.valueOf(item.getId()));
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, item.getTitle());
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "article");

                analytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);


                Intent intent = new Intent(MainActivity.this, ArticleActivity.class);
                intent.putExtra(ArticleActivity.EXTRA_ITEM,item);
                startActivity(intent);
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setAdapter(adapter);
       /* ImageButton fab = (ImageButton) findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adapter.x += 1;
                    adapter.notifyDataSetChanged();
                }
            });
        }*/

        //addDummyItems();

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        loadRealItems();

    }

    private void loadRealItems(){

        Request request = new Request.Builder().url("https://hacker-news.firebaseio.com/v0/topstories.json").build();
        okHttpClient.newCall(request).enqueue(new Callback() { //enqueue ne face pe un alt thread, puteam execute, dar sunt pe main thread si imi bloca tot
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("OkHttp", e.getMessage(), e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonString = response.body().string();

                Gson gson = new GsonBuilder().create(); //pot pune inainte de create .setDateFormat("dd.mm.yy") sau altele ca sa pun conditii suplimentare

           //     try { //try catch sunt pentru Json, cand folosesc Gson nu mai e nevoie
                    Type typeToken = new TypeToken<List<Integer>>() {}.getType();
                    List<Integer> storyIds = gson.fromJson(jsonString, typeToken);

//                    JSONArray jsonArray = new JSONArray(jsonString);
//                    int size = Math.min(10, jsonArray.length()); //in caz ca in api nu avem suficiente date, e mai sigur asa

                    final List<NewsItem> stories = new ArrayList<>();
                    int size = Math.min(storyIds.size(),20);
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

                        /*   JSONObject storyJsonObject = new JSONObject(storyJsonString); //aici fac cu Json, dar mai bine folosesc Gson ca sa fie mai simplu

                        if(!storyJsonObject.has("url")){
                            size+=1;
                            continue; //ignoram elementele fara url (nu toate storiurile au url)
                        }

                        NewsItem newsItem = new NewsItem();
                        newsItem.setId(storyId);
                        newsItem.setTitle(storyJsonObject.getString("title"));
                        newsItem.setContentUrl(storyJsonObject.getString("url"));*/

                        //stories.add(newsItem);
                    }

                    runOnUiThread(new Runnable() { //vrem sa facem update pe threadul principal, iar noi suntem pe cel secundar, deci folosim metoda asta
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            adapter.updateItems(stories);


                        }
                    });

//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
            }
        });
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
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.saved_news:
                //Toast.makeText(this, "Saved News", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, SavedNewsActivity.class);
                startActivity(intent);
                break;
            case R.id.menu_about:
                Toast.makeText(this,"These are the news from Hacker News",Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.saved_news) {
            Intent intent = new Intent(MainActivity.this, SavedNewsActivity.class);
            startActivity(intent);
        } else if (id == R.id.menu_about) {
            Toast.makeText(this,"These are the news from Hacker News",Toast.LENGTH_SHORT).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}