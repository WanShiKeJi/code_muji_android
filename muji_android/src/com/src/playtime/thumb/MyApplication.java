package com.src.playtime.thumb;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.litepal.LitePalApplication;
import org.litepal.crud.DataSupport;

import android.content.Context;
import android.view.WindowManager;

import com.src.playtime.thumb.bean.ContactModel;
import com.src.playtime.thumb.bean.SmsModel;
import com.src.playtime.thumb.utils.MuJiMethod;

public class MyApplication extends LitePalApplication {

	public List<ContactModel> mContactDatas;

	public List<ContactModel> mTelRecordDatas;

	public List<SmsModel> mSmsDatas;

	public List<SmsModel> TempSmsDatas;

	public MuJiMethod Muji;

	public int screenWidth, screenheight;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		init();
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

}
