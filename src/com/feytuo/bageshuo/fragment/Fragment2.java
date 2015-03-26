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
import com.feytuo.bageshuo.adapter.BageshuoAdapter;
import com.feytuo.bageshuo.widget.XListView;
import com.feytuo.bageshuo.widget.XListView.IXListViewListener;

/**
 * 八哥说模块
 * 
 * @date 2015-03-25
 * 
 * @version v1.0
 * 
 * @author tangpeng
 * 
 */
public class Fragment2 extends Fragment implements IXListViewListener {
	private XListView bageshuoXlv;
	private Handler mHandler;
	private ArrayList<Map<String, Object>> dlist;
	private BageshuoAdapter bageshuoadapter;
	private String[] arraytitle = { "好好学习，天天向上 用粤语这么说？", "恭喜发财，红包拿来 用闽南语怎么说？",
			"你吃饭了没有 用长沙话怎么说", "你今天很漂亮，像花儿一样用东北话怎么说", "你美的用四川话怎么说" };
	private int[] arrayuserhead = { R.drawable.lunbo, R.drawable.lunbo,
			R.drawable.lunbo, R.drawable.lunbo, R.drawable.lunbo };

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fg2, container, false);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		TextView titleTv = (TextView) getActivity().findViewById(
				R.id.top_bar_no_back_title);

		titleTv.setText("八哥说方言");// 设置标题；
		bageshuoXlv = (XListView) getActivity().findViewById(R.id.bageshuo_xlv);// 你这个listview是在这个layout里面
		bageshuoXlv.setPullLoadEnable(true);// 设置让它上拉，FALSE为不让上拉，便不加载更多数据

		bageshuoadapter = new BageshuoAdapter(getActivity(), getData());

		bageshuoXlv.setAdapter(bageshuoadapter);
		bageshuoXlv.setXListViewListener(this);
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
		bageshuoXlv.stopRefresh();
		bageshuoXlv.stopLoadMore();
		bageshuoXlv.setRefreshTime("刚刚");
	}

	// 刷新
	@Override
	public void onRefresh() {
		mHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				getData();
				bageshuoXlv.setAdapter(bageshuoadapter);
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
				bageshuoadapter.notifyDataSetChanged();
				onLoad();
			}
		}, 2000);
	}

}
