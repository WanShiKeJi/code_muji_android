package com.src.playtime.thumb.message;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.text.format.DateFormat;

import com.ab.util.AbDateUtil;
import com.src.playtime.thumb.R;
import com.src.playtime.thumb.baseadapter.CAdapter;
import com.src.playtime.thumb.baseadapter.MultiItemTypeSupport;
import com.src.playtime.thumb.baseadapter.ViewHolder;
import com.src.playtime.thumb.bean.SmsModel;
import com.src.playtime.thumb.utils.BaseUtil;

public class MessageAdapter extends CAdapter<SmsModel> {

	private SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd  HH:mm:ss");

	public MessageAdapter(Context context, List<SmsModel> mDatas, int layoutId,
			MultiItemTypeSupport<SmsModel> multiItemTypeSupport) {
		super(context, mDatas, layoutId, multiItemTypeSupport);

	}

	@Override
	public void convert(ViewHolder viewHolder, SmsModel item, int position) {
		viewHolder.setText(R.id.message_list_name, item.getName());
		viewHolder.setText(R.id.message_list_date, BaseUtil.getStrTime(item.getDate()));
		viewHolder.setText(R.id.message_list_body, item.getBody());
	}


}
