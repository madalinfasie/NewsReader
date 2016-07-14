package madalin.newsreader;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import madalin.newsreader.models.CommentItem;
import madalin.newsreader.models.NewsItem;

/**
 * Created by madalin2 on 27.06.2016.
 */
public class NewsCommentsAdapter extends RecyclerView.Adapter<NewsCommentsAdapter.ViewHolder> {

    private final List<CommentItem> items = new ArrayList<>();

    public void updateComments(List<CommentItem> commentItems){
        items.clear();
        items.addAll(commentItems);
        notifyDataSetChanged();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.comments_layout, parent, false);
        final ViewHolder holder = new ViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CommentItem item = items.get(position);
        holder.item = item;
        holder.author.setText(Html.fromHtml(item.getAuthor()));
        holder.text.setText(Html.fromHtml(item.getText()));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView author;
        TextView text;
        CommentItem item;

        public ViewHolder(View itemView) {
            super(itemView);
            author = (TextView) itemView.findViewById(R.id.comName);
            text = (TextView) itemView.findViewById(R.id.comText);

        }
    }
}
