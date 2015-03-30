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
import com.feytuo.bageshuo.adapter.TopiclistAdapter;
import com.feytuo.bageshuo.widget.XListView;
import com.feytuo.bageshuo.widget.XListView.IXListViewListener;

/**
 * 话题列表
 * 
 * @version v1.0
 * 
 * @date 2015-03-15
 * 
 * @author tangpeng
 * 
 */
public class TopicList extends Activity implements IXListViewListener {
	private XListView TopiclistXlv;
	private Handler mHandler;
	private ArrayList<Map<String, Object>> dlist;
	private TopiclistAdapter adapter;
	private String[] arraytitle = { "了不起！库克要捐出所有的财产", "曲平不过瘾，三星要造弯曲手机",
			"朋友跟我哭诉，说因为太穷而经常失恋", "驾驶舱必要有2个人以上", "我们都是和自己赛跑的人","了不起！库克要捐出所有的财产" };
	private int[] arrayico = { R.drawable.a01, R.drawable.a02,
			R.drawable.a03, R.drawable.a04, R.drawable.a05, R.drawable.a01 };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_topic_list);
		initView();
	}

	private void initView() {
		TextView titleTv = (TextView) findViewById(R.id.top_bar_title);
		titleTv.setText(R.string.topiclist);// 设置标题

		TopiclistXlv = (XListView) findViewById(R.id.topiclist_xlv);// 你这个listview是在这个layout里面
		 TopiclistXlv.setPullLoadEnable(true);
		adapter = new TopiclistAdapter(TopicList.this, getData());
		TopiclistXlv.setAdapter(adapter);
		TopiclistXlv.setXListViewListener(this);
		mHandler = new Handler();
	}

	private ArrayList<Map<String, Object>> getData() {
		dlist = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < arraytitle.length; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("arraytitle", arraytitle[i]);
			map.put("arrayico", arrayico[i]);
			dlist.add(map);
		}
		return dlist;
	}

	/** 停止刷新， */
	private void onLoad() {
		TopiclistXlv.stopRefresh();
		TopiclistXlv.stopLoadMore();
		TopiclistXlv.setRefreshTime("刚刚");
	}

	// 刷新
	@Override
	public void onRefresh() {
		mHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				getData();
				TopiclistXlv.setAdapter(adapter);
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

	public void onBackBtn(View v) {
		finish();
	}

}
