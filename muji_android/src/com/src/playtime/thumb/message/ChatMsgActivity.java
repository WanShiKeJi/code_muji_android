package com.src.playtime.thumb.message;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
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

    //listview列表
	@ViewInject(R.id.lv_chatmsg_list)
	private ListView mListView;
    //联系人姓名
	@ViewInject(R.id.tv_chatmsg_name)
	private TextView mTvName;
    //联系人电话
	@ViewInject(R.id.tv_chatmsg_tel)
	private TextView mTvTel;
    //输入框信息
	@ViewInject(R.id.ed_chatmsg_msg)
	private EditText mEdMsg;
    //发送按钮
	@ViewInject(R.id.tv_chatmsg_send)
	private TextView mTvSend;
    //头部联系人信息等布局
	@ViewInject(R.id.ll_chatmsg_header)
	private LinearLayout mLlHeader;
    //头部全选等布局
    @ViewInject(R.id.rl_chatmsg_header)
    private RelativeLayout mRlHeader;
    //底部输入框布局
    @ViewInject(R.id.ll_chatmsg_bottom)
    private LinearLayout mLlBottom;
    //底部复制删除等布局
    @ViewInject(R.id.ll_chatmsg_bottoms)
    private LinearLayout mLlBottoms;
    //头部选择短信项
    @ViewInject(R.id.tv_chatmsg_selectoridx)
    private TextView mTvSelectorTitel;
    //底部复制按钮
    @ViewInject(R.id.tv_chatmsg_copy)
    private TextView mTvCopy;
    @ViewInject(R.id.tv_chatmsg_share)
    private  TextView mTvShare;
    @ViewInject(R.id.tv_chatmsg_delet)
    private  TextView mTvDelete;
    //复制粘贴管理
    ClipboardManager clip;
    //每条短信布局check的按钮
    private List<CheckBox> mListCheck;
    //是否执行返回键
    private boolean isKeyDown=true;
    //底部三个按钮的容器
    private TextView[] mTvArray;


	private ContactModel mContactModel;

	private List<SmsModel> mData;

	private ChatMsgAdapter mAdapter;

	private String tel;
    //如果有关键字,则用下标定位到那一行
    private int selection=-1;

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
        mListCheck=new ArrayList<CheckBox>();
        mTvArray=new TextView[3];
        mTvArray[0]=mTvCopy;
        mTvArray[1]=mTvShare;
        mTvArray[2]=mTvDelete;
        clip = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
		// mContactModel = (ContactModel)
		// getIntent().getSerializableExtra("data");
		tel = getIntent().getStringExtra("tel");
		String name = getIntent().getStringExtra("name");
        String inspection=getIntent().getStringExtra("inspection");

		for (int i=0;i<mApp.TempSmsDatas.size();i++) {
            SmsModel mSmsModel=mApp.TempSmsDatas.get(i);
			if (mSmsModel.getAddress() == null) {
				continue;
			}
			if (mSmsModel.getAddress().equals(tel)) {
				mSmsModel.setType("" + (Math.random() > 0.5 ? 1 : 0));
                mData.add(mSmsModel);
                if(mSmsModel.getBody().contains(inspection)){
                    selection=mData.size()-1;
                }

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
        mAdapter.setSelectorStringTitle(mTvSelectorTitel);
        mAdapter.setBottomTextView(mTvArray);
        mAdapter.inspection=inspection;
		mListView.setAdapter(mAdapter);
        mListView.setOnItemLongClickListener(mOnItemLong);
		int totalHeight = 0;
		int listHeight = mApp.screenheight - mTvSend.getHeight()
				- mLlHeader.getHeight();
        if(TextUtils.isEmpty(inspection)) {
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
        }else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Log.e("selection---------->","===>"+selection);
                    mListView.setSelection(selection);
                }
            }, 50);
        }

    }

	@Override
	@OnClick({ R.id.tv_chatmsg_send, R.id.tv_chatmsg_call,R.id.tv_chatmsg_cancel
            ,R.id.tv_chatmsg_selector,R.id.tv_chatmsg_delet ,R.id.tv_chatmsg_copy,
            R.id.tv_chatmsg_share})
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
        case R.id.tv_chatmsg_cancel:
                mLlHeader.setVisibility(View.VISIBLE);
                mLlBottom.setVisibility(View.VISIBLE);
                mRlHeader.setVisibility(View.GONE);
                mLlBottoms.setVisibility(View.GONE);
                mAdapter.setAllChildCheckBox(false);
               // mAdapter.notifyDataSetChanged();
                break;
        case R.id.tv_chatmsg_selector:
                CheckBox cb= (CheckBox) v;
                if (cb.isChecked()){
                    cb.setText("全不选");
                    mTvSelectorTitel.setText("已选择"+mData.size()+"项");
                    notifyDataSetChangedSel(true);
                }else{
                    cb.setText("全选");
                    mTvSelectorTitel.setText("请选择项目");
                    notifyDataSetChangedSel(false);
                }
                break;
        case R.id.tv_chatmsg_delet:
            if(mAdapter.getSelectorIdx()==0){
                showToast("没有选中任何选项！");
                return;
            }
                int idx=-1;
                for (int i = 0; i <mData.size() ;) {
                    if(mData.get(i).getSel()){
                        mData.remove(i);
                        idx=i;
                        continue;
                    }
                    i++;
                }
                mAdapter.notifyDataSetChanged();
                break;
        case R.id.tv_chatmsg_copy:
            if(mAdapter.getSelectorIdx()==0){
                showToast("没有选中任何选项！");
                return;
            }
            StringBuffer sb=new StringBuffer();
            for (int i = 0; i < mData.size(); i++) {
                if(mData.get(i).getSel()){
                    sb.append(mData.get(i).getBody()+"\n\n");
                }
            }
            clip.setPrimaryClip(ClipData.newPlainText("text",sb.toString()));
            showToast("复制成功！");
                break;
        case R.id.tv_chatmsg_share:
            if(mAdapter.getSelectorIdx()==0){
                showToast("没有选中任何选项！");
                return;
            }
            StringBuffer sbshare=new StringBuffer();
            for (int i = 0; i < mData.size(); i++) {
                if(mData.get(i).getSel()){
                    sbshare.append(mData.get(i).getBody()+"\n\n");
                }
            }
            Intent intent=new Intent(mAct,AddMsgActivity.class);
            intent.putExtra("body",sbshare.toString());
            startActivity(intent);
                break;
		}
	}



    /**
     * checkbox是否全选
     * @param bool
     */
    public void notifyDataSetChangedSel(boolean bool){
        for (int i = 0; i <mData.size() ; i++) {
            mData.get(i).setSel(bool);
        }
        mAdapter.notifyDataSetChanged();
    }

    AdapterView.OnItemLongClickListener mOnItemLong=new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            mData.get(position).setSel(true);
            mAdapter.setSelectorIdx(1);
            mTvSelectorTitel.setText("已选择"+1+"项");
            mAdapter.setAllChildCheckBox(true);
            mLlHeader.setVisibility(View.GONE);
            mLlBottom.setVisibility(View.GONE);
            mRlHeader.setVisibility(View.VISIBLE);
            mLlBottoms.setVisibility(View.VISIBLE);
            isKeyDown=false;
            mTvCopy.setEnabled(true);
            mTvShare.setEnabled(true);
            mTvDelete.setEnabled(true);
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(!isKeyDown){
            mLlHeader.setVisibility(View.VISIBLE);
            mLlBottom.setVisibility(View.VISIBLE);
            mRlHeader.setVisibility(View.GONE);
            mLlBottoms.setVisibility(View.GONE);
            mAdapter.setAllChildCheckBox(false);
            isKeyDown=true;
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
