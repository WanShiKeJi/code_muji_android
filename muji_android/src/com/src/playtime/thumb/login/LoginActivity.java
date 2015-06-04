package com.src.playtime.thumb.login;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.src.playtime.thumb.BaseActivity;
import com.src.playtime.thumb.R;
import com.src.playtime.thumb.widget.ClearEditText;
import com.src.playtime.thumb.widget.swipeback.SwipeBackActivity;

/**
 * Created by wanfei on 2015/5/29.
 */
public class LoginActivity extends SwipeBackActivity{

    @ViewInject(R.id.ced_login_user)
    private ClearEditText mEdUser;
    @ViewInject(R.id.ced_login_key)
    private ClearEditText mEdKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ViewUtils.inject(mAct);
        init();
    }

    /**
     * 初始化
     */
    public void init(){
        mEdUser.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(TextUtils.isEmpty(s.toString())){
                    Drawable drawable=getResources().getDrawable(R.drawable.icon_login_user);
                    drawable.setBounds(0,0,drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight());
                    mEdUser.setCompoundDrawables(drawable,null,null,null);
                }else{
                    Drawable drawable=getResources().getDrawable(R.drawable.icon_login_usersel);
                    drawable.setBounds(0,0,drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight());
                    mEdUser.setCompoundDrawables(drawable,null,null,null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mEdKey.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(TextUtils.isEmpty(s.toString())){
                    Drawable drawable=getResources().getDrawable(R.drawable.icon_login_key);
                    drawable.setBounds(0,0,drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight());
                    mEdKey.setCompoundDrawables(drawable,null,null,null);
                }else{
                    Drawable drawable=getResources().getDrawable(R.drawable.icon_login_keysel);
                    drawable.setBounds(0,0,drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight());
                    mEdKey.setCompoundDrawables(drawable,null,null,null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    @OnClick({R.id.bt_login,R.id.tv_login_forget,R.id.tv_login_register})
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.bt_login:
                Login();
                break;
            case R.id.tv_login_forget:
                startActivity(new Intent(mAct,ForgetActivity.class));
                break;
            case R.id.tv_login_register:
                startActivity(new Intent(mAct,RegisterActivity.class));
                break;

        }
    }

    public void Login(){
        if(TextUtils.isEmpty(mEdUser.getText().toString())){
            showToast("请输入用户名!");
            return;
        }else if(TextUtils.isEmpty(mEdKey.getText().toString())){
            showToast("请输入密码!");
            return;
        }
        showProgressDialog();
        AVUser.logInInBackground(mEdUser.getText().toString(),mEdKey.getText().toString(), new LogInCallback() {
            public void done(AVUser user, AVException e) {
                if (user != null) {
                    // 登录成功
                    showToast("登录成功!");
                    finish();
                } else {
                    // 登录失败
                    showToast("登录失败,请检查您的用户名和密码!");

                }
                removeProgressDialog();
            }
        });
    }
}
