package com.example.aidenzou.webview_test;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by aidenZou on 15/12/20.
 */
public class WebHost {

    public Context mContext;

    public WebHost(Context context) {
        this.mContext = context;
    }

    public void callJs() {
        Toast.makeText(mContext, "call from js", Toast.LENGTH_LONG).show();
    }
}
