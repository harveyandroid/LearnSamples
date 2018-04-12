package com.harvey.webview;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.HashMap;
import java.util.Set;

/**
 * 通过WebView的loadUrl（）
 */
public class MainActivity extends AppCompatActivity {
    static final String TAG = "webView";
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        webView = (WebView) findViewById(R.id.webView);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        // 通过addJavascriptInterface()将Java对象映射到JS对象
        //参数1：Javascript对象名
        //参数2：Java对象名
        webView.addJavascriptInterface(new AndroidtoJs(), "test");//AndroidtoJS类对象映射到js的test对象
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webView.loadUrl("file:///android_asset/javascript.html");
        webView.setWebViewClient(new WebViewClient() {

            /**
             * 拦截 url 跳转,在里边添加点击链接跳转或者操作
             *
             * 如果return true，则会屏蔽系统默认的显示URL结果的行为，
             * 不需要处理的URL也需要调用loadUrl()来加载进WebVIew，
             * 不然就会出现白屏；如果return false，则系统默认的加载URL行为是不会被屏蔽的，
             * 所以一般建议大家return false，
             * 我们只关心我们关心的拦截内容，对于不拦截的内容，让系统自己来处理即可。
             */
            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String url) {
                Log.e(TAG, "shouldOverrideUrlLoading: ");

                // 步骤1:
                // 根据协议的参数，判断是否是所需要的url
                // 一般根据scheme（协议格式） & authority（协议名）判断（前两个参数）
                //假定传入进来的 url = "js://webview?arg1=111&arg2=222"（同时也是约定好的需要拦截的）
                Uri uri = Uri.parse(url);
                // 如果url的协议 = 预先约定的 js 协议
                // 就解析往下解析参数
                if (uri.getScheme().equals("js")) {
                    // 如果 authority  = 预先约定协议里的 webview，即代表都符合约定的协议
                    // 所以拦截url,下面JS开始调用Android需要的方法
                    if (uri.getAuthority().equals("webview")) {
                        //  步骤2：
                        // 执行JS所需要调用的逻辑
                        Log.e(TAG, "js拦截url调用了Android的方法: ");
                        // 可以在协议上带有参数并传递到Android上
                        HashMap<String, String> params = new HashMap<>();
                        Set<String> collection = uri.getQueryParameterNames();
                        Log.e(TAG, "js拦截url调用了Android的方法:参数 " + collection.toString());
                        String result = "收到JS的数据";
                        webView.loadUrl("javascript:returnResult('" + result + "')");
                    }
                    return true;
                }
                return false;
            }

            /**
             * 在开始加载网页时会回调
             */
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                Log.e(TAG, "onPageStarted: " + url);
                super.onPageStarted(view, url, favicon);
            }

            /**
             * 在结束加载网页时会回调
             */
            @Override
            public void onPageFinished(WebView view, String url) {
                Log.e(TAG, "onPageFinished: " + url);
                super.onPageFinished(view, url);
            }

            /**
             * 在每一次请求资源时，都会通过这个函数来回调
             */
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                return super.shouldInterceptRequest(view, request);
            }

            /**
             * 加载错误的时候会回调，在其中可做错误处理，比如再请求加载一次，或者提示404的错误页面
             */
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                Log.e(TAG, "onReceivedError: ");
                super.onReceivedError(view, request, error);
            }

            /**
             * 当接收到https错误时，会回调此函数，在其中可以做错误处理
             */
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                Log.e(TAG, "onReceivedSslError: ");
                super.onReceivedSslError(view, handler, error);
            }

        });
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                Log.e(TAG, "onProgressChanged: " + newProgress);
            }

            @Override
            public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
                return super.onJsConfirm(view, url, message, result);
            }

            // 拦截输入框(原理同方式2)
            // 参数message:代表promt（）的内容（不是url）
            // 参数result:代表输入框的返回值
            @Override
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
                Log.e(TAG, "onJsPrompt: url=" + url + ",message=" + message);
                // 根据协议的参数，判断是否是所需要的url(原理同方式2)
                // 一般根据scheme（协议格式） & authority（协议名）判断（前两个参数）
                //假定传入进来的 url = "js://webview?arg1=111&arg2=222"（同时也是约定好的需要拦截的）
                Uri uri = Uri.parse(message);
                // 如果url的协议 = 预先约定的 js 协议
                // 就解析往下解析参数
                if (uri.getScheme().equals("js")) {
                    // 如果 authority  = 预先约定协议里的 webview，即代表都符合约定的协议
                    // 所以拦截url,下面JS开始调用Android需要的方法
                    if (uri.getAuthority().equals("webview")) {
                        // 执行JS所需要调用的逻辑
                        System.out.println("js调用了Android的方法");
                        // 可以在协议上带有参数并传递到Android上
                        HashMap<String, String> params = new HashMap<>();
                        Set<String> collection = uri.getQueryParameterNames();
                        //参数result:代表消息框的返回值(输入值)
                        result.confirm("js调用了Android的方法成功啦");
                    }
                    return true;
                }
                return super.onJsPrompt(view, url, message, defaultValue, result);
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
                Log.e(TAG, "onJsAlert: url=" + url + ",message=" + message);
                AlertDialog.Builder b = new AlertDialog.Builder(MainActivity.this);
                b.setTitle("Alert");
                b.setMessage(message);
                b.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        result.confirm();
                    }
                });
                b.setCancelable(false);
                b.create().show();
                return true;
            }
        });
    }

    public void requestJs(View view) {
        // 通过Handler发送消息
        view.post(new Runnable() {
            @Override
            public void run() {
                // 注意调用的JS方法名要对应上
                // 第一种调用javascript的callJS()方法
//                webView.loadUrl("javascript:callJS()");
                //第种（1.因为该方法的执行不会使页面刷新，而第一种方法（loadUrl ）的执行则会 2.Android 4.4 后才可使用)
                webView.evaluateJavascript("javascript:callJS()", new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {
                        //此处为 js 返回的结果
                        Log.e(TAG, "evaluateJavascript:onReceiveValue --" + value);
                    }
                });
            }
        });

    }
}
