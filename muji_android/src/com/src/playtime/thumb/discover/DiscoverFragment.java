package com.src.playtime.thumb.discover;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lidroid.xutils.ViewUtils;
import com.src.playtime.thumb.BaseFragment;
import com.src.playtime.thumb.R;

public class DiscoverFragment extends BaseFragment {

	private View view;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_discover, null);
		ViewUtils.inject(this, view);
		setBarTitle(view, "发现");
		return view;
	}

}
