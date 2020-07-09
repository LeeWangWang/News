package cn.edu.sdtbu.news;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import cn.edu.sdtbu.news.entity.ChoseItem;
import cn.edu.sdtbu.news.ui.columnAdapter.GridAdapter;
import cn.edu.sdtbu.news.utils.network.EasyOkHttp;
import cn.edu.sdtbu.news.utils.network.HttpCallBack;

public class ColumnActivity extends AppCompatActivity {
    private static final String TAG = ColumnActivity.class.getSimpleName();
    private ArrayList<ChoseItem> choseTagList;
    private Button btnEntrance;
    // 用户id
    private static int subscribeBy;
    // 订阅的专栏id数组
    private static Integer[] tagIds;
    private static String strTagIds = "";
    // 订阅的数量
    private static int num = 0;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_column);

        getSupportActionBar().hide();
        initData();
        intent = this.getIntent();

//        Log.d("用户id",intent.getStringExtra("userid"));
        initView();
    }
    private void initData() {
        choseTagList = new ArrayList<>();
        String[] tags = {"民生", "文化", "娱乐", "体育", "财经", "房产", "汽车", "教育", "科技", "军事","旅游","国际","证券","三农","电竞","英语"};
        for (int i = 0; i < tags.length; i++) {
            ChoseItem item = new ChoseItem();
            item.setName(tags[i]);
            item.setChose(false);
            choseTagList.add(item);
        }
    }
    private void initView() {
        RecyclerView rcvGrid = findViewById(R.id.btn_chose_list);
        btnEntrance = findViewById(R.id.btn_entrance);
        GridLayoutManager managerGrid = new GridLayoutManager(this, 3);
        rcvGrid.setLayoutManager(managerGrid);
        final GridAdapter adapter = new GridAdapter(this);
        adapter.setGridDataList(choseTagList);
        rcvGrid.setHasFixedSize(true);
        rcvGrid.setAdapter(adapter);
//        Log.d("HELLO",adapter.getmNameSet().toString());
        btnEntrance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adapter.getmNameSet().size() < 3){
                    Toast.makeText(ColumnActivity.this, "最少选择三个", Toast.LENGTH_SHORT).show();
                }else {
                    Log.d("进入了Column", String.valueOf(intent.getIntExtra("userid",10)));
                    subscribeBy = intent.getIntExtra("userid",10);
                    for (int i = 0;i < choseTagList.size();i++){
                        if (choseTagList.get(i).isChose()){
                            num++;
                        }
                    }
                    tagIds = new Integer[num];
                    num = 0;
                    for (int i = 0;i < choseTagList.size();i++){
                        if (choseTagList.get(i).isChose()){
                            tagIds[num] = i+4;
                            if (strTagIds == ""){
                                strTagIds += tagIds[num];
                            }
                            else {
                                strTagIds += ","+tagIds[num];
                            }
                            num++;
                        }
                    }
                    // 参数

                    EasyOkHttp.post("http://47.98.156.16:8080/subscribe/add")
                            .add("subscribeBy",subscribeBy)
                            .add("tagIds",strTagIds)
                            .build(new HttpCallBack<String>() {
                                @Override
                                public void success(String o) {
                                    // Log.d("TAG", "onResp: " +o);
                                    Intent intent = new Intent(ColumnActivity.this, MainActivity.class);
                                    startActivity(intent);
                                }
                                @Override
                                public void error(String err) {
                                    // Log.d("TAG", "onResp: " +err);
                                }
                            }, EasyOkHttp.StringTYPE);
                }
            }
        });
    }

    //处理eventbus
    /*@Subscribe(threadMode = ThreadMode.MAIN)
    public void handleMessage(GridAdapter.ChoseEvent event) {
        mNameSet = (HashSet<String>) event.getNameSet();
        if (mNameSet.size() != 0) {
            mStartBtn.setEnabled(true);
            mStartBtn.setTextColor(Color.WHITE);
//            mStartBtn.setBackground(getResources().getDrawable(R.drawable.news));
        } else {
            mStartBtn.setEnabled(false);
            mStartBtn.setTextColor(Color.parseColor("#4a4a4a"));
//            mStartBtn.setBackground(getResources().getDrawable(R.drawable.news));
        }
    }*/

}