package com.src.playtime.thumb;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.litepal.LitePalApplication;
import org.litepal.crud.DataSupport;

import android.app.Activity;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.WindowManager;

import com.avos.avoscloud.AVOSCloud;
import com.src.playtime.thumb.bean.ContactModel;
import com.src.playtime.thumb.bean.SmsModel;
import com.src.playtime.thumb.utils.MuJiMethod;

public class MyApplication extends LitePalApplication {

    /**联系人数据*/
	public List<ContactModel> mContactDatas;
    /**拨打号码记录*/
	public List<ContactModel> mTelRecordDatas;
    /**短信数据*/
	public List<SmsModel> mSmsDatas;
    /**临时短信数据*/
	public List<SmsModel> TempSmsDatas;

	public MuJiMethod Muji;
    /**屏幕高和宽*/
	public int screenWidth, screenheight;

    //各种运营商呼转前缀
    public List<String[]> mSimPrefix;
    //当前手机运营商呼叫转移的前缀
    public String[] mStrPrefix=new String[2];
    //leancloud配置信息
    String AppID="85m0pvb0vv1iluti5sk0xsou1mkftzn06a3f1ompvza9xc7z";
    String AppKey="orluh89ufnpvl773b68w5gcdk4dxfrahzwaahz7c46ettn44";


	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
        AVOSCloud.initialize(this, AppID, AppKey);
		init();
        SimOperator();
	}

	public void init() {
		mContactDatas = new ArrayList<ContactModel>();
		mTelRecordDatas = new ArrayList<ContactModel>();
		mSmsDatas = new ArrayList<SmsModel>();
		TempSmsDatas = new ArrayList<SmsModel>();
		Muji = MuJiMethod.getInstance(this);
		WindowManager wm = (WindowManager) getContext().getSystemService(
				Context.WINDOW_SERVICE);
		screenWidth = wm.getDefaultDisplay().getWidth();
		screenheight = wm.getDefaultDisplay().getHeight();
	}

    /**
     * 获得各大运营商呼叫转移的前缀
     */
    public void SimOperator(){
        TelephonyManager telManager = (TelephonyManager) getSystemService(Activity.TELEPHONY_SERVICE);
        int simIdx = telManager.SIM_STATE_ABSENT;
        mSimPrefix=new ArrayList<String[]>();
        mSimPrefix.add(new String[]{"**21*","##21#"});
        mSimPrefix.add(new String[]{"**21*","##21#"});
        mSimPrefix.add(new String[]{"*72","*720"});
        //当前没有sim卡或者sim卡不可用
//        if (1 == simIdx) {
//            simIdx=-1;
//            Log.e("---------->","=======>");
//            return;
//        }
        String operator = telManager.getSimOperator();
        Log.e("operator","------>"+operator);
        if(operator!=null)
        {
            //中国移动
            if(operator.equals("46000") || operator.equals("46002")||operator.equals("46007"))
            {
                mStrPrefix=mSimPrefix.get(0);
                Log.e("","");
            }else
                //中国联通
                if(operator.equals("46001")){
                    mStrPrefix=mSimPrefix.get(1);
                }else
                    //中国电信
                    if(operator.equals("46003")){
                        mStrPrefix=mSimPrefix.get(2);
                    }
        }

    }

}
