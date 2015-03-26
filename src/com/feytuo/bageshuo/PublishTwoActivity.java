package com.feytuo.bageshuo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 发布的模块
 * 
 * @verson:v1.0
 * 
 * @date:2015-03-18
 * 
 * @author tangpeng
 * 
 * 
 */
public class PublishTwoActivity extends Activity {
	private EditText publish_text_et ;
	private TextView  publish_surplus_tv;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_publish_two);

		initView();
	}

	private void initView() {
		publish_text_et=(EditText) findViewById(R.id.publish_text_et);
		publish_surplus_tv=(TextView)findViewById(R.id.publish_surplus_tv);
		publish_text_et.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				publish_surplus_tv.setText(s.length() + "/144");
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
	}


	public void publishTwoDeleteBtn(View v) {
		publish_text_et.setText("");
		publish_text_et.clearFocus();
		finish();
	}

	public void publishSuccess(View v) {
		Intent intent = new Intent();
		 intent.setClass(this,MainActivity.class);
		startActivity(intent);
	}

}
