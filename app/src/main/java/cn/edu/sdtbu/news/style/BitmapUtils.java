package cn.edu.sdtbu.news.style;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;

/**
 * @Author:李旺旺
 * @Date:2020/7/8 16:26
 * @Description:
 */
public class BitmapUtils {
    public static Bitmap circleBitmap(Bitmap source){
        //默认只对宽进行处理
        int width=source.getWidth();
        Bitmap bitmap=Bitmap.createBitmap(width,width,Bitmap.Config.ARGB_8888);
        Canvas canvas=new Canvas(bitmap);
        Paint paint=new Paint();    //设置抗锯齿
        paint.setAntiAlias(true);
        canvas.drawCircle(width/2,width/2,width/2,paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(source,0,0,paint);
        return bitmap;
    }
    /**   * 该方法用于图片压缩处理,注意width、height参数的类型必须是float   * */
    public static Bitmap zoom(Bitmap source,float width,float height){
        Matrix matrix=new Matrix();    //图片进行压缩处理
        matrix.postScale(width/source.getWidth(),height/source.getHeight());
        Bitmap bitmap = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, false);
        return bitmap;
    }
}
