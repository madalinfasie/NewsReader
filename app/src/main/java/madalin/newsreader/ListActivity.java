package madalin.newsreader;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

public class ListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        NewsListAdapter adapter = new NewsListAdapter();

        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(adapter);
    }

}
