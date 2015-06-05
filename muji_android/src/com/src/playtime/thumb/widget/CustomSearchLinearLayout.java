package com.src.playtime.thumb.widget;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ab.util.AbViewUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.src.playtime.thumb.MyApplication;
import com.src.playtime.thumb.R;
import com.src.playtime.thumb.baseadapter.CAdapter;
import com.src.playtime.thumb.bean.ContactModel;
import com.src.playtime.thumb.utils.BaseUtil;
import com.src.playtime.thumb.utils.PinyinSearch;
import com.waitingfy.callhelper.GetLocationByNumber;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;

/**
 * 
 * @author wanfei 键盘布局帮助类
 */
public class CustomSearchLinearLayout implements OnClickListener, TextWatcher {

	private Context context;
	/** popup布局 */
	private View layout;
	/** popup实例 */
	public PopupWindow mPopup;

	@ViewInject(R.id.ibtn_key_0)
	private ImageButton mKey0;
	@ViewInject(R.id.ibtn_key_1)
	private ImageButton mKey1;
	@ViewInject(R.id.ibtn_key_2)
	private ImageButton mKey2;
	@ViewInject(R.id.ibtn_key_3)
	private ImageButton mKey3;
	@ViewInject(R.id.ibtn_key_4)
	private ImageButton mKey4;
	@ViewInject(R.id.ibtn_key_5)
	private ImageButton mKey5;
	@ViewInject(R.id.ibtn_key_6)
	private ImageButton mKey6;
	@ViewInject(R.id.ibtn_key_7)
	private ImageButton mKey7;
	@ViewInject(R.id.ibtn_key_8)
	private ImageButton mKey8;
	@ViewInject(R.id.ibtn_key_9)
	private ImageButton mKey9;
	@ViewInject(R.id.ibtn_key_l)
	private ImageButton mKeyl;
	@ViewInject(R.id.ibtn_key_r)
	private ImageButton mKeyr;
	@ViewInject(R.id.edit_search)
	public EditText et;
	/** 符合条件的联系人数组 */
	private List<ContactModel> contactList;
	/** 总联系数组 */
	private List<ContactModel> AllcontactList;
	/** 适配器 */
	private CAdapter<ContactModel> mAdapter;
	/** 记录输入字符串的长度 */
	private int mStrIdx = -1;
	/** listview */
	private ListView mListView;

	private MyApplication mApp;

	private View mView;

    private String oldString,newString;

	public CustomSearchLinearLayout getCustomView(Context context,
			ViewGroup view) {
		this.context = context;
		this.mView = view;
		mApp = (MyApplication) context.getApplicationContext();
		init(view);
		return this;
	}

	public void init(ViewGroup view) {
		// 用布局加载器加载一个布局
		// View layout = LayoutInflater.from(context).inflate(
		// R.layout.keyboard_popup, null);
		// // 初始化一个popupwindow
		// mPopup = new PopupWindow(layout);
		// // 控制popupwindow的宽度和高度
		// mPopup.setWidth(LayoutParams.MATCH_PARENT);
		// mPopup.setHeight(AbViewUtil.dip2px(context, 300));
		// mPopup.setAnimationStyle(R.style.popwin_anim_style);
		ViewUtils.inject(this, view);
		contactList = new ArrayList<ContactModel>();
		AllcontactList = new ArrayList<ContactModel>();
		et.addTextChangedListener(this);
        et.setInputType(InputType.TYPE_NULL);
	}

	/**
	 * 键盘带动画显示
	 */
	public void setVisibility() {
		mView.setAnimation(AnimationUtils.loadAnimation(context,
				R.anim.abc_slide_in_bottom));
		mView.setVisibility(View.VISIBLE);
	}

	/**
	 * 键盘带动画隐藏
	 */
	public void setGoneVisibility() {
		mView.setAnimation(AnimationUtils.loadAnimation(context,
				R.anim.abc_slide_out_bottom));
		mView.setVisibility(View.GONE);
	}

