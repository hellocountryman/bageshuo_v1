package com.feytuo.bageshuo;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

	private EditText registerVerivationEt;//输入验证码
	private EditText registerVerivationvordEt;//输入密码
	private Button registeVerivationAgainBtn;//点击获取验证码
	
//	int count = 60;
//	private TimerTask timerTask;
//	private Timer timer;
	private TimeCount time;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_input_verification);

		initView();
	}

	public void initView() {

		registerVerivationEt = (EditText) findViewById(R.id.register_verivation_et);
		registerVerivationvordEt = (EditText) findViewById(R.id.register_verivation_word_et);
		registeVerivationAgainBtn = (Button) findViewById(R.id.register_verivation_again_btn);

		TextView titleTv = (TextView) findViewById(R.id.top_bar_title);
		titleTv.setText("注册");// 设置标题；

		time = new TimeCount(60000, 1000);//构造CountDownTimer对象
	}

	/**
	 * 重新获取验证码（点击获取验证码）
	 * @param v
	 */
	public void RegisterVerivationAgainTv(View v) {
//		startCount();
		time.start();
	}

//	public void startCount() {
//		timer = new Timer();
//		timerTask = new TimerTask() {
//			@Override
//			public void run() {
//				if (count > 0) {
//					registeVerivationAgainBtn.setText("" + count);
//				}
//				else {
//					registeVerivationAgainBtn.setText("重新获取");
//					count--;
//				}
//
//			}
//		};
//		timer.schedule(timerTask, 0, 1000);
//	}

	public void registerVerigicationNextBtn(View v) {
		if ("".equals(registerVerivationvordEt.getText().toString())) {
			Toast.makeText(this, "密码不能为空！", Toast.LENGTH_SHORT).show();
			return;
		}
		Intent intent = new Intent();
		intent.setClass(this, UserSetting.class);
		startActivity(intent);
//		timer.cancel();
	}

	public void onBackBtn(View v) {
		finish();
	}
	
	
	class TimeCount extends CountDownTimer {
		public TimeCount(long millisInFuture, long countDownInterval) {
		super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
		}
		@Override
		public void onFinish() {//计时完毕时触发
			registeVerivationAgainBtn.setText("重新验证");
			registeVerivationAgainBtn.setClickable(true);
		}
		@Override
		public void onTick(long millisUntilFinished){//计时过程显示
			registeVerivationAgainBtn.setClickable(false);
			registeVerivationAgainBtn.setText(millisUntilFinished /1000+"秒");
		}
		}
}
