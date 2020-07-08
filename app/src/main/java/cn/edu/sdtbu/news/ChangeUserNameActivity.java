package cn.edu.sdtbu.news;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.edu.sdtbu.news.style.BaseUser;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class ChangeUserNameActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView cancelChangeUserName;     //取消修改手机号
    private TextView showUserName;              //展示手机号
    private EditText newUserName;               //新的手机号
    private EditText inputCode;                //输入的验证码
    private TextView getChangeCode;             //获取验证码
    private Button confirmChangeUserName;       //确认修改

    private SharedPreferences preferences;
    private String oldAccount;

    private EventHandler eventHandler;          //子线程处理短信
    private boolean isSucceed = true;           //是否成功
    private TimeCount time;       //定时任务

    private final String TAG = "ChangeUserNameActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_user_name);

        //隐藏顶部Actionbar
        getSupportActionBar().hide();

        initView();

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        oldAccount = preferences.getString(BaseUser.ACCOUNT_PREF,"");
        Log.d(TAG, ""+oldAccount.length());
        String message = "验证码将发送到手机" + oldAccount.substring(0,3) + "****" + oldAccount.substring(7, 11);
        showUserName.setText(message);

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
                    Toast.makeText(getApplicationContext(), "手机号修改成功", Toast.LENGTH_LONG).show();

                    startActivity(new Intent(ChangeUserNameActivity.this, MyInformationActivity.class));
                }
            } else {
                if(isSucceed) {
                    getChangeCode.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(),"验证码获取失败请重新获取", Toast.LENGTH_LONG).show();
                    getChangeCode.requestFocus();
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
            inputCode.requestFocus();
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
            inputCode.requestFocus();
            return false;
        }else if (code.length() != 6) {
            Toast.makeText(getApplicationContext(),"您的验证码不正确", Toast.LENGTH_LONG).show();
            inputCode.requestFocus();
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
            getChangeCode.setClickable(false);
            getChangeCode.setText("(" + millisUntilFinished/1000 + ")" + "秒后重试");
        }

        @Override
        public void onFinish() {
            getChangeCode.setText("重新获取验证码");
            getChangeCode.setClickable(true);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterAllEventHandler();
        if (time != null) {
            time.cancel();
        }
    }

    private void initView() {
        cancelChangeUserName = (ImageView) findViewById(R.id.img_cancel_change_user_name);
        showUserName = (TextView) findViewById(R.id.tv_show_user_name);
        newUserName = (EditText) findViewById(R.id.et_new_user_name);
        inputCode = (EditText) findViewById(R.id.et_change_code);
        getChangeCode = (TextView) findViewById(R.id.tv_get_phone_code);
        confirmChangeUserName = (Button) findViewById(R.id.bt_confirm_change_user_name);

        cancelChangeUserName.setOnClickListener(this);
        getChangeCode.setOnClickListener(this);
        confirmChangeUserName.setOnClickListener(this);

        newUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                confirmChangeUserName.setEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(newUserName.getText().toString().trim()) || TextUtils.isEmpty(inputCode.getText().toString().trim())) {
                    confirmChangeUserName.setEnabled(false);
                }else {
                    confirmChangeUserName.setBackgroundColor(Color.parseColor("#349FF1"));
                    confirmChangeUserName.setEnabled(true);
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
            case R.id.img_cancel_change_user_name :
                finish();
                break;
            case R.id.tv_get_phone_code :
                if (checkTel(newUserName.getText().toString().trim()) && !checkCode(inputCode.getText().toString().trim())) {
                    SMSSDK.submitVerificationCode("86", oldAccount, inputCode.getText().toString().trim());
                    isSucceed = false;
                }
                break;
            case R.id.bt_confirm_change_user_name :
                break;
            default :
                break;
        }
    }
}