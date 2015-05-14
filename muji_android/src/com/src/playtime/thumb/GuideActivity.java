package com.src.playtime.thumb;

import android.os.Bundle;
import android.view.Window;

/**
 * Created by wanfei on 2015/5/12.
 */
public class GuideActivity extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_guide);

    }
}
