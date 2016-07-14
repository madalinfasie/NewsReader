package madalin.newsreader;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by madalin2 on 11.06.2016.
 */
public class NewsListAdapter extends BaseAdapter {


    @Override
    public int getCount() {
        return 100;//cate elemente vor fi in total in lista mea
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //decide cand afiseaza urmatorul view dupa ce dau scroll
        //se apeleaza de 7 ori, iar cand un element iese din View, cel care apare ii va lua "memoria" e mai eficient
        Log.i("ListView", "Item" + position + "convertView:" + convertView);//logul
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_layout, parent, false);
            ViewHolder holder = new ViewHolder();
            holder.image = (ImageView) convertView.findViewById(R.id.image);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.title);
            holder.description = (TextView) convertView.findViewById(R.id.newsText);
            //holder tine toate referintele pentru findView ca sa ne fie mai usor si mai putin costisitor
            convertView.setTag(holder); //seteaza holder ca si tag si poate fi folosit mai departe
        }
        //TextView tvTitle = (TextView) convertView.findViewById(R.id.title);
        ViewHolder holder = (ViewHolder) convertView.getTag();
        holder.tvTitle.setText(String.format("This is item %d", position));
        return convertView;
    }

    static class ViewHolder{
        ImageView image;
        TextView tvTitle;
        TextView description;

    }

}
