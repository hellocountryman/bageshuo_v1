package com.feytuo.bageshuo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class Welcome extends Activity{

	private App app;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		app = (App) getApplication();
		int uId = app.getUid();
		Intent intent = new Intent();
		if(uId > 0){
			intent.setClass(this, MainActivity.class);
		}else{
			intent.setClass(this, ExcessiveViewPagerActivity.class);
		}
		startActivity(intent);
		finish();
	}
}
