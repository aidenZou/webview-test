package com.example.aidenzou.webview_test;

//import android.support.v7.app.ActionBarActivity;

import android.app.Activity;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;


//public class MainActivity extends ActionBarActivity {
public class MainActivity extends Activity {

    private WebView webView;

    private Button back;
    private Button refresh;
    private TextView titleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = (WebView) findViewById(R.id.webView);
        back = (Button) findViewById(R.id.back);
        refresh = (Button) findViewById(R.id.refresh);
        titleView = (TextView) findViewById(R.id.title);

        loadWeb();
    }

    public void loadWeb() {
        String url = "http://192.168.1.222:8001/2#!/";
        //url = "http://m.baidu.com";
        //url = "https://youcai.shequcun.com/#!/";

        // 启用js
        webView.getSettings().setJavaScriptEnabled(true);
        // 映射
        webView.addJavascriptInterface(new WebHost(this), "js");

        // 配置调试WebViews
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }

        // localStorage
        WebSettings settings = webView.getSettings();
        settings.setDomStorageEnabled(true);
        settings.setAppCacheMaxSize(1024 * 1024 * 8);
        String appCachePath = getApplicationContext().getCacheDir().getAbsolutePath();
        settings.setAppCachePath(appCachePath);
        settings.setAllowFileAccess(true);
        settings.setAppCacheEnabled(true);

        // 设置title
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                titleView.setText(title);
                super.onReceivedTitle(view, title);
            }
        });

        // 重写如下方法,阻止通过默认浏览器打开网页,此方法可以在webview中打开链接而不会跳转到外部浏览器
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }

            // 网页加载完
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                //在这里执行你想调用的js函数
            }

            // WebView 错误码处理
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);

                view.loadUrl("file:///android_asset/error.html");
            }

            // 处理https请求，为WebView处理ssl证书设置
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();  // 接受信任所有网站的证书
                // handler.cancel();   // 默认操作 不处理
                // handler.handleMessage(null);  // 可做其他处理
                //super.onReceivedSslError(view, handler, error);
            }
        });

        // 屏蔽掉长按事件 因为webview长按时将会调用系统的复制控件
        webView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });

        webView.loadUrl(url);

        //按钮监听
        refresh.setOnClickListener(new MyLisenter());
        back.setOnClickListener(new MyLisenter());
    }

    // 重写onKeyDown,监听物理按键，使WebView能够返回历史页面
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();
            return true;
        } else {
            finish();
        }

//        if (webView.canGoBack() && event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
//            webView.goBack();
//            return true;
//        }
        return super.onKeyDown(keyCode, event);
    }

    // 按钮事件监听
    class MyLisenter implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.refresh:
                    webView.reload();
                    break;
                case R.id.back:
                    // finish();
                    webView.goBack();
                    break;

                default:
                    break;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
