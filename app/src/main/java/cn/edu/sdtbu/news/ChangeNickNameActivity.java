package cn.edu.sdtbu.news;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.edu.sdtbu.news.style.BaseUser;
import cn.edu.sdtbu.news.utils.network.EasyOkHttp;
import cn.edu.sdtbu.news.utils.network.HttpCallBack;

public class ChangeNickNameActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView cancelEditImageView;          //取消编辑图标
    private TextView confirmTextView;               //确认选项
    private EditText inputInfoEditView;             //输入框
    private TextView inputNumberTextView;           //输入字数

    private int wordLimitNum = 20;                  //字数上限

    private final String TAG = "ChangeNickNameActivity";
    private SharedPreferences preferences;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_nickname);

        //隐藏顶部Actionbar
        getSupportActionBar().hide();

        initView();
    }

    private void initView() {
        cancelEditImageView = findViewById(R.id.img_cancel_modify);
        confirmTextView = findViewById(R.id.tv_confirm);
        inputInfoEditView = findViewById(R.id.et_input_info);
        inputNumberTextView = findViewById(R.id.tv_input_num);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        userId = preferences.getString(BaseUser.USER_ID_PREF,"");

        cancelEditImageView.setOnClickListener(this);
        confirmTextView.setOnClickListener(this);

        inputInfoEditView.clearFocus();
        inputInfoEditView.requestFocus();

        inputInfoEditView.addTextChangedListener(new TextWatcher() {
            //记录输入的字数
            private CharSequence enterWords;
            private int selectionStart;
            private int selectionEnd;
            private int enteredWords;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //实时记录输入的字数
                enterWords= s;
            }

            @Override
            public void afterTextChanged(Editable s) {
                //已输入字数
                enteredWords= wordLimitNum - s.length();
                //TextView显示剩余字数
                inputNumberTextView.setText(20 - enteredWords +  "/10字");
                selectionStart = inputInfoEditView.getSelectionStart();
                selectionEnd = inputInfoEditView.getSelectionEnd();
                if (enterWords.length() > wordLimitNum) {
                    //删除多余输入的字（不会显示出来）
                    s.delete(selectionStart - 1, selectionEnd);
                    int tempSelection = selectionEnd;
                    inputInfoEditView.setText(s);
                    //设置光标在最后
                    inputInfoEditView.setSelection(tempSelection);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_cancel_modify :
                finish();
                break;
            case R.id.tv_confirm :
                String newNickname = inputInfoEditView.getText().toString().trim();
                Log.d(TAG, "修改昵称，用户Id: " + userId + " 昵称: " + newNickname);
                if (checkNickName(newNickname)) {
                    EasyOkHttp.get("http://47.98.156.16:8080/user/updateNickNameByuserid?userid=" +userId+ "&nickname=" +newNickname)
                            .build(new HttpCallBack<String>() {
                                @Override
                                public void success(String string) {
                                    Log.d(TAG, "修改昵称成功，用户Id：" + string);
                                }
                                @Override
                                public void error(String err) {
                                }
                            }, EasyOkHttp.StringTYPE);
                    finish();
                }
                break;
            default :
                break;
        }
    }

    private boolean checkNickName(String nickname) {
        Pattern pattern = Pattern.compile("^[\\u4E00-\\u9FA5A-Za-z0-9]{1,10}$");
        Matcher matcher = pattern.matcher(nickname);
        if (nickname.length() > 10) {
            return false;
        }
        if (!matcher.matches()) {
            Toast.makeText(getApplicationContext(),"昵称包含非法字符，请重新输入", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}