	@Override
	@OnClick({ R.id.ibtn_key_0, R.id.ibtn_key_1, R.id.ibtn_key_2,
			R.id.ibtn_key_3, R.id.ibtn_key_4, R.id.ibtn_key_5, R.id.ibtn_key_6,
			R.id.ibtn_key_7, R.id.ibtn_key_8, R.id.ibtn_key_9, R.id.ibtn_key_l,
			R.id.ibtn_key_r, R.id.ibtn_key_del })
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ibtn_key_0:
			et.getText().insert(et.getSelectionEnd(), "0");
			break;
		case R.id.ibtn_key_1:
			et.getText().insert(et.getSelectionEnd(), "1");
			break;
		case R.id.ibtn_key_2:
			et.getText().insert(et.getSelectionEnd(), "2");
			break;
		case R.id.ibtn_key_3:
			et.getText().insert(et.getSelectionEnd(), "3");
			break;
		case R.id.ibtn_key_4:
			et.getText().insert(et.getSelectionEnd(), "4");
			break;
		case R.id.ibtn_key_5:
			et.getText().insert(et.getSelectionEnd(), "5");
			break;
		case R.id.ibtn_key_6:
			et.getText().insert(et.getSelectionEnd(), "6");
			break;
		case R.id.ibtn_key_7:
			et.getText().insert(et.getSelectionEnd(), "7");
			break;
		case R.id.ibtn_key_8:
			et.getText().insert(et.getSelectionEnd(), "8");
			break;
		case R.id.ibtn_key_9:
			et.getText().insert(et.getSelectionEnd(), "9");
			break;
		case R.id.ibtn_key_l:
			et.getText().insert(et.getSelectionEnd(), "*");
			break;
		case R.id.ibtn_key_r:
			et.getText().insert(et.getSelectionEnd(), "#");
			break;
		case R.id.ibtn_key_del: // 删除输入框内容
			int length = et.getSelectionEnd();
			if (length > 0) {
				et.getText().delete(length - 1, length);
			}
		}
	}

	// public void dismiss() {
	// mPopup.dismiss();
	// }

	// public int getHeight() {
	// return mPopup.getHeight();
	// }

	// public void showAtLocation(View parent, int gravity, int x, int y) {
	// mPopup.showAtLocation(parent, gravity, x, y);
	// }

	@Override
	public void afterTextChanged(Editable arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforeTextChanged(CharSequence s, int arg1, int arg2,
			int arg3) {
		    oldString=s.toString();

	}

	@Override
	public void onTextChanged(final CharSequence s, int arg1, int arg2, int arg3) {
		if (TextUtils.isEmpty(s)) {
			// Collections.reverse(mApp.mTelRecordDatas);
           // PinyinSearch.mRegularArray.clear();
			mAdapter.refresh(mApp.mTelRecordDatas, s.toString());
			if (mApp.mTelRecordDatas.isEmpty()) {
				mListView.setVisibility(View.GONE);
			}
		} else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    List<ContactModel> temp;
                    if(oldString.length()<s.toString().length()){
                     temp=PinyinSearch.FindRegularPinYin(s.toString(),mApp.mContactDatas,true);
                    }else{
                     temp=PinyinSearch.FindRegularPinYin(s.toString(),mApp.mContactDatas,false);
                     Log.e("customSearch======>","====>false");
                    }

                    Message msg=handler.obtainMessage();
                    msg.what=0;
                    msg.obj=temp;
                    handler.sendMessage(msg);
                }
            }).start();
           // mAdapter.refresh(temp, s.toString());
