package madalin.newsreader.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import madalin.newsreader.models.NewsItem;

/**
 * Created by madalin2 on 02.07.2016.
 */
public class NewsDataBaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "news.db"; //numele bazei de date, daca exista, o apeleaza, daca nu exista, o creaza
    private static final int DATABASE_VERSION = 1;

    interface NewsTable{
        String TABLE_NAME = "favorites";
        String COLUMN_ID = "_id";
        String COLUMN_TITLE = "title";
        String COLUMN_URL = "url";
    }

    public NewsDataBaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + NewsTable.TABLE_NAME + " (" +
                NewsTable.COLUMN_ID + " INTEGER PRIMARY KEY, " +
                NewsTable.COLUMN_TITLE+ " TEXT, " +
                NewsTable.COLUMN_URL + " TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insert(NewsItem item){
        ContentValues contentValues = new ContentValues();
        contentValues.put(NewsTable.COLUMN_ID, item.getId());
        contentValues.put(NewsTable.COLUMN_TITLE, item.getTitle());
        contentValues.put(NewsTable.COLUMN_URL, item.getContentUrl());

        SQLiteDatabase db = getWritableDatabase();
        db.insert(NewsTable.TABLE_NAME, null, contentValues);
    }

    public List<NewsItem> getAll(){
        String[] columns = new String[]{NewsTable.COLUMN_ID, NewsTable.COLUMN_TITLE, NewsTable.COLUMN_URL};
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(NewsTable.TABLE_NAME, columns, null, null, null, null,null);

        List<NewsItem>newsItems = new ArrayList<>();

        if(cursor.moveToFirst()){
            do{
                NewsItem newsItem = new NewsItem();
                newsItem.setId(cursor.getInt(cursor.getColumnIndexOrThrow(NewsTable.COLUMN_ID)));
                newsItem.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(NewsTable.COLUMN_TITLE)));
                newsItem.setContentUrl(cursor.getString(cursor.getColumnIndexOrThrow(NewsTable.COLUMN_URL)));

                newsItems.add(newsItem);
            }while (cursor.moveToNext());
        }

        cursor.close();
        return newsItems;
    }

    public void remove(NewsItem newsItem){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(NewsTable.TABLE_NAME, NewsTable.COLUMN_ID + " = ?", new String[] {String.valueOf(newsItem.getId())}); //semnul ? se inlocuieste in expresia delete-ului pentru a scurta textul
    }

}
