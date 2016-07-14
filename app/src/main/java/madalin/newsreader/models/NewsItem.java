package madalin.newsreader.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by madalin2 on 18.06.2016.
 */
public class NewsItem implements Parcelable {

    @SerializedName("id") //serializam componentele pe care le va lua GSON-ul pentru ca el va cauta dupa numele campurilor si ca sa nu avem incredere oarba
    private long id;
    @SerializedName("title")
    private String title;
    @SerializedName("url")
    private String contentUrl;
    @SerializedName("kids")
    private List<Integer> comments;
    @SerializedName("by")
    private String description;
    private String categoryName;
    private String pictureUrl;

    public NewsItem(){

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    //Parcelable methods

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(categoryName);
        dest.writeString(contentUrl);
        dest.writeString(pictureUrl);
        dest.writeList(comments);

    }
    protected NewsItem(Parcel in) {
        id = in.readLong();
        title = in.readString();
        description = in.readString();
        categoryName = in.readString();
        contentUrl = in.readString();
        pictureUrl = in.readString();

        comments = new ArrayList<>();
        in.readList(comments, getClass().getClassLoader());

    }

    public static final Creator<NewsItem> CREATOR = new Creator<NewsItem>() {
        @Override
        public NewsItem createFromParcel(Parcel in) {
            return new NewsItem(in);
        }

        @Override
        public NewsItem[] newArray(int size) {
            return new NewsItem[size];
        }
    };

    public List<Integer> getComments() {
        return comments;
    }

    public void setComments(List<Integer> comments) {
        this.comments = comments;
    }
}
