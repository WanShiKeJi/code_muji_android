package com.src.playtime.thumb;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SignUpCallback;
import com.src.playtime.thumb.phone.CallPhoneActivity;

public class BaseFragment extends Fragment implements OnClickListener {

	protected MyApplication mApp;
	protected Activity mAct;
	/** Notification管理 */
	public NotificationManager mNotificationManager;
    /**配置拇机号码和邮箱的dialog*/
    protected  AlertDialog mConfigDialog;
    /**配置拇机号码和邮箱的view*/
    protected View mConfigView;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mAct = activity;
		mApp = (MyApplication) mAct.getApplication();
		mNotificationManager = (NotificationManager) mAct
				.getSystemService(mAct.NOTIFICATION_SERVICE);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
	}

	public void setBarTitle(View view, String str) {
		TextView tv = (TextView) view.findViewById(R.id.header_title);
		TextView iv = (TextView) view.findViewById(R.id.header_right);
		iv.setVisibility(View.GONE);
		tv.setText(str);
	}

	public void setBarTitle(View view, String str, int resid) {
		TextView tv = (TextView) view.findViewById(R.id.header_title);
		TextView iv = (TextView) view.findViewById(R.id.header_right);
		iv.setBackgroundResource(resid);
		iv.setOnClickListener(this);
		tv.setText(str);
	}

	public void showToast(String str) {
		Toast.makeText(mAct, str, Toast.LENGTH_SHORT).show();
	}

	/** 显示常驻通知栏 */
	public void showCzNotify() {
		// Notification mNotification = new
		// Notification();//为了兼容问题，不用该方法，所以都采用BUILD方式建立
		// Notification mNotification = new
		// Notification.Builder(this).getNotification();//这种方式已经过时
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				mAct);
		// //PendingIntent 跳转动作
		Intent intent = new Intent(mAct, CallPhoneActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(mAct, 0,
				intent, 0);
		mBuilder.setSmallIcon(R.drawable.call_out).setTicker("拨出电话")
				.setContentTitle("正在拨打电话").setContentText("00")
				.setContentIntent(pendingIntent);
		Notification mNotification = mBuilder.build();
		// 设置通知 消息 图标
		mNotification.icon = R.drawable.call_out;
		// 在通知栏上点击此通知后自动清除此通知
		mNotification.flags = Notification.FLAG_ONGOING_EVENT;// FLAG_ONGOING_EVENT
																// 在顶部常驻，可以调用下面的清除方法去除
																// FLAG_AUTO_CANCEL
																// 点击和清理可以去调
		// 设置显示通知时的默认的发声、震动、Light效果
		mNotification.defaults = Notification.DEFAULT_VIBRATE;
		// 设置发出消息的内容
		mNotification.tickerText = "正在拨打电话";
		// 设置发出通知的时间
		mNotification.when = System.currentTimeMillis();
		// mNotification.flags = Notification.FLAG_AUTO_CANCEL;
		// //在通知栏上点击此通知后自动清除此通知
		// mNotification.setLatestEventInfo(this, "常驻测试",
		// "使用cancel()方法才可以把我去掉哦", null); //设置详细的信息 ,这个方法现在已经不用了
		mNotificationManager.notify(100, mNotification);
	}

    /**
     * 获取shared数据
     * @param name
     * @return
     */
    public SharedPreferences getSharedPreferences(String name){
        SharedPreferences mShared=mAct.getSharedPreferences(name,Activity.MODE_PRIVATE);
        return mShared;
    }


    /**
     * 显示Dialog是否可以取消
     *
     * @param title
     * @param message
     * @param onOKClickListener
     * @param isCancelAble
     * @return
     */
    public AlertDialog showDialog(String title, String message,
                                  DialogInterface.OnClickListener onOKClickListener,
                                  boolean isCancelAble) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mAct);
        AlertDialog alertDialog = builder.setTitle(title).setMessage(message)
                .setPositiveButton("取消", onOKClickListener)
                .setCancelable(isCancelAble).create();
        alertDialog.show();
        return alertDialog;
    }

    /**
     * 自定义监听事件和按钮名称
     *
     * @param title
     *            标题
     * @param message
     *            内容
     * @param posTitle
     *            确认按钮，null则不添加
     * @param onPosClickListener
     * @param negTitle
     *            取消按钮，null则不添加
     * @param onNegClickListener
     *
     * @param isCanCancel
     *            按back是否可以取消dialog
     * @return
     */
    public AlertDialog showDialog(String title, String message,
                                  String posTitle,
                                  DialogInterface.OnClickListener onPosClickListener,
                                  String negTitle,
                                  DialogInterface.OnClickListener onNegClickListener,
                                  boolean isCanCancel, boolean isSystemDialog) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mAct);
        builder.setMessage(message).setTitle(title);
        if (posTitle != null) {
            builder.setPositiveButton(posTitle, onPosClickListener);
        }
        if (negTitle != null) {
            builder.setNegativeButton(negTitle, onNegClickListener);
        }
        builder.setCancelable(isCanCancel);
        AlertDialog dialog = builder.create();
        if (isSystemDialog) {
            dialog.getWindow().setType(
                    WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        }
        dialog.show();
        return dialog;
    }

    /**
     * 显示一个配置拇机号码和邮箱的dialog
     *
     * @return
     */
    public View showConfigDialog(String email,String phone) {
        mConfigView=LayoutInflater.from(mAct).inflate(R.layout.dialog_config,null);
        AlertDialog.Builder builder = new AlertDialog.Builder(mAct);
        mConfigDialog = builder.setView(mConfigView).create();
        mConfigView.findViewById(R.id.tv_config_cancel).setOnClickListener(this);
        mConfigView.findViewById(R.id.tv_config_confirm).setOnClickListener(this);
        EditText mEmail= (EditText) mConfigView.findViewById(R.id.ed_config_email);
        EditText mPhone= (EditText) mConfigView.findViewById(R.id.ed_config_phone);
        mEmail.setText(email);
        mPhone.setText(phone);
        mConfigDialog.show();
        return mConfigView;
    }

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
	}

	@Override
	public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_config_confirm:
                putConfigShared();
                break;
            case R.id.tv_config_cancel:
                mConfigDialog.dismiss();
                break;
        }
	}

    public  void  putConfigShared(){
        EditText mEmail= (EditText) mConfigView.findViewById(R.id.ed_config_email);
        EditText mPhone= (EditText) mConfigView.findViewById(R.id.ed_config_phone);
        EditText mPass= (EditText) mConfigView.findViewById(R.id.ed_config_password);
        final String email=mEmail.getText().toString().trim();
        final String phone=mPhone.getText().toString().trim();
        final String password=mPass.getText().toString().trim();
        SharedPreferences mShare=this.getSharedPreferences("muji");
        final SharedPreferences.Editor mEditor=mShare.edit();
        if(email.equals("")||phone.equals("")){
            showToast("请把信息填写完整！");
        return;
        }
        AVUser avUser=new AVUser();
        avUser.setUsername(email);
        avUser.setPassword(password);
        avUser.setEmail(email);
        avUser.put("mujiphone",phone);
        avUser.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(AVException e) {
                if (e==null){
                    //注册成功
                    mEditor.putString("email",email);
                    mEditor.putString("phone",phone);
                    mEditor.putString("password",password);
                    mEditor.commit();
                    mConfigDialog.dismiss();
                    callTransfer(phone);
                }else{
                    if(e.getCode()==203){
                        showToast("该邮箱已被注册!");
                    }
                }
            }
        });


    }

    public void callTransfer(String phone){
        SharedPreferences mShare=this.getSharedPreferences("muji");
        boolean bool=mShare.getBoolean("isTransfer",false);
        Intent intent;
        SharedPreferences.Editor mEditor=mShare.edit();
        if(bool){
            String tel= Uri.encode(mApp.mStrPrefix[1]);
            intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+tel));
            mEditor.putBoolean("isTransfer",false);
        }else{
            String tel=Uri.encode(mApp.mStrPrefix[0]+phone+"#");
            intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+tel));
            mEditor.putBoolean("isTransfer",true);
        }
        mEditor.commit();
        mAct.startActivity(intent);
    }

}
