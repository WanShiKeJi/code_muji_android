package com.src.playtime.thumb;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.src.playtime.thumb.blueService.BlueServiceManage;
import com.src.playtime.thumb.phone.CallPhoneActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author WF
 * @date 2015-3-18
 * 
 */
public class BaseActivity extends FragmentActivity implements OnClickListener {

	public MyApplication mApp;
	public BaseActivity mAct;
	protected ProgressDialog mProgressDialog;
	/** Notification管理 */
	public NotificationManager mNotificationManager;

	public Notification mNotification;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mAct = this;
		mApp = (MyApplication) getApplication();
		mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

	}

	/** 显示常驻通知栏 */
	public NotificationCompat.Builder showCzNotify() {
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
		mBuilder.setSmallIcon(R.drawable.call_out).setContentText("00")
				.setContentIntent(pendingIntent);
		mNotification = mBuilder.build();
		// 设置通知 消息 图标
		mNotification.icon = R.drawable.call_out;
		// 在通知栏上点击此通知后自动清除此通知
		mNotification.flags = Notification.FLAG_ONGOING_EVENT;// FLAG_ONGOING_EVENT
																// 在顶部常驻，可以调用下面的清除方法去除
																// FLAG_AUTO_CANCEL
																// 点击和清理可以去调
		// 设置显示通知时的默认的发声、震动、Light效果
		// mNotification.defaults = Notification.DEFAULT_VIBRATE;
		// 设置发出消息的内容
		mNotification.tickerText = "正在拨打电话";
		// 设置发出通知的时间
		mNotification.when = System.currentTimeMillis();
		// mNotification.flags = Notification.FLAG_AUTO_CANCEL;
		// //在通知栏上点击此通知后自动清除此通知
		// mNotification.setLatestEventInfo(this, "常驻测试",
		// "使用cancel()方法才可以把我去掉哦", null); //设置详细的信息 ,这个方法现在已经不用了
		mNotificationManager.notify(100, mNotification);
        showToast("zhixing------notification");
		return mBuilder;
	}

	/**
	 * 清除所有通知栏
	 * */
	public void clearAllNotify() {
		mNotificationManager.cancelAll();// 删除你发的所有通知
	}

    /**
     * 获取shared数据
     * @param name
     * @return
     */
    public SharedPreferences getSharedPreferences(String name){
        SharedPreferences mShared=getSharedPreferences(name,Activity.MODE_PRIVATE);
        return mShared;
    }

	/**
	 * 初始化ActionBar
	 * 
	 * @param layoutId
	 *            ActionBar布局ID
	 * @return
	 */
	@SuppressLint("NewApi")
	protected ActionBar initActionBar(int layoutId) {
		// 1.得到一个ActionBar
		ActionBar mActionBar = getActionBar();
		mActionBar.removeAllTabs();
		mActionBar.setDisplayShowCustomEnabled(true);
		mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM,
				ActionBar.DISPLAY_SHOW_CUSTOM);
		mActionBar.setBackgroundDrawable(null);
		mActionBar.setStackedBackgroundDrawable(null);
		mActionBar.setSplitBackgroundDrawable(null);
		View view = getLayoutInflater().inflate(layoutId, null);
		ActionBar.LayoutParams params = new ActionBar.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);
		mActionBar.setCustomView(view, params);
		return mActionBar;
	}

	/**
	 * 返回标题
	 * 
	 * @param title
	 *            标题名字
	 * @return
	 */
	@SuppressLint("NewApi")
	protected TextView initActionBar(String title) {
		ActionBar mActionBar = initActionBar(R.layout.activity_header);
		TextView tv = (TextView) mActionBar.getCustomView().findViewById(
				R.id.header_title);
		tv.setText(title);
		return tv;
	}


	/****************** Dialog Start ********************/
	public ProgressDialog showProgressDialog(String message) {
		if (mProgressDialog != null && mProgressDialog.isShowing()) {
			return mProgressDialog;
		}
		mProgressDialog = ProgressDialog.show(mAct, null, message);
		return mProgressDialog;
	}

	public ProgressDialog showProgressDialog() {
		mProgressDialog = showProgressDialog("请稍后...");
		return mProgressDialog;
	}

	public void removeProgressDialog() {
		if (mProgressDialog != null && mProgressDialog.isShowing()) {
			mProgressDialog.dismiss();
		}
	}

	/****************** Dialog End ********************/

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// mLifeManager.removeActivity(mAct);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
	}

	/**
	 * 描述：Toast提示文本.
	 * 
	 * @param text
	 *            文本
	 */
	public void showToast(String text) {
		Toast.makeText(this, "" + text, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 描述：Toast提示文本.
	 * 
	 * @param resId
	 *            文本的资源ID
	 */
	public void showToast(int resId) {
		Toast.makeText(this, "" + this.getResources().getText(resId),
				Toast.LENGTH_SHORT).show();
	}


	/**
	 * 描述：在线程中提示文本信息.
	 * 
	 * @param toast
	 *            消息what值为0
	 */
	public void showToastInThread(final String toast) {
		// Message msg = baseHandler.obtainMessage(0);
		// Bundle bundle = new Bundle();
		// bundle.putString("Msg", toast);
		// msg.setData(bundle);
		// baseHandler.sendMessage(msg);

		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				showToast(toast);
			}
		});
	}

	/**
	 * 显示一个按钮的Dialog
	 * 
	 * @return
	 */
	public AlertDialog showDialog(String title, String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(mAct);
		AlertDialog dialog = builder.setMessage(message).setTitle(title)
				.setPositiveButton("确定", null).create();
		dialog.show();
		return dialog;
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
				.setPositiveButton("确定", onOKClickListener)
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
	 * 显示列表 Dialog
	 * 
	 * @param title
	 * @param items
	 * @param onClickListener
	 *            在dialog 中的listView tag中
	 */
	public AlertDialog showDialog(String title, String[] items,
			DialogInterface.OnClickListener onClickListener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(mAct);
		AlertDialog dialog = builder.setItems(items, onClickListener)
				.setTitle(title).create();
		dialog.show();
		return dialog;
	}

	public String getText(TextView tv) {
		return tv.getText().toString().trim();
	}

	public boolean isEmpty(TextView tv) {
		return getText(tv).length() == 0;
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		// case R.id.tvLeft:
		// case R.id.ibBack:
		// finish();
		// break;
		// default:
		// break;
		}
	}

}
