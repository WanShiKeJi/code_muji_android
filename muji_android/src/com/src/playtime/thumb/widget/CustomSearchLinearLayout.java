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
 * @author wanfei 键盘布局帮助类
 */
public class CustomSearchLinearLayout implements OnClickListener, TextWatcher {

    private Context context;
    /**
     * popup布局
     */
    private View layout;
    /**
     * popup实例
     */
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
    /**
     * 符合条件的联系人数组
     */
    private List<ContactModel> contactList;
    /**
     * 总联系数组
     */
    private List<ContactModel> AllcontactList;
    /**
     * 适配器
     */
    private CAdapter<ContactModel> mAdapter;
    /**
     * 记录输入字符串的长度
     */
    private int mStrIdx = -1;
    /**
     * listview
     */
    private ListView mListView;

    private MyApplication mApp;

    private View mView;

    private String oldString;

    public CustomSearchLinearLayout getCustomView(Context context,
                                                  ViewGroup view) {
        this.context = context;
        this.mView = view;
        mApp = (MyApplication) context.getApplicationContext();
        init(view);
        return this;
    }

    public void init(ViewGroup view) {
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
    @OnClick({R.id.ibtn_key_0, R.id.ibtn_key_1, R.id.ibtn_key_2,
            R.id.ibtn_key_3, R.id.ibtn_key_4, R.id.ibtn_key_5, R.id.ibtn_key_6,
            R.id.ibtn_key_7, R.id.ibtn_key_8, R.id.ibtn_key_9, R.id.ibtn_key_l,
            R.id.ibtn_key_r, R.id.ibtn_key_del})
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

    @Override
    public void afterTextChanged(Editable arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void beforeTextChanged(CharSequence s, int arg1, int arg2,
                                  int arg3) {
        oldString = s.toString();

    }

    @Override
    public void onTextChanged(final CharSequence s, int arg1, int arg2, int arg3) {
        if (TextUtils.isEmpty(s)) {
            mAdapter.refresh(mApp.mTelRecordDatas, s.toString());
            if (mApp.mTelRecordDatas.isEmpty()) {
                mListView.setVisibility(View.GONE);
            }
        } else {
            List<ContactModel> temp;
            if (oldString.length() < s.toString().length()) {
                temp = PinyinSearch.FindRegularPinYin(s.toString(), mApp.mContactDatas, true);
            }else{
                temp = PinyinSearch.FindRegularPinYin(s.toString(), mApp.mContactDatas, false);
                }
            mAdapter.refresh(temp, et.getText().toString());
            mListView.setVisibility(View.VISIBLE);
            }

        }

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
}
