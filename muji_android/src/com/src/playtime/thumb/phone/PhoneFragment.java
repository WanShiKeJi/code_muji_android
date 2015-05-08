package com.src.playtime.thumb.phone;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.litepal.crud.DataSupport;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.ab.util.AbViewUtil;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.src.playtime.thumb.BaseFragment;
import com.src.playtime.thumb.R;
import com.src.playtime.thumb.bean.ContactModel;
import com.src.playtime.thumb.utils.BaseUtil;
import com.src.playtime.thumb.utils.PinyinComparator;
import com.src.playtime.thumb.widget.CustomSearchLinearLayout;
import com.tjerkw.slideexpandable.library.ExpandCollapseAnimation;
import com.tjerkw.slideexpandable.library.MSlidExpandableListAdapter;
import com.tjerkw.slideexpandable.library.SlideExpandableListAdapter;

/**
 * 
 * @author 万飞
 * 
 */
public class PhoneFragment extends BaseFragment {

	private View view;

	private CustomSearchLinearLayout mPopup;

	@ViewInject(R.id.lv_phone_list)
	private SwipeMenuListView mListView;

	private PhoneAdapter mAdapter;

	private MSlidExpandableListAdapter mSlideAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_phone, null);
		ViewUtils.inject(this, view);
		// 设置标题
		setBarTitle(view, "电话");
		initData();
		return view;
	}

	public void setPhoneFragmentPop(CustomSearchLinearLayout mPopup) {
		this.mPopup = mPopup;
	}

	/**
	 * 初始化listview列表
	 */
	public void initData() {
		mApp.mTelRecordDatas = DataSupport.findAll(ContactModel.class);
		Collections.reverse(mApp.mTelRecordDatas);
		mAdapter = new PhoneAdapter(mAct, mApp.mTelRecordDatas,
				R.layout.phone_list_item, null);
		mSlideAdapter = new MSlidExpandableListAdapter(mAdapter,
				R.id.phone_list_expand, R.id.expandable);
		// 对listview列表设置适配器
		mListView.setAdapter(mSlideAdapter);
		mPopup.setContactList(mApp.mContactDatas, mAdapter, mListView);
		if (!mApp.mTelRecordDatas.isEmpty()) {
			mListView.setVisibility(View.VISIBLE);
		}
		initSwipeMenu();
	}

	/**
	 * 初始化滑动删除
	 */
	public void initSwipeMenu() {
		SwipeMenuCreator creator = new SwipeMenuCreator() {
			@Override
			public void create(SwipeMenu menu) {
				// create "delete" item
				SwipeMenuItem deleteItem = new SwipeMenuItem(mAct);
				// set item background
				deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
						0x3F, 0x25)));
				// set item width
				deleteItem.setWidth(AbViewUtil.dip2px(mAct, 90));
				// set a icon
				deleteItem.setIcon(R.drawable.ic_delete);
				// add to menu
				menu.addMenuItem(deleteItem);
			}
		};

		// set creator
		mListView.setMenuCreator(creator);

		// step 2. listener item click event
		mListView.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(int position, SwipeMenu menu,
					int index) {
				switch (index) {
				case 0:
					Toast.makeText(mAct, "删除" + position, Toast.LENGTH_SHORT)
							.show();
					// 从数据库里删除该条数据
					mApp.mTelRecordDatas.get(position).delete();
					// 在从数据库里把数据取出
					mApp.mTelRecordDatas = DataSupport
							.findAll(ContactModel.class);
					Collections.reverse(mApp.mTelRecordDatas);
					// 把删除后的数据设置到适配器里
					mAdapter.refresh(mApp.mTelRecordDatas, "");
					break;
				}
				return false;
			}
		});

		/**
		 * 监听点击的item
		 */
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// 展开点击的item
				View mBt = view.findViewById(R.id.phone_list_expand);
				View target = view.findViewById(R.id.expandable);
				target.measure(parent.getWidth(), parent.getHeight());
				mSlideAdapter.checkExpandable(mBt, target, position);
				target.requestLayout();

			}
		});
	}

	@Override
	public void onResume() {
		super.onResume();
		mAdapter.refresh(mApp.mTelRecordDatas, "");
		if (!mApp.mTelRecordDatas.isEmpty()) {
			mListView.setVisibility(View.VISIBLE);
		}

	}

}
