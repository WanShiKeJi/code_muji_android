package com.src.playtime.thumb.baseadapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class CAdapter<T> extends BaseAdapter {

	protected LayoutInflater mInflater;

	protected Context context;

	protected List<T> mDatas;

	protected int layoutId;
	// 验证字符串用
	public String inspection = "";

	protected MultiItemTypeSupport<T> mMultiItemSupport;

    protected List<ViewGroup> mAllChildView;

	/**
	 * 
	 * @param context
	 * @param mDatas
	 *            适配器数据
	 * @param layoutId
	 *            布局ID
	 * @param multiItemTypeSupport
	 *            多种布局接口
	 */
	public CAdapter(Context context, List<T> mDatas, int layoutId,
			MultiItemTypeSupport<T> multiItemTypeSupport) {
		this.context = context;
		this.mDatas = mDatas;
		this.layoutId = layoutId;
		this.mMultiItemSupport = multiItemTypeSupport;
	}

	/**
	 * 
	 * @param context
	 * @param mDatas
	 *            适配器数据
	 * @param multiItemTypeSupport
	 *            多种布局接口
	 */
	public CAdapter(Context context, List<T> mDatas,
			MultiItemTypeSupport<T> multiItemTypeSupport) {
		this.context = context;
		this.mDatas = mDatas;
		this.mMultiItemSupport = multiItemTypeSupport;
	}

	@Override
	public int getCount() {
		return mDatas.isEmpty() ? 0 : mDatas.size();
	}

	@Override
	public T getItem(int position) {
		return mDatas.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public int getViewTypeCount() {
		if (mMultiItemSupport != null)
			return mMultiItemSupport.getViewTypeCount() + 1;
		return 2;
	}

	@Override
	public int getItemViewType(int position) {
		if (mMultiItemSupport != null)
			return mMultiItemSupport.getItemViewType(position,
					mDatas.get(position));
		return position >= mDatas.size() ? 0 : 1;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = getAdapterHolder(position, convertView, parent);
		convert(viewHolder, getItem(position), position);
		return viewHolder.getConvertView();
	}

	/**
	 * 根据条件返回一个viewholder
	 * 
	 * @param position
	 *            下标
	 * @param convertView
	 *            视图
	 * @param parent
	 *            父视图
	 * @return
	 */
	public ViewHolder getAdapterHolder(int position, View convertView,
			ViewGroup parent) {

		if (mMultiItemSupport != null) {
			return ViewHolder.get(
					context,
					convertView,
					parent,
					mMultiItemSupport.getLayoutId(position,
							mDatas.get(position)), position);
		} else {
			return ViewHolder.get(context, convertView, parent, layoutId,
					position);
		}
	}

	/**
	 * 更新列表数据
	 * 
	 * @param mDatas
	 *            列表数据
	 * @param str
	 *            输入框字符串
	 */
	public void refresh(List<T> mDatas, String str) {
		// if (!mDatas.isEmpty()) {
		this.mDatas = mDatas;
		this.inspection = str;
		notifyDataSetChanged();
		// }
	}

	/**
	 * 暴露一个外部方法传递viewholder 和数据
	 * 
	 * @param viewHolder
	 * @param item
	 */
	public abstract void convert(ViewHolder viewHolder, T item, int position);

}
