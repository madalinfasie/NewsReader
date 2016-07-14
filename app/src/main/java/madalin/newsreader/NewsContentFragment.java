package madalin.newsreader;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import madalin.newsreader.models.NewsItem;

/**
 * Created by madalin2 on 25.06.2016.
 */
public class NewsContentFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_article_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        NewsItem item = getArguments().getParcelable(ArticleActivity.EXTRA_ITEM);//ne da bundle-ul dat in ArticleActivity

        final WebView wvView = (WebView) view.findViewById(R.id.web_view); //nu putem folosi findViewById pentru ca nu am activitate, asa ca folosesc view.
        //mai trebuie sa mai trec extra in webview si aia e
        //wvView.getSettings().setJavaScriptEnabled(true);

        wvView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                return false;
            }
        });
        wvView.loadUrl(item.getContentUrl());
    }
}