//			List<ContactModel> temp = PinyinSearch.FindPinyin(s.toString(),
//					AllcontactList, true);
//			if (temp.isEmpty()) {
//				ContactModel model = new ContactModel();
//				model.setName(s.toString());
//				model.setTelnum("");
//                 if (s.length()>=7){
//                     String tempstr=s.toString().substring(0,7).trim();
//                    model.setOperators(BaseUtil.getOperator(tempstr));
//                    model.setAttribution(GetLocationByNumber.getCallerInfo(tempstr, context));
//                }
//				temp.add(model);
//			}
//			mAdapter.refresh(temp, s.toString());
//			mListView.setVisibility(View.VISIBLE);
		}

	}

    android.os.Handler handler=new android.os.Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mAdapter.refresh((List<ContactModel>)msg.obj,et.getText().toString());
            mListView.setVisibility(View.VISIBLE);
        }
    };


	/**
	 * 设置查询所要用到的参数
	 * 
	 * @param contactList
	 *            查询的数据
	 * @param mAdapter
	 *            适配器
	 * @param listview
	 *            listview列表
	 */
	public void setContactList(List<ContactModel> contactList,
			CAdapter<ContactModel> mAdapter, ListView listview) {
		this.AllcontactList = contactList;
		this.mAdapter = mAdapter;
		this.mListView = listview;
	}

	/**
	 * 按号码-拼音搜索联系人
	 * 
	 * @param str
	 */
	public void search(String str) {
		contactList.clear();
		// 如果搜索条件以0 1 +开头则按号码搜索
		if (str.toString().startsWith("0") || str.toString().startsWith("1")
				|| str.toString().startsWith("+")) {
			for (ContactModel model : AllcontactList) {
				if (model.getTelnum().contains(str)) {
					model.setGroup(str);
					contactList.add(model);
				}
			}
			mAdapter.refresh(contactList, str);
			// adapter.refresh(contactList, false);
			return;
		}
		StringBuffer sb = new StringBuffer();
		// 记录字符串长度
		mStrIdx = str.length();
		// 获取每一个数字对应的字母列表并以'-'隔开
		for (int i = 0; i < str.length(); i++) {
			sb.append((str.charAt(i) <= '9' && str.charAt(i) >= '0') ? BaseUtil.STRS[str
					.charAt(i) - '0'] : str.charAt(i));
			if (i != str.length() - 1) {
				sb.append("-");
			}
		}

		for (ContactModel model : AllcontactList) {
			if (contains(sb.toString(), model, str)) {
				contactList.add(model);
			} else if (model.getTelnum().contains(str)) {
				model.group = str;
				contactList.add(model);
			}
		}
		mAdapter.refresh(contactList, str);
	}

	/**
	 * 根据拼音搜索
	 * 
	 * @param str
	 *            正则表达式

	 * @return
	 */
	public boolean contains(String str, ContactModel model, String search) {
		if (TextUtils.isEmpty(model.getTelnum())) {
			return false;
		}
		model.group = "";
		// 搜索条件大于6个字符将不按拼音首字母查询
		if (search.length() < 6) {
			/**
			 * 根据首字母进行模糊查询（把对应的按键字母格式转化为[adc]） 例如：2-->[adc] 3-->[def]
			 */
			Pattern pattern = Pattern.compile("^"
					+ str.toUpperCase().replace("-", "[*+#a-z]*"));
			Matcher matcher = pattern.matcher(model.getPyname());
			Log.e("=============>", str.toUpperCase().replace("-", "[*+#a-z]*"));
			if (matcher.find()) {
				String tempStr = matcher.group();
				for (int i = 0; i < tempStr.length(); i++) {
					if (tempStr.charAt(i) >= 'A' && tempStr.charAt(i) <= 'Z') {
						model.group += tempStr.charAt(i);
					}
				}
				return true;
			}

		}
		String s = searchPinYin(str, model.getPyname(), model);
		if (!s.equals("") && s.charAt(0) >= 'A' && s.charAt(0) <= 'Z')
			return true;
		else
			return false;
	}

	/**
	 * 根据全拼搜索
	 * 
	 * @param str
	 * @param pyname
	 * @param model
	 * @return
	 */
	public String searchPinYin(String str, String pyname, ContactModel model) {
		// （Pattern.CASE_INSENSITIVE 对大小写进行不敏感搜索）
		String s[] = str.replace("-", " ").split(" ");
		s[0] = s[0].toUpperCase();
		String comstr = "";
		StringBuffer sb = new StringBuffer();
		int idx = -1;
		for (int i = 0; i < s.length; i++) {
			comstr += s[i];
		}
		Pattern pattern = Pattern.compile(str.replace("-", "").toUpperCase());
		Matcher matcher = pattern.matcher(pyname.replaceAll("[a-z]", ""));
		boolean flag = matcher.find();
		if (flag) {
			model.group += matcher.group();
			return model.group;
		} else if (s.length > 1) {
			comstr = "";
			for (int i = 1; i < s.length; i++) {
				s[i] = s[i].toLowerCase();
			}
			for (int j = 0; j < s.length; j++) {
				if (j > 0)
					comstr += "[*+#a-z]*" + s[j];
				else
					comstr += s[j];
			}
			Pattern pattern1 = Pattern
					.compile(comstr, Pattern.CASE_INSENSITIVE);
			Matcher matcher1 = pattern1.matcher(pyname);
			flag = matcher1.find();
			if (flag) {
				model.group += matcher1.group();
				if (!(model.group.charAt(0) >= 'A' && model.group.charAt(0) <= 'Z')) {
					model.group = "";
					return model.group;
				} else {
					for (int i = 0; i < s.length; i++) {
						Pattern pattern2 = Pattern.compile(s[i],
								Pattern.CASE_INSENSITIVE);
						Matcher matcher2 = pattern2.matcher(model.group);
						flag = matcher2.find(idx == -1 ? idx + 1 : idx + 1);
						if (idx == -1) {
							idx = matcher2.start();
						}
						if (flag) {
							String d = matcher2.group();
							String tempstr = "";
							if (matcher2.start() - idx == 1
									|| matcher2.start() - idx == 0) {
								sb.append(d);
							} else if (d.charAt(0) >= 'A' && d.charAt(0) <= 'Z') {
								String tempa = sb.substring(sb.length() - 1,
										sb.length());
								if (sb.length() >= 1 && tempa.charAt(0) >= 'a'
										&& tempa.charAt(0) <= 'z') {
									tempstr = tempa + d;
									if (model.group.contains(tempstr))
										sb.append(d);
									else
										return model.group = "";
								} else
									sb.append(d);
							} else {
								return model.group = "";
							}
							idx = matcher2.start();
						}
					}

					return model.group = sb.toString();
				}

			}
			return model.group;
		}

		return model.group;
	}
}
