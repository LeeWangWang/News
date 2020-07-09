package cn.edu.sdtbu.news.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;

import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import java.util.ArrayList;

import cn.edu.sdtbu.news.R;

public class NewsShareActivity extends AppCompatActivity implements View.OnClickListener {

    //QQ
    public static String QQ_APP_ID = "101496221";
    Activity activity = NewsShareActivity.this;
    //分享的三个控件
    ImageView qqFriend,qzone,tvCopylink;
    TextView tvCode = null;
    //动画相关
    int screenWidth = 0;
    ViewGroup rootView;
    static final int ANIM_TIME = 500;
    int mExtarFlag = 0x00;
    //前一界面传过来的值
    public static String URL = "";
    public static String TITLE = "";
    public static String IMG = "";
    //腾讯主对象
    public static Tencent mTencent;
    //分享类型
    int shareType = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_share);
        initData();
        initView();
    }

    public void initData() {
        Intent intent = getIntent();
        if(intent == null) {
            URL = "https://mini.eastday.com/mobile/200701140257152.html";
            TITLE = "标题";
            return;
        }else{
            URL = intent.getStringExtra("url");
            TITLE = intent.getStringExtra("title");
        }
    }

    public void initView() {
        tvCopylink = findViewById(R.id.copyLink);
        tvCopylink.setOnClickListener(this);

        qqFriend = findViewById(R.id.qqFriend);
        qzone = findViewById(R.id.qzone);

        findViewById(R.id.parent).setOnClickListener(this);
        qqFriend.setOnClickListener(this);
        qzone.setOnClickListener(this);

        rootView = getWindow().getDecorView().findViewById(android.R.id.content);
        /**
         * 三个控件开始的位置
         */
        qqFriend.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                qqFriend.getViewTreeObserver().removeOnPreDrawListener(this);
                qqFriend.setTranslationX(screenWidth / 2);
                qqFriend.setTranslationY(qqFriend.getHeight() * 2);
                return false;
            }
        });
        qzone.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                qzone.getViewTreeObserver().removeOnPreDrawListener(this);
                qzone.setTranslationX(screenWidth / 2);
                qzone.setTranslationY(qqFriend.getHeight() * 2);
                return false;
            }
        });
        tvCopylink.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                tvCopylink.getViewTreeObserver().removeOnPreDrawListener(this);
                tvCopylink.setTranslationX(screenWidth / 2);
                tvCopylink.setTranslationY(qqFriend.getHeight() * 2);
                return false;
            }
        });
        //滑动到原始位置
        qqFriend.post(new Runnable() {
            @Override
            public void run() {
                moveInAnim(false);
            }
        });
    }

    /**
     * 移入动画
     * @param isHideCode
     */
    private void moveInAnim(boolean isHideCode) {
        ObjectAnimator friendAnimatorX = ObjectAnimator.ofFloat(qqFriend, "TranslationX", 0);
        ObjectAnimator friendAnimatorY = ObjectAnimator.ofFloat(qqFriend, "TranslationY", 0);
        ObjectAnimator timelineAnimatorX = ObjectAnimator.ofFloat(qzone, "TranslationX", 0);
        ObjectAnimator timelineAnimatorY = ObjectAnimator.ofFloat(qzone, "TranslationY", 0);
        ObjectAnimator copyAnimatorX = ObjectAnimator.ofFloat(tvCopylink, "TranslationX", 0);
        ObjectAnimator copyAnimatorY = ObjectAnimator.ofFloat(tvCopylink, "TranslationY", 0);

        AnimatorSet set = new AnimatorSet();
        set.setDuration(ANIM_TIME);

        if (isHideCode) {
            ObjectAnimator animatorX = ObjectAnimator.ofFloat(tvCode, "ScaleX", 0.1f);
            ObjectAnimator animatorY = ObjectAnimator.ofFloat(tvCode, "ScaleY", 0.1f);
            set.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    tvCode.setVisibility(View.INVISIBLE);
                }
            });
            set.playTogether(friendAnimatorX, friendAnimatorY, timelineAnimatorX, timelineAnimatorY
                    , copyAnimatorX, copyAnimatorY, animatorX, animatorY);
        } else {
            set.setInterpolator(new FastOutSlowInInterpolator());
            set.playTogether(friendAnimatorX, friendAnimatorY, timelineAnimatorX, timelineAnimatorY
                    ,  copyAnimatorX, copyAnimatorY);
        }
        set.start();
    }

    /**
     * 移出动画
     * @param isFinishActivity
     * @param isShowCode
     */
    private void moveOutAnim(boolean isFinishActivity, boolean isShowCode) {
        ObjectAnimator timelineAnimatorX = ObjectAnimator.ofFloat(qzone, "TranslationX", screenWidth / 2);
        ObjectAnimator timelineAnimatorY = ObjectAnimator.ofFloat(qzone, "TranslationY", -qqFriend.getHeight() * 2);
        ObjectAnimator qrcodeAnimatorX = ObjectAnimator.ofFloat(qqFriend, "TranslationX", -screenWidth / 2);
        ObjectAnimator qrcodeAnimatorY = ObjectAnimator.ofFloat(qqFriend, "TranslationY", qqFriend.getHeight() * 2);
        ObjectAnimator copyAnimatorX = ObjectAnimator.ofFloat(tvCopylink, "TranslationX", screenWidth / 2);
        ObjectAnimator copyAnimatorY = ObjectAnimator.ofFloat(tvCopylink, "TranslationY", qqFriend.getHeight() * 2);

        AnimatorSet set = new AnimatorSet();
        set.setDuration(ANIM_TIME);

        if (isShowCode) {
            //不做操作
        } else {
            set.playTogether(timelineAnimatorX, timelineAnimatorY
                    , qrcodeAnimatorX, qrcodeAnimatorY, copyAnimatorX, copyAnimatorY);
        }

        if (isFinishActivity) {
            set.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    NewsShareActivity.this.finish();
                    overridePendingTransition(0, 0);
                }
            });
        }

        set.start();
    }
    /**
     * 复制
     */
    private void copyToClipBoard() {
        ClipboardManager myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Copied Text", URL);
        myClipboard.setPrimaryClip(clip);
        Toast.makeText(activity, "已复制分享链接", Toast.LENGTH_SHORT).show();
    }

    /**
     * 返回
     */
    private void back() {
        if (tvCode != null && tvCode.isShown()) {
            moveInAnim(true);
            return;
        }
        moveOutAnim(true, false);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.copyLink:
                //复制链接
                copyToClipBoard();
                break;
            case R.id.parent:
                //返回
                back();
                break;
            case R.id.qqFriend:
                //发送给QQ朋友
                qqFriend();
                break;
            case R.id.qzone:
                //分享到QQ空间
                qzone();
                break;
            default:
                break;
        }
    }

    /**
     * 分享到QQ空间
     */
    private void qzone() {
        final Bundle params = new Bundle();
        params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, shareType);
        params.putString(QzoneShare.SHARE_TO_QQ_TITLE, TITLE);
        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, URL);
        params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, URL);
        ArrayList<String> imgUrlList = new ArrayList<>();
        imgUrlList.add("http://f.hiphotos.baidu.com/image/h%3D200/sign=6f05c5f929738bd4db21b531918a876c/6a600c338744ebf8affdde1bdef9d72a6059a702.jpg");
        params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL,imgUrlList);// 图片地址
        if (shareType == QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT) {
            doShareToQzone(params);
        }
    }
    /**
     * 启动分享
     * @param params
     */
    private void doShareToQzone(final Bundle params) {
        mTencent = Tencent.createInstance(QQ_APP_ID, this);
        mTencent.shareToQzone(activity, params, qZoneShareListener);
    }

    /**
     * 分享空间的回调
     */
    IUiListener qZoneShareListener = new IUiListener() {

        @Override
        public void onCancel() {
            Toast.makeText(activity, R.string.qq_qzone_cancel, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(UiError e) {
            Log.d("ShareListActivity",e.errorMessage);
            Toast.makeText(activity, R.string.qq_qzone_error, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onComplete(Object response) {
            Toast.makeText(activity, R.string.qq_qzone_succ, Toast.LENGTH_SHORT).show();
            activity.finish();
        }

    };
    /**
     * 发送给QQ朋友
     */
    private void qqFriend() {
        final Bundle params = new Bundle();
        params.putString(QQShare.SHARE_TO_QQ_TITLE, TITLE);
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, URL);
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, URL);
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        doShareToQQ(params);
        return;
    }

    /**
     * 分享到QQ
     * @param params
     */
    private void doShareToQQ(final Bundle params) {
        mTencent = Tencent.createInstance(QQ_APP_ID, NewsShareActivity.this);
        mTencent.shareToQQ(activity, params, qqShareListener);
    }

    /**
     * 分享到QQ的回调
     */
    IUiListener qqShareListener = new IUiListener() {
        @Override
        public void onCancel() {
            if (shareType != QQShare.SHARE_TO_QQ_TYPE_IMAGE) {
                Toast.makeText(activity, R.string.qqshare_cancel, Toast.LENGTH_SHORT).show();
            }
        }
        @Override
        public void onComplete(Object response) {
            Toast.makeText(activity, R.string.qqshare_succ, Toast.LENGTH_SHORT).show();
//            activity.finish();
        }
        @Override
        public void onError(UiError e) {
            Toast.makeText(activity, R.string.qqshare_error, Toast.LENGTH_SHORT).show();
        }
    };

}