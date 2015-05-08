package com.tjerkw.slideexpandable.library;

import java.util.ArrayList;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

/**
 * ListAdapter that adds sliding functionality to a list. Uses
 * R.id.expandalbe_toggle_button and R.id.expandable id's if no ids are given in
 * the contructor.
 *
 * @author tjerk
 * @date 6/13/12 8:04 AM
 */
public class SlideExpandableListAdapter extends
		AbstractSlideExpandableListAdapter implements StickyListHeadersAdapter,
		SectionIndexer {

	private int toggle_button_id;
	private int expandable_view_id;

	private SlidExpandManage mSlidManage;

	public SlideExpandableListAdapter(ListAdapter wrapped,
			int toggle_button_id, int expandable_view_id,
			SlidExpandManage mSlidExpand) {
		super(wrapped);
		this.toggle_button_id = toggle_button_id;
		this.expandable_view_id = expandable_view_id;
		this.mSlidManage = mSlidExpand;
	}

	@Override
	public View getExpandToggleButton(View parent) {
		return parent.findViewById(toggle_button_id);
	}

	@Override
	public View getExpandableView(View parent) {
		return parent.findViewById(expandable_view_id);
	}

	@Override
	public View getHeaderView(int position, View convertView, ViewGroup parent) {
		if (mSlidManage == null)
			return super.getView(position, convertView, parent);
		return mSlidManage.getHeaderView(position, convertView, parent);
	}

	/**
	 * Remember that these have to be static, postion=1 should always return the
	 * same Id that is.
	 */
	@Override
	public long getHeaderId(int position) {
		if (mSlidManage == null)
			return 0;
		return mSlidManage.getHeaderId(position);
	}

	@Override
	public Object[] getSections() {
		if (mSlidManage == null)
			return null;
		return mSlidManage.getSections();
	}

	@Override
	public int getPositionForSection(int section) {
		if (mSlidManage == null)
			return 0;
		return mSlidManage.getPositionForSection(section, wrapped);
	}

	@Override
	public int getSectionForPosition(int position) {
		if (mSlidManage == null)
			return 0;
		return mSlidManage.getSectionForPosition(position);
	}

}
