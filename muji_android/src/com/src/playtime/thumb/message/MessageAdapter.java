package com.src.playtime.thumb.message;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.text.style.ForegroundColorSpan;

import com.ab.util.AbDateUtil;
import com.src.playtime.thumb.R;
import com.src.playtime.thumb.baseadapter.CAdapter;
import com.src.playtime.thumb.baseadapter.MultiItemTypeSupport;
import com.src.playtime.thumb.baseadapter.ViewHolder;
import com.src.playtime.thumb.bean.ContactModel;
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
		viewHolder.setText(R.id.message_list_body, setSpecifiedTextsColor(item.getBody(),inspection));
	}

    /**
     * 返回一个匹配好的字符串（带颜色）
     *
     * @return
     */
    public SpannableStringBuilder setSpecifiedTextsColor(String text, String specifiedTexts)
    {
        int idx=text.indexOf(inspection);
        int startIdx;
        if(idx>20){
            if(inspection.length()>10){
                text = "..."+text.substring(idx, text.length());
            }else {
//                if(idx - 10+inspection.length()==idx){
//                    startIdx=idx;
//                }else{
                    startIdx=idx-10+inspection.length();
               // }
                text = "..."+text.substring(startIdx, text.length());
            }
        }
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
