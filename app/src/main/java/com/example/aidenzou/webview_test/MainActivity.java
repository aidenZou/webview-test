package com.example.aidenzou.webview_test;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {

    private WebView webView;

    private Button back;
    private Button refresh;
    private TextView titleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = (WebView) findViewById(R.id.webView);
        webView.loadUrl("http://m.baidu.com");

        // 启用js
        webView.getSettings().setJavaScriptEnabled(true);
        // 映射
        webView.addJavascriptInterface(new WebHost(this), "js");


        back = (Button) findViewById(R.id.back);
        refresh = (Button) findViewById(R.id.refresh);
        titleView = (TextView) findViewById(R.id.title);

        // 重写如下方法,阻止通过默认浏览器打开网页
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                // 设置title
                titleView.setText(title);
                super.onReceivedTitle(view, title);
            }
        });

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }

            // WebView 错误码处理
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);

                view.loadUrl("file:///android_asset/error.html");
            }
        });

        refresh.setOnClickListener(new MyLisenter());
        back.setOnClickListener(new MyLisenter());
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
                    finish();
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
