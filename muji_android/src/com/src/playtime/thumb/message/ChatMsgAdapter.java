package com.src.playtime.thumb.message;

import java.util.List;

import android.content.Context;

import com.ab.util.AbDateUtil;
import com.src.playtime.thumb.R;
import com.src.playtime.thumb.baseadapter.CAdapter;
import com.src.playtime.thumb.baseadapter.MultiItemTypeSupport;
import com.src.playtime.thumb.baseadapter.ViewHolder;
import com.src.playtime.thumb.bean.SmsModel;
import com.src.playtime.thumb.utils.BaseUtil;

public class ChatMsgAdapter extends CAdapter<SmsModel> {

	public ChatMsgAdapter(Context context, List<SmsModel> mDatas,
			MultiItemTypeSupport<SmsModel> multiItemTypeSupport) {
		super(context, mDatas, multiItemTypeSupport);
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

	}

}
