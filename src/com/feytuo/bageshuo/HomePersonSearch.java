package com.feytuo.bageshuo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * 搜索人或者社区
 * 
 * @version v1.0
 * 
 * @date 2015-03-26
 * 
 * @author tangpeng
 * 
 */
public class HomePersonSearch extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_person_search);
		initView();
	}

	private void initView() {

		TextView titleTv = (TextView) findViewById(R.id.top_bar_title);
		titleTv.setText("搜索");// 设置标题；

	}

	public void onBackBtn(View v) {
		finish();
	}

}
