package com.feytuo.bageshuo.activity;

import com.feytuo.bageshuo.R;
import com.feytuo.bageshuo.R.id;
import com.feytuo.bageshuo.R.layout;
import com.feytuo.bageshuo.R.string;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * 列表详情
 * 
 * @version v1.0
 * 
 * @date 2015-03-27
 * 
 * @author tangpeng
 * 
 */
public class TopicDetails extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_topic_details);
		initView();
	}

	private void initView() {

		TextView titleTv = (TextView) findViewById(R.id.top_bar_title);
		titleTv.setText(R.string.topicdetails);// 设置标题；
		

	}

	public void onBackBtn(View v) {
		finish();
	}

}
