package com.src.playtime.thumb.baseadapter;

public interface MultiItemTypeSupport<T> {

	/**
	 * 返回一个布局ID
	 * 
	 * @param position
	 *            当前listview下标
	 * @param t
	 *            当前数据
	 * @return
	 */
	int getLayoutId(int position, T t);

	/**
	 * 返回当前view的类型个数
	 * 
	 * @return
	 */
	int getViewTypeCount();

	/**
	 * 由position返回view type id
	 * 
	 * @param postion
	 * @param t
	 * @return
	 */
	int getItemViewType(int postion, T t);

}
