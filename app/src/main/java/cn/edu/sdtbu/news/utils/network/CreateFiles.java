package cn.edu.sdtbu.news.utils.network;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Environment;

import androidx.core.app.ActivityCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Response;
/**
 * @author lishengqi
 * @since 2020/6/25 16：18
 * @describe 文件请求写文件相关
 */
public class CreateFiles {

    String filenameTemp = Environment.getExternalStorageDirectory().getAbsolutePath()+ ""+ "/hhaudio" + ".txt";

    //创建文件夹及文件
    public static void CreateText(String filenameTemp) throws IOException {
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
        if (!file.exists()) {
            try {
                //按照指定的路径创建文件夹
                file.mkdirs();
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
        File dir = new File(filenameTemp);
        if (!dir.exists()) {
            try {
                //在指定的文件夹中创建文件
                dir.createNewFile();
            } catch (Exception e) {
            }
        }

    }

    //向已创建的文件中写入数据
    public static void print(Response response, String updateFile) {
        InputStream is = null;
        byte[] buf = new byte[4096];
        int len = 0;
        FileOutputStream fos = null;
        try {
            Long totalSize = response.body().contentLength();
            Long downloadSize = 0L;
            is = response.body().byteStream();

            fos = new FileOutputStream(updateFile, true);
            while ((len = is.read(buf)) != -1) {
                downloadSize += len;
                fos.write(buf, 0, len);
            }
            fos.flush();
            if (totalSize >= downloadSize) {

            } else {
            }
        } catch (IOException e) {
        }
    }
    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE };

    /**
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }
    }
}