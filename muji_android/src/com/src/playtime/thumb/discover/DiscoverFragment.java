package com.src.playtime.thumb.discover;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SignUpCallback;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.src.playtime.thumb.BaseFragment;
import com.src.playtime.thumb.R;
import com.src.playtime.thumb.blueService.BlueService;
import com.src.playtime.thumb.blueService.BlueServiceManage;
import com.src.playtime.thumb.login.LoginActivity;
import com.src.playtime.thumb.widget.GifMovieView;

public class DiscoverFragment extends BaseFragment {
    //手机号码
    @ViewInject(R.id.tv_discover_phone)
    private TextView mTvPhone;
    //拇机号码
    @ViewInject(R.id.tv_discover_mjphone)
    private TextView mTvMjPhone;
    //登录按钮
    @ViewInject(R.id.tv_discover_login)
    private TextView mTvLogin;
    //gif蓝牙
    @ViewInject(R.id.gif_discover_ble)
    private GifMovieView mGifBle;
    //gif基站
    @ViewInject(R.id.gif_discover_base)
    private GifMovieView mGifBase;
    //ble绑定按钮
    @ViewInject(R.id.tv_discover_ble)
    private TextView mTvBle;
    //拇机配置按钮
    @ViewInject(R.id.tv_discover_config)
    private TextView mTvConfig;

	private View view;
    //用户缓存
    private  AVUser currentUser;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_discover, null);
		ViewUtils.inject(this, view);
		setBarTitle(view, "发现");
		return view;
	}


    public void init(){
         currentUser = AVUser.getCurrentUser();
        if (currentUser != null) {
            mTvPhone.setText(currentUser.getUsername());
            mTvMjPhone.setText(currentUser.getString("mujiphone"));
            if(!TextUtils.isEmpty(mTvMjPhone.getText().toString())){
                mGifBase.setBackgroundResource(R.color.transparent);
                mGifBase.setMovieResource(R.drawable.gif_base);
                mGifBle.setMovieResource(R.drawable.gif_ble);
                mTvConfig.setBackgroundResource(R.drawable.icon_discover_bgred);
                mTvConfig.setText("修改");
            }
            mTvLogin.setText("退出");
            mTvLogin.setBackgroundResource(R.drawable.icon_discover_bgred);
        }else{
            mTvPhone.setText("");
            mTvMjPhone.setText("");
            mTvLogin.setText("登录");
            mGifBase.setBackgroundResource(R.drawable.icon_discover_base);
            mGifBase.setMovieResource(R.drawable.icon_discover_base);
            mTvConfig.setBackgroundResource(R.drawable.icon_discover_bggreen);
            mTvConfig.setText("配置");
            mTvLogin.setBackgroundResource(R.drawable.icon_discover_bggreen);
        }
    }

    @Override
    @OnClick({R.id.tv_discover_login,R.id.tv_discover_config,R.id.tv_discover_ble})
    public void onClick(View v) {
        super.onClick(v);
        switch(v.getId()){
            case R.id.tv_discover_login:
                if(currentUser==null){
                    startActivity(new Intent(mAct, LoginActivity.class));
                }else{
                    AVUser.logOut();
                    init();
                }
                break;
            case R.id.tv_discover_config:
                if(currentUser==null){
                    showToast("请先登录!");
                    return;
                }
                showConfigDialog();
                break;
            case R.id.tv_discover_ble:
                  //showToast("正在扫描设备");
                  if(mTvBle.getText().toString().equals("解绑")){
                      mTvBle.setText("绑定");
                      mTvBle.setBackgroundResource(R.drawable.icon_discover_bggreen);
                      mGifBle.setBackgroundResource(R.drawable.icon_discover_ble);
                      mGifBle.setMovieResource(R.drawable.icon_discover_ble);
                  }else {
                      showBleDialog();
                  }
//                BlueServiceManage.getBlueServerManage(mAct).mBlueServer.initDevice();
                break;
            case R.id.tv_config_confirm:
                putConfigShared();
                break;
            case R.id.tv_dialogble_cancel:
                mConfigDialog.dismiss();
                break;
            case R.id.tv_dialogble_confirm:
                mApp.mBlueManage.mBlueServer.initDevice();
                if(mTvBle.getText().toString().equals("绑定")){
                    mTvBle.setText("解绑");
                    mTvBle.setBackgroundResource(R.drawable.icon_discover_bgred);
                    mGifBle.setBackgroundResource(R.color.transparent);
                    mGifBase.setMovieResource(R.drawable.gif_base);
                    mGifBle.setMovieResource(R.drawable.gif_ble);
                }
                mConfigDialog.dismiss();
                break;
        }
    }

    public  void  putConfigShared(){
        EditText mPhone= (EditText) mConfigView.findViewById(R.id.ed_config_phone);
        final String phone=mPhone.getText().toString().trim();
        if(phone.equals("")){
            showToast("请填写拇机号码！");
            return;
        }
        AVUser currentUser = AVUser.getCurrentUser();
        currentUser.put("mujiphone",phone);
        currentUser.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(AVException e) {
                if(e==null){
                mTvMjPhone.setText(phone);
                mGifBase.setBackgroundResource(R.color.transparent);
                mGifBase.setMovieResource(R.drawable.gif_base);
                }else{
                showToast("配置失败!");
                }
            }
        });
        mConfigDialog.dismiss();
    }

    /**
     * 显示一个是否开启蓝牙的通知
     *
     * @return
     */
    public View showBleDialog() {
        mConfigView=LayoutInflater.from(mAct).inflate(R.layout.dialog_ble,null);
        AlertDialog.Builder builder = new AlertDialog.Builder(mAct);
        mConfigDialog = builder.setView(mConfigView).create();
        mConfigView.findViewById(R.id.tv_dialogble_cancel).setOnClickListener(this);
        mConfigView.findViewById(R.id.tv_dialogble_confirm).setOnClickListener(this);
        //  EditText mEmail= (EditText) mConfigView.findViewById(R.id.ed_config_email);
        //EditText mPhone= (EditText) mConfigView.findViewById(R.id.ed_config_phone);
//        mEmail.setText(email);
//        mPhone.setText(phone);
        mConfigDialog.show();
        return mConfigView;
    }

    @Override
    public void onResume() {
        super.onResume();
        init();
    }
}
