package cn.edu.sdtbu.news;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cn.edu.sdtbu.news.entity.FavoriteListData;
import cn.edu.sdtbu.news.style.BaseUser;
import cn.edu.sdtbu.news.ui.bookmarksAdapter.NewsListAdapter;
import cn.edu.sdtbu.news.utils.network.EasyOkHttp;
import cn.edu.sdtbu.news.utils.network.HttpCallBack;

public class BookMarksActivity extends AppCompatActivity {
//    HashSet<String> mNameSet = new HashSet<String>();
    private static ArrayList<FavoriteListData> mTagList;
    private static ArrayList<String> tagName = new ArrayList<>();
    private static ArrayList<String> creatTime = new ArrayList<>();
    private static ArrayList<String> newsTitle = new ArrayList<>();
    private static ArrayList<Integer> newsID = new ArrayList<>();
    private int id;
    private SharedPreferences preferences;



    private SearchView svBookMarksItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_marks);
        Button btnSearchNews = (Button) findViewById(R.id.btn_search_news);
        svBookMarksItem  = (SearchView) findViewById(R.id.sv_book_marks_item);
        getSupportActionBar().hide();


        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String userId = preferences.getString(BaseUser.USER_ID_PREF,"");
        id = Integer.valueOf(userId);

        initData(id);
        svBookMarksItem.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!TextUtils.isEmpty(query)){
                    initData(id,query);
                }else{
                }
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        btnSearchNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    private void initData(int user_id) {
        int id = user_id;
        EasyOkHttp.get("http://47.98.156.16:8080/favorite/selectFavoByidAndKW")
                .add("newsid",id)
                .build(new HttpCallBack<String>() {
                    @Override
                    public void success(String o) {
                        Gson gson = new Gson();
                        Type listType = new TypeToken<ArrayList<FavoriteListData>>(){}.getType();
                        List<FavoriteListData> resultList = gson.fromJson(o,listType);
                        for (int i = 0;i < resultList.size();i++){
                            tagName.add(resultList.get(i).getTagName());
                            creatTime.add(resultList.get(i).getCreatTime());
                            newsTitle.add(resultList.get(i).getNewsTitle());
                            newsID.add(resultList.get(i).getNewsid());
                        }
                        mTagList = new ArrayList<>();
                        for (int i = 0; i < tagName.size(); i++) {
                            FavoriteListData item = new FavoriteListData();
                            item.setTagName(tagName.get(i));
                            item.setCreatTime(creatTime.get(i));
                            item.setNewsTitle(newsTitle.get(i));
                            item.setNewsid(newsID.get(i));
                            mTagList.add(item);
                        }
                        initView();
                        tagName.clear();
                        creatTime.clear();
                        newsTitle.clear();
                    }
                    @Override
                    public void error(String err) {
//                        Log.d("TAG", "onResp: " +err);
                    }
                }, EasyOkHttp.ListTYPE);
    }
    private void initView() {
        RecyclerView rcvGrid = findViewById(R.id.pic_chose_list);
        GridLayoutManager managerGrid = new GridLayoutManager(this, 1);
        rcvGrid.setLayoutManager(managerGrid);
        final NewsListAdapter adapter = new NewsListAdapter(mTagList, BookMarksActivity.this);
        rcvGrid.setAdapter(adapter);
    }
    private void initData(int user_id ,String str) {
        int id = user_id;
        EasyOkHttp.get("http://47.98.156.16:8080/favorite/selectFavoByidAndKW")
                .add("newsid",id)
                .add("keyWord",str)
                .build(new HttpCallBack<String>() {
                    @Override
                    public void success(String o) {
                        Gson gson = new Gson();
                        Type listType = new TypeToken<ArrayList<FavoriteListData>>(){}.getType();
                        List<FavoriteListData> resultList = gson.fromJson(o,listType);
                        for (int i = 0;i < resultList.size();i++){
                            tagName.add(resultList.get(i).getTagName());
                            creatTime.add(resultList.get(i).getCreatTime());
                            newsTitle.add(resultList.get(i).getNewsTitle());
                            newsID.add(resultList.get(i).getNewsid());
                        }
                        mTagList = new ArrayList<>();
                        for (int i = 0; i < tagName.size(); i++) {
                            FavoriteListData item = new FavoriteListData();
                            item.setTagName(tagName.get(i));
                            item.setCreatTime(creatTime.get(i));
                            item.setNewsTitle(newsTitle.get(i));
                            item.setNewsid(newsID.get(i));
                            mTagList.add(item);
                        }
                        initView();
                        tagName.clear();
                        creatTime.clear();
                        newsTitle.clear();
                    }
                    @Override
                    public void error(String err) {
                        Log.d("TAG", "onResp: " +err);
                    }
                }, EasyOkHttp.ListTYPE);
    }

}