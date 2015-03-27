package com.feytuo.bageshuo;

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
public class Setting extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting);
		initView();
	}

	private void initView() {

		TextView titleTv = (TextView) findViewById(R.id.top_bar_title);
		titleTv.setText("设置");// 设置标题；

	}

	public void onBackBtn(View v) {
		finish();
	}

}
