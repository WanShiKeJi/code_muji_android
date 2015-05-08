package com.src.playtime.thumb.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.SlidingDrawer;

import com.src.playtime.thumb.R;


public class MySlidingDrawer extends SlidingDrawer {

	private boolean mVertical;
	private int mBottomOffset;
	private int mTopOffset;
	private boolean mAllowSingleTap;
	private boolean mAnimateOnClick;
	private int mHandleId;
	private int mContentId;
	private int mTapThreshold;
	private int mMaximumTapVelocity;
	private int mMaximumMinorVelocity;
	private int mMaximumMajorVelocity;
	private int mVelocityUnits;
	private int mMaximumAcceleration;

	private static final int TAP_THRESHOLD = 6;
	private static final float MAXIMUM_TAP_VELOCITY = 100.0f;
	private static final float MAXIMUM_MINOR_VELOCITY = 50.0f;
	private static final float MAXIMUM_MAJOR_VELOCITY = 100.0f;
	private static final float MAXIMUM_ACCELERATION = 1000.0f;
	private static final int VELOCITY_UNITS = 1000;

	public MySlidingDrawer(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.SlidingDrawer, 0, 0);
		int orientation = a.getInt(R.styleable.SlidingDrawer_orientation,
				ORIENTATION_VERTICAL);
		mVertical = orientation == ORIENTATION_VERTICAL;
		mBottomOffset = (int) a.getDimension(
				R.styleable.SlidingDrawer_bottomOffset, 0.0f);
		mTopOffset = (int) a.getDimension(R.styleable.SlidingDrawer_topOffset,
				0.0f);
		mAllowSingleTap = a.getBoolean(
				R.styleable.SlidingDrawer_allowSingleTap, true);
		mAnimateOnClick = a.getBoolean(
				R.styleable.SlidingDrawer_animateOnClick, true);

		int handleId = a.getResourceId(R.styleable.SlidingDrawer_handle, 0);
		if (handleId == 0) {
			throw new IllegalArgumentException(
					"The handle attribute is required and must refer "
							+ "to a valid child.");
		}
		int contentId = a.getResourceId(R.styleable.SlidingDrawer_content, 0);
		if (contentId == 0) {
			throw new IllegalArgumentException(
					"The content attribute is required and must refer "
							+ "to a valid child.");
		}

		if (handleId == contentId) {
			throw new IllegalArgumentException(
					"The content and handle attributes must refer "
							+ "to different children.");
		}

		mHandleId = handleId;
		mContentId = contentId;

		final float density = getResources().getDisplayMetrics().density;
		mTapThreshold = (int) (TAP_THRESHOLD * density + 0.5f);
		mMaximumTapVelocity = (int) (MAXIMUM_TAP_VELOCITY * density + 0.5f);
		mMaximumMinorVelocity = (int) (MAXIMUM_MINOR_VELOCITY * density + 0.5f);
		mMaximumMajorVelocity = (int) (MAXIMUM_MAJOR_VELOCITY * density + 0.5f);
		mMaximumAcceleration = (int) (MAXIMUM_ACCELERATION * density + 0.5f);
		mVelocityUnits = (int) (VELOCITY_UNITS * density + 0.5f);

		a.recycle();
		setAlwaysDrawnWithCacheEnabled(false);
	}
}
