package cn.edu.sdtbu.news.ui;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cn.edu.sdtbu.news.Constant;
import cn.edu.sdtbu.news.R;
import cn.edu.sdtbu.news.SearchResultActivity;
import cn.edu.sdtbu.news.entity.News;
import cn.edu.sdtbu.news.entity.NewsVO;
import cn.edu.sdtbu.news.ui.adapter.NewsListAdapter;
import cn.edu.sdtbu.news.utils.network.EasyOkHttp;
import cn.edu.sdtbu.news.utils.network.HttpCallBack;

public class FavoriteFragment extends Fragment {

    RecyclerView rvNewsListView;
    NewsListAdapter adapter;

    public static final String PREFFIX_URL = Constant.BASE_URL +"news/";


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        getActivity().findViewById(R.id.searchView2).setVisibility(View.VISIBLE);
        View view = inflater.inflate(R.layout.favorite_fragment, container, false);
        rvNewsListView = view.findViewById(R.id.rv_subscribe_news);
        getNews();
        return view;
    }

    private void getNews(){
        EasyOkHttp.post(PREFFIX_URL+"subscribe1").add("userId",28)
                .build(new HttpCallBack<String>() {
                    @Override
                    public void success(String s) {
                        Gson gson = new Gson();
                        Type listType = new TypeToken<ArrayList<NewsVO>>(){}.getType();
                        List<NewsVO> resultList = gson.fromJson(s,listType);

                        adapter = new NewsListAdapter(resultList);
                        rvNewsListView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        rvNewsListView.setAdapter(adapter);
                    }
                    @Override
                    public void error(String err) {

                    }
                    },EasyOkHttp.ListTYPE);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

}