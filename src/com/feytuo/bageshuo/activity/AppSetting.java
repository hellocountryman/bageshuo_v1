package com.feytuo.bageshuo.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

import com.feytuo.bageshuo.R;
import com.feytuo.chat.activity.BaseActivity;
import com.feytuo.chat.activity.SettingsFragment;

/**
 * 系统设置的模块
 * 
 * @version v1.0
 * 
 * @date 2015-03-15
 * 
 * @author tangpeng
 * 
 */
public class AppSetting extends BaseActivity {

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
		
		Fragment settingFragment = new SettingsFragment();
		getSupportFragmentManager().beginTransaction()
								   .add(R.id.setting_container, settingFragment)
								   .commit();

	}

	public void onBackBtn(View v) {
		finish();
	}

}
