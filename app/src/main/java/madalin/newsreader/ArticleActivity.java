package madalin.newsreader;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import madalin.newsreader.models.NewsItem;

/**
 * Created by madalin2 on 18.06.2016.
 */
public class ArticleActivity extends AppCompatActivity {
    public final static String EXTRA_ITEM = "item";
    private ViewPager pager;
    private TabLayout tablayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_layout);

        NewsItem item = getIntent().getParcelableExtra(EXTRA_ITEM);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(item.getTitle());
        //toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp));
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ArticleActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        pager = (ViewPager) findViewById(R.id.view_pager);
        tablayout = (TabLayout) findViewById(R.id.tab_layout);

        //definim fragmentManager pentru a putea incarca fragmentele pe taburi
        FragmentManager fragmentManager = getSupportFragmentManager();
        NewsDetailsPagerAdapter adapter = new NewsDetailsPagerAdapter(fragmentManager, item);
        pager.setAdapter(adapter);

        tablayout.setupWithViewPager(pager);
        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tablayout));

        //loadContentFragment(extra);

      /*
        final WebView wvView = (WebView) findViewById(R.id.web_view);
        //mai trebuie sa mai trec extra in webview si aia e
        //wvView.getSettings().setJavaScriptEnabled(true);
        wvView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                return false;
            }
        });

        wvView.loadUrl(extra);*/

    }

//    private void loadContentFragment(String url){
//        NewsContentFragment fragment = new NewsContentFragment();
//        Bundle bundle = new Bundle();
//        bundle.putString(EXTRA_URL, url);
//        //fragmentul nu poate avea argumente in constructor, deci folosim setArguments(un bundle creat de noi, similar cu argumentele din intent, chestii serializabile)
//        fragment.setArguments(bundle);

//        FragmentManager fragmentManager = getSupportFragmentManager();
//
//        fragmentManager.beginTransaction().replace(R.id.contentFragment, fragment).commit();
//
//    }
}
