package com.feytuo.bageshuo.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.feytuo.bageshuo.R;
import com.feytuo.bageshuo.adapter.MyCenterAdapter;
import com.feytuo.bageshuo.widget.XListView;
import com.feytuo.bageshuo.widget.XListView.IXListViewListener;
/**
 *自己个人中心适配器
 * 
 * @date 2015-03-26
 * 
 * @version v1.0
 * 
 * @author tangpeng
 * 
 */
public class CenterOneFragment extends Fragment implements IXListViewListener {
	private XListView centerOneXlv;
	private Handler mHandler;
	private ArrayList<Map<String, Object>> dlist;
	private MyCenterAdapter adapter;
	private String[] arraytitle = { "从女性的角度来看，一件喜欢的商品打折了从女性的角度来看从女性的角度来看",
			"tangxiao", "朋友跟我哭诉，说因为太穷而经常失恋", "朋友跟我哭诉，说因为太穷而经常失恋", "tangxiao" };
	private int[] arrayuserhead = { R.drawable.lunbo, R.drawable.lunbo,
			R.drawable.lunbo, R.drawable.lunbo, R.drawable.lunbo };

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_center_one, container,
				false);

		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initView();
	}

	private void initView() {
		centerOneXlv = (XListView) getActivity().findViewById(R.id.center_one_xlv);// 你这个listview是在这个layout里面
		centerOneXlv.setPullRefreshEnable(false);// 不让他刷新
		adapter = new MyCenterAdapter(getActivity(), getData());
		centerOneXlv.setAdapter(adapter);
		centerOneXlv.setXListViewListener(this);
		mHandler = new Handler();
	}

	private ArrayList<Map<String, Object>> getData() {
		dlist = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < arraytitle.length; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("hometitle", arraytitle[i]);
			map.put("homeuerhead", arrayuserhead[i]);
			dlist.add(map);
		}
		return dlist;
	}

	/** 停止刷新， */
	private void onLoad() {
		centerOneXlv.stopRefresh();
		centerOneXlv.stopLoadMore();
		centerOneXlv.setRefreshTime("刚刚");
	}

	// 刷新
	@Override
	public void onRefresh() {
		mHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				getData();
				centerOneXlv.setAdapter(adapter);
				onLoad();
			}
		}, 2000);
	}

	// 加载更多
	@Override
	public void onLoadMore() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				getData();
				adapter.notifyDataSetChanged();
				onLoad();
			}
		}, 2000);
	}
}
