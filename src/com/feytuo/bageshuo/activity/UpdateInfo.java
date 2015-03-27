package com.feytuo.bageshuo.activity;

import com.feytuo.bageshuo.R;
import com.feytuo.bageshuo.R.id;
import com.feytuo.bageshuo.R.layout;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * 乡里乡亲的模块
 * 
 * @version v1.0
 * 
 * @date 2015-03-15
 * 
 * @author tangpeng
 * 
 */
public class UpdateInfo extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.update_info);
		initView();
	}

	private void initView() {

		TextView titleTv = (TextView) findViewById(R.id.top_bar_title);
		titleTv.setText("修改信息");// 设置标题；

	}

	public void onBackBtn(View v) {
		finish();
	}

}