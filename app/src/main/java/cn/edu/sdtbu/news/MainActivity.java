package cn.edu.sdtbu.news;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    SearchView svNewsSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        //顶栏

        BottomNavigationView nav=findViewById(R.id.bottomNavigationView);
        //传入host的id
        NavController controller = Navigation.findNavController(this, R.id.fragment);

        //工具条配置
        AppBarConfiguration configuration = new AppBarConfiguration.Builder(nav.getMenu()).build();

        svNewsSearch = findViewById(R.id.searchView2);
        svNewsSearch.setSubmitButtonEnabled(true);
        svNewsSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    //输入完成后，提交时触发的方法，一般情况是点击输入法中的搜索按钮才会触发，表示现在正式提交了
                    public boolean onQueryTextSubmit(String query)
                    {
                        if(TextUtils.isEmpty(query))
                        {
                            Toast.makeText(MainActivity.this, "请输入查找内容！", Toast.LENGTH_SHORT).show();
                        }else {
                            Intent intent = new Intent(MainActivity.this, SearchResultActivity.class);

                            Bundle bundle = new Bundle();         //创建Bundle对象
                            bundle.putString("keyword",query);    //装入数据
                            intent.putExtras(bundle);             //把Bundle塞入Intent里面
                            startActivity(intent);
                        }
                        return false;

                    }

                    //在输入时触发的方法，当字符真正显示到searchView中才触发，像是拼音，在输入法组词的时候不会触发
                    public boolean onQueryTextChange(String newText)
                    {
                        return true;
                    }
                });

        //svNewsSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        //{
        //    //输入完成后，提交时触发的方法，一般情况是点击输入法中的搜索按钮才会触发，表示现在正式提交了
        //    public boolean onQueryTextSubmit(String query)
        //    {
        //        if(TextUtils.isEmpty(query))
        //        {
        //            Toast.makeText(MainActivity.this, "请输入查找内容！", Toast.LENGTH_SHORT).show();
        //        }else {
        //            Intent intent = new Intent(MainActivity.this, SearchResultActivity.class);
        //
        //            Bundle bundle = new Bundle();         //创建Bundle对象
        //            bundle.putString("keyword",query);    //装入数据
        //            intent.putExtras(bundle);             //把Bundle塞入Intent里面
        //            startActivity(intent);
        //        }
        //        return false;
        //
        //    }
        //
        //    //在输入时触发的方法，当字符真正显示到searchView中才触发，像是拼音，在输入法组词的时候不会触发
        //    public boolean onQueryTextChange(String newText)
        //    {
        //        return true;
        //    }
        //});


        //装配
        NavigationUI.setupActionBarWithNavController(this,controller,configuration);
        NavigationUI.setupWithNavController(nav,controller);
    }

}