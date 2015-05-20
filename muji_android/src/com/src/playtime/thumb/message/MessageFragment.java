package com.src.playtime.thumb.message;

import java.util.Collections;
import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.Toast;

import com.ab.util.AbViewUtil;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.src.playtime.thumb.BaseFragment;
import com.src.playtime.thumb.R;
import com.src.playtime.thumb.bean.ContactModel;
import com.src.playtime.thumb.bean.SmsModel;
import com.src.playtime.thumb.utils.PinyinSearch;

import org.litepal.crud.DataSupport;

public class MessageFragment extends BaseFragment implements TextWatcher,
		OnItemClickListener {

	// 初始化一个view视图
	private View view;

	private View mHeaderView;

	private TextView mHeaderTvClear;

	private EditText mHeaderEd;

	private MessageAdapter mAdapter;

	@ViewInject(R.id.lv_message_list)
	private SwipeMenuListView mListView;

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
        initSwipeMenu();
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
        mListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu,
                                           int index) {
                switch (index) {
                    case 0:
                        mApp.mSmsDatas.remove(position);
                        mAdapter.notifyDataSetChanged();
                        break;
                }
                return false;
            }
        });
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
