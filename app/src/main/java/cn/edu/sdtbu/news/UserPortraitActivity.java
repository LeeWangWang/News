package cn.edu.sdtbu.news;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import cn.edu.sdtbu.news.utils.network.EasyOkHttp;
import cn.edu.sdtbu.news.utils.network.HttpCallBack;
/*
   功能：填写基本信息
   作者：张海龙
   创建时间：2020-6-30
   修改：
 */


public class UserPortraitActivity extends AppCompatActivity {

    // 性别
    private static final String[] userGender = {"男","女"};
    // 职业
    private static final String[] userCareer = {"自由职业","个体经营","服务业","餐饮业","军警","公务员","教育","学生","金融","交通","建筑","IT","新闻出版","其他"};
    // 学历
    private static final String[] userGegree = {"未入学","初中及以下","高中（含职校）","大学","研究生","博士及以上"};
    // 性别文字显示
    private TextView viewGender;
    // 学历文字显示
    private TextView viewDegree;
    // 职业文字显示
    private TextView viewCareer;
    // 年龄获取
    private TextView viewAge;

    // 数据后台访问使用
//    private static final String[] get = new String[4];
    private static String genderName;   // 性别
    private static int age;             // 年龄
    private static int degreeId;        // 学历
    private static int careerId;        // 职业
    private static int userId;          // 用户id

//    private static final String[] m={"A型","B型","O型","AB型","其他"};




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_portrait);
        getSupportActionBar().hide();

//        Log.d("进入了","进入了");
        viewGender = (TextView) findViewById(R.id.gender_textView);
        viewDegree = (TextView) findViewById(R.id.degree_textView);
        viewCareer = (TextView) findViewById(R.id.career_textView);
        viewAge    = (TextView) findViewById(R.id.edit_year);
        Button btn_conf = (Button) findViewById(R.id.button);

        Intent intent = this.getIntent();

//        Log.d("用户id",intent.getStringExtra("userid"));
        Spinner spinner_gender = (Spinner) findViewById(R.id.gender_spinner);
        Spinner spinner_degree = (Spinner) findViewById(R.id.degree_spinner);
        Spinner spinner_career = (Spinner) findViewById(R.id.career_spinner);


        ArrayAdapter<String> adapter_gender = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,userGender);
        adapter_gender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_gender.setAdapter(adapter_gender);
        spinner_gender.setOnItemSelectedListener(new SpinnerSelectedListenerGender());
        spinner_gender.setVisibility(View.VISIBLE);

        ArrayAdapter<String> adapter_degree = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,userGegree);
        adapter_gender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_degree.setAdapter(adapter_degree);
        spinner_degree.setOnItemSelectedListener(new SpinnerSelectedListenerDegree());
        spinner_degree.setVisibility(View.VISIBLE);

        ArrayAdapter<String> adapter_career = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,userCareer);
        adapter_gender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_career.setAdapter(adapter_career);
        spinner_career.setOnItemSelectedListener(new SpinnerSelectedListenerCareer());
        spinner_career.setVisibility(View.VISIBLE);


        btn_conf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean ageCheck = viewAge.getText().toString().matches("[1-9][0-9]{0,2}");                //正整数
                if (ageCheck){
                    age = Integer.parseInt(viewAge.getText().toString());
                    userId = Integer.parseInt(intent.getStringExtra("userid"));
                    //userId = 102;
//                    Log.d("Gender",genderName);
//                    Log.d("age",""+age);
//                    Log.d("Degree", String.valueOf(degreeId));
//                    Log.d("Career", String.valueOf(careerId));
//                    Log.d("userId", String.valueOf(userId));
                    // 将数据存入
                    EasyOkHttp.post("http://47.98.156.16:8080/user/portrait")
                            .add("gender",genderName)
                            .add("age",age)
                            .add("degreeId",degreeId)
                            .add("careerId",careerId)
                            .add("userId",userId)
                            .build(new HttpCallBack<String>() {
                                @Override
                                public void success(String o) {
                                    Log.d("TAG", "onResp: " +o);
                                    Intent intent = new Intent(UserPortraitActivity.this, ColumnActivity.class);
                                    intent.putExtra("userid",userId);
                                    startActivity(intent);
                                }
                                @Override
                                public void error(String err) {
                                    Log.d("TAG", "onResp: " +err);
                                }
                            }, EasyOkHttp.StringTYPE);
                }
                else {
                    Toast.makeText(UserPortraitActivity.this, "年龄输入不合法", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    class SpinnerSelectedListenerGender implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                                   long arg3) {
            viewGender.setText("性别：");
            genderName = userGender[arg2];
        }
        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }
    class SpinnerSelectedListenerDegree implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                                   long arg3) {
            viewDegree.setText("学历：");
            degreeId = arg2+1;
        }
        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }
    class SpinnerSelectedListenerCareer implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                                   long arg3) {
            viewCareer.setText("职业：");
            careerId = arg2+1;
        }
        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }

}