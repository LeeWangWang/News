package cn.edu.sdtbu.news.utils.network;

import java.io.File;

/**
 * @author lishengqi
 * @since 2020/6/25 15：18
 * @describe 下载文件的回调
 */
public abstract class HttpFileCallBack {
    public void start() {
    }

    public abstract void progress(float size);

    public abstract void success(File file);

    public abstract void error(String err);

    public void end() {
    }
}


















