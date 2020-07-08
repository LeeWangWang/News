package cn.edu.sdtbu.news;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView cancelRegisterImageView;      //取消注册
    private EditText nicknameEditText;              //昵称输入框
    private EditText usernameEditText;              //账号输入框
    private EditText passwordEditText;              //密码输入框
    private Button registerButton;                  //注册按钮
    private CheckBox rememberPass;                  //记住密码
    private TextView getCodeTextView;               //获取验证码

    private SharedPreferences preferences;

    private final String TAG = "RegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //隐藏顶部Actionbar
        getSupportActionBar().hide();

        initView();

    }

    private void initView() {
        cancelRegisterImageView = (ImageView) findViewById(R.id.img_cancel_register);
        nicknameEditText = (EditText) findViewById(R.id.et_nickname_register);
        usernameEditText = (EditText) findViewById(R.id.et_username_register);
        passwordEditText = (EditText) findViewById(R.id.et_password_register);
        registerButton = (Button) findViewById(R.id.btn_register);
        getCodeTextView = (TextView) findViewById(R.id.tv_get_register_code);

        cancelRegisterImageView.setOnClickListener(this);
        registerButton.setOnClickListener(this);
        getCodeTextView.setOnClickListener(this);

        nicknameEditText.clearFocus();
        nicknameEditText.requestFocus();

        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                registerButton.setEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String nickname = nicknameEditText.getText().toString().trim();
                String username = usernameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                if (TextUtils.isEmpty(nickname) || TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                    //按钮不可用
                    registerButton.setBackgroundColor(Color.parseColor("#D7D7D7"));
                    registerButton.setEnabled(false);
                }else {
                    registerButton.setBackgroundColor(Color.parseColor("#349FF1"));
                    registerButton.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_cancel_register :
                finish();
                break;
            case R.id.btn_register :
                //点击了注册按钮
                String nickname = nicknameEditText.getText().toString().trim();
                final String username = usernameEditText.getText().toString().trim();
                final String password = passwordEditText.getText().toString().trim();
                preferences = PreferenceManager.getDefaultSharedPreferences(RegisterActivity.this);
                //利用正则验证手机号
                if (checkName(nickname) && checkTel(username) && checkPassword(password)) {
                    String Url = "http://47.98.156.16:8080/user/signup?username=" + username + "&password=" + password + "&nickname=" + nickname;
                    Log.d(TAG, "昵称、手机号、密码正则校验通过");
                    EasyOkHttp.post(Url)
                            .build(new HttpCallBack<String>() {
                                @Override
                                public void success(String string) {
                                    Log.d(TAG, "新注册用户id：" + string);
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putString(BaseUser.USER_ID_PREF, string);
                                    editor.putString(BaseUser.ACCOUNT_PREF, usernameEditText.getText().toString().trim());
                                    editor.putString(BaseUser.PASSWORD_PREF, passwordEditText.getText().toString().trim());
                                    editor.putString(BaseUser.LOGIN_PREF, "true");
                                    editor.apply();
                                    startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                                }
                                @Override
                                public void error(String err) {
                                }
                            },EasyOkHttp.StringTYPE);
                }
                break;
            default :
                break;
        }
    }

    private boolean checkName(String nickname) {
        if (TextUtils.isEmpty(nickname)) {
            Toast.makeText(getApplicationContext(),"请输入您的昵称", Toast.LENGTH_LONG).show();
            nicknameEditText.requestFocus();
            return false;
        }
        if (nickname.length() > 20 ) {
            Toast.makeText(getApplicationContext(),"昵称长度再1~20字符之间", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private boolean checkTel(String tel) {
        if (TextUtils.isEmpty(tel)) {
            Toast.makeText(getApplicationContext(),"请输入您的手机号", Toast.LENGTH_LONG).show();
            usernameEditText.requestFocus();
            return false;
        }
        Pattern pattern = Pattern.compile("^[1][3,4,5,7,8,9][0-9]{9}$");
        Matcher matcher = pattern.matcher(tel);
        if (!matcher.matches()) {
            Toast.makeText(getApplicationContext(),"请输入正确的手机号", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private boolean checkPassword(String password) {
        if (TextUtils.isEmpty(password)){
            Toast.makeText(getApplicationContext(),"请输入密码", Toast.LENGTH_LONG).show();
            passwordEditText.requestFocus();
            return false;
        }
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9]\\w{5,15}$");
        Matcher matcher = pattern.matcher(password);
        if (!matcher.matches()) {
            Toast.makeText(getApplicationContext(),"密码只能6~16位英文和数字", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }


}