package madalin.newsreader;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import madalin.newsreader.models.NewsItem;

/**
 * Created by madalin2 on 25.06.2016.
 */
public class NewsDetailsPagerAdapter extends FragmentPagerAdapter {

    private final NewsItem item;

    public NewsDetailsPagerAdapter(FragmentManager fm, NewsItem item) {
        super(fm);
        this.item = item;
    }

    //mai e FragmentStatePagerAdapter care se ocupa si de lifecycle-ul fragmentului

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position){ //pozitia tabului: aleg pe ce tab sunt si spun ce fac
            case 0:
                fragment = new NewsContentFragment();
                break;
            case 1:
                fragment = new NewsCommentsFragment();
                break;
        }
        Bundle bundle = new Bundle();
        bundle.putParcelable(ArticleActivity.EXTRA_ITEM, item);
        //fragmentul nu poate avea argumente in constructor, deci folosim setArguments(un bundle creat de noi, similar cu argumentele din intent, chestii serializabile)
        fragment.setArguments(bundle);
        return  fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) { //eu nu am denumirea taburilor, asa ca le setez eu
        String title = " ";
        switch (position){
            case 0:
                title = "Content";
                break;
            case 1:
                title = "Comments";
                break;
        }
        return title;
    }
}
