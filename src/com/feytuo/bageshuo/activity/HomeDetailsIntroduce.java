package com.feytuo.bageshuo.activity;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.feytuo.bageshuo.R;
import com.feytuo.bageshuo.R.id;
import com.feytuo.bageshuo.R.layout;
import com.feytuo.bageshuo.R.style;
import com.feytuo.bageshuo.widget.MyDialog;

/**
 * 
 * 社区详情介绍，主要使用webview加载网页
 * 
 * @version v1.0
 * 
 *@date 2015-02-17
 * 
 * @author tangpeng
 * 
 */
public class HomeDetailsIntroduce extends Activity {

	private Button homeDetailsIntroduceAttentionBtn;// 关注按钮，点击取消或者关注

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_details_introduce);

		initView();
	}

	private void initView() {

		TextView titleTv = (TextView) findViewById(R.id.top_bar_title);
		titleTv.setText("北京社区");// 设置标题；

		Button homeDetailsAtttentionBtn = (Button) findViewById(R.id.home_details_atttention_btn);
		homeDetailsAtttentionBtn.setVisibility(View.GONE);// 隐藏关注的按钮

		homeDetailsIntroduceAttentionBtn = (Button) findViewById(R.id.home_details_introduce_attention_btn);
		homeDetailsIntroduceAttentionBtn.setOnClickListener(new listener());
		
	};

	class listener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.home_details_introduce_attention_btn:

				Dialog dialog = new MyDialog(HomeDetailsIntroduce.this,
						R.style.MyDialog);
				dialog.show();
				break;

			default:
				break;
			}
		}

	}

	public void onBackBtn(View v) {
		finish();
	}

}
