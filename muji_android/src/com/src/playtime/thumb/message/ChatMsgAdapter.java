package com.src.playtime.thumb.message;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.ab.util.AbDateUtil;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.src.playtime.thumb.R;
import com.src.playtime.thumb.baseadapter.CAdapter;
import com.src.playtime.thumb.baseadapter.MultiItemTypeSupport;
import com.src.playtime.thumb.baseadapter.ViewHolder;
import com.src.playtime.thumb.bean.SmsModel;
import com.src.playtime.thumb.utils.BaseUtil;

public class ChatMsgAdapter extends CAdapter<SmsModel> {

    private boolean isCheckBox=false;

    private int selectorIdx=0;

    private TextView selectorTv;

    private TextView[] mTvArray;

	public ChatMsgAdapter(Context context, List<SmsModel> mDatas,
			MultiItemTypeSupport<SmsModel> multiItemTypeSupport) {
		super(context, mDatas, multiItemTypeSupport);
        mAllChildView=new ArrayList<ViewGroup>();
	}

	@Override
	public void convert(ViewHolder viewHolder, SmsModel item, final int position) {
        int idx=0;
        if (position>=1)
        idx=AbDateUtil.getOffectMinutes(Long.valueOf(mDatas.get(position - 1).getDate()), Long.valueOf(item.getDate()));
		switch (viewHolder.layoutId) {
		case R.layout.chatmsg_left:
			viewHolder.setText(R.id.tv_chatmsg_left, setSpecifiedTextsColor(item.getBody(),inspection));
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
			viewHolder.setText(R.id.tv_chatmsg_right, setSpecifiedTextsColor(item.getBody(),inspection));
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

        final CheckBox mCkBox=viewHolder.getView(R.id.cb_chatmsg);
        //checkBox的选中监听
        mCkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCkBox.isChecked()){
                    selectorIdx+=1;
                    mDatas.get(position).setSel(true);
                }else {
                    selectorIdx-=1;
                    mDatas.get(position).setSel(false);
                }
                if(selectorIdx>0){
                    selectorTv.setText("已选择"+selectorIdx+"项");
                    for (int i = 0; i <mTvArray.length ; i++) {
                        mTvArray[i].setEnabled(true);
                    }
                }else{
                    selectorTv.setText("请选择项目");
                    for (int i = 0; i <mTvArray.length ; i++) {
                        mTvArray[i].setEnabled(false);
                    }
                }
            }
        });
        //根据当前的状态来判断是否选中
        if(mDatas.get(position).getSel()){
            mCkBox.setChecked(true);
        }else{
            mCkBox.setChecked(false);
        }
        //checkbox按钮是否显示
        if(isCheckBox){
            mCkBox.setVisibility(View.VISIBLE);
        }else {
            mCkBox.setVisibility(View.GONE);
        }
        mAllChildView.add((ViewGroup) viewHolder.getConvertView());
	}

    public void setSelectorStringTitle(TextView selectorTv){
        this.selectorTv=selectorTv;
    }


    public int getSelectorIdx(){
        return selectorIdx;
    }

    public void setSelectorIdx(int selectorIdx) {
        this.selectorIdx = selectorIdx;
    }

    public void setBottomTextView(TextView[] tv){
        this.mTvArray=tv;
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

    /**
     * 返回一个匹配好的字符串（带颜色）
     *
     * @return
     */
    public SpannableStringBuilder setSpecifiedTextsColor(String text, String specifiedTexts)
    {
        SpannableStringBuilder styledText = new SpannableStringBuilder(text);
        if(TextUtils.isEmpty(inspection)){
            return styledText;
        }
        List<Integer> sTextsStartList = new ArrayList<Integer>();
        int sTextLength = specifiedTexts.length();
        String temp = text;
        int lengthFront = 0;//记录被找出后前面的字段的长度
        int start = -1;
        do
        {
            start = temp.indexOf(specifiedTexts);

            if(start != -1)
            {
                start = start + lengthFront;
                sTextsStartList.add(start);
                lengthFront = start + sTextLength;
                temp = text.substring(lengthFront);
            }

        }while(start != -1);


        for(Integer i : sTextsStartList)
        {
            styledText.setSpan(
                    new ForegroundColorSpan(Color.RED),
                    i,
                    i + sTextLength,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        return styledText;
    }

}
