package com.src.playtime.thumb.phone;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;

import android.app.Notification;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Chronometer.OnChronometerTickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.src.playtime.thumb.BaseActivity;
import com.src.playtime.thumb.MainActivity;
import com.src.playtime.thumb.R;
import com.src.playtime.thumb.bean.ContactModel;
import com.src.playtime.thumb.utils.BaseUtil;

import org.litepal.crud.DataSupport;

public class CallPhoneActivity extends BaseActivity implements Runnable {

    @ViewInject(R.id.call_phone_name)
    private TextView mTvName;
    @ViewInject(R.id.call_phone_phone)
    private TextView mTvPhone;
    @ViewInject(R.id.chronometer)
    private Chronometer timer;
    /**当前通话显示*/
    @ViewInject(R.id.call_phone_info)
    private TextView mTvInfo;
    /**挂断按钮*/
    @ViewInject(R.id.call_phone_close)
    private  TextView mTvClose;
    /**接听按钮*/
    @ViewInject(R.id.call_phone_connect)
    private TextView mTvConnect;
    /**挂断布局*/
    @ViewInject(R.id.call_phone_llclose)
    private LinearLayout mLyClose;
    /**接听布局*/
    @ViewInject(R.id.call_phone_llconnect)
    private  LinearLayout mLyConnect;

    // 通知栏
    private NotificationCompat.Builder mBuilder;

    // 开始线程
    private Thread mThread;
    // 是否一直循环
    private boolean isRun = true;

    /**时间*/
    private long mBase;

    private StringBuilder mRecycle = new StringBuilder(8);

    private ContactModel mContatct;

    /**通话类型*/
    private String state;
    /**
     * 通话时长
     */
    private String duration = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.call_phone);
        ViewUtils.inject(mAct);
        init();
    }

    public void init() {
        mBase = SystemClock.elapsedRealtime();
        mContatct = (ContactModel) getIntent().getSerializableExtra(
                "data");
        state=getIntent().getStringExtra("state");
        mTvName.setText(mContatct.getName());
        if (mContatct.getTelnum().equals("")) {
            mTvPhone.setText(mContatct.getName());
        } else {
            mTvPhone.setText(mContatct.getTelnum());
        }
        if (getIntent().getBooleanExtra("isShowNotifi", false)) {
            mBuilder = showCzNotify();
            mThread = new Thread(this);
            mThread.start();
        }
        if (state.equals("0")){
            mLyConnect.setVisibility(View.GONE);
        }
        timer.start();
        // timer.setOnChronometerTickListener(this);
    }

    @Override
    @OnClick({R.id.call_phone_close, R.id.call_phone_connect})
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.call_phone_close:
                Finish();
                break;
            case R.id.call_phone_connect:
                break;
        }
    }

    public void Finish() {
        isRun = false;
        mContatct.setDuration(timer.getText().toString());
        mContatct.save();
        mApp.mTelRecordDatas = DataSupport.findAll(ContactModel.class);
        Collections.reverse(mApp.mTelRecordDatas);
        mAct.clearAllNotify();
        finish();
        overridePendingTransition(R.anim.out_alpha, R.anim.in_alpha);
    }

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        showToast("zhixing------");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
            // startActivity(new Intent(mAct, MainActivity.class));
            overridePendingTransition(R.anim.out_alpha, R.anim.in_alpha);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void run() {
        while (isRun) try {
            Thread.sleep(1000);
            long seconds = SystemClock.elapsedRealtime() - mBase;
            seconds /= 1000;
            duration = DateUtils.formatElapsedTime(mRecycle, seconds);
            mBuilder.setContentText(duration);
            mNotification = mBuilder.build();
            mNotification.flags = Notification.FLAG_ONGOING_EVENT;
            mNotificationManager.notify(100, mNotification);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (!isRun) {
            mAct.clearAllNotify();
        }
    }

    // @Override
    // public void onChronometerTick(Chronometer chronometer) {
    // long seconds = SystemClock.elapsedRealtime() - mBase;
    // seconds /= 1000;
    // String text = DateUtils.formatElapsedTime(mRecycle, seconds);
    // SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
    // mBuilder.setContentText(text);
    // mNotification = mBuilder.build();
    // mNotification.flags = Notification.FLAG_ONGOING_EVENT;
    // mNotificationManager.notify(100, mNotification);
    // Log.e("onChronometerTick------------>", "zhixing");
    // // 将当前时间显示在TextView组件中
    // }
}
