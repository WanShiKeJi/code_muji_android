package com.src.playtime.thumb.blueService;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import com.src.playtime.thumb.MyApplication;


public class BlueServiceManage {

	private static BlueServiceManage mMServerManage;

	private MyApplication mApp;

	private Context context;

	private boolean isKitKat;

	public BlueService mBlueServer;

	public BlueServiceManage(Context context) {
		mApp = (MyApplication) context.getApplicationContext();
		isKitKat = Build.VERSION.SDK_INT >= 18;
		this.context = context;
	}

	public static BlueServiceManage getBlueServerManage(Context context) {
		if (mMServerManage == null) {
			mMServerManage = new BlueServiceManage(context);
		}
		return mMServerManage;
	}

	/**
	 * 用来判断服务是否运行.
	 *
	 * @param className
	 *            判断的服务名字
	 * @return true 在运行 false 不在运行
	 */
	public static boolean isServiceRunning(Context mContext, String className) {
		boolean isRunning = false;
		ActivityManager activityManager = (ActivityManager) mContext
				.getSystemService(Context.ACTIVITY_SERVICE);
		for (RunningServiceInfo service : activityManager
				.getRunningServices(Integer.MAX_VALUE)) {
			if (className.equals(service.service.getClassName())) {
				isRunning = true;
			}
		}
		return isRunning;
	}

	/**
	 * 连接蓝牙服务
	 */
	public void ConnectBlueServer() {
		if (isKitKat) {
			Intent intent = new Intent(context, BlueService.class);
			context.bindService(intent, connection, Context.BIND_AUTO_CREATE);
		}

	}

	private ServiceConnection connection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			BlueService.MyBinder binder = (BlueService.MyBinder) service;
			mBlueServer = binder.getService();
            // 绑定成功后通知操作
			// handler.sendEmptyMessage(BIND_SUCCESS);
			// mBlueServer.initDevice();
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {

		}

	};

}
