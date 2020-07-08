package cn.edu.sdtbu.news.utils.network;

import android.Manifest;
import android.annotation.SuppressLint;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @author lishengqi
 * @since 2020/6/25 16：18
 * @describe 对OKHttp的封装
 */
public class EasyOkHttp {
    /**
     * 请求方式
     */
    private static int METHOD = 1000;
    /**
     * post
     */
    private static final int POST = 1001;
    /**
     * get
     */
    private static final int GET = 1002;

    /**
     * 返回类型ListTYPE
     */
    public static final int ListTYPE = 1001;
    /**
     * 返回类型ObjectTYPE
     */
    public static final int ObjectTYPE = 1002;
    /**
     * 返回类型ObjectTYPE
     */
    public static final int StringTYPE = 1003;

    /**
     * EasyOkHttp
     */
    private static EasyOkHttp easyOkHttp;

    /**
     *请求参数，键值对格式，分为文件和字符串
     */
    private Map<String, Object> params = new HashMap();
    private Map<String, File> files = new HashMap<>();

    public static void instance() {
        easyOkHttp = new EasyOkHttp();
    }

    private static OkHttpClient okHttpClient;
    private static RequestBody requestBody;
    private static Request request;
    private static String requestUrl;

    public static EasyOkHttp post(String url) {
        instance();
        okHttpClient = new OkHttpClient();
        METHOD = POST;
        requestUrl = url;
        return easyOkHttp;
    }

    /**
     * 创建
     * @param url
     * @return
     */
    public static EasyOkHttp get(String url) {
        instance();
        okHttpClient = new OkHttpClient();
        METHOD = GET;
        requestUrl = url;
        return easyOkHttp;
    }
    public EasyOkHttp add(String key, Object value) {
        params.put(key, String.valueOf(value));
        return this;
    }

    public EasyOkHttp add(Map<String, String> map) {
        params.putAll(map);
        return this;
    }

    public EasyOkHttp add(String key, File file) {
        files.put(key, file);
        return this;
    }

