package com.src.playtime.thumb.phone;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Intents;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.ab.util.AbAppUtil;
import com.src.playtime.thumb.BaseActivity;
import com.src.playtime.thumb.R;
import com.src.playtime.thumb.baseadapter.CAdapter;
import com.src.playtime.thumb.baseadapter.MultiItemTypeSupport;
import com.src.playtime.thumb.baseadapter.ViewHolder;
import com.src.playtime.thumb.bean.ContactModel;
import com.src.playtime.thumb.message.ChatMsgActivity;
import com.src.playtime.thumb.utils.BaseUtil;
import com.src.playtime.thumb.utils.MuJiMethod;
import com.src.playtime.thumb.utils.PinyinSearch;

/**
 * 
 * @author wanfei
 * 
 */
public class PhoneAdapter extends CAdapter<ContactModel> implements
		OnClickListener {

	private BaseActivity mAct;

	public PhoneAdapter(Context context, List<ContactModel> mDatas,
			int layoutId,
			MultiItemTypeSupport<ContactModel> multiItemTypeSupport) {
		super(context, mDatas, layoutId, multiItemTypeSupport);
		mAct = (BaseActivity) context;
	}

	@Override
	public void convert(ViewHolder viewHolder, ContactModel item, int position) {
		viewHolder.setText(R.id.phone_list_name, getMatchString(item));
		viewHolder.setText(R.id.phone_list_tel, getReplaceTelnum(item), true);
		viewHolder.setText(R.id.phone_list_time, item.getDate(), true);
        viewHolder.setText(R.id.phone_list_duration,item.getDuration(),true);
        viewHolder.setTextBackgroundResource(R.id.phone_list_state,item.getState());
		viewHolder.setOnClickTag(this, R.id.footer_rb_phone, item);
		viewHolder.setOnClickTag(this, R.id.footer_rb_msg, item);
		viewHolder.setOnClickTag(this, R.id.footer_rb_edit, item);
		if (inspection.equals("")) {
			item.setGroup("");
		}

	}

	public String getReplaceTelnum(ContactModel item) {
		if (inspection.equals("") || item.getTelnum().equals("")) {
			return item.getTelnum();
		}
		return item.getTelnum().replace(inspection,
				"<font color=#ff0000>" + inspection + "</font>");
	}

	/**
	 * 返回一个匹配好的字符串（带颜色）
	 *
	 * @return
	 */
	public SpannableStringBuilder getMatchString(ContactModel model) {
		String tempStr = model.getName();
		SpannableStringBuilder style = new SpannableStringBuilder(tempStr
				+ "---" + model.group);
		// 如果没有相同的字符就直接返回不带颜色的style
		if (inspection.equals("") || model.group.equals("")) {
			return style;
		}

        String Pyname="";
        String group="";
        for (int i = 0; i <model.getName().length() ; i++) {
            if(PinyinSearch.isChinese(model.getName().charAt(i))){
                Pyname+=BaseUtil.getPingYin(model.getName().charAt(i)+"").charAt(0);
            }else{
                Pyname+=model.getName().charAt(i);
            }
        }
        //记录下标,找到相同的下标才加1
        int idx=0;
        for (int i = 0; i <model.getGroup().length() ; i++) {
            for (int j = idx; j <Pyname.length() ; j++) {
                if (model.getGroup().charAt(i)==Pyname.charAt(j)){
                    group+=model.getGroup().charAt(i);
                    idx++;
                    break;
                }
            }
        }
        //把*号转化成\*
        group=group.replaceAll("\\*","\\\\\\*");
//        //把*号转化成\*
//        model.group=model.group.replaceAll("\\*","\\\\\\*");
		// 如果名字首字符开头是大小写字母
		if (model.getName().charAt(0) >= 'a'
				&& model.getName().charAt(0) <= 'z'
				|| model.getName().charAt(0) >= 'A'
				&& model.getName().charAt(0) <= 'Z') {
			// 把名字赋值给临时字符串
			String tempName = model.getPyname();
			// 匹配到的字符串
			String s = "";
			// 开始循环查找
			for (int i = 0; i < model.getGroup().length(); i++) {
				// 如果当前这个是空格
				if (tempName.charAt(i) == ' ') {
					// 则要把空格带进去一起匹配
					s += "[ ]" + model.group.charAt(i);
					// 把匹配到的空格替换掉,否则后面的匹配会出问题
					tempName = tempName.replaceFirst(" ", "");
				} else {
					// 要是不是空格,就直接添加字符到字符串里
					s += model.group.charAt(i);
				}

			}
            s=s.replaceAll("\\*","\\\\\\*");
			// 把匹配好的字符串带进去和拼音名字匹配
			Pattern pattern = Pattern.compile(s);
			Matcher matcher = pattern.matcher(Pyname);
			if (matcher.find()) {
				// 查找到了就把开始位置和结束位置记下
				int startIdx = matcher.start();
				int endIdx = matcher.end();
				// 从开始位置到结束位置开始上色 返回一个style
				style.setSpan(new ForegroundColorSpan(Color.RED), startIdx,
						endIdx, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
				return style;
			}
		}
		// 如果名字不是首字符不是大小写字母,就直接去匹配每个汉字的拼音的大写首字母
        Pattern pattern = Pattern.compile(group,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(Pyname);
		boolean flag = matcher.find();
		if (flag) {
			int startIdx = matcher.start();
			int endIdx = matcher.end();
			// 从开始位置到结束位置上色
			style.setSpan(new ForegroundColorSpan(Color.RED), startIdx, endIdx,
					Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
		}
		return style;
	}

	@Override
	public void onClick(View v) {
		ContactModel model = (ContactModel) v.getTag();
		switch (v.getId()) {
        case R.id.footer_rb_phone:
                MuJiMethod.getInstance(mAct).callOut(context, model);
			break;
		case R.id.footer_rb_edit:
			startSystemContacts(model);
			break;
		case R.id.footer_rb_msg:
			Intent intent = new Intent(mAct, ChatMsgActivity.class);
			intent.putExtra("tel", model.getTelnum());
			intent.putExtra("name", model.getName());
			mAct.startActivity(intent);
			break;
		}

	}

	/**
	 * 根据当前号码是否存在通讯录来跳转不同的界面
	 * 
	 * @param model
	 */
	public void startSystemContacts(ContactModel model) {
		Intent intent;
		if (model.getContactId().equals("")) {
			intent = new Intent(Intent.ACTION_INSERT, Uri.withAppendedPath(
					Uri.parse("content://com.android.contacts"), "contacts"));
			intent.putExtra(Intents.Insert.PHONE, model.getName());
			mAct.startActivity(intent);
		} else {
			intent = new Intent(Intent.ACTION_EDIT,
					Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI,
							model.getContactId()));
			mAct.startActivityForResult(intent, 1);
			// mAct.startActivity(intent);
		}
	}
}
