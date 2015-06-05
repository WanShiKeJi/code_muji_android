package com.src.playtime.thumb.message;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.ab.util.AbDateUtil;
import com.src.playtime.thumb.R;
import com.src.playtime.thumb.baseadapter.CAdapter;
import com.src.playtime.thumb.baseadapter.MultiItemTypeSupport;
import com.src.playtime.thumb.baseadapter.ViewHolder;
import com.src.playtime.thumb.bean.SmsModel;
import com.src.playtime.thumb.utils.BaseUtil;

public class ChatMsgAdapter extends CAdapter<SmsModel> {

    private boolean isCheckBox=false;

	public ChatMsgAdapter(Context context, List<SmsModel> mDatas,
			MultiItemTypeSupport<SmsModel> multiItemTypeSupport) {
		super(context, mDatas, multiItemTypeSupport);
        mAllChildView=new ArrayList<ViewGroup>();
	}

	@Override
	public void convert(ViewHolder viewHolder, SmsModel item, int position) {
        int idx=0;
        if (position>=1)
        idx=AbDateUtil.getOffectMinutes(Long.valueOf(mDatas.get(position - 1).getDate()), Long.valueOf(item.getDate()));
		switch (viewHolder.layoutId) {
		case R.layout.chatmsg_left:
			viewHolder.setText(R.id.tv_chatmsg_left, item.getBody());
            if (position>=1) {
                if (idx >= 5) {
                    viewHolder.setText(R.id.tv_chatmsg_leftdate, BaseUtil.getStrTime(item.getDate()));
                    viewHolder.setVisibility(R.id.tv_chatmsg_leftdate);
                }else{
                    viewHolder.setText(R.id.tv_chatmsg_leftdate, BaseUtil.getStrTime(item.getDate()));
                    //viewHolder.setVisibilityGone(R.id.tv_chatmsg_leftdate);
                }
            }else{
                viewHolder.setText(R.id.tv_chatmsg_leftdate, BaseUtil.getStrTime(item.getDate()));
                viewHolder.setVisibility(R.id.tv_chatmsg_leftdate);
            }
			break;
		case R.layout.chatmsg_right:
			viewHolder.setText(R.id.tv_chatmsg_right, item.getBody());
            if (position>=1) {
                if (idx>= 5) {
                    viewHolder.setText(R.id.tv_chatmsg_rightdate, BaseUtil.getStrTime(item.getDate()));
                    viewHolder.setVisibility(R.id.tv_chatmsg_rightdate);
                }else{
                    viewHolder.setText(R.id.tv_chatmsg_rightdate, BaseUtil.getStrTime(item.getDate()));
                    //viewHolder.setVisibilityGone(R.id.tv_chatmsg_rightdate);
                }
            }else{
                viewHolder.setText(R.id.tv_chatmsg_rightdate, BaseUtil.getStrTime(item.getDate()));
                viewHolder.setVisibility(R.id.tv_chatmsg_rightdate);
            }
			break;
		}

        CheckBox mCkBox=viewHolder.getView(R.id.cb_chatmsg);
        if(isCheckBox){
           mCkBox.setVisibility(View.VISIBLE);
        }else {
            mCkBox.setVisibility(View.GONE);
        }
        mAllChildView.add((ViewGroup) viewHolder.getConvertView());
	}

    /**
     * 返回所有listview的子view
     * @return
     */
    public List<ViewGroup> getAllListChildView(){
        return mAllChildView;
    }

    /**
     * 返回所有listview的数量
     * @return
     */
    public int getAllListChildCount(){
        return mAllChildView.size();
    }

    /**
     *
     */

    public void setAllChildCheckBox(boolean bool){
        this.isCheckBox=bool;
    }

}
