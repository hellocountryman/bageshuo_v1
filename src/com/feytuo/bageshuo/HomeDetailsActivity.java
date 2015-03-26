package com.feytuo.bageshuo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.feytuo.bageshuo.adapter.HomeDetailsListViewAdapter;
import com.feytuo.bageshuo.adapter.InvitationDetailsAdapter;
import com.feytuo.bageshuo.widget.HomeDetailsListView;
import com.feytuo.bageshuo.widget.XListView;
import com.feytuo.bageshuo.widget.XListView.IXListViewListener;

/**
 * 社区详情的模块
 * 
 * @date 2015-03-16
 * 
 * @version v1.0
 * 
 * @author tangpeng
 * 
 */
public class HomeDetailsActivity extends Activity implements IXListViewListener {

	private XListView HomeDetailsXlv;
	private Handler mHandler;
	private ArrayList<Map<String, Object>> dlist;
	private HomeDetailsListViewAdapter adapter;
	private String[] arraytitle = { "从女性的角度来看，一件喜欢的商品打折了从女性的角度来看从女性的角度来看",
			"tangxiao", "朋友跟我哭诉，说因为太穷而经常失恋", "朋友跟我哭诉，说因为太穷而经常失恋", "tangxiao" };
	private int[] arrayuserhead = { R.drawable.lunbo, R.drawable.lunbo,
			R.drawable.lunbo, R.drawable.lunbo, R.drawable.lunbo };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_details);

		initView();
	}

	private void initView() {
		TextView titleTv = (TextView) findViewById(R.id.top_bar_title_publish_tv);
		titleTv.setText("社区");// 设置标题；

		HomeDetailsXlv = (XListView) findViewById(R.id.home_details_xlv);// 你这个listview是在这个layout里面
		HomeDetailsXlv.setPullLoadEnable(true);// 设置让它上拉，FALSE为不让上拉，便不加载更多数据

		adapter = new HomeDetailsListViewAdapter(HomeDetailsActivity.this,
				getData());

		HomeDetailsXlv.setAdapter(adapter);
		HomeDetailsXlv.setXListViewListener(this);
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
		HomeDetailsXlv.stopRefresh();
		HomeDetailsXlv.stopLoadMore();
		HomeDetailsXlv.setRefreshTime("刚刚");
	}

	// 刷新
	@Override
	public void onRefresh() {
		mHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				getData();
				HomeDetailsXlv.setAdapter(adapter);
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

	public void publishBackBtn(View v) {
		finish();
	}

	public void publishbtn(View v) {
		Intent intent = new Intent();
		intent.setClass(this, PublishActivity.class);
		startActivity(intent);
	}

}
