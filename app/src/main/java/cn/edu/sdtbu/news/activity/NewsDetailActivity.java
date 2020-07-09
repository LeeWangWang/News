package cn.edu.sdtbu.news.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cn.edu.sdtbu.news.R;
import cn.edu.sdtbu.news.activity.diyView.CommentExpandableListView;
import cn.edu.sdtbu.news.adapter.MyExpandAdapter;
import cn.edu.sdtbu.news.entity.Comment;
import cn.edu.sdtbu.news.entity.Favorite;
import cn.edu.sdtbu.news.entity.User;
import cn.edu.sdtbu.news.entity.lishengqi.CommentDetail;
import cn.edu.sdtbu.news.entity.lishengqi.CommentReplyDetail;
import cn.edu.sdtbu.news.style.BaseUser;
import cn.edu.sdtbu.news.utils.network.EasyOkHttp;
import cn.edu.sdtbu.news.utils.network.HttpCallBack;

public class NewsDetailActivity extends AppCompatActivity implements View.OnClickListener{
     //日志
    private static final String TAG = "TestNewsActivity";
     //CommentExpandableListView控件相关
    private CommentExpandableListView commentExpandableListView;
    private TextView btComment;
    private List<CommentDetail> commentsList;
    private MyExpandAdapter adapter;
    //网页、弹窗、返回、收藏、分享
    private BottomSheetDialog dialog;
    private ImageView newsDetailBack;
    private WebView webView;
    private ImageView commentCollect;
    private ImageView newsDetailShare;
    private ImageView commentShare;
    boolean isCollect = false;
    //前一界面传过来的数据
    Integer newsid = 0;
    String newsTitle = "";
    String webUrl = "http://47.98.156.16:8080";
    String userid = "";
    String headPath = "";
    User commentUser = null;
    /**
     * Intent传值
     * @param context
     * @param newsid
     * @param newsTitle
     */
    public static void actionStart(Context context,Integer newsid,String newsTitle) {
        Intent intent = new Intent(context, NewsDetailActivity.class);
        intent.putExtra("newsid",newsid);
        intent.putExtra("newsTitle",newsTitle);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        commentExpandableListView = findViewById(R.id.detail_page_lv_comment);
        btComment = findViewById(R.id.detail_page_do_comment);
        webView = findViewById(R.id.web_view);
        btComment.setOnClickListener(this);
        newsDetailShare = findViewById(R.id.news_detail_share);
        commentShare = findViewById(R.id.comment_share);
        commentCollect = findViewById(R.id.comment_collect);
        newsDetailBack = findViewById(R.id.news_detail_back);
        getUserid();
        shareAndCollect(); //收藏、返回和分享的监听
        initData(); //别的控件的数据初始化
    }

