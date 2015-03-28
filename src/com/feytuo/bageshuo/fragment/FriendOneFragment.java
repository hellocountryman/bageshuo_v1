package com.feytuo.bageshuo.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.feytuo.bageshuo.R;

/**
 * 显示所有会话记录，比较简单的实现，更好的可能是把陌生人存入本地，这样取到的聊天记录是可控的
 * 
 */
public class FriendOneFragment extends Fragment {

	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_friend_one, container, false);
		
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Log.i("987654321", "-->onActivityCreated");
	}

}
