package cn.edu.sdtbu.news.ui;

import android.content.Intent;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import cn.edu.sdtbu.news.LoginActivity;
import cn.edu.sdtbu.news.MyInformationActivity;
import cn.edu.sdtbu.news.R;
import cn.edu.sdtbu.news.entity.User;
import cn.edu.sdtbu.news.style.BaseUser;
import cn.edu.sdtbu.news.utils.network.EasyOkHttp;
import cn.edu.sdtbu.news.utils.network.HttpCallBack;

public class AboutMeFragment extends Fragment implements View.OnClickListener {

    //private AboutMeViewModel mViewModel;

    private ImageView mHeadPortraitImageView;       //用户头像
    private Button mLoginButton;                    //登录按钮
    private TextView nickNameTextView;              //用户昵称
    private LinearLayout editInfoLayout;            //修改资料
    private LinearLayout myCollectionLayout;        //我的收藏
    private LinearLayout exitLoginLayout;           //退出登录

    private final String TAG = "AboutMeFragmentActivity";

    private SharedPreferences preferences;

    public static AboutMeFragment newInstance() {
        return new AboutMeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.about_me_fragment, container, false);
        getActivity().findViewById(R.id.searchView2).setVisibility(View.GONE);

        //初始化界面
        initView(view);

        return view;
    }

    private void initView(View view) {

        mHeadPortraitImageView = view.findViewById(R.id.img_head_portrait);
        mLoginButton = view.findViewById(R.id.btn_to_login);
        nickNameTextView = view.findViewById(R.id.tv_nickname);
        editInfoLayout = view.findViewById(R.id.ll_edit_information);
        myCollectionLayout = view.findViewById(R.id.ll_my_collection);
        exitLoginLayout = view.findViewById(R.id.ll_exit_login);

        mLoginButton.setOnClickListener(this);
        editInfoLayout.setOnClickListener(this);
        myCollectionLayout.setOnClickListener(this);
        exitLoginLayout.setOnClickListener(this);

    }

    /**
     * 刷新界面
     */
    @Override
    public void onResume() {
        super.onResume();
        refreshData();
    }

    private void refreshData() {
        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = preferences.edit();
        String loginFlag = preferences.getString(BaseUser.LOGIN_PREF,"");
        String userId = preferences.getString(BaseUser.USER_ID_PREF,"");
        if (loginFlag.equals("true")) {
            //从数据库中获取用户头像、昵称
            EasyOkHttp.get("http://47.98.156.16:8080/user/selectUserByuserid")
                    .add("userid", userId)
                    .build(new HttpCallBack<String>() {
                        @Override
                        public void success(String string) {
                            Gson gson = new Gson();
                            User user = gson.fromJson(string, User.class);
                            Log.d(TAG, user.toString());
                            nickNameTextView.setText(user.getNickname());
                        }
                        @Override
                        public void error(String err) {
                        }
                    },EasyOkHttp.StringTYPE);
            mHeadPortraitImageView.setImageResource(R.mipmap.head_portrait);
            nickNameTextView.setVisibility(View.VISIBLE);
            mLoginButton.setVisibility(View.INVISIBLE);
        } else {
            //设置默认头像
            mHeadPortraitImageView.setImageResource(R.mipmap.head_portrait);
            nickNameTextView.setVisibility(View.INVISIBLE);
            mLoginButton.setVisibility(View.VISIBLE);
            editInfoLayout.setVisibility(View.INVISIBLE);
            myCollectionLayout.setVisibility(View.INVISIBLE);
            exitLoginLayout.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_to_login :
                startActivity(new Intent(getActivity(), LoginActivity.class));
                break;
            case R.id.ll_edit_information :
                Intent intent = new Intent(getActivity(), MyInformationActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_my_collection :
                startActivity(new Intent());
                break;
            case R.id.ll_exit_login :
                //修改PreferenceManager中isLogin的标志位
                SharedPreferences.Editor editor = preferences.edit();
                editor.remove(BaseUser.ACCOUNT_PREF);
                editor.remove(BaseUser.PASSWORD_PREF);
                editor.remove(BaseUser.USER_PREF);
                editor.remove(BaseUser.LOGIN_PREF);
                editor.remove(BaseUser.USER_ID_PREF);
                //设置默认头像
                mHeadPortraitImageView.setImageResource(R.mipmap.head_portrait);
                nickNameTextView.setVisibility(View.INVISIBLE);
                mLoginButton.setVisibility(View.VISIBLE);
                editInfoLayout.setVisibility(View.INVISIBLE);
                myCollectionLayout.setVisibility(View.INVISIBLE);
                exitLoginLayout.setVisibility(View.INVISIBLE);
                Log.d(TAG,"退出登录成功");
            default :
                break;
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

}