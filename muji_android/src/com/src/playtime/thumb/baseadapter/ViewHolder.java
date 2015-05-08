package com.src.playtime.thumb.baseadapter;

import android.content.Context;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.src.playtime.thumb.R;

/**
 * 
 * @author wanfei
 *
 */
public class ViewHolder {

	private final SparseArray<View> mViews;

	private View mConvertView;

	public int layoutId;

	private int position;

	private ViewHolder(Context context, ViewGroup parent, int layoutId,
			int position) {
		this.layoutId = layoutId;
		this.position = position;
		mViews = new SparseArray<View>();
		mConvertView = LayoutInflater.from(context).inflate(layoutId, parent,
				false);
		mConvertView.setTag(this);
	}

	public static ViewHolder get(Context context, View convertView,
			ViewGroup parent, int layoutId, int position) {
		if (convertView == null) {
			return new ViewHolder(context, parent, layoutId, position);
		}

		ViewHolder viewHolder = (ViewHolder) convertView.getTag();
		// 如果布局ID不同 则返回不同的viewholder
		if (viewHolder.layoutId != layoutId) {
			return new ViewHolder(context, parent, layoutId, position);
		}
		viewHolder.position = position;
		return viewHolder;

	}

	public <T extends View> T getView(int viewId) {
		View view = mViews.get(viewId);
		if (view == null) {
			view = mConvertView.findViewById(viewId);
			mViews.put(viewId, view);
		}
		return (T) view;
	}

	public View getConvertView() {
		return mConvertView;
	}

	/**
	 * 自定义设置文本
	 * 
	 * @param layoutId
	 *            资源ID
	 * @param str
	 *            字符
	 * @param bool
	 *            字符串为空的时候是否要隐藏
	 */
	public void setText(int layoutId, String str, boolean bool) {
		TextView view = getView(layoutId);
		if (str.equals("") && bool) {
			view.setVisibility(View.GONE);
		} else {
			view.setVisibility(View.VISIBLE);
		}
		view.setText(Html.fromHtml(str));

	}

    /**
     * 根据state显示不同通话状态图标
     * @param layoutId
     * @param state
     *          0是去电
     *          1是来电
     *          2是未接
     */
    public void setTextBackgroundResource(int layoutId,String state){
        TextView view = getView(layoutId);
        if (state.equals("")) {
            view.setVisibility(View.GONE);
            return;
        }
        view.setVisibility(View.VISIBLE);
        if(state.equals("0")){
            view.setBackgroundResource(R.drawable.icon_call_out);
        }else if (state.equals("1")){
            view.setBackgroundResource(R.drawable.icon_call_in);
        }else if (state.equals("2")){
            view.setBackgroundResource(R.drawable.icon_call_missed);
        }
    }

	/**
	 * 自定义设置文本
	 * 
	 * @param layoutId
	 *            资源ID
	 * @param str
	 *            字符
	 */
	public void setText(int layoutId, String str) {
		TextView view = getView(layoutId);
		view.setText(str);

	}

	/**
	 * 自定义设置文本
	 * 
	 * @param layoutId
	 *            资源ID
	 * @param str
	 *            字符
	 */
	public void setText(int layoutId, SpannableStringBuilder str) {
		TextView view = getView(layoutId);
		view.setText(str);

	}

	public void setOnClickTag(OnClickListener onClickListener, int layoutId,
			Object obj) {
		View view = getView(layoutId);
		view.setTag(obj);
		view.setOnClickListener(onClickListener);
	}

    /**
     * 设置view隐藏
     * @param layoutId
     */
    public void setVisibilityGone(int layoutId){
        View view=getView(layoutId);
        view.setVisibility(View.GONE);
    }

    /**
     * 设置view显示
     * @param layoutId
     */
    public void setVisibility(int layoutId){
        View view=getView(layoutId);
        view.setVisibility(View.VISIBLE);
    }

}
