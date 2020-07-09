package cn.edu.sdtbu.news.ui;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cn.edu.sdtbu.news.Constant;
import cn.edu.sdtbu.news.R;
import cn.edu.sdtbu.news.entity.NewsVO;
import cn.edu.sdtbu.news.style.BaseUser;
import cn.edu.sdtbu.news.ui.adapter.NewsListAdapter;
import cn.edu.sdtbu.news.utils.network.EasyOkHttp;
import cn.edu.sdtbu.news.utils.network.HttpCallBack;

public class RecommendFragment extends Fragment {
    RecyclerView rvNewsListView;
    NewsListAdapter adapter;
    private SharedPreferences preferences;

    private static final String TAG = "RecommendFragment";

    public static final String PREFFIX_URL = Constant.BASE_URL +"news/";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        getActivity().findViewById(R.id.searchView2).setVisibility(View.VISIBLE);
        View view = inflater.inflate(R.layout.recommend_fragment, container, false);
        rvNewsListView = view.findViewById(R.id.rv_recommond_news);

        try {
            preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String userId = preferences.getString(BaseUser.USER_ID_PREF,"");

            getNews(userId);
            Log.d(TAG, "onCreateView: "+userId);
        }catch (NullPointerException e){
            e.printStackTrace();
            getNews("28");
        }

        return view;
    }

    private void getNews(String userId){
        Log.d(TAG, "onCreateView: "+userId);

        if(userId == null || userId.equals("")){
            userId = "28";
        }
        EasyOkHttp.get(PREFFIX_URL+"recommand").add("userId",userId)
                .build(new HttpCallBack<String>() {
                    @Override
                    public void success(String s) {
                        Log.d("okhttp",s);
                        //
                        Gson gson = new Gson();
                        Type listType = new TypeToken<ArrayList<NewsVO>>(){}.getType();
                        List<NewsVO> resultList = gson.fromJson(s,listType);

                        adapter = new NewsListAdapter(getContext(),resultList);
                        rvNewsListView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        rvNewsListView.setAdapter(adapter);
                    }
                    @Override
                    public void error(String err) {
                        Log.d("okhttp",err);
                    }
                }, EasyOkHttp.ListTYPE);
    }
    @Override

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //mViewModel = ViewModelProviders.of(this).get(RecommendViewModel.class);
    }

}