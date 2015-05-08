package com.src.playtime.thumb.contacts;

import java.util.List;

import se.emilsjolander.stickylistheaders.IndexScroller;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.view.View;
import android.view.View.OnClickListener;
import com.src.playtime.thumb.BaseActivity;
import com.src.playtime.thumb.MainActivity;
import com.src.playtime.thumb.R;
import com.src.playtime.thumb.baseadapter.CAdapter;
import com.src.playtime.thumb.baseadapter.MultiItemTypeSupport;
import com.src.playtime.thumb.baseadapter.ViewHolder;
import com.src.playtime.thumb.bean.ContactModel;
import com.src.playtime.thumb.message.ChatMsgActivity;
import com.src.playtime.thumb.utils.MuJiMethod;
import com.src.playtime.thumb.widget.MySlideLinearLayout;

/**
 * 
 * @author wanfei
 *
 */
public class ContactsAdapter extends CAdapter<ContactModel> implements
		OnClickListener {
	// 自定义listview检索
	protected IndexScroller mIndexScroller;
	// 自定义的linear布局，防止滑动冲突
	protected MySlideLinearLayout mSlideLinear;

	private BaseActivity mAct;

	/**
	 * 构造方法
	 * 
	 * @param context
	 * @param mDatas
	 * @param layoutId
	 * @param multiItemTypeSupport
	 */
	public ContactsAdapter(Context context, List<ContactModel> mDatas,
			int layoutId,
			MultiItemTypeSupport<ContactModel> multiItemTypeSupport) {
		super(context, mDatas, layoutId, multiItemTypeSupport);
		mAct = (BaseActivity) context;
	}

	/**
	 * 把自定义的检索view设置进来
	 * 
	 * @param mIndexScroller
	 */
	public void setIndexScroller(IndexScroller mIndexScroller) {
		this.mIndexScroller = mIndexScroller;
	}

	@Override
	public void convert(ViewHolder viewHolder, ContactModel item, int position) {
		viewHolder.setText(R.id.text, item.getName());
		mSlideLinear = viewHolder.getView(R.id.expandable_toggle_button);
		mSlideLinear.setIndexScroller(mIndexScroller);
		viewHolder.setOnClickTag(this, R.id.footer_rb_phone, item);
		viewHolder.setOnClickTag(this, R.id.footer_rb_msg, item);
		viewHolder.setOnClickTag(this, R.id.footer_rb_edit, item);
	}

	@Override
	public void onClick(View v) {
		ContactModel model = (ContactModel) v.getTag();
		Intent intent;
		switch (v.getId()) {
		case R.id.footer_rb_phone:
			MuJiMethod.getInstance(mAct).callOut(context, model);
			break;
		case R.id.footer_rb_edit:
			intent = new Intent(Intent.ACTION_EDIT,
					Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI,
							model.getContactId()));
			mAct.startActivityForResult(intent, MainActivity.ContactsIdx);
			break;
		case R.id.footer_rb_msg:
			intent = new Intent(mAct, ChatMsgActivity.class);
			intent.putExtra("tel", model.getTelnum());
			intent.putExtra("name", model.getName());
			mAct.startActivity(intent);
			break;
		}

	}
}
