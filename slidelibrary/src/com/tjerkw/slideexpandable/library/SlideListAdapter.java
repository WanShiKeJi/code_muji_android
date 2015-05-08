package com.tjerkw.slideexpandable.library;

import android.view.View;
import android.widget.ListAdapter;

public class SlideListAdapter extends AbstractSlideExpandableListAdapter {

	protected int toggle_button_id;

	protected int expandable_view_id;

	public SlideListAdapter(ListAdapter wrapped, int toggle_button_id,
			int expandable_view_id) {
		this(wrapped);
		this.toggle_button_id = toggle_button_id;
		this.expandable_view_id = expandable_view_id;
	}

	public SlideListAdapter(ListAdapter wrapped) {
		super(wrapped);
	}

	@Override
	public View getExpandToggleButton(View parent) {
		// TODO Auto-generated method stub
		return parent.findViewById(toggle_button_id);
	}

	@Override
	public View getExpandableView(View parent) {
		// TODO Auto-generated method stub
		return parent.findViewById(expandable_view_id);
	}

}
