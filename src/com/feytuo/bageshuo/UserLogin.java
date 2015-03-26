package com.feytuo.bageshuo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 用户登录页面
 * 
 * @version v1.0
 * 
 * @date 2015-03-18
 * 
 * @author tangpeng
 * 
 */
public class UserLogin extends Activity {

	private EditText loginPhoneEt;// 手机号码
	private EditText loginWordEt;// 密码
	private Button loginOkBtn;// 登录
	private TextView loginWeiboTv;// 微博登录
	private TextView loginQqTv;// qq登录
	private TextView loginWeixinTv;// 微信登录
	private TextView loginForgetwordTv;// 忘记密码？

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_login);
		initView();
	}

	public void initView() {
		TextView titleTv = (TextView) findViewById(R.id.top_bar_title);
		titleTv.setText("注册");// 设置标题；

		loginPhoneEt = (EditText) findViewById(R.id.login_phone_et);
		loginWordEt = (EditText) findViewById(R.id.login_word_et);
		loginOkBtn = (Button) findViewById(R.id.login_ok_btn);
		loginWeiboTv = (TextView) findViewById(R.id.login_weibo_tv);
		loginQqTv = (TextView) findViewById(R.id.login_qq_tv);
		loginWeixinTv = (TextView) findViewById(R.id.login_weixin_tv);
		loginForgetwordTv = (TextView) findViewById(R.id.login_forgetword_tv);

		listener listen = new listener();
		loginOkBtn.setOnClickListener(listen);

	}

	class listener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.login_ok_btn:
				loginOkBtn();
				break;
			case R.id.login_weibo_tv:
				Toast.makeText(UserLogin.this, "你点击了微博", Toast.LENGTH_LONG)
						.show();

				break;
			case R.id.login_qq_tv:
				Toast.makeText(UserLogin.this, "你点击了QQ博", Toast.LENGTH_LONG)
						.show();
				break;
			case R.id.login_weixin_tv:
				Toast.makeText(UserLogin.this, "你点击了微信", Toast.LENGTH_LONG)
						.show();
				break;
			case R.id.login_forgetword_tv:
				Toast.makeText(UserLogin.this, "你点击了密码", Toast.LENGTH_LONG)
						.show();
				break;

			default:
				break;
			}
		}

	}

	// 点击确定的时候确定
	public void loginOkBtn() {
		if ("".equals(loginPhoneEt.getText().toString())) {
			Toast.makeText(this, "用户名不能为空！", Toast.LENGTH_LONG).show();
			return;
		}
		if ("".equals(loginWordEt.getText().toString())) {
			Toast.makeText(this, "密码不能为空！", Toast.LENGTH_LONG).show();
			return;
		}
	}

	public void onBackBtn(View v) {
		finish();
	}

}
