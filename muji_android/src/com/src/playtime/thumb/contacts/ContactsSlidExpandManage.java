package com.src.playtime.thumb.contacts;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.src.playtime.thumb.MyApplication;
import com.src.playtime.thumb.R;
import com.src.playtime.thumb.bean.ContactModel;
import com.tjerkw.slideexpandable.library.SlidExpandManage;
import com.tjerkw.slideexpandable.library.StringMatcher;
/**
 * 
 * @author wanfei
 *
 */
public class ContactsSlidExpandManage implements SlidExpandManage {

	private Context mContext;

	private LayoutInflater mInflater;

	private List<ContactModel> mContactDatas;

	private MyApplication mApp;

	private String mSections = "#ABCDEFGHIJKLMNOPQRSTUVWXYZ";

	public ContactsSlidExpandManage(Context context) {
		this.mContext = context;
		mInflater = LayoutInflater.from(mContext);
		mApp = (MyApplication) context.getApplicationContext();
        mContactDatas=new ArrayList<ContactModel>();
	}

	@Override
	public View getHeaderView(int position, View convertView, ViewGroup parent) {
		HeaderViewHolder holder;
		if (convertView == null) {
			holder = new HeaderViewHolder();
			convertView = mInflater.inflate(R.layout.header, parent, false);
			holder.text = (TextView) convertView
					.findViewById(R.id.header_list_first);
			convertView.setTag(holder);
		} else {
			holder = (HeaderViewHolder) convertView.getTag();
		}

        CharSequence headerChar="";
		// set header text as first char in name
        if(mContactDatas.isEmpty()){
             headerChar = mApp.mContactDatas.get(position).getPyname().subSequence(
                    0, 1);
        }else {
             headerChar = mContactDatas.get(position).getPyname().subSequence(
                    0, 1);
        }
		holder.text.setText(headerChar);
		return convertView;
	}

	@Override
	public long getHeaderId(int position) {
		// TODO Auto-generated method stub
        if(mContactDatas.isEmpty()){
            return mApp.mContactDatas.get(position).getPyname().toLowerCase().subSequence(0, 1)
                    .charAt(0);
        }else{
            return mContactDatas.get(position).getPyname().toLowerCase().subSequence(0, 1)
                    .charAt(0);
        }

	}

	class HeaderViewHolder {
		TextView text;
	}


    public void refreshHeaderCharData(List<ContactModel> data){
        this.mContactDatas=data;
    }

	//
	// class ViewHolder {
	// TextView text;
	// }

	@Override
	public int getPositionForSection(int section, ListAdapter wrapped) {
		// If there is no item for current section, previous section will be
		// selected
		// Log.e("wrapped-------->", model.getPyname());
		for (int i = section; i >= 0; i--) {
			for (int j = 0; j < wrapped.getCount(); j++) {
				ContactModel model = (ContactModel) wrapped.getItem(j);
				if (i == 0) {
					// For numeric section
					for (int k = 0; k <= 9; k++) {
						if (StringMatcher.match(
								String.valueOf(model.getPyname().charAt(0)),
								String.valueOf(k)))
							return j;
					}
				} else {
					if (StringMatcher.match(
							String.valueOf(model.getPyname().charAt(0)),
							String.valueOf(mSections.charAt(i))))
						return j;
				}
			}
		}
		return 0;
	}

	@Override
	public int getSectionForPosition(int position) {
		return 0;
	}

	@Override
	public Object[] getSections() {
		String[] sections = new String[mSections.length()];
		for (int i = 0; i < mSections.length(); i++)
			sections[i] = String.valueOf(mSections.charAt(i));
		return sections;
	}

}
