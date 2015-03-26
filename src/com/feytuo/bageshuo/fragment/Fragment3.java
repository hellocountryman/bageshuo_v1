package com.feytuo.bageshuo.fragment;

import java.util.ArrayList;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.feytuo.bageshuo.R;
import com.feytuo.bageshuo.adapter.FindbsAdapter;
import com.feytuo.bageshuo.model.FindCardModel;
import com.feytuo.bageshuo.widget.FindCardContainer;

/**
 * 发现模块的功能
 * 
 * @version v1.0
 * 
 * @data 2015-03-24
 * 
 * @author tangpeng
 * 
 */
public class Fragment3 extends Fragment {
	private FindCardContainer mCardContainer;
	private ArrayList<FindCardModel> dlist;
	private FindbsAdapter bsapt;
	private Button likeBtn;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fg3, container, false);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		initView();

		super.onActivityCreated(savedInstanceState);
	}
	private void initView() {
		mCardContainer = (FindCardContainer)getActivity().findViewById(R.id.find_card_view);
		likeBtn = (Button)getActivity().findViewById(R.id.find_like_btn);
		likeBtn.setOnClickListener(new listener());
		Resources r = getResources();
		dlist = new ArrayList<FindCardModel>();
		for (int j = 0; j < 15; j++) {
			dlist.add(new FindCardModel("Title" + j, "这是一段话哦", r
					.getDrawable(R.drawable.recordbtn_10)));
		}
		bsapt = new FindbsAdapter(getActivity(), dlist);
		mCardContainer.setAdapter(bsapt);
	}


	class listener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
			switch (v.getId()) {
			case R.id.find_like_btn:
				FindCardContainer cardcontainer = new FindCardContainer(
						getActivity());
				int i = cardcontainer.getposition();
				Toast.makeText(getActivity(), "不知道会不会成功" + i,
						Toast.LENGTH_SHORT).show();
				break;

			default:
				break;
			}
			getActivity().startActivity(intent);
		}

	}

}
