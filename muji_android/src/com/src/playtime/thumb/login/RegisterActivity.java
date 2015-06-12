package com.src.playtime.thumb.login;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVMobilePhoneVerifyCallback;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.RequestMobileCodeCallback;
import com.avos.avoscloud.SignUpCallback;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.src.playtime.thumb.R;
import com.src.playtime.thumb.widget.ClearEditText;
import com.src.playtime.thumb.widget.swipeback.SwipeBackActivity;

/**
 * Created by wanfei on 2015/6/1.
 */
public class RegisterActivity extends SwipeBackActivity{


    @ViewInject(R.id.ced_register_user)
    private  ClearEditText mEdUser;
    @ViewInject(R.id.ced_register_key)
    private  ClearEditText mEdKey;
    @ViewInject(R.id.ced_register_smscode)
    private  ClearEditText mEdSmscode;
    @ViewInject(R.id.bt_register_request)
    private Button mBtRequest;
    @ViewInject(R.id.ced_register_keys)
    private ClearEditText mEdKeys;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ViewUtils.inject(mAct);
    }

    @Override
    @OnClick({R.id.bt_register,R.id.tv_register_cancel,R.id.bt_register_request})
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.bt_register:
                Register();
                break;
            case R.id.tv_register_cancel:
                finish();
                break;
            case R.id.bt_register_request:
                if(TextUtils.isEmpty(mEdUser.getText().toString())){
                    showToast("请填写11位手机号");
                    return;
                }else{
                    timer.start();
                    requestSMSCode(mEdUser.getText().toString());
                }
                break;
        }
    }

    public void requestSMSCode(String phone){
        AVOSCloud.requestSMSCodeInBackgroud(phone,"拇机","注册",0,new RequestMobileCodeCallback() {
            @Override
            public void done(AVException e) {
                if(e==null){
                   //showToast("已发送");
                }
            }
        });


    }

    /**
     * 注册账号
     */
    public void Register(){
        if(TextUtils.isEmpty(mEdKey.getText().toString())
                ||TextUtils.isEmpty(mEdUser.getText().toString())
                ||TextUtils.isEmpty(mEdSmscode.getText().toString())
                ||TextUtils.isEmpty(mEdKeys.getText().toString())){
            showToast("请把信息填写完整!");
            return;
        }

        if(!mEdKey.getText().toString().equals(mEdKeys.getText().toString())){
            showToast("两次输入的密码不一致!");
            return;
        }

        AVOSCloud.verifySMSCodeInBackground(mEdSmscode.getText().toString(),mEdUser.getText().toString(),new AVMobilePhoneVerifyCallback() {
            @Override
            public void done(AVException e) {
                if(e==null){
                    AVUser user = new AVUser();
                    user.setUsername(mEdUser.getText().toString());
                    user.setPassword(mEdKey.getText().toString());
                    user.setMobilePhoneNumber(mEdUser.getText().toString());
                    user.signUpInBackground(new SignUpCallback() {
                        public void done(AVException e) {
                            if (e == null) {
                                // successfully
                                showToast("注册成功!");
                                finish();
                            } else {
                                // failed
                                showToast("注册失败!");
                            }
                        }
                    });
                }else{
                    showToast("验证码不正确!");
                }
            }
        });

    }

    CountDownTimer timer=new CountDownTimer(60000,1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            mBtRequest.setClickable(false);
            mBtRequest.setText(millisUntilFinished /1000+"秒");

        }

        @Override
        public void onFinish() {
            mBtRequest.setText("重新验证");
            mBtRequest.setClickable(true);
        }
    };
}
