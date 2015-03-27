package com.feytuo.bageshuo.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.feytuo.bageshuo.R;
import com.feytuo.bageshuo.R.drawable;
import com.feytuo.bageshuo.R.id;
import com.feytuo.bageshuo.R.layout;
import com.feytuo.bageshuo.adapter.OtherCenterAdapter;
import com.feytuo.bageshuo.widget.XListView;
import com.feytuo.bageshuo.widget.XListView.IXListViewListener;

public class OtherCenter extends Activity implements IXListViewListener {
	private XListView otherCenterXlv;
	private Handler mHandler;
	private ArrayList<Map<String, Object>> dlist;
	private OtherCenterAdapter adapter;
	private String[] arraytitle = { "从女性的角度来看，一件喜欢的商品打折了从女性的角度来看从女性的角度来看",
			"tangxiao", "朋友跟我哭诉，说因为太穷而经常失恋", "朋友跟我哭诉，说因为太穷而经常失恋", "tangxiao" };
	private int[] arrayuserhead = { R.drawable.lunbo, R.drawable.lunbo,
			R.drawable.lunbo, R.drawable.lunbo, R.drawable.lunbo };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.other_center);
		
		initView();
	}

	private void initView() {
		TextView titleTv = (TextView) findViewById(R.id.top_bar_title);
		titleTv.setText("个人中心");// 设置标题；

		otherCenterXlv = (XListView) findViewById(R.id.other_center_xlv);// 你这个listview是在这个layout里面
		otherCenterXlv.setPullRefreshEnable(false);// 不让他刷新
		adapter = new OtherCenterAdapter(OtherCenter.this,getData());
		otherCenterXlv.setAdapter(adapter);
		otherCenterXlv.setXListViewListener(this);
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
		otherCenterXlv.stopRefresh();
		otherCenterXlv.stopLoadMore();
		otherCenterXlv.setRefreshTime("刚刚");
	}

	// 刷新
	@Override
	public void onRefresh() {
		mHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				getData();
				otherCenterXlv.setAdapter(adapter);
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

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			this.finish();
		}
		return false;
	}
	

	public void onBackBtn(View v)
	{
		finish();
	}
}
