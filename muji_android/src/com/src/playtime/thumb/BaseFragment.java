package com.src.playtime.thumb;

import com.src.playtime.thumb.phone.CallPhoneActivity;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class BaseFragment extends Fragment implements OnClickListener {

	protected MyApplication mApp;
	protected Activity mAct;
	/** Notification管理 */
	public NotificationManager mNotificationManager;

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

	}

}
