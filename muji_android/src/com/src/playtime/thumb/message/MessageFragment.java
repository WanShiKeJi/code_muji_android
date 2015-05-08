package com.src.playtime.thumb.message;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.src.playtime.thumb.BaseFragment;
import com.src.playtime.thumb.R;
import com.src.playtime.thumb.bean.ContactModel;
import com.src.playtime.thumb.bean.SmsModel;
import com.src.playtime.thumb.utils.PinyinSearch;

public class MessageFragment extends BaseFragment implements TextWatcher,
		OnItemClickListener {

	// 初始化一个view视图
	private View view;

	private View mHeaderView;

	private TextView mHeaderTvClear;

	private EditText mHeaderEd;

	private MessageAdapter mAdapter;

	@ViewInject(R.id.lv_message_list)
	private ListView mListView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_message, null);
		ViewUtils.inject(this, view);
		setBarTitle(view, "短信", R.drawable.add_message);
		init();
		return view;
	}

	public void init() {
		mAdapter = new MessageAdapter(mAct, mApp.mSmsDatas,
				R.layout.message_list_item, null);
		mListView.setAdapter(mAdapter);
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
		mListView.addHeaderView(mHeaderView);
		mListView.setOnItemClickListener(this);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.search_header_tv:
			mHeaderEd.setText("");
			break;
		case R.id.header_right:
			startActivity(new Intent(mAct,AddMsgActivity.class));
			break;
		}

	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		if (TextUtils.isEmpty(s)) {
			mAdapter.refresh(mApp.mSmsDatas, "");
			mHeaderTvClear.setVisibility(View.GONE);
		} else {
			mHeaderTvClear.setVisibility(View.VISIBLE);
			// search(s.toString());
			List<SmsModel> temp = PinyinSearch.FindSms(s.toString(),
					mApp.mSmsDatas);
			mAdapter.refresh(temp, "");
		}

	}

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = new Intent(mAct, ChatMsgActivity.class);
		intent.putExtra("tel", mApp.mSmsDatas.get(position - 1).getAddress());
		intent.putExtra("name", mApp.mSmsDatas.get(position - 1).getName());
		startActivity(intent);
	}

}
