package com.feytuo.bageshuo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 用户注册输入手机号码页面
 * 
 * @version v1.0
 * 
 * @date 2015-03-19
 * 
 * @author tangpeng
 * 
 */
public class UserRegistInputPhone extends Activity {

	private EditText registerInputPhoneEt;//

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_input_phone);

		initView();
	}

	public void initView() {
		TextView titleTv = (TextView) findViewById(R.id.top_bar_title);
		titleTv.setText("注册");// 设置标题；
		registerInputPhoneEt = (EditText) findViewById(R.id.register_input_phone_et);
	}

	public void onBackBtn(View v) {
		finish();
	}

	public void registerInputPhoneNextBtn(View v) {
		String phoneNumber = registerInputPhoneEt.getText().toString();
		if (TextUtils.isEmpty(phoneNumber)) {
			Toast.makeText(this, "号码不能为空！", Toast.LENGTH_SHORT).show();
			return;
		}
		if(phoneNumber.getBytes().length != 11){
			Toast.makeText(this, "号码不够11位", Toast.LENGTH_SHORT).show();
			return;
		}
		
		Intent intent = new Intent();
		intent.setClass(this, UserRegistVerification.class);
		intent.putExtra("phone_number", phoneNumber);
		startActivity(intent);
		finish();
	}

	
}
