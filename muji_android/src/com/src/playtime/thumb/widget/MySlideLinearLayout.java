package com.src.playtime.thumb.widget;

import se.emilsjolander.stickylistheaders.IndexScroller;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;

public class MySlideLinearLayout extends LinearLayout {

	private IndexScroller mIndexScroller;

	public MySlideLinearLayout(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public MySlideLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@SuppressLint("NewApi")
	public MySlideLinearLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void setIndexScroller(IndexScroller mIndexScroller) {
		this.mIndexScroller = mIndexScroller;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (mIndexScroller == null) {
			return false;
		}
		if (mIndexScroller.contains(event.getX(), event.getY()))
			return false;

		return super.onTouchEvent(event);
	}

}
