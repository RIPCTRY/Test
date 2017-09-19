package com.xt.test;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by xt on 04/09/2017.
 */
public class WebViewActivity extends AppCompatActivity {

    @BindView(R.id.btn1)
    Button btn1;
    @BindView(R.id.btn2)
    Button btn2;
    @BindView(R.id.web_view)
    WebView webView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_view);
        ButterKnife.bind(this);

        WebSettings webSetting = webView.getSettings();//得到设置属性对象

        webSetting.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);//缓存模式
        webSetting.setDomStorageEnabled(true);//DOM缓存设置
        //webSetting.setAppCacheMaxSize(1024*1024*8);//设置缓存大小
        webSetting.setAllowContentAccess(true);
        String cacheDir = this.getApplicationContext().getDir("cache",MODE_PRIVATE).getParent();
        webSetting.setAppCachePath(cacheDir);
        webSetting.setAppCacheEnabled(true);

        webSetting.setJavaScriptEnabled(true); //支持调用js
        //webSetting.setDefaultTextEncodingName("UTF-8");//修改默认支持格式，使支持中文
        //传一个java对象和一个接口名，在javascript代码中用这个接口名代替这个对象，通过接口名调用Android接口的方法
        webView.addJavascriptInterface(new JavascriptInterfaceImpl(this, webView), "Android");
        //web view client 帮助web view处理各种通知 请求事件
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(request.toString());
                return true;
            }
        });
        //web chrome client 主要用来辅助web view处理javascript的对话框、网站图标、网站标题以及网页加载进度
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }
        });

        //载入页面
        webView.loadUrl("file:///android_asset/index.html");

    }

    @OnClick({R.id.btn1, R.id.btn2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn1:
                webView.loadUrl("javascript:myFunction()");
                break;
            case R.id.btn2:
                webView.loadUrl("javascript:asyncFun()");
                break;
        }
    }

    /*
    public static class Async {
        private static final String TAG = "Async";

        public interface CallBack {
            void onResult(String result);
        }

        public void asyncFun(final CallBack callBack) {
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    SystemClock.sleep(3000);
                    callBack.onResult("async result");
                }
            }.start();
        }

        public void test() {
            this.asyncFun(new CallBack() {
                @Override
                public void onResult(String result) {
                    Log.d(TAG, "onResult: " + result);
                }
            });
        }

    }*/

}
