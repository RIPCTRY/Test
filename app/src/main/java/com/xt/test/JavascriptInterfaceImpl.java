package com.xt.test;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

/**
 * 自定义的Android代码和JavaScript之间的桥梁
 * Created by xt on 04/09/2017.
 */
public class JavascriptInterfaceImpl {

    private static final String TAG = "JavascriptInterfaceImpl";
    private Context mContext;
    private WebView mWebView;
    private Handler mHandler;

    public JavascriptInterfaceImpl(Context context, WebView webView) {
        this.mContext = context;
        this.mWebView = webView;
        mHandler = new Handler(Looper.getMainLooper());
    }

    //如果target大于等于17需要加上注解
    @JavascriptInterface
    public void showToast(String toast) {
        Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
    }

    //同步方法
    @JavascriptInterface
    public String syncExec() {
        return "hello android";
    }

    //异步方法
    @JavascriptInterface
    public void asyncExec(final String msg, final String callbackId) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                SystemClock.sleep(5000);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        String url = "javascript:" + callbackId + "('" + msg + " from android " + "')";
                        mWebView.loadUrl(url);
                    }
                });
            }
        }.start();
    }

    //接收javascript的异步消息
    @JavascriptInterface
    public void callBack(String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }

}