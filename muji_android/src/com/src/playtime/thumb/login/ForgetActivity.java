package com.src.playtime.thumb.login;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVMobilePhoneVerifyCallback;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.RequestMobileCodeCallback;
import com.avos.avoscloud.UpdatePasswordCallback;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.src.playtime.thumb.R;
import com.src.playtime.thumb.widget.ClearEditText;
import com.src.playtime.thumb.widget.swipeback.SwipeBackActivity;

/**
 * Created by wanfei on 2015/6/1.
 */
public class ForgetActivity extends SwipeBackActivity{

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
    @ViewInject(R.id.tv_register_title)
    private TextView mTvTitle;
    @ViewInject(R.id.ll_register_clause)
    private LinearLayout mLlCloause;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ViewUtils.inject(mAct);
        init();

    }

    public void init(){
     mLlCloause.setVisibility(View.GONE);
     mTvTitle.setText("重置密码");
    }

    @Override
    @OnClick({R.id.bt_register,R.id.bt_register_request,R.id.tv_register_cancel})
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.bt_register:
                EqualsSmsCode();
                break;
            case R.id.bt_register_request:
                sendSmsCode();
                break;
            case R.id.tv_register_cancel:
                finish();
                break;
        }
    }

    /**
     * 验证短信验证码
     */
    public void EqualsSmsCode(){
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

        AVUser.resetPasswordBySmsCodeInBackground(mEdSmscode.getText().toString(), mEdKey.getText().toString(), new UpdatePasswordCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    showToast("重置密码成功!");
                    finish();
                }
            }
        });
    }

    /**
     * 发送短信验证码
     */
    public void sendSmsCode(){
        if(TextUtils.isEmpty(mEdUser.getText().toString())){
            showToast("请填写11位手机号");
            return;
        }else{
            timer.start();
            AVUser.requestPasswordResetBySmsCodeInBackground(mEdUser.getText().toString(),new RequestMobileCodeCallback() {
                @Override
                public void done(AVException e) {
                    //if(e!=null)
                }
            });
        }
    }



    /**
     * 短信验证计时
     */
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
