package cn.edu.sdtbu.news.utils.network;


import java.lang.reflect.ParameterizedType;

/**
 * @author lishengqi
 * @since 2020/6/25 15：18
 * @describe 对OKHttp的封装
 */
public abstract class HttpCallBack<T> {

    public Class<T> getTClass()
    {
        Class<T> tClass = (Class<T>)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        return tClass;
    }

    public void start() {
    }

    /**
     * 成功的回调
     * @param t
     */
    public abstract void success(T t);

    /**
     *错误的回调
     * @param err
     */
    public abstract void error(String err);

    public void end() {
    }
}



































