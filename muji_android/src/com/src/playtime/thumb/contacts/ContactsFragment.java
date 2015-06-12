package com.src.playtime.thumb.contacts;

import java.util.ArrayList;
import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.src.playtime.thumb.BaseFragment;
import com.src.playtime.thumb.R;
import com.src.playtime.thumb.bean.ContactModel;
import com.src.playtime.thumb.phone.PhoneAdapter;
import com.src.playtime.thumb.utils.BaseUtil;
import com.src.playtime.thumb.utils.PinyinComparator;
import com.src.playtime.thumb.utils.PinyinSearch;
import com.tjerkw.slideexpandable.library.SlidExpandManage;
import com.tjerkw.slideexpandable.library.SlideExpandableListAdapter;

public class ContactsFragment extends BaseFragment implements TextWatcher,
		OnItemClickListener {

	private View view;

	private int UPDATE_CONTACTS = 0;

	private ContactsAdapter mAdapter;

	private SlideExpandableListAdapter mSlidAdapter;

	private ContactsSlidExpandManage mContactsManage;

	@ViewInject(R.id.lv_contact_list)
	private StickyListHeadersListView stickyList;

	private View mHeaderView;

	private TextView mHeaderTvClear;

	private EditText mHeaderEd;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_contacts, null);
		ViewUtils.inject(this, view);
		// 设置标题
		setBarTitle(view, "通讯录", R.drawable.contact_add);
		init();
		return view;
	}

	public void init() {
		// 加载list头部view
		mHeaderView = LayoutInflater.from(mAct).inflate(
				R.layout.list_search_header, null);
		// 初始化头部view的button按钮
		mHeaderTvClear = (TextView) mHeaderView
				.findViewById(R.id.search_header_tv);
		mHeaderTvClear.setOnClickListener(this);
		// 初始化头部view的edit
		mHeaderEd = (EditText) mHeaderView
				.findViewById(R.id.search_header_edit);
		mHeaderEd.addTextChangedListener(this);
		// 初始化一个侧边检索点击item滑动的类
		mContactsManage = new ContactsSlidExpandManage(mAct);
		mAdapter = new ContactsAdapter(mAct, mApp.mContactDatas,
				R.layout.contacts_list_item, null);
		mSlidAdapter = new SlideExpandableListAdapter(mAdapter,
				R.id.expandable_toggle_button, R.id.expandable, mContactsManage);
		mAdapter.setIndexScroller(stickyList.getIndexScroller());
		stickyList.addHeaderView(mHeaderView);
		// stickyList.addFooterView(LayoutInflater.from(mAct).inflate(
		// R.layout.list_footer, null));
		// stickyList.setEmptyView(findViewById(R.id.empty));
		stickyList.setDrawingListUnderStickyHeader(true);
		stickyList.setAreHeadersSticky(true);
		stickyList.setAdapter(mSlidAdapter);
		stickyList.setFastScrollEnabled(true);
		stickyList.setOnItemClickListener(this);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.header_right:
			Intent intent = new Intent(Intent.ACTION_INSERT);
			intent.setType("vnd.android.cursor.dir/person");
			intent.setType("vnd.android.cursor.dir/contact");
			intent.setType("vnd.android.cursor.dir/raw_contact");
			startActivityForResult(intent, UPDATE_CONTACTS);
			break;
		case R.id.search_header_tv:
			mHeaderEd.setText("");
			break;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==UPDATE_CONTACTS){


		List<ContactModel> mContactDatas = new ArrayList<ContactModel>();
		// 读取系统数据库里的联系人信息
		Cursor cursor = mAct.getContentResolver().query(Phone.CONTENT_URI,
				BaseUtil.PHONES_PROJECTION, null, null, null);
		ContactModel m = null;
		// 查找出的信息逐个添加到list容器里
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			String name = cursor.getString(0);
			String pyname = BaseUtil.getPingYin(name);
			m = new ContactModel();
			m.setName(name);
			m.setTelnum(cursor.getString(1).replace(" ", "").replace("-", ""));
			m.setContactId(cursor.getString(3));
			if (!(pyname.charAt(0) >= 'A' && pyname.charAt(0) <= 'Z' || pyname
					.charAt(0) >= 'a' && pyname.charAt(0) <= 'z')) {
				m.setPyname("#" + pyname);
			} else {
				m.setPyname(pyname);
			}
            m.setPynameList(BaseUtil.getPinYinNum(name));
			mContactDatas.add(m);
		}
		cursor.close();
		mApp.mContactDatas.clear();
		mApp.mContactDatas.addAll(PinyinComparator.sort(mContactDatas));
		// 刷新数据
		mAdapter.refresh(mApp.mContactDatas, "");
        }
	}

	@Override
	public void onResume() {
		super.onResume();
		//this.onActivityResult(0, 0, null);
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		if (TextUtils.isEmpty(s)) {
			mAdapter.refresh(mApp.mContactDatas, s.toString());
			mHeaderTvClear.setVisibility(View.GONE);
            mContactsManage.refreshHeaderCharData(mApp.mContactDatas);
		} else {
			mHeaderTvClear.setVisibility(View.VISIBLE);
			// search(s.toString());
			List<ContactModel> temp = PinyinSearch.FindPinyin(s.toString(),
					mApp.mContactDatas, false);
			mAdapter.refresh(temp, "");
            mContactsManage.refreshHeaderCharData(temp);
		}

	}

	@Override
	public void afterTextChanged(Editable s) {

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// 展开点击的item
		View mBt = view.findViewById(R.id.phone_list_expand);
		View target = view.findViewById(R.id.expandable);
		target.measure(parent.getWidth(), parent.getHeight());
		mSlidAdapter.checkExpandable(mBt, target, position);
		target.requestLayout();
	}

}
