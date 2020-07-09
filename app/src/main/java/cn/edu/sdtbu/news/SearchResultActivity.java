package cn.edu.sdtbu.news;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cn.edu.sdtbu.news.entity.NewsVO;
import cn.edu.sdtbu.news.ui.adapter.NewsListAdapter;
import cn.edu.sdtbu.news.utils.network.EasyOkHttp;
import cn.edu.sdtbu.news.utils.network.HttpCallBack;

public class SearchResultActivity extends AppCompatActivity {
    RecyclerView rvNewsListView;
    NewsListAdapter adapter;

    public static final String PREFFIX_URL = Constant.BASE_URL +"news/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        rvNewsListView = findViewById(R.id.rc_search_result);
        Intent intent = this.getIntent();       //获取已有的intent对象
        Bundle bundle = intent.getExtras();   //获取intent里面的bundle对象
        String keyword = bundle.getString("keyword");   //获取Bundle里面的字符串
        //
        this.setTitle("\""+keyword+"\""+"的搜索结果");

        getNews(keyword);

    }

    private void getNews(String keyword){
        EasyOkHttp.post(PREFFIX_URL+"selectNewsByKeyword").add("keyword",keyword)
                .build(new HttpCallBack<String>() {
                    @Override
                    public void success(String s) {
                        Gson gson = new Gson();
                        Type listType = new TypeToken<ArrayList<NewsVO>>(){}.getType();
                        List<NewsVO> resultList = gson.fromJson(s,listType);

                        adapter = new NewsListAdapter(SearchResultActivity.this,resultList);
                        rvNewsListView.setLayoutManager(new LinearLayoutManager(SearchResultActivity.this));
                        rvNewsListView.setAdapter(adapter);
                    }
                    @Override
                    public void error(String err) {

                    }
                }, EasyOkHttp.ListTYPE);
    }
}