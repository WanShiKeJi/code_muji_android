package com.src.playtime.thumb;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.PhoneLookup;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SlidingDrawer;

import com.ab.util.AbViewUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.src.playtime.thumb.baseadapter.FragmentTabAdapter;
import com.src.playtime.thumb.bean.ContactModel;
import com.src.playtime.thumb.bean.SmsModel;
import com.src.playtime.thumb.contacts.ContactsFragment;
import com.src.playtime.thumb.discover.DiscoverFragment;
import com.src.playtime.thumb.message.MessageFragment;
import com.src.playtime.thumb.phone.PhoneFragment;
import com.src.playtime.thumb.utils.BaseUtil;
import com.src.playtime.thumb.utils.MuJiMethod;
import com.src.playtime.thumb.utils.PinyinComparator;
import com.src.playtime.thumb.widget.CustomSearchLinearLayout;

public class MainActivity extends BaseActivity {

	@ViewInject(R.id.button1)
	private RadioButton mRb1;

	@ViewInject(R.id.button2)
	private RadioButton mRb2;

	@ViewInject(R.id.button3)
	private RadioButton mRb3;

	@ViewInject(R.id.button4)
	private RadioButton mRb4;

	@ViewInject(R.id.rb_call)
	private RadioButton mRbCall;

	@ViewInject(R.id.rgp)
	private RadioGroup mRg;

	@ViewInject(R.id.ll_keyboard)
	private LinearLayout mKeyboard;

	@ViewInject(R.id.sd_keyboard)
	private LinearLayout mSlidKeyboard;

	private CustomSearchLinearLayout mSearchView;

	private List<Fragment> mFragmentList = new ArrayList<Fragment>();
	private RadioButton mRbList[] = new RadioButton[4];
	// 电话的fragment
	private PhoneFragment mPhone;
	// 通讯录的fragment
	private ContactsFragment mContacts;
	// 短信的fragment
	private MessageFragment mMessage;
	// 发现的fragment
	private DiscoverFragment mDiscover;

