package com.feytuo.bageshuo.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

import com.feytuo.bageshuo.App;
import com.feytuo.bageshuo.Global;
import com.feytuo.bageshuo.R;
import com.feytuo.bageshuo.R.id;
import com.feytuo.bageshuo.R.layout;
import com.feytuo.bageshuo.util.AppInfoUtil;
import com.feytuo.bageshuo.util.SyncHttpTask;
import com.feytuo.bageshuo.util.SyncHttpTask.CallBack;

/**
 * 用户输入验证码以及密码
 * 
 * @version v1.0
 * 
 * @date 2015-03-19
 * 
 * @author tangpeng
 * 
 */
public class UserRegistVerification extends Activity {

	private EditText registerVerivationEt;// 输入验证码
	private EditText registerVerivationvPasswordEt;// 输入密码
	private Button registeVerivationAgainBtn;// 点击获取验证码
	private TextView registerVerigicationHintTv;// 发送提示

	// int count = 60;
	// private TimerTask timerTask;
	// private Timer timer;
	private TimeCount time;
	private String phoneNumber;
	private App app;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_input_verification);
		app = (App) getApplication();
		phoneNumber = getIntent().getStringExtra("phone_number");
		initView();
		initDate();
	}

	public void initView() {

		registerVerivationEt = (EditText) findViewById(R.id.register_verivation_et);
		registerVerivationvPasswordEt = (EditText) findViewById(R.id.register_verivation_password_et);
		registeVerivationAgainBtn = (Button) findViewById(R.id.register_verivation_again_btn);

		registerVerigicationHintTv = (TextView) findViewById(R.id.register_verigication_hint_tv);
		registerVerigicationHintTv.setText("已给手机号码" + phoneNumber + "发送一条验证短信");

		TextView titleTv = (TextView) findViewById(R.id.top_bar_title);
		titleTv.setText("注册");// 设置标题；
	}

	private void initDate() {
		// TODO Auto-generated method stub
		time = new TimeCount(60000, 1000);// 构造CountDownTimer对象

		// 初始化shareSDK短信接口
		SMSSDK.initSDK(this, Global.SMS_APPKEY, Global.SMS_APPSECRET);
		EventHandler eh = new EventHandler() {

			@Override
			public void afterEvent(int event, int result, Object data) {

				Message msg = new Message();
				msg.arg1 = event;
				msg.arg2 = result;
				msg.obj = data;
				handler.sendMessage(msg);
			}

		};
		SMSSDK.registerEventHandler(eh);
		sendSMS();
		time.start();
	}

	/**
	 * 重新获取验证码（点击获取验证码）
	 * 
	 * @param v
	 */
	public void RegisterVerivationAgainTv(View v) {
		// startCount();
		sendSMS();
		time.start();
	}

	/**
	 * 验证短信验证码(下一步)
	 * 
	 * @param v
	 */
	public void registerVerigicationNextBtn(View v) {
		if (TextUtils.isEmpty(registerVerivationEt.getText().toString())) {
			Toast.makeText(this, "验证码不能为空", Toast.LENGTH_SHORT).show();
		}
		if (TextUtils.isEmpty(registerVerivationvPasswordEt.getText().toString())) {
			Toast.makeText(this, "密码不能为空",Toast.LENGTH_SHORT).show();
			return ;
		}
		SMSSDK.submitVerificationCode("86", phoneNumber,
				registerVerivationEt.getText().toString());
	}

	public void onBackBtn(View v) {
		finish();
	}

	class TimeCount extends CountDownTimer {
		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
		}

		@Override
		public void onFinish() {// 计时完毕时触发
			registeVerivationAgainBtn.setText("重新验证");
			registeVerivationAgainBtn.setClickable(true);
		}

		@Override
		public void onTick(long millisUntilFinished) {// 计时过程显示
			registeVerivationAgainBtn.setClickable(false);
			registeVerivationAgainBtn.setTextColor(getResources().getColor(R.color.grey));
			registeVerivationAgainBtn.setText(millisUntilFinished / 1000 + "秒");
		}
	}

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			int event = msg.arg1;
			int result = msg.arg2;
			Object data = msg.obj;
			if (result == SMSSDK.RESULT_COMPLETE) {
				// 短信注册成功后，返回MainActivity,然后提示新好友
				if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {// 提交验证码成功
					Toast.makeText(getApplicationContext(), "提交验证码成功",
							Toast.LENGTH_SHORT).show();
					//倒计时取消
					if (time != null) {
						time.cancel();
					}
					//用户注册
					UserRegister();
				} else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
					Toast.makeText(getApplicationContext(), "验证码已经发送",
							Toast.LENGTH_SHORT).show();
				} else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {// 返回支持发送验证码的国家列表
					Toast.makeText(getApplicationContext(), "获取国家列表成功",
							Toast.LENGTH_SHORT).show();
				}
			} else {
				((Throwable) data).printStackTrace();
				Toast.makeText(getApplicationContext(), "验证码不正确，请重新输入",
						Toast.LENGTH_SHORT).show();
			}

		}

	};

	/**
	 * 发送短信
	 * 
	 * @param phoneNumber
	 */
	public void sendSMS() {
		if (!TextUtils.isEmpty(phoneNumber)) {
			SMSSDK.getVerificationCode("86", phoneNumber);
		}
	}

	/**
	 * 用户注册
	 */
	private void UserRegister() {
		// TODO Auto-generated method stub
		final String uPwd = registerVerivationvPasswordEt.getText().toString();
		String params = "u_name="+phoneNumber
				+"&u_pwd="+uPwd
				+"&u_type=normal"
				+"&device_id="+AppInfoUtil.getDeviceId(this)
				+"&u_push_id="+"push_id";
		new SyncHttpTask().doGetTask(Global.USER_REGISTER, params,new CallBack() {
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
						app.saveUname(phoneNumber);
						app.saveUpwd(uPwd);
						//页面跳转到用户设置界面
						Intent intent = new Intent();
						intent.setClass(UserRegistVerification.this, UserSetting.class);
						startActivity(intent);
						finish();
					}else{//code==101
						Toast.makeText(UserRegistVerification.this, "注册失败",Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			@Override
			public void failure(String response) {
				// TODO Auto-generated method stub
				Toast.makeText(UserRegistVerification.this, "注册失败",Toast.LENGTH_SHORT).show();
			}
		});
		
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (time != null) {
			time.cancel();
		}
		SMSSDK.unregisterAllEventHandler();
	}
}