    /**
     * 查询登录的用户id
     */
    private void getUserid(){
//        userid = ；
        //获取数据
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(NewsDetailActivity.this);
        String userId = preferences. getString (BaseUser.USER_ID_PREF, "");
        userid = userId;
        EasyOkHttp.get(webUrl+"/user/selectUserByuserid")
                .add("userid",Integer.parseInt(userId))
                .build(new HttpCallBack<User>() {
                    @Override
                    public void success(User user) {
                        Log.d("sssss",user.toString()+"////");
                        if (user.getAvatarPath()== null || user.getAvatarPath().length() < 1) {
                            user.setAvatarPath(webUrl+"/images/headImageMale.jpg");
                        }
                        commentUser = user;
                        Log.d("sssssppp",commentUser.toString()+"////");
                        headPath = user.getAvatarPath();
                    }
                    @Override
                    public void error(String err) {
                        Toast.makeText(NewsDetailActivity.this,"用户信息获取失败",Toast.LENGTH_SHORT).show();
                    }
                }, EasyOkHttp.ObjectTYPE);
    }
    /**
     * 分享、收藏、返回
     */
    private void shareAndCollect(){

        Intent intent = getIntent();
        Integer newsisIntent = intent.getIntExtra("newsid",0);
        newsid = newsisIntent;
        newsTitle = intent.getStringExtra("newsTitle");
        newsDetailShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                skipShareActi(newsTitle,webUrl+"/news/selectNewsByid?newid="+newsid);
            }
        });
        commentShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                skipShareActi(newsTitle,webUrl+"/news/selectNewsByid?newid="+newsid);
            }
        });
        commentCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isCollect){
                    EasyOkHttp.get(webUrl+"/favorite/deleteFavoByid")
                            .add("newsid",newsid)
                            .add("userid",userid)
                            .build(new HttpCallBack<String>() {
                                @Override
                                public void success(String favorite) {
                                    if (Integer.parseInt(favorite) > 0) {
                                        isCollect = false;
                                        commentCollect.setColorFilter(Color.parseColor("#000000"));
                                        Toast.makeText(NewsDetailActivity.this,"取消收藏",Toast.LENGTH_SHORT).show();
                                    }
                                }
                                @Override
                                public void error(String err) {
                                    commentsList = new ArrayList<>();
                                }
                            }, EasyOkHttp.StringTYPE);
                }else {
                    EasyOkHttp.get(webUrl+"/favorite/addFavo")
                            .add("newsid",newsid)
                            .add("userid",userid)
                            .build(new HttpCallBack<String>() {
                                @Override
                                public void success(String favorite) {
                                    if (Integer.parseInt(favorite) > 0) {
                                        isCollect = true;
                                        commentCollect.setColorFilter(Color.parseColor("#FF5C5C"));
                                        Toast.makeText(NewsDetailActivity.this,"收藏成功",Toast.LENGTH_SHORT).show();
                                    }
                                }
                                @Override
                                public void error(String err) {
                                    commentsList = new ArrayList<>();
                                }
                            }, EasyOkHttp.StringTYPE);
                }
            }
        });
        newsDetailBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    /**
     * 回去评论和网页的数据
     */
    private void initData(){

        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient ( new WebViewClient()) ;
        webView.loadUrl(webUrl+"/news/selectNewsByid?newid="+newsid);

        EasyOkHttp.get(webUrl+"/comment/selectCommentByNewsid")
                .add("newsid",newsid)
                .build(new HttpCallBack<String>() {
                    @Override
                    public void success(String commentDetails) {
                        Gson gson = new Gson();
                        Type listType = new TypeToken<ArrayList<CommentDetail>>(){}.getType();
                        List<CommentDetail>   obj = gson.fromJson(commentDetails, listType);
                        commentsList = obj;
                        initExpandableListView(commentsList);
                    }
                    @Override
                    public void error(String err) {
                        commentsList = new ArrayList<>();
                    }
                }, EasyOkHttp.ListTYPE);
        EasyOkHttp.get(webUrl+"/favorite/selectFavoByid")
                .add("newsid",newsid)
                .add("userid",userid)
                .build(new HttpCallBack<Favorite>() {
                    @Override
                    public void success(Favorite favorite) {
                        if (favorite != null) {
                            isCollect = true;
                            commentCollect.setColorFilter(Color.parseColor("#FF5C5C"));
                        }
                    }
                    @Override
                    public void error(String err) {
                        commentsList = new ArrayList<>();
                    }
                }, EasyOkHttp.ObjectTYPE);
    }
    /**
     * 初始化评论和回复列表
     */
    private void initExpandableListView(final List<CommentDetail> commentList) {
        commentExpandableListView.setGroupIndicator(null);
        //默认展开所有回复
        adapter = new MyExpandAdapter(this, commentList);
        commentExpandableListView.setAdapter(adapter);
        for (int i = 0; i < commentList.size(); i++) {
            commentExpandableListView.expandGroup(i);
        }
        commentExpandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int groupPosition, long l) {
                boolean isExpanded = expandableListView.isGroupExpanded(groupPosition);
//                Log.e("ceshi", "onGroupClick: 当前的评论id>>>" + commentList.get(groupPosition).getId());
//                父子列表展开的代码
//                if(isExpanded){
//                    expandableListView.collapseGroup(groupPosition);
//                }else {
//                    expandableListView.expandGroup(groupPosition, true);
//                }
//                System.out.println(commentList.get(groupPosition));
                showReplyDialog(groupPosition);
                return true;
            }
        });
        commentExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long l) {
                Toast.makeText(NewsDetailActivity.this,"点击了回复",Toast.LENGTH_SHORT).show();
                showMultilevelReplyDialog(groupPosition,childPosition);
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.detail_page_do_comment){
            showCommentDialog();
        }
    }
    /**
     * 弹出评论框
     */
    private void showCommentDialog(){
        dialog = new BottomSheetDialog(this, R.style.BottomSheetStyle);
        View commentView = LayoutInflater.from(this).inflate(R.layout.comment_dialog_layout, null);
        final EditText commentText =  commentView.findViewById(R.id.dialog_comment_et);
        final Button bt_comment =  commentView.findViewById(R.id.dialog_comment_bt);
        //长度限制
        final TextView textLimit = commentView.findViewById(R.id.text_limit);
        showTextLimit(commentText,textLimit);
        dialog.setContentView(commentView);
        showKeyBoard(commentText);

        /**
         * 解决bsd显示不全的情况
         */
//        View parent = (View) commentView.getParent();
//        BottomSheetBehavior behavior = BottomSheetBehavior.from(parent);
//        commentView.measure(0, 0);
//        behavior.setPeekHeight(commentView.getMeasuredHeight());

        bt_comment.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String commentContent = commentText.getText().toString().trim();
                if (!TextUtils.isEmpty(commentContent)) {

                    //commentOnWork(commentContent);
                    dialog.dismiss();
                    Comment comment = new Comment();
                    comment.setNewsId(newsid);
                    comment.setCreateBy(Integer.parseInt(userid));
                    comment.setCommentContent(commentContent);
                    addComment(comment);

                    CommentDetail detailBean = new CommentDetail(commentUser.getNickname(), commentContent, "刚刚");
                    detailBean.setUserLogo(commentUser.getAvatarPath());
                    //要写一下才能用
                    adapter.addTheCommentData(detailBean);
//                    Toast.makeText(NewsDetailActivity.this, "评论成功", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(NewsDetailActivity.this, "评论内容不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });
        commentText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!TextUtils.isEmpty(charSequence) && charSequence.length() > 2) {
                    bt_comment.setBackgroundColor(Color.parseColor("#2196F3"));
                } else {
                    bt_comment.setBackgroundColor(Color.parseColor("#D8D8D8"));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        dialog.show();

    }

    /**
     * 弹出回复框
     */
    private void showReplyDialog(final int position){
        dialog = new BottomSheetDialog(this, R.style.BottomSheetStyle);
        View commentView = LayoutInflater.from(this).inflate(R.layout.comment_dialog_layout,null);
        final EditText commentText =  commentView.findViewById(R.id.dialog_comment_et);
        final Button bt_comment =  commentView.findViewById(R.id.dialog_comment_bt);
        commentText.setHint("回复 " + commentsList.get(position).getNickName() + " 的评论:");
        //长度限制
        final TextView textLimit = commentView.findViewById(R.id.text_limit);
        showTextLimit(commentText,textLimit);
        dialog.setContentView(commentView);
        showKeyBoard(commentText);
        bt_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String replyContent = commentText.getText().toString().trim();
                if(!TextUtils.isEmpty(replyContent)){

                    dialog.dismiss();

                    Comment comment = new Comment();
                    comment.setNewsId(newsid);
                    comment.setCreateBy(Integer.parseInt(userid));
                    comment.setCommentContent(replyContent);
                    comment.setParentId(commentsList.get(position).getId());
                    addCommentReply(comment);

                    CommentReplyDetail detailBean = new CommentReplyDetail(commentUser.getNickname()+
                            "回复"+commentsList.get(position).getNickName(),replyContent);
                    detailBean.setUserLogo(commentUser.getAvatarPath());
                    adapter.addTheReplyData(detailBean, position);
                    commentExpandableListView.expandGroup(position);
                    Toast.makeText(NewsDetailActivity.this,"回复成功",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(NewsDetailActivity.this,"回复内容不能为空",Toast.LENGTH_SHORT).show();
                }
            }
        });
        commentText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!TextUtils.isEmpty(charSequence) && charSequence.length()>2){
                    bt_comment.setBackgroundColor(Color.parseColor("#2196F3"));
                }else {
                    bt_comment.setBackgroundColor(Color.parseColor("#D8D8D8"));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        dialog.show();
    }

    /**
     * 多级评论的弹出框
     * @param groupPosition
     * @param childPosition
     * @describe 弹出回复框
     */
    private void showMultilevelReplyDialog(final int groupPosition,final int childPosition){
        dialog = new BottomSheetDialog(this, R.style.BottomSheetStyle);
        View commentView = LayoutInflater.from(this).inflate(R.layout.comment_dialog_layout,null);
        final EditText commentText =  commentView.findViewById(R.id.dialog_comment_et);
        final Button bt_comment =  commentView.findViewById(R.id.dialog_comment_bt);
        //长度限制
        final TextView textLimit = commentView.findViewById(R.id.text_limit);
        showTextLimit(commentText,textLimit);
        String oriNickName = commentsList.get(groupPosition).getReplyList().get(childPosition).getNickName();
        String replyNickName = oriNickName.substring(0,oriNickName.indexOf("回"));
        commentText.setHint("回复 " + replyNickName + " 的评论:");
        dialog.setContentView(commentView);
        showKeyBoard(commentText);
        bt_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String replyContent = commentText.getText().toString().trim();
                if(!TextUtils.isEmpty(replyContent)){

                    dialog.dismiss();

                    Comment comment = new Comment();
                    comment.setNewsId(newsid);
                    comment.setCreateBy(Integer.parseInt(userid));
                    comment.setCommentContent(replyContent);
                    comment.setParentId(commentsList.get(groupPosition).getReplyList().get(childPosition).getId());
                    addCommentReply(comment);


                    CommentReplyDetail detailBean = new CommentReplyDetail(commentUser.getNickname()+
                            "回复"+replyNickName,replyContent);
                    detailBean.setUserLogo(commentUser.getAvatarPath());
                    adapter.addTheReplyData(detailBean, groupPosition);
                    commentExpandableListView.expandGroup(groupPosition);
                    Toast.makeText(NewsDetailActivity.this,"回复成功",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(NewsDetailActivity.this,"回复内容不能为空",Toast.LENGTH_SHORT).show();
                }
            }
        });
        commentText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!TextUtils.isEmpty(charSequence) && charSequence.length()>2){
                    bt_comment.setBackgroundColor(Color.parseColor("#2196F3"));
                }else {
                    bt_comment.setBackgroundColor(Color.parseColor("#D8D8D8"));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        dialog.show();
    }


    /**
     * 请求后台，添加评论
     * @param comment
     * @return
     */
    private String addComment(Comment comment){

        EasyOkHttp.get(webUrl+"/comment/addComment")
                .add("newid",comment.getNewsId()+"")
                .add("userid",comment.getCreateBy()+"")
                .add("content",comment.getCommentContent())
                .build(new HttpCallBack<String>() {
                    @Override
                    public void success(String s) {
                        Toast.makeText(NewsDetailActivity.this, "评论成功", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void error(String err) {
                        Toast.makeText(NewsDetailActivity.this, "评论失败", Toast.LENGTH_SHORT).show();
                    }
                }, EasyOkHttp.StringTYPE);
        return "1";
    }

    /**
     * 请求后台，添加回复
     * @param comment
     * @return
     */
    private String addCommentReply(Comment comment){
        EasyOkHttp.get(webUrl+"/comment/addComment")
                .add("newid",comment.getNewsId()+"")
                .add("userid",comment.getCreateBy()+"")
                .add("content",comment.getCommentContent())
                .add("parentid",comment.getParentId())
                .build(new HttpCallBack<String>() {
                    @Override
                    public void success(String s) {
                        Toast.makeText(NewsDetailActivity.this, "回复成功", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void error(String err) {
                        Toast.makeText(NewsDetailActivity.this, "回复失败", Toast.LENGTH_SHORT).show();
                    }
                }, EasyOkHttp.StringTYPE);
        return "1";
    }

    /**
     * 跳转分享界面
     * @param title
     * @param url
     */
    private void skipShareActi(String title,String url){
        Intent intent = new Intent(NewsDetailActivity.this, NewsShareActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("url", url);
        startActivity(intent);
//        overridePendingTransition(0, 0);
    }

    /**
     * 弹出键盘
     * @param commentText
     */
    private void showKeyBoard(EditText commentText){
        commentText.setFocusable(true);
        commentText.setFocusableInTouchMode(true);
        commentText.requestFocus();
        InputMethodManager inputManager = (InputMethodManager)commentText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(commentText, 0);
    }

    /**
     * 字数限制
     * @param commentText
     * @param textLimit
     */
    private void showTextLimit(final EditText commentText, final TextView textLimit){
        commentText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence chars, int arg1, int arg2,
                                      int arg3) {
            }

            @Override
            public void beforeTextChanged(CharSequence chars, int arg1,
                                          int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String commentContent = commentText.getText().toString();
                textLimit.setText(commentContent.length()+"/255");
            }
        });
    }
}