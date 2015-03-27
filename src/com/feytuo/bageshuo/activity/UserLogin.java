package com.feytuo.bageshuo.activity;

import org.json.JSONException;
import org.json.JSONObject;

import com.feytuo.bageshuo.App;
import com.feytuo.bageshuo.Global;
import com.feytuo.bageshuo.R;
import com.feytuo.bageshuo.R.id;
import com.feytuo.bageshuo.R.layout;
import com.feytuo.bageshuo.util.AppInfoUtil;
import com.feytuo.bageshuo.util.SyncHttpTask;
import com.feytuo.bageshuo.util.SyncHttpTask.CallBack;

import android.app.Activity;
import android.content.Intent;
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

	private App app;
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
		app = (App) getApplication();
		setContentView(R.layout.user_login);
		initView();
	}

	public void initView() {
		TextView titleTv = (TextView) findViewById(R.id.top_bar_title);
		titleTv.setText("登陆");// 设置标题；

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
		final String uName = loginPhoneEt.getText().toString();
		final String uPwd = loginWordEt.getText().toString();
		if ("".equals(uName)) {
			Toast.makeText(this, "用户名不能为空！", Toast.LENGTH_LONG).show();
			return;
		}
		if ("".equals(uPwd)) {
			Toast.makeText(this, "密码不能为空！", Toast.LENGTH_LONG).show();
			return;
		}
		//登录
		String params = "u_name="+uName
				+"&u_pwd="+uPwd
				+"&u_type=normal"
				+"&device_id="+AppInfoUtil.getDeviceId(this)
				+"&u_push_id="+"push_id";
		new SyncHttpTask().doGetTask(Global.USER_NORMAL_LOGIN, params, new CallBack() {
			
			@Override
			public void success(String response) {
				// TODO Auto-generated method stub
				try {
					JSONObject jsonObject = new JSONObject(response);
					int code = jsonObject.getInt("code");
					if(code == Global.NET_SUCCESS){//code==100
						//登录环信服务器
						//保存u_id、u_name、u_pwd
						int uId = jsonObject.getJSONObject("data").getInt("u_id");
						app.saveUid(uId);
						app.saveUname(uName);
						app.saveUpwd(uPwd);
						//页面跳转到用户设置界面
						Intent intent = new Intent();
						intent.setClass(UserLogin.this, MainActivity.class);
						startActivity(intent);
						finish();
					}else{//code==101
						Toast.makeText(UserLogin.this, "用户名或密码错误",Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					Toast.makeText(UserLogin.this, "登录失败，服务器问题",Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				}
			}
			
			@Override
			public void failure(String response) {
				// TODO Auto-generated method stub
				Toast.makeText(UserLogin.this, "登录失败",Toast.LENGTH_SHORT).show();
			}
		});
	}

	public void onBackBtn(View v) {
		finish();
	}

}
