package com.src.playtime.thumb.message;

import android.content.Context;

import com.src.playtime.thumb.R;
import com.src.playtime.thumb.baseadapter.CAdapter;
import com.src.playtime.thumb.baseadapter.MultiItemTypeSupport;
import com.src.playtime.thumb.baseadapter.ViewHolder;
import com.src.playtime.thumb.bean.ContactModel;

import java.util.List;

/**
 * Created by wanfei on 2015/4/27.
 */
public class AddMsgAdapter extends CAdapter<ContactModel>{

    public AddMsgAdapter(Context context, List<ContactModel> mDatas, int layoutId, MultiItemTypeSupport<ContactModel> multiItemTypeSupport) {
        super(context, mDatas, layoutId, multiItemTypeSupport);
    }

    @Override
    public void convert(ViewHolder viewHolder, ContactModel item, int position) {
        viewHolder.setText(R.id.tv_contact_pop_name,item.getName());
        viewHolder.setText(R.id.tv_contact_pop_tel,item.getTelnum());
    }
}
