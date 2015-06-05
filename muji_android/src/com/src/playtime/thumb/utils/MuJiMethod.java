package com.src.playtime.thumb.utils;

import java.text.SimpleDateFormat;
import java.util.Collections;

import org.litepal.crud.DataSupport;

import com.src.playtime.thumb.MyApplication;
import com.src.playtime.thumb.R;
import com.src.playtime.thumb.bean.ContactModel;
import com.src.playtime.thumb.phone.CallPhoneActivity;
import com.waitingfy.callhelper.GetLocationByNumber;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

/**
 * 
 * @author wanfei
 *
 */
public class MuJiMethod {

	private static MuJiMethod mMuJi;

	private MyApplication mApp;

	private Context context;

   // public static SimpleDateFormat sDateFormat = new SimpleDateFormat("yy/MM/dd HH:mm");

	public MuJiMethod(Context context) {
		mApp = (MyApplication) context.getApplicationContext();
		this.context = context;
	}

	public static synchronized MuJiMethod getInstance(Context context) {
		if (mMuJi == null) {
			mMuJi = new MuJiMethod(context);
		}
		return mMuJi;
	}

	/**
	 * 请求蓝牙配对
	 * 
	 * @return 0-失败，1-成功
	 */
	public int requestBTpair() {

		return 0;
	}

	/**
	 * 请求连接
	 * 
	 * @return 0-失败，1-成功
	 */
	public int receiveBTLink() {

		return 0;
	}

	/**
	 * 握手消息
	 * 
	 * @return 0-失败，1-成功
	 */
	public int handShake() {
		return 0;
	}

	/**
	 * 来电话
	 * 
	 * @return 0-失败，1-成功
	 */
	public int callIn() {
		return 0;
	}

	/**
	 * 我挂/接状态返回
	 * 
	 * @return 0-失败，1-成功
	 */
	public int callInAction(int myAction) {
		return 0;
	}

	/**
	 * 打电话
	 * 
	 * @return 0-失败，1-成功
	 */
	public int callOut(Context context, Object obj) {
		String Tel = "";
		if (obj instanceof ContactModel) {
			ContactModel model = (ContactModel) obj;
			if (model.getTelnum().equals("")) {
				Tel = model.getName();
			} else {
				Tel = model.getTelnum();
			}
		} else {
			Tel = obj.toString();
		}
		ContactModel TempModel = new ContactModel();
		String date = System.currentTimeMillis()+"";
        String attribution= GetLocationByNumber.getCallerInfo(Tel, context);
        String operators=BaseUtil.getOperator(Tel);
		// 遍历通讯录 如果有相同号码 则把数据库的信息取出赋值到model里
		for (int i = 0; i < mApp.mContactDatas.size(); i++) {
			if (mApp.mContactDatas.get(i).getTelnum().replace("+86", "")
					.equals(Tel.replace("+86", ""))) {
				TempModel = mApp.mContactDatas.get(i);
				TempModel.setDate(date);
                TempModel.setAttribution(attribution);
                TempModel.setOperators(operators);
				break;
			}
		}
		// 如果遍历不到 则新建一个model
		if (TempModel.getName().equals("")) {
			TempModel.setName(Tel);
			// model.setTelnum(mPopup.et.getText().toString());
			TempModel.setDate(date);
            TempModel.setAttribution(attribution);
            TempModel.setOperators(operators);
		}
		boolean isShowNotifi = false;
        //判断是否在拨打电话
		if (!BaseUtil.isInLauncher(context,
				"com.src.playtime.thumb.phone.CallPhoneActivity")) {
			isShowNotifi = true;
		}
        TempModel.setState("0");
		Intent intent = new Intent(context, CallPhoneActivity.class);
		intent.putExtra("data", TempModel);
		intent.putExtra("isShowNotifi", isShowNotifi);
        intent.putExtra("state","0");
		context.startActivity(intent);
		((Activity) context).overridePendingTransition(R.anim.out_alpha,
				R.anim.in_alpha);
		return 1;
	}

	/**
	 * 对方挂/接状态返回
	 * 
	 * @return 0-失败，1-成功
	 */
	public int callOutAction(int myAction) {
		return 0;
	}

	/**
	 * 我挂电话
	 * 
	 * @return
	 */
	public int callEndByMe() {
		return 0;
	}

	/**
	 * 他挂电话
	 * 
	 * @return
	 */
	public int callEndByHim() {
		return 0;
	}

	/**
	 * 接收短信
	 * 
	 * @return
	 */
	public int msgIn() {
		return 0;
	}

	/**
	 * 发短信
	 * 
	 * @return
	 */
	public int msgOut() {
		return 0;
	}

}
