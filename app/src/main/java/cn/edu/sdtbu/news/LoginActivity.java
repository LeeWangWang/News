package cn.edu.sdtbu.news;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.mob.MobSDK;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.edu.sdtbu.news.entity.User;
import cn.edu.sdtbu.news.style.BaseUser;
import cn.edu.sdtbu.news.utils.network.EasyOkHttp;
import cn.edu.sdtbu.news.utils.network.HttpCallBack;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

@RequiresApi(api = Build.VERSION_CODES.O)
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView cancelLoginImageView;         //取消登录
    private EditText usernameEditText;              //账号输入框
    private EditText passwordEditText;              //密码输入框
    private Button loginButton;                     //登录按钮
    private TextView noPasswordTextView;            //免密登录
    private TextView toRegisterTextView;            //新用户
    private TextView getCodeTextView;               //获取验证码
    private LinearLayout rememberPasswordLayout;
    private CheckBox rememberCheBok;                //记住密码

    private boolean flag = true;                    //账号或者短信登录

    private String account;                         //用户输入的账号
    private String password;                        //用户输入的密码

    private SharedPreferences preferences;

    private EventHandler eventHandler;
    private boolean isSucceed = true;
    private TimeCount time;

    private User user_model;      //存储在PreferenceManager的默认用户

    private final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //隐藏顶部Actionbar
        getSupportActionBar().hide();

        initView();

        /************************************测试代码************************************/
        /*//存储数据
        user_model = new User(73,"17844159525","123456","张宁泽",
                "","男",25,4,13,12);
        Log.d(TAG,"创建对象成功");
        SPUtil.setObject(this, "user_model", user_model);
        Log.d(TAG,"序列化存储成功");

        //读取数据
        User user = SPUtil.getObject(this, "user_model");
        Log.d(TAG,"数据获取成功");
        Log.d(TAG, user.toString());*/
        /************************************测试代码************************************/

        //记住密码功能,从PreferenceManager获取账号和密码
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isRemeber = preferences.getBoolean(BaseUser.REMEMBER_PWD_PREF, false);
        if (isRemeber) {
            try {
                usernameEditText.setText(preferences.getString(BaseUser.ACCOUNT_PREF, ""));
                passwordEditText.setText(preferences.getString(BaseUser.PASSWORD_PREF, ""));
            } catch (Exception e) {
                e.printStackTrace();
            }
            rememberCheBok.setChecked(true);
        }

        //获取短信验证码功能
        eventHandler = new EventHandler(){
            @Override
            public void afterEvent(int event, int result, Object data) {
                // TODO 此处不可直接处理UI线程，处理后续操作需传到主线程中操作
                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                handler.sendMessage(msg);
            }
        };
        //注册一个事件回调监听，用于处理SMSSDK接口请求的结果
        SMSSDK.registerEventHandler(eventHandler);
        time = new TimeCount(60000,1000);

        usernameEditText.clearFocus();
        usernameEditText.requestFocus();

    }

    /**
     * 界面的初始化
     */
    private void initView() {
        cancelLoginImageView = (ImageView) findViewById(R.id.img_cancel_login);
        usernameEditText = (EditText) findViewById(R.id.et_username);
        passwordEditText = (EditText) findViewById(R.id.et_password);
        loginButton = (Button) findViewById(R.id.btn_login);
        noPasswordTextView = (TextView) findViewById(R.id.tv_no_password);
        toRegisterTextView = (TextView) findViewById(R.id.tv_to_register);
        getCodeTextView = (TextView)findViewById(R.id.tv_get_login_code);
        rememberPasswordLayout = (LinearLayout) findViewById(R.id.layout_remember_password);
        rememberCheBok = (CheckBox) findViewById(R.id.cb_remember_password);

        cancelLoginImageView.setOnClickListener(this);
        loginButton.setOnClickListener(this);
        noPasswordTextView.setOnClickListener(this);
        toRegisterTextView.setOnClickListener(this);
        getCodeTextView.setOnClickListener(this);

        MobSDK.submitPolicyGrantResult(true, null);

        //账号密码登录或者短信登录
        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                loginButton.setEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(usernameEditText.getText()) && TextUtils.isEmpty(passwordEditText.getText())) {
                    //按钮不可用
                    loginButton.setBackgroundColor(Color.parseColor("#D7D7D7"));
                    loginButton.setEnabled(false);
                }else {
                    loginButton.setBackgroundColor(Color.parseColor("#349FF1"));
                    loginButton.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterAllEventHandler();
        if (time != null) {
            time.cancel();
        }
    }

    /**
     * 使用Handler来分发Message对象到主线程中，处理事件
     */
    @SuppressLint("HandlerLeak")
    Handler handler=new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int event = msg.arg1;
            int result = msg.arg2;
            Object data = msg.obj;
            if(result== SMSSDK.RESULT_COMPLETE) {
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                    Toast.makeText(getApplicationContext(), "验证码输入正确,登录成功", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                }
            } else {
                if(isSucceed) {
                    getCodeTextView.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(),"验证码获取失败请重新获取", Toast.LENGTH_LONG).show();
                    usernameEditText.requestFocus();
                } else {
                    Toast.makeText(getApplicationContext(),"验证码输入错误", Toast.LENGTH_LONG).show();
                }
            }
        }
    };

    /**
     * 检查手机号的正则表达式
     * @param tel
     * @return
     */
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

    /**
     * 检查验证码的格式
     * @param code
     * @return
     */
    private boolean checkCode(String code) {
        if (TextUtils.isEmpty(code)) {
            Toast.makeText(getApplicationContext(),"请输入您的验证码", Toast.LENGTH_LONG).show();
            passwordEditText.requestFocus();
            return false;
        }else if (code.length() != 6) {
            Toast.makeText(getApplicationContext(),"您的验证码不正确", Toast.LENGTH_LONG).show();
            passwordEditText.requestFocus();
            return false;
        }
        return true;
    }

    /**
     * 60000代表的是 60秒每隔1秒去更改btnGetCode获取验证码按钮的显示的时间（执行onTick()方法）
     * 60秒之后执行onFinish()。
     */
    class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            getCodeTextView.setClickable(false);
            getCodeTextView.setText("(" + millisUntilFinished/1000 + ")" + "秒后重试");
        }

        @Override
        public void onFinish() {
            getCodeTextView.setText("重新获取验证码");
            getCodeTextView.setClickable(true);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_cancel_login :
                finish();
                break;
            case R.id.tv_get_login_code :
                //利用正则验证手机号
                if (checkTel(usernameEditText.getText().toString().trim())) {
                    time.start();
                    SMSSDK.getVerificationCode("86", usernameEditText.getText().toString().trim());
                    passwordEditText.requestFocus();
                }
                break;
            case R.id.btn_login :
                //点击登录按钮
                if (usernameEditText.getText().toString().equals("") || passwordEditText.getText().toString().equals("")) {
                }else {
                    //利用正则验证手机号
                    if (!checkTel(usernameEditText.getText().toString().trim())) {
                        break;
                    }
                    if (noPasswordTextView.getText().toString().equals("免密登录")) {
                        account = usernameEditText.getText().toString().trim();
                        password = passwordEditText.getText().toString().trim();

                        EasyOkHttp.post("http://47.98.156.16:8080/user/signin?username="+account+"&password="+ password)
                                .build(new HttpCallBack<String>() {
                                    @Override
                                    public void success(String data) {
                                        if (data.equals("-1")) {
                                            Toast.makeText(LoginActivity.this, "账号或密码不正确", Toast.LENGTH_SHORT).show();
                                        }else {
                                            String strUserName = usernameEditText.getText().toString().trim();
                                            String strPassword = passwordEditText.getText().toString().trim();
                                            SharedPreferences.Editor editor = preferences.edit();
                                            if (rememberCheBok.isChecked()) {
                                                //记住账号和密码
                                                editor.putBoolean(BaseUser.REMEMBER_PWD_PREF, true);
                                                Log.d(TAG,"记住密码");
                                            }else {
                                                editor.clear();
                                            }
                                            editor.putString(BaseUser.ACCOUNT_PREF, strUserName);
                                            editor.putString(BaseUser.PASSWORD_PREF, strPassword);
                                            editor.putString(BaseUser.USER_ID_PREF, data);
                                            editor.putString(BaseUser.LOGIN_PREF,"true");
                                            editor.apply();
                                            Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                                            Log.d(TAG,"登录成功，用户Id: "+ data);
                                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                        }
                                    }
                                    @Override
                                    public void error(String err) {
                                    }
                                },EasyOkHttp.StringTYPE);
                    }else {
                        //手机验证码登录
                        if (!checkCode(passwordEditText.getText().toString().trim())) {
                            break;
                        }
                        SMSSDK.submitVerificationCode("86", usernameEditText.getText().toString().trim(), passwordEditText.getText().toString().trim());
                        isSucceed = false;
                    }
                }
                break;
            case R.id.tv_no_password :
                if (flag) {
                    //账号密码登录
                    noPasswordTextView.setText("账号密码登陆");
                    usernameEditText.setHint("请输入手机号");
                    passwordEditText.setHint("请输入手机验证码");
                    passwordEditText.setText("");
                    getCodeTextView.setHint("获取验证码");
                    rememberPasswordLayout.setVisibility(View.INVISIBLE);
                    flag = false;
                } else {
                    //手机验证码登录
                    noPasswordTextView.setText("免密登录");
                    usernameEditText.setHint("手机号");
                    passwordEditText.setHint("密码");
                    passwordEditText.setText("");
                    getCodeTextView.setHint("");
                    rememberPasswordLayout.setVisibility(View.VISIBLE);
                    rememberCheBok.setChecked(false);
                    flag = true;
                }
                break;
            case R.id.tv_to_register :
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                break;
            default :
                break;
        }
    }

}