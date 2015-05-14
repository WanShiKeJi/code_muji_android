package com.src.playtime.thumb.discover;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;
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

    @Override
    @OnClick({R.id.ll_discover_config})
    public void onClick(View v) {
        super.onClick(v);
        switch(v.getId()){
            case R.id.ll_discover_config:
                String email=getSharedPreferences("muji").getString("email","");
                String phone=getSharedPreferences("muji").getString("phone","");
                showConfigDialog(email,phone);
                break;
        }
    }



}