    /**
     * 回调函数返回结果类型
     */
    private final int START = 10001;
    private final int SUCCESS = 10002;
    private final int ERROR = 10003;
    private final int END = 10004;
    private HttpCallBack httpCallBack;
    /**
     * 异步消息处理机制
     */
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case START:
                    httpCallBack.start();
                    break;
                case SUCCESS:
                    httpCallBack.success(msg.obj);
                    break;
                case ERROR:
                    if (msg.obj != null) {
                        httpCallBack.error(msg.obj.toString());
                    }
                    httpCallBack.end();
                    break;
                case END:
                    httpCallBack.end();
                    break;
            }
        }
    };
    /**
     * 基于http的get和post请求传入除文件外的混合参数
     * 通过addFormDataPart可以添加多个上传的文件
     */
    public <T> void build(final HttpCallBack<T> httpCallBack, final int resultType) {
        this.httpCallBack = httpCallBack;
        handler.sendEmptyMessage(START);
        //添加参数
        if (params == null) {
            params = new HashMap<>();
        }
        FormBody.Builder formBody = new FormBody.Builder();

        for (String key : params.keySet()) {
            formBody.add(key, (String) params.get(key));
        }
        requestBody = formBody.build();
        //参数添加到此
        if (METHOD == GET) {
            if (params.size() != 0){
                requestUrl = requestUrl+"?";
            }
            for (String key : params.keySet()) {
                requestUrl += key+"="+(String) params.get(key);
            }
            request = new Request.Builder()
                    .url(requestUrl)
                    .get()//默认就是GET请求，可以不写
                    .build();
        } else if (METHOD == POST) {
            request = new Request.Builder()
                    .url(requestUrl)
                    .post(requestBody)
                    .build();
        }
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message message = new Message();
                message.obj = e.getMessage();
                message.what = ERROR;
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String jsonStr = response.body().string();
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    if (!(jsonObject.getString("code").equals("0"))) {
                        Message message = new Message();
                        message.obj = "数据请求失败";
                        message.what = ERROR;
                        handler.sendMessage(message);
                    }else {
                        String data = jsonObject.getString("data");
                        Gson gson = new Gson();
                        Object obj = null;
                        if (resultType == ListTYPE) {
                            JSONObject listDataObject = new JSONObject(data);
                            String listData = listDataObject.getString("records");
                            obj = listData;
                        }else if (resultType == ObjectTYPE){
                            Class<T> tClass = httpCallBack.getTClass();
                            obj = gson.fromJson(data, tClass);
                        }else{
                            obj = data;
                        }
                        Message message = new Message();
                        message.obj = obj;
                        message.what = SUCCESS;
                        handler.sendMessage(message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(END);
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
                handler.sendEmptyMessage(END);
            }
        });
    }

    private String guessMimeType(String fileName) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(fileName);
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }
    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    /**
     * 文件下载
     *
     * @param url path路径
     * @param destFileDir 本地存储的文件夹路径
     * @param httpFileCallBack 自定义回调接口
     */
    private static String downUrl;
    private static String filrDir;
    private long totalSize = 0L;    //APK总大小
    private long downloadSize = 0L;  // 下载的大小
    private float count = 0L;       //下载百分比
    private static File updateFile;
    public static EasyOkHttp downFile(String realURL) {
        instance();
        downUrl = realURL;
//        filrDir = destFileDir;
//        updateFile = new File(filrDir);
        return easyOkHttp;
    }

    public void down(HttpFileCallBack httpFileCallBack) {
        this.httpFileCallBack = httpFileCallBack;
        if (params == null) {
            params = new HashMap<>();
        }
        if (params.size() != 0){
            downUrl = downUrl+"?";
        }
        for (String key : params.keySet()) {
            downUrl += key+"="+(String) params.get(key);
        }
        OkHttpUtils.get()
                .url(downUrl)
                .build()
                .execute(new FileCallBack(Environment.getExternalStorageDirectory().getAbsolutePath(),
                        new Date().getTime()+".jpg") {
                    @Override
                    public void inProgress(float progress) {
                        Message message = new Message();
                        message.obj = progress;
                        message.what = FILE_PROGRESS;
                        fileHandler.sendMessage(message);
                    }

                    @Override
                    public void onError(com.squareup.okhttp.Request request, Exception e) {
                        Message message = new Message();
                        message.obj = "下载失败";
                        message.what = FILE_ERROR;
                        fileHandler.sendMessage(message);
                    }

                    @Override
                    public void onResponse(File response) {
                        updateFile = response;
                        Message message = new Message();
                        message.obj = "下载成功";
                        message.what = FILE_SUCCESS;
                        fileHandler.sendMessage(message);

                    }

                });
    }
    private HttpFileCallBack httpFileCallBack;

    @SuppressLint("HandlerLeak")
    Handler fileHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case FILE_START:
                    httpFileCallBack.start();
                    break;
                case FILE_PROGRESS:
                    httpFileCallBack.progress((float) msg.obj);
                    break;
                case FILE_SUCCESS:
                    httpFileCallBack.success(updateFile);
                    break;
                case FILE_ERROR:
                    if (msg.obj != null) {
                        httpFileCallBack.error(msg.obj.toString());
                    }
                    httpFileCallBack.end();
                    break;
                case FILE_END:
                    httpFileCallBack.end();
                    break;
            }
        }
    };


    private final int FILE_START = 100001;
    private final int FILE_SUCCESS = 100002;
    private final int FILE_ERROR = 100003;
    private final int FILE_END = 100004;
    private static final int FILE_PROGRESS = 100005;

    public static EasyOkHttp postUpdateFile(String url) {
        instance();
        okHttpClient = new OkHttpClient();
        requestUrl = url;
        return easyOkHttp;
    }
    /**
     * 基于http的文件上传（传入文件数组和key）混合参数和文件请求
     * 通过addFormDataPart可以添加多个上传的文件
     */
    public <T> void buildByFile(final HttpCallBack<T> myDataCallBack) {
        this.httpCallBack = myDataCallBack;
        if (params == null) {
            params = new HashMap<>();
        }
        MultipartBody.Builder multipartBody1 = new MultipartBody.Builder();
        multipartBody1.setType(MultipartBody.FORM);
        for (String key : params.keySet()) {
            multipartBody1.addFormDataPart(key, (String) params.get(key));
        }
        for (String key : files.keySet()) {
            String f = files.get(key).getAbsoluteFile().getAbsolutePath();
//            System.out.println(f.substring(f.indexOf(".")+1,f.length())+"///");
            multipartBody1.addFormDataPart(key,"1593164069517.jpg",
                    RequestBody.create(MediaType.parse("image/"+
                            f.substring(f.indexOf(".")+1,f.length())),files.get(key)));
        }

        requestBody = multipartBody1.build();
        Request request = new Request.Builder()
                .url(requestUrl)
                .post(requestBody)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message message = new Message();
                message.obj = e.getMessage();
                message.what = ERROR;
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonStr = response.body().string();
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(jsonStr);
                    String data = jsonObject.getString("data");
                    Message message = new Message();
                    message.obj = data;
                    message.what = SUCCESS;
                    handler.sendMessage(message);

                } catch (JSONException e) {
                    Message message = new Message();
                    message.obj = e.getMessage();
                    message.what = ERROR;
                    handler.sendMessage(message);
                }
            }
        });
    }

}
