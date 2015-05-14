package com.src.playtime.thumb.message;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.src.playtime.thumb.BaseActivity;
import com.src.playtime.thumb.R;
import com.src.playtime.thumb.baseadapter.MultiItemTypeSupport;
import com.src.playtime.thumb.bean.ContactModel;
import com.src.playtime.thumb.bean.SmsModel;
import com.src.playtime.thumb.utils.BaseUtil;
import com.src.playtime.thumb.utils.MuJiMethod;
import com.src.playtime.thumb.widget.swipeback.SwipeBackActivity;

public class ChatMsgActivity extends SwipeBackActivity{

	@ViewInject(R.id.lv_chatmsg_list)
	private ListView mListView;

	@ViewInject(R.id.tv_chatmsg_name)
	private TextView mTvName;
	@ViewInject(R.id.tv_chatmsg_tel)
	private TextView mTvTel;
	@ViewInject(R.id.ed_chatmsg_msg)
	private EditText mEdMsg;
	@ViewInject(R.id.tv_chatmsg_send)
	private TextView mTvSend;
	@ViewInject(R.id.ll_chatmsg_header)
	private LinearLayout mLlHeader;

	private ContactModel mContactModel;

	private List<SmsModel> mData;

	private ChatMsgAdapter mAdapter;

	private String tel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chatmsg);
		ViewUtils.inject(mAct);
		init();
	}

	/**
	 * 初始化
	 */
	public void init() {
		mData = new ArrayList<SmsModel>();
		// mContactModel = (ContactModel)
		// getIntent().getSerializableExtra("data");
		tel = getIntent().getStringExtra("tel");
		String name = getIntent().getStringExtra("name");
		for (SmsModel mSmsModel : mApp.TempSmsDatas) {
			if (mSmsModel.getAddress() == null) {
				continue;
			}
			if (mSmsModel.getAddress().equals(tel)) {
				mSmsModel.setType("" + (Math.random() > 0.5 ? 1 : 0));
				mData.add(mSmsModel);
			}
		}

		// 设置短信名字
		mTvName.setText(name);
		// 如果短信的名字和号码是一样的 就隐藏号码
		if (name.equals(tel)) {
			mTvTel.setVisibility(View.GONE);
		}
		mTvTel.setText(tel);
		mAdapter = new ChatMsgAdapter(mAct, mData, mMultiItemTypeSupport);
		mListView.setAdapter(mAdapter);
        mListView.setOnItemLongClickListener(mOnItemLong);
		int totalHeight = 0;
		int listHeight = mApp.screenheight - mTvSend.getHeight()
				- mLlHeader.getHeight();
		// 计算所有item的高度
		for (int i = 0, len = mAdapter.getCount(); i < len; i++) {
			View listItem = mAdapter.getView(i, null, mListView);
			listItem.measure(0, 0); // 计算子项View 的宽高
			int list_child_item_height = listItem.getMeasuredHeight()
					+ mListView.getDividerHeight();
			totalHeight += list_child_item_height; // 统计所有子项的总高度
			// 如果所有子项总高度大于listview的高度 就用倒叙显示listview
			if (listHeight <= totalHeight) {
				mListView.setStackFromBottom(true);
				return;
			}
		}
	}

	@Override
	@OnClick({ R.id.tv_chatmsg_send, R.id.tv_chatmsg_call })
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.tv_chatmsg_send:
			if (mEdMsg.getText().toString().equals("")) {
				return;
			}
			SmsModel temp = new SmsModel();
			temp.setBody(mEdMsg.getText().toString());
			temp.setName(mTvName.getText().toString());
			temp.setAddress(mTvName.getText().toString());
			temp.setType("0");
            temp.setDate(System.currentTimeMillis()+"");
			mData.add(temp);
			mAdapter.refresh(mData, "");
			mEdMsg.setText("");
			mListView.setSelection(mAdapter.getCount());
			break;
		case R.id.tv_chatmsg_call:
			mApp.Muji.callOut(mAct, tel);
			break;
		}
	}

    AdapterView.OnItemLongClickListener mOnItemLong=new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
           for (int i = 0; i <mAdapter.getAllListChildCount() ; i++) {
                ViewGroup tempGroup=mAdapter.getAllListChildView().get(i);
                //temp.setVisibility(View.GONE);
//               ViewGroup temp=(ViewGroup)parent.getParent();
//               View v=temp.getChildAt(i);
//               if(temp==null){
//                   showToast("null");
//               }
//                ViewGroup g = (ViewGroup)temp.getParent();
               CheckBox mCkBox= (CheckBox) tempGroup.findViewById(R.id.cb_chatmsg);
               mCkBox.setVisibility(View.VISIBLE);
               mAdapter.setAllChildCheckBox(true);

            }
            return false;
        }
    };


    /**
     * 不同布局管理
     */
	MultiItemTypeSupport<SmsModel> mMultiItemTypeSupport = new MultiItemTypeSupport<SmsModel>() {

		@Override
		public int getLayoutId(int position, SmsModel t) {
			if (t.getType().equals("1")) {
				return R.layout.chatmsg_left;
			}
			return R.layout.chatmsg_right;
		}

		@Override
		public int getViewTypeCount() {
			// TODO Auto-generated method stub
			return 2;
		}

		@Override
		public int getItemViewType(int postion, SmsModel t) {

			return 0;
		}
	};

}
