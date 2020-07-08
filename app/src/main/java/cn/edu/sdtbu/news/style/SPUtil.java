package cn.edu.sdtbu.news.style;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

/**
 * @Author:李旺旺
 * @Date:2020/7/6 16:50
 * @Description:
 */
public class SPUtil {
    /**
     * 使用SharedPreference保存序列化对象
     * 用Base64.encode将字节文件转换成Base64编码保存在String中
     *
      * @param context 上下文
     * @param key     储存对象的key
     * @param object  object对象  对象必须实现Serializable序列化，否则会出问题，
     *                out.writeObject 无法写入 Parcelable 序列化的对象
     */
    public static void setObject(Context context, String key, Object object) {
        SharedPreferences sp = context.getSharedPreferences("SP_NAME", Context.MODE_PRIVATE);
        //创建字节输出流
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //创建字节对象输出流
        ObjectOutputStream out = null;
        try {
            //然后通过将字对象进行64转码，写入sp中
            out = new ObjectOutputStream(baos);
            out.writeObject(object);
            String objectValue = new String(Base64.encode(baos.toByteArray(), Base64.DEFAULT));
            SharedPreferences.Editor editor = sp.edit();
            editor.putString(key, objectValue);
            editor.commit();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.close();
                }

                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取SharedPreference保存的对象
     * 使用Base64解密String，返回Object对象
     *
     * @param context 上下文
     * @param key     储存对象的key
     * @param <T>     泛型
     * @return 返回保存的对象
     */
    public static <T> T getObject(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences("SP_NAME", Context.MODE_PRIVATE);
        if (sp.contains(key)) {
            String objectValue = sp.getString(key, null);
            byte[] buffer = Base64.decode(objectValue, Base64.DEFAULT);
            //一样通过读取字节流，创建字节流输入流，写入对象并作强制转换
            ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
            ObjectInputStream ois = null;
            try {
                ois = new ObjectInputStream(bais);
                T t = (T) ois.readObject();
                return t;
            } catch (StreamCorruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (bais != null) {
                        bais.close();
                    }

                    if (ois != null) {
                        ois.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

}
