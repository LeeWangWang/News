package cn.edu.sdtbu.news;

import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.edu.sdtbu.news.entity.Age;
import cn.edu.sdtbu.news.entity.Career;
import cn.edu.sdtbu.news.entity.Degree;
import cn.edu.sdtbu.news.entity.Sex;
import cn.edu.sdtbu.news.entity.User;
import cn.edu.sdtbu.news.style.AgeScrollView;
import cn.edu.sdtbu.news.style.BaseUser;
import cn.edu.sdtbu.news.style.BitmapUtils;
import cn.edu.sdtbu.news.style.CareerScrollView;
import cn.edu.sdtbu.news.style.DegreeScrollView;
import cn.edu.sdtbu.news.style.SexScrollView;
import cn.edu.sdtbu.news.utils.network.EasyOkHttp;
import cn.edu.sdtbu.news.utils.network.HttpCallBack;

public class MyInformationActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = "MyInformationActivity";

    private ImageView cancelEditImageView;      //取消编辑
    private TextView changePassTextView;        //修改密码
    private ImageView changeHeadImageView;      //修改头像
    private LinearLayout nicknameLayout;        //修改昵称
    private LinearLayout usernameLayout;        //修改手机号
    private LinearLayout genderLayout;          //修改性别
    private LinearLayout ageLayout;             //修改年龄
    private LinearLayout educationLayout;       //修改学历
    private LinearLayout vocationLayout;        //修改职业

    private TextView innerNickName;             //昵称
    private TextView innerUserName;             //手机号
    private TextView innerGender;               //手机号
    private TextView innerAge;                  //手机号
    private TextView innerEducation;            //手机号
    private TextView innerVocation;             //手机号

    private SexScrollView sexScrollView;        //滚动选择器1
    private RelativeLayout sexRelLayout;        //性别选择器布局
    private List<Sex> sexList;                  //性别数据
    private int[] sexId;                        //性别id
    private String[] sexName;                   //性别名称
    private Button sexYesButton;                //性别按钮

    private AgeScrollView ageScrollView;        //滚动选择器2
    private RelativeLayout ageRelLayout;        //年龄选择器布局
    private List<Age> ageList;                  //年龄数据
    private int[] ageId;                        //年龄id
    private String[] ageName;                   //年龄名称
    private Button ageYesButton;                //年龄按钮


    private CareerScrollView careerScrollView;  //滚动选择器3
    private RelativeLayout careerLayout;        //职业选择器布局
    private List<Career> careerList;            //职业数据
    private int[] careerId;                     //职业id
    private String[] careerName;                //职业名称
    private Button careerYesButton;             //确定按钮

    private DegreeScrollView degreeScrollView;  //滚动选择器4
    private RelativeLayout degreeLayout;        //学历选择器布局
    private List<Degree> degreeList;            //学历数据
    private int[] degreeId;                     //学历编号
    private String[] degreeName;                //学历名称
    private Button degreeYesButton;             //确定按钮

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_information);

        //隐藏顶部Actionbar
        getSupportActionBar().hide();

        initView();
        initData();

        /*sexYesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sexRelLayout.setVisibility(View.GONE);
            }
        });
        ageYesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ageLayout.setVisibility(View.GONE);
            }
        });*/
        careerYesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                careerLayout.setVisibility(View.GONE);
            }
        });
        degreeYesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                degreeLayout.setVisibility(View.GONE);
            }
        });
    }

    private void initView() {
        cancelEditImageView = (ImageView) findViewById(R.id.img_cancel_edit);
        changePassTextView = (TextView) findViewById(R.id.tv_change_password);
        changeHeadImageView = (ImageView) findViewById(R.id.img_change_head);
        nicknameLayout = (LinearLayout) findViewById(R.id.ll_nickname);
        usernameLayout = (LinearLayout) findViewById(R.id.ll_username);
        genderLayout = (LinearLayout) findViewById(R.id.ll_gender);
        ageLayout = (LinearLayout) findViewById(R.id.ll_age);
        educationLayout = (LinearLayout) findViewById(R.id.ll_education);
        vocationLayout  = (LinearLayout) findViewById(R.id.ll_vocation);

        innerNickName = (TextView) findViewById(R.id.tv_nickname_inner);
        innerUserName = (TextView) findViewById(R.id.tv_username_inner);
        innerGender = (TextView) findViewById(R.id.tv_gender_inner);
        innerAge = (TextView) findViewById(R.id.tv_age_inner);
        innerEducation = (TextView) findViewById(R.id.tv_education_inner);
        innerVocation = (TextView) findViewById(R.id.tv_vocation_inner);

        sexRelLayout = (RelativeLayout) findViewById(R.id.sex_rel);
        sexScrollView = (SexScrollView) findViewById(R.id.sex_scrlllview);
        sexYesButton = (Button) findViewById(R.id.bt_sex_yes);

        ageRelLayout = (RelativeLayout) findViewById(R.id.age_rel);
        ageScrollView = (AgeScrollView) findViewById(R.id.age_scrlllview);
        ageYesButton = (Button) findViewById(R.id.bt_age_yes);

        careerLayout = (RelativeLayout) findViewById(R.id.career_rel);
        careerScrollView = (CareerScrollView) findViewById(R.id.career_scrlllview);
        careerYesButton = (Button) findViewById(R.id.bt_career_yes);

        degreeLayout = (RelativeLayout) findViewById(R.id.degree_rel);
        degreeScrollView = (DegreeScrollView) findViewById(R.id.degree_scrlllview);
        degreeYesButton = (Button) findViewById(R.id.bt_degree_yes);

        cancelEditImageView.setOnClickListener(this);
        changePassTextView.setOnClickListener(this);
        changeHeadImageView.setOnClickListener(this);
        nicknameLayout.setOnClickListener(this);
        usernameLayout.setOnClickListener(this);
        genderLayout.setOnClickListener(this);
        ageLayout.setOnClickListener(this);
        educationLayout.setOnClickListener(this);
        vocationLayout .setOnClickListener(this);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "刷新数据");
        refreshData();
    }

    private void refreshData() {
        final String userId = preferences.getString(BaseUser.USER_ID_PREF,"");
        EasyOkHttp.get("http://47.98.156.16:8080/user/selectUserByuserid")
                .add("userid", userId)
                .build(new HttpCallBack<String>() {
                    @Override
                    public void success(String string) {
                        Gson gson = new Gson();
                        User user = gson.fromJson(string, User.class);
                        Log.d(TAG, user.toString());
                        innerNickName.setText(user.getNickname());
                        innerUserName.setText(user.getUsername());
                        if (user.getGender() != null) {
                            innerGender.setText(user.getGender());
                        }
                        if (user.getAge() != null) {
                            innerAge.setText(String.valueOf(user.getAge()));
                        }
                        if (user.getDegreeId() != null) {
                            innerEducation.setText(findDegreeById(user.getDegreeId()));
                        }
                        if (user.getCareerId() != null) {
                            innerVocation.setText(findCareerById(user.getCareerId()));
                        }
                    }
                    @Override
                    public void error(String err) {
                    }
                }, EasyOkHttp.StringTYPE);
    }

    private String findCareerById(int id) {
        switch (id) {
            case 4:
                return "民生";
            case 5 :
                return "文化";
            case 6 :
                return "娱乐";
            case 7 :
                return "体育";
            case 8 :
                return "财经";
            case 9 :
                return "房产";
            case 10 :
                return "汽车";
            case 11 :
                return "教育";
            case 12 :
                return "科技";
            case 13 :
                return "军事";
            case 14 :
                return "旅游";
            case 15 :
                return "国际";
            case 16 :
                return "证券";
            case 17 :
                return "三农";
            case 18 :
                return "电竞";
            default:
                return "英语";
        }
    }
    private String findDegreeById(int id) {
        switch (id) {
            case 1:
                return "未入学";
            case 2 :
                return "初中及以下";
            case 3 :
                return "高中(含职校)";
            case 4 :
                return "大学生";
            case 5 :
                return "研究生";
            default:
                return "博士及以上";
        }
    }

    /**
     * 初始化数据
     */
    private void initData() {
        /*sexList = new ArrayList<Sex>();
        sexId = new int[] {1, 2};
        sexName = new String[] {"男", "女"};
        for (int i = 0; i < sexName.length; i++) {
            sexList.add(new Sex(sexId[i], sexName[i]));
        }
        Log.d(TAG,sexList.get(0).toString());
        sexScrollView.setData(sexList);
        sexScrollView.setSelected(0);

        ageList = new ArrayList<Age>();
        for (int i = 0; i < 120; i++) {
            ageId[i] = i;
            ageName[i] = String.valueOf(i);
        }
        for (int i = 0; i < sexName.length; i++) {
            ageList.add(new Age(ageId[i], ageName[i]));
        }
        Log.d(TAG,ageList.get(0).toString());
        ageScrollView.setData(ageList);
        ageScrollView.setSelected(0);*/

        careerList = new ArrayList<Career>();
        careerId = new int[] { 4, 5, 6, 7, 8, 9 ,10 ,11 ,12 ,13 ,14 ,15 ,16 ,17 ,18 ,19};
        careerName = new String[] { "民生", "文化", "娱乐", "体育", "财经", "房产" ,"汽车" ,"教育" ,"科技" ,"军事" ,"旅游" ,"国际" ,"证券" ,"三农" ,"电竞" ,"英语"};
        for (int i = 0; i < careerName.length; i++) {
            careerList.add(new Career(careerId[i], careerName[i]));
        }
        //Log.d(TAG,careerList.get(0).toString());
        careerScrollView.setData(careerList);
        careerScrollView.setSelected(0);

        degreeList = new ArrayList<Degree>();
        degreeId = new int[] { 1, 2, 3, 4, 5, 6 };
        degreeName = new String[] {"未入学" ,"初中及以下" ,"高中(含职校)" ,"大学生" ,"研究生" ,"博士及以上"};
        for (int i = 0; i < degreeName.length; i++) {
            degreeList.add(new Degree(degreeId[i], degreeName[i]));
        }
        //Log.d(TAG, degreeList.get(0).toString());
        degreeScrollView.setData(degreeList);
        degreeScrollView.setSelected(0);
    }

    // 滚动选择器选中事件
    CareerScrollView.onSelectListener pickerListener = new CareerScrollView.onSelectListener() {
        @Override
        public void onSelect(Career career) {
            System.out.println("职业选择：" + career.getId() + career.getCareerName());

        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            Uri imageData = data.getData();
            String path = getPath(imageData);
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            Bitmap bitmap1 = BitmapUtils.zoom(bitmap, changeHeadImageView.getWidth(), changeHeadImageView.getHeight());
            Bitmap bitmap2 = BitmapUtils.circleBitmap(bitmap1);
            //加载显示
            changeHeadImageView.setImageBitmap(bitmap2);
            //bitmap图片上传到服务器......
            Log.d(TAG, "图片上传到服务器");
            String userid = preferences.getString(BaseUser.USER_ID_PREF, "");
            File file = new File(path);
            EasyOkHttp.postUpdateFile("http://47.98.156.16:8080/user/updateAvatarPathByuserid")
                    .add("userid",userid)
                    .add("file",file)
                    .buildByFile(new HttpCallBack<String>() {
                        @Override
                        public void success(String o) {
                            Log.d(TAG, "修改头像成功，用户id： " + data);
                        } @Override
                        public void error(String err) {
                            Log.d(TAG, "修改头像失败");
                        }
                    });
            //bitmap图片保存到本地
            saveImage(bitmap2);
        }else if(requestCode==200 && resultCode==RESULT_OK && data!=null){//系统相机
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            BitmapUtils.zoom(bitmap,changeHeadImageView.getWidth(),changeHeadImageView.getHeight());
            bitmap = BitmapUtils.circleBitmap(bitmap);
            //加载显示
            changeHeadImageView.setImageBitmap(bitmap);
            //bitmap图片保存到本地
            File file = saveImage(bitmap);
            //bitmap图片上传到服务器......
        }
    }

    private File saveImage(Bitmap bitmap) {
        File filesDir;
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){//判断sd卡是否挂载
            //路径1：storage/sdcard/Android/data/包名/files
            filesDir = this.getExternalFilesDir("");
        }else{//手机内部存储
            //路径：data/data/包名/files
            filesDir = this.getFilesDir();
        }
        FileOutputStream fos = null;
        try {
            File file = new File(filesDir,"icon.png");
            fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100,fos);
            return file;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally{
            if(fos != null){
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    //如果本地有,就不需要再去联网去请求
    private boolean readImage() {
        File filesDir;
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){//判断sd卡是否挂载
            //路径1：storage/sdcard/Android/data/包名/files
            filesDir = getExternalFilesDir("");
        }else{//手机内部存储
            //路径：data/data/包名/files
            filesDir = getFilesDir();
        }
        File file = new File(filesDir,"icon.png");
        if(file.exists()){
            //存储--->内存
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            changeHeadImageView.setImageBitmap(bitmap);
            return true;
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private String getPath(Uri uri) {
        int sdkVersion = Build.VERSION.SDK_INT;
        //高于4.4.2的版本
        if (sdkVersion >= 19) {
            Log.e("TAG", "uri auth: " + uri.getAuthority());
            if (isExternalStorageDocument(uri)) {
                String docId = DocumentsContract.getDocumentId(uri);
                String[] split = docId.split(":");
                String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));
                return getDataColumn(this, contentUri, null, null);
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(this, contentUri, selection, selectionArgs);
            } else if (isMedia(uri)) {
                String[] proj = {MediaStore.Images.Media.DATA};
                Cursor actualimagecursor = this.managedQuery(uri, proj, null, null, null);
                int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                actualimagecursor.moveToFirst();
                return actualimagecursor.getString(actual_image_column_index);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();
            return getDataColumn(this, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    /**
     * uri路径查询字段
     * @param context
     * @param uri
     * @param selection
     * @param selectionArgs
     * @return
     */
    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    private boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
    public static boolean isMedia(Uri uri) {
        return "media".equals(uri.getAuthority());
    }
    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_cancel_edit :
                finish();
                break;
            case R.id.tv_change_password :
                startActivity(new Intent(this, ChangePasswordActivity.class));
                break;
            case R.id.img_change_head :
                AlertDialog.Builder builder = new AlertDialog.Builder(MyInformationActivity.this);
                builder.setTitle("获取图片方式");
                builder.setItems(new String[]{"拍照", "相册"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(intent1, 200);
                                break;
                            case 1:
                                Intent intent2 = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(intent2, 100);
                                break;
                            default:
                                break;
                        }
                    }
                });
                builder.create().show();
                break;
            case R.id.ll_nickname :
                startActivity(new Intent(this, ChangeNickNameActivity.class));
                break;
            case R.id.ll_username :
                //startActivity(new Intent(this, ChangeUserNameActivity.class));
                break;
            case R.id.ll_gender :
                //sexRelLayout.setVisibility(View.VISIBLE);
                break;
            case R.id.ll_age :
                //ageRelLayout.setVisibility(View.VISIBLE);
                break;
            case R.id.ll_education :
                //degreeLayout.setVisibility(View.VISIBLE);
                break;
            case R.id.ll_vocation :
                //careerLayout.setVisibility(View.VISIBLE);
                break;
            default :
                break;
        }
    }

}