	// 从通讯录返回的值
	public static final int ContactsIdx = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		ViewUtils.inject(this);
		initPopupwindow();
		InitFragmentView();
		ReadContactsData();
        //Log.e("byte--------->","---"+(byte)Integer.parseInt("12",16));
        MuJiMethod.getInstance(mAct).sendBTData("13058121800",(byte)0x00);
//       new Thread(new Runnable() {
//            @Override
//            public void run() {
       // getSmsFromPhone();
//           }
//       }).start();


	}

	public void InitFragmentView() {
		mRbList[0] = mRb1;
		mRbList[1] = mRb2;
		mRbList[2] = mRb3;
		mRbList[3] = mRb4;
		// 初始化一个电话界面
		mPhone = new PhoneFragment();
		// 初始化一个通讯录界面
		mContacts = new ContactsFragment();
		// 初始化一个短信界面
		mMessage = new MessageFragment();
		// 初始化一个发现界面
		mDiscover = new DiscoverFragment();
		// 把键盘弹出界面的实例设置到电话界面里
		mPhone.setPhoneFragmentPop(mSearchView);
        mPhone.setDisconverRadioButton(mRbList);
		mFragmentList.add(mPhone);
		mFragmentList.add(mMessage);
		mFragmentList.add(mContacts);
		mFragmentList.add(mDiscover);
		FragmentTabAdapter mTabAdapter = new FragmentTabAdapter(mAct,
				mFragmentList, R.id.tab_frame, mRg, mRbList);
	}

	@Override
	@OnClick({ R.id.button1, R.id.button2, R.id.button3, R.id.button4,
			R.id.rb_call })
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.button1:
			isCheckedState();
			break;
		case R.id.rb_call:
			callOutTel();
			break;
		default:
			// 按了其它任何按钮就把按钮一的背景图片设为手机背景图，并把标记值设为false
			setDrawableBackground(R.drawable.phone);
			mRb1.setText("电话");
			// 并去除键盘布局
			mSlidKeyboard.setVisibility(View.GONE);
			// 隐藏呼叫按钮
			mRbCall.setVisibility(View.GONE);
			// 显示按钮二三
			mRb2.setVisibility(View.VISIBLE);
			mRb3.setVisibility(View.VISIBLE);
			break;
		}
	}

	/**
	 * 拨出号码
	 */
	public void callOutTel() {
		if (!mSearchView.et.getText().equals("")
				&& mSearchView.et.getText().length() >= 3) {
			mApp.Muji.callOut(mAct, mSearchView.et.getText().toString());
		} else {
			showToast("号码过短,请重新输入!");
		}
	}

	/**
	 * 初始化popupwindow
	 */
	public void initPopupwindow() {
		// 从popup帮助类中得到popup的实例
		mSearchView = new CustomSearchLinearLayout().getCustomView(mAct,
				mSlidKeyboard);
	}

	/**
	 * 根据按钮一的状态来显示按钮
	 */
	public void isCheckedState() {
		if (mRb1.getText().toString().equals("电话")) {
			mRb1.setText("展开");
		}
		// 获得mRb1按钮的位置
		int[] location = new int[2];
		mRb1.getLocationOnScreen(location);
		// 如果按钮是选中状态
		if (mRb1.isChecked()) {
			// 这里我给按钮设定了一个标记值，如果标记值为false
			if (mRb1.getText().toString().equals("展开")) {
				// 则设置背景图片为收起状态
				setDrawableBackground(R.drawable.collapse);
				// 并把这个值标记为true
				mRb1.setText("收起");
				// 去除键盘布局 如果键盘布局当前为显示状态
				if (mSlidKeyboard.getVisibility() == View.VISIBLE) {
					mSearchView.setGoneVisibility();
				}
				// 隐藏呼叫按钮
				mRbCall.setVisibility(View.GONE);
				// 显示按钮二三
				mRb2.setVisibility(View.VISIBLE);
				mRb3.setVisibility(View.VISIBLE);
			} else
			// 如果按钮标记值为true
			if (mRb1.getText().toString().equals("收起")) {
				// 隐藏按钮三和按钮二
				mRb2.setVisibility(View.GONE);
				mRb3.setVisibility(View.GONE);
				// 显示呼叫按钮
				mRbCall.setVisibility(View.VISIBLE);
				// 则设置背景图片为展开状态
				setDrawableBackground(R.drawable.collapse_selected);
				// 并把按钮标记值设为false
				mRb1.setText("展开");
				// 显示一个带动画的键盘布局
				mSearchView.setVisibility();

			}
		}
	}

	@SuppressLint("NewApi")
	public void setDrawableBackground(int resid) {
		// 获得Drawble图片
		Drawable drawable = getResources().getDrawable(resid);
		// 这一步必须要做,否则不会显示.
		// mRb1.setBackground(drawable);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(),
				drawable.getMinimumHeight());
		mRb1.setCompoundDrawables(null, drawable, null, null);
	}

	/**
	 * 读取系统联系人信息
	 */
	public void ReadContactsData() {
		List<ContactModel> mDatas = new ArrayList<ContactModel>();
		Cursor cursor = mAct.getContentResolver().query(Phone.CONTENT_URI,
				BaseUtil.PHONES_PROJECTION, null, null, null);
		ContactModel m = null;
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			String name = cursor.getString(0);
			m = new ContactModel();
			String pyname = BaseUtil.getPingYin(name.toUpperCase());
			m.setName(name);
			m.setTelnum(cursor.getString(1).replace(" ", "").replace("-", ""));
			m.setContactId(cursor.getString(3));
			// 只要不是大小写字母就归为#分类下
			if (!(pyname.charAt(0) >= 'A' && pyname.charAt(0) <= 'Z' || pyname
					.charAt(0) >= 'a' && pyname.charAt(0) <= 'z')) {
				m.setPyname("#" + pyname);
			} else {
				m.setPyname(pyname);
			}
            m.setPynameList(BaseUtil.getPinYinNum(pyname));
			mDatas.add(m);
		}
		cursor.close();
		mApp.mContactDatas.addAll(PinyinComparator.sort(mDatas));
	}

	/**
	 * 获取短信内容
	 */
	public void getSmsFromPhone() {
		ContentResolver cr = getContentResolver();
		String[] projection = getResources().getStringArray(R.array.sms_key);
		Cursor cur = cr.query(Uri.parse("content://sms/"), projection, null,
				null, "date desc");
		if (null == cur)
			return;
		while (cur.moveToNext()) {
			SmsModel SmsModel = new SmsModel();
			String number = cur.getString(cur.getColumnIndex("address"));//
			// 手机号
			SmsModel.setAddress(number);
			String name = getContactNameByAddr(number);//
			SmsModel.setName(name);
			// 联系人姓名列表
			SmsModel.setBody(cur.getString(cur.getColumnIndex("body")));
			String date = cur.getString(cur.getColumnIndex("date"));
			SmsModel.setDate(date);
			// SmsModel.save();
			// Pattern pattern = Pattern.compile(" [a-zA-Z0-9]{10}");
			// Matcher matcher = pattern.matcher(body);
			// if (matcher.find()) {
			// String res = matcher.group().substring(1, 11);
			// Log.e("res-------------->", res);
			// }
			int idx = -1;
			if (mApp.mSmsDatas.size() > 0) {
				for (int i = 0; i < mApp.mSmsDatas.size(); i++) {
					if (mApp.mSmsDatas.get(i).getName() == null)
						continue;
					if (mApp.mSmsDatas.get(i).getName()
							.equals(SmsModel.getName())) {
						idx++;
					}
				}
				if (idx == -1) {
					mApp.mSmsDatas.add(SmsModel);
				}
			} else {
				mApp.mSmsDatas.add(SmsModel);
			}
			mApp.TempSmsDatas.add(SmsModel);

		}

        Collections.sort(mApp.TempSmsDatas, new Comparator<SmsModel>() {
            @Override
            public int compare(SmsModel lhs, SmsModel rhs) {
                long log1 = Long.valueOf(lhs.getDate().substring(0, 10));
                long log2 = Long.valueOf(rhs.getDate().substring(0, 10));
                if (log1 - log2 >= 0) {
                    return 1;
                }
                return -1;
            }
        });
	}

	/**
	 * 根据联系人地址匹配联系人姓名
	 * 
	 * @param phoneNumber
	 *            联系人地址
	 * @return
	 */
	public String getContactNameByAddr(String phoneNumber) {
		Uri personUri = Uri.withAppendedPath(
				ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
				Uri.encode(phoneNumber));
		Cursor cur = mAct.getContentResolver().query(personUri,
				new String[] { PhoneLookup.DISPLAY_NAME }, null, null, null);
		if (cur.moveToFirst()) {
			int nameIdx = cur.getColumnIndex(PhoneLookup.DISPLAY_NAME);
			String name = cur.getString(nameIdx);
			cur.close();
			return name;
		}
		cur.close();
		return phoneNumber;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case ContactsIdx:
			mContacts.onActivityResult(requestCode, resultCode, data);
			break;
		}

	}
}
