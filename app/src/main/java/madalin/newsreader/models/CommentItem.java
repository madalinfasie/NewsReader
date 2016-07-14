package madalin.newsreader.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by madalin2 on 28.06.2016.
 */
public class CommentItem{

    @SerializedName("id")
    private int id;
    @SerializedName("text")
    private String text;
    @SerializedName("by")
    private String author;

    public CommentItem(){

    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
