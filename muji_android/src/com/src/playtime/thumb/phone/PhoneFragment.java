package com.src.playtime.thumb.phone;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.litepal.crud.DataSupport;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.text.TextUtils;
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
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.util.AbViewUtil;
import com.avos.avoscloud.AVUser;
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

    private RadioButton mRbList[];

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
		setBarTitle(view, "电话",R.drawable.icon_call_change);
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
				// 建一个删除item
				SwipeMenuItem deleteItem = new SwipeMenuItem(mAct);
				// 设置item的背景
				deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
						0x3F, 0x25)));
				// 设置item的宽
				deleteItem.setWidth(AbViewUtil.dip2px(mAct, 90));
				// 设置一个图标
				deleteItem.setIcon(R.drawable.ic_delete);
				// 添加一个菜单
				menu.addMenuItem(deleteItem);
			}
		};

		// 把creator设置进listview里
		mListView.setMenuCreator(creator);

		// 设置监听item
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
    public void onClick(View v) {
        super.onClick(v);
        switch(v.getId()){
            case R.id.header_right:
                isConfigDialog();
                break;
//            case R.id.tv_config_confirm:
//                isConfigDialog();
//                break;
        }
    }

    public void isConfigDialog(){
        AVUser currentUser=AVUser.getCurrentUser();
        if(currentUser!=null){
            if(TextUtils.isEmpty(currentUser.getString("mujiphone"))){
                GoDisConverDialog();
            }else{
                String title="";
                final String phone=currentUser.getString("mujiphone");
                if(this.getSharedPreferences("muji").getBoolean("isTransfer",false)){
                    title="到拇机"+phone+"的呼转取消?";
                }else{
                    title="手机呼转到拇机"+phone+"?";
                }
                showDialog("",title,"OK",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        callTransfer(phone);
                    }
                },"不OK",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                },true,false);
            }
        }else{
            GoDisConverDialog();
        }
    }

    public void GoDisConverDialog(){
        View mConfigView=LayoutInflater.from(mAct).inflate(R.layout.dialog_godisconver,null);
        AlertDialog.Builder builder = new AlertDialog.Builder(mAct);
        final AlertDialog mConfigDialog = builder.setView(mConfigView).create();
        TextView Ok= (TextView) mConfigView.findViewById(R.id.tv_godis_confirm);
        TextView noOk= (TextView) mConfigView.findViewById(R.id.tv_godis_cancel);
        Ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**手动触发按钮事件*/
                mRbList[3].performClick();
                mConfigDialog.dismiss();
            }
        });
        noOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mConfigDialog.dismiss();
            }
        });
        mConfigDialog.show();
    }

    public void setDisconverRadioButton(RadioButton[] rb){
        this.mRbList=rb;
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
