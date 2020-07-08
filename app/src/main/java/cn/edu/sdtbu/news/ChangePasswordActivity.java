package cn.edu.sdtbu.news;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.edu.sdtbu.news.style.BaseUser;
import cn.edu.sdtbu.news.utils.network.EasyOkHttp;
import cn.edu.sdtbu.news.utils.network.HttpCallBack;

public class ChangePasswordActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView cancelChangePass;         //取消修改密码
    private EditText oldPassEditText;           //旧密码
    private EditText newPassEditText;           //新密码
    private EditText confirmNewPassEditText;    //确认新密码
    private Button confirmChangePassButton;     //确认修改

    private final String TAG = "ChangePasswordActivity";
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        //隐藏顶部Actionbar
        getSupportActionBar().hide();

        cancelChangePass = findViewById(R.id.img_cancel_change_pass);
        oldPassEditText = findViewById(R.id.et_input_old_pass);
        newPassEditText = findViewById(R.id.et_input_new_pass);
        confirmNewPassEditText = findViewById(R.id.et_input_confirm_new_pass);
        confirmChangePassButton = findViewById(R.id.bt_confirm_change_password);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        cancelChangePass.setOnClickListener(this);
        confirmChangePassButton.setOnClickListener(this);

        oldPassEditText.clearFocus();
        oldPassEditText.requestFocus();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_cancel_change_pass :
                finish();
                break;
            case R.id.bt_confirm_change_password :
                //从数据库中取出账号和密码
                String pass1 = oldPassEditText.getText().toString().trim();
                final String pass2 = newPassEditText.getText().toString().trim();
                String pass3 = confirmNewPassEditText.getText().toString().trim();
                if (chechPassword(pass1, pass2, pass3)){
                    Log.d(TAG, "密码正则表达式通过");
                    String password = preferences.getString(BaseUser.PASSWORD_PREF,"");
                    String userid = preferences.getString(BaseUser.USER_ID_PREF, "");
                    Log.d(TAG,"旧密码" + password + "输入的旧密码：" + pass1 + "  输入的新密码："+ pass2 + "   确认新密码：" + pass3);
                    if (password.equals(pass1)) {
                        EasyOkHttp.get("http://47.98.156.16:8080/user/updatePasswordByuserid?userid="+userid +"&oldPassword="+pass1+"&newPassword="+pass2)
                                .build(new HttpCallBack<String>() {
                                    @Override
                                    public void success(String string) {
                                        Log.d(TAG, "修改密码成功，用户ID：" + string);
                                        SharedPreferences.Editor editor = preferences.edit();
                                        editor.putString(BaseUser.PASSWORD_PREF, pass2);
                                        finish();
                                    }
                                    @Override
                                    public void error(String err) {
                                        Log.d(TAG, "修改密码失败，用户主键：" + err);
                                    }
                                },EasyOkHttp.StringTYPE);
                    }
                }
                break;
            default :
                break;
        }
    }

    private boolean chechPassword(String pass1, String pass2, String pass3) {
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9]\\w{5,15}$");
        //密码1
        Matcher matcher1 = pattern.matcher(pass1);
        if (!matcher1.matches()) {
            Toast.makeText(getApplicationContext(),"密码只能6~16位英文和数字", Toast.LENGTH_LONG).show();
            oldPassEditText.requestFocus();
            return false;
        }
        //密码2
        Matcher matcher2 = pattern.matcher(pass2);
        if (!matcher2.matches()) {
            Toast.makeText(getApplicationContext(),"密码只能6~16位英文和数字", Toast.LENGTH_LONG).show();
            newPassEditText.requestFocus();
            return false;
        }
        //密码3
        Matcher matcher3 = pattern.matcher(pass3);
        if (!matcher3.matches()) {
            Toast.makeText(getApplicationContext(),"密码只能6~16位英文和数字", Toast.LENGTH_LONG).show();
            confirmNewPassEditText.requestFocus();
            return false;
        }

        if (!pass2.equals(pass3)) {
            Toast.makeText(this, "两次密码不一致，请重新输入", Toast.LENGTH_SHORT).show();
            newPassEditText.requestFocus();
            return false;
        }
        return true;
    }

}