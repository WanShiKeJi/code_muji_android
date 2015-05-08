package com.tjerkw.slideexpandable.library;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;

/**
 * 
 * @author wanfei
 *
 */
public interface SlidExpandManage {
	/**
	 * 返回每个分组头部view
	 * 
	 * @param position
	 * @param convertView
	 * @param parent
	 * @return
	 */
	public View getHeaderView(int position, View convertView, ViewGroup parent);

	/**
	 * 返回头部ID
	 * 
	 * @param position
	 * @return
	 */
	public long getHeaderId(int position);

	public Object[] getSections();

	public int getPositionForSection(int section, ListAdapter adapter);

	public int getSectionForPosition(int position);
}
