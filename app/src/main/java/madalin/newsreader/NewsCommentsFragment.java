package madalin.newsreader;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import madalin.newsreader.models.CommentItem;
import madalin.newsreader.models.NewsItem;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by madal in2 on 25.06.2016.
 */
public class NewsCommentsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_news_comments, container, false);

    }

    NewsCommentsAdapter adapter;
    OkHttpClient okHttpClient;
    ProgressBar progressBar;
    RecyclerView recyclerView;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        okHttpClient = new OkHttpClient();
        adapter = new NewsCommentsAdapter();
        recyclerView = (RecyclerView) view.findViewById(R.id.comRecycler);
        recyclerView.setAdapter(adapter);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);

        NewsItem item = getArguments().getParcelable(ArticleActivity.EXTRA_ITEM);

        LoadCommentsTask loadCommentsTask = new LoadCommentsTask();
        loadCommentsTask.execute(item);
    }

    private class LoadCommentsTask extends AsyncTask<NewsItem, Void, List<CommentItem>> {
        @Override
        protected void onPreExecute() { //ruleaza pe threadul principal inainte sa inceapa executia efectiva a thredului nostru
            super.onPreExecute();
            recyclerView.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<CommentItem> doInBackground(NewsItem... params) { //ruleaza pe thredul nostru (background)
            try {
                NewsItem newsItem = params[0];
                Gson gson = new GsonBuilder().create();
                List<CommentItem> comments = new ArrayList<>();

                for (Integer id : newsItem.getComments()) {
                    Request storyRequest = new Request.Builder().url(String.format("https://hacker-news.firebaseio.com/v0/item/%d.json?print=pretty", id)).build();
                    Response storyResponse = okHttpClient.newCall(storyRequest).execute();

                    String storyJsonString = storyResponse.body().string();

                    CommentItem commentItem = gson.fromJson(storyJsonString, CommentItem.class);
                    comments.add(commentItem);

                }
                return comments;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }

        @Override
        protected void onPostExecute(List<CommentItem> comments) { //din nou pe threadul principal
            super.onPostExecute(comments);
            recyclerView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            adapter.updateComments(comments);

        }
    }

}
