package cn.edu.sdtbu.news;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class NewsDetailActivity extends AppCompatActivity {
    TextView tvNote;
    private static final String TAG = "NewsDetailActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        tvNote = findViewById(R.id.tv_note);

        //获取用户
        Uri uri = getIntent().getData();
        if (uri != null) {
            String url = uri.toString();
            String id= uri.getQueryParameter("id");
            String title= uri.getQueryParameter("title");
            tvNote.setText("这篇文章是："+id+"号，标题为"+title+"的文章");

            Toast.makeText(NewsDetailActivity.this,"这篇文章是："+id+"号，标题为"+title+"的文章",Toast.LENGTH_LONG);
        }else {
            Log.d(TAG, "onCreate: "+"no!!!!!!!!!!!!!!!!!!1");
        }


    }
}