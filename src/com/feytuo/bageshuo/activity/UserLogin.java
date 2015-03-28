package com.feytuo.bageshuo.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.feytuo.bageshuo.App;
import com.feytuo.bageshuo.Global;
import com.feytuo.bageshuo.R;
import com.feytuo.bageshuo.share_qq.Share_QQ;
import com.feytuo.bageshuo.share_weibo.Share_Weibo;
import com.feytuo.bageshuo.util.AppInfoUtil;
import com.feytuo.bageshuo.util.SyncHttpTask;
import com.feytuo.bageshuo.util.SyncHttpTask.CallBack;
import com.feytuo.bageshuo.wxapi.Share_weixin;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.constant.WBConstants;
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
public class UserLogin extends Activity implements IWeiboHandler.Response{

	private final String TAG = "UserLogin";
	private App app;
	private EditText loginPhoneEt;// 手机号码
	private EditText loginWordEt;// 密码
	private Button loginOkBtn;// 登录
	private TextView loginWeiboTv;// 微博登录
	private TextView loginQqTv;// qq登录
	private TextView loginWeixinTv;// 微信登录
	private TextView loginForgetwordTv;// 忘记密码？
	
	private Share_Weibo shareWeibo;
	private Share_QQ shareQq;
	private Share_weixin shareWeixin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		app = (App) getApplication();
		shareQq = new Share_QQ(this);
		shareWeixin = new Share_weixin(this);
		shareWeibo = new Share_Weibo(this);
		if (savedInstanceState != null) {
			shareWeibo.getmWeiboShareAPI().handleWeiboResponse(getIntent(),
					this);
		}
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

		Listener listener = new Listener();
		loginOkBtn.setOnClickListener(listener);
		loginWeiboTv.setOnClickListener(listener);
		loginQqTv.setOnClickListener(listener);
		loginWeixinTv.setOnClickListener(listener);
		loginForgetwordTv.setOnClickListener(listener);
	}

	class Listener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.login_ok_btn:
				loginOkBtn();
				break;
			case R.id.login_weibo_tv:
//				Toast.makeText(UserLogin.this, "你点击了微博", Toast.LENGTH_LONG)
//						.show();
				shareWeibo.SSOAuthorize(UserLogin.this, true, "", 0);
				break;
			case R.id.login_qq_tv:
//				Toast.makeText(UserLogin.this, "你点击了QQ", Toast.LENGTH_LONG)
//						.show();
				shareQq.qqLogin();
				break;
			case R.id.login_weixin_tv:
//				Toast.makeText(UserLogin.this, "你点击了微信", Toast.LENGTH_LONG)
//						.show();
				shareWeixin.loginWechat();
				break;
			case R.id.login_forgetword_tv:
				Toast.makeText(UserLogin.this, "你点击了忘记密码", Toast.LENGTH_LONG)
						.show();
				Intent intentRegister = new Intent();
				intentRegister.putExtra("isRegister", false);
				intentRegister.setClass(UserLogin.this, UserRegistInputPhone.class);
				startActivity(intentRegister);
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
	
	/**
	 * 第三方登录
	 * @param openId
	 * @param nickName
	 * @param headBmp
	 * @param type
	 */
	public void threeLoginSuccess(final String openId,final String nickName,final Bitmap headBmp,String type){
		Log.i(TAG, "进入服务器注册过程");
		//本地服务器注册
		String params = "u_name="+openId
				+"&u_pwd="+openId
				+"&u_type="+type
				+"&device_id="+AppInfoUtil.getDeviceId(this)
				+"&u_push_id="+"push_id";
		new SyncHttpTask().doGetTask(Global.USER_THREE_LOGIN, params, new CallBack() {
			
			@Override
			public void success(String response) {
				// TODO Auto-generated method stub
				try {
					JSONObject jsonObject = new JSONObject(response);
					int code = jsonObject.getInt("code");
					if(code == Global.NET_SUCCESS || code == Global.NET_EXIST){//code==100、99
						//登录环信服务器
						//保存u_id、u_name、u_pwd
						int uId = jsonObject.getJSONObject("data").getInt("u_id");
						app.saveUid(uId);
						app.saveUname(openId);
						app.saveUpwd(openId);
						//页面跳转到用户设置界面
						Intent intent = new Intent();
						if(code == Global.NET_SUCCESS){//code == 100新用户
							intent.setClass(UserLogin.this, UserSetting.class);
							Bundle bundle = new Bundle();
							bundle.putString("nick_name", nickName);
							bundle.putParcelable("head_bmp", headBmp);
							intent.putExtras(bundle);
						}else{//code == 99老用户
							intent.setClass(UserLogin.this, MainActivity.class);
						}
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
		//跳转
	}

	/**
	 * 微博分享回调接口
	 */
	@Override
	public void onResponse(BaseResponse baseResp) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		switch (baseResp.errCode) {
		case WBConstants.ErrorCode.ERR_OK:
			// Toast.makeText(getActivity(),
			// R.string.weibosdk_demo_toast_share_success,
			// Toast.LENGTH_LONG).show();
			break;
		case WBConstants.ErrorCode.ERR_CANCEL:
			// Toast.makeText(getActivity(),
			// R.string.weibosdk_demo_toast_share_canceled,
			// Toast.LENGTH_LONG).show();
			break;
		case WBConstants.ErrorCode.ERR_FAIL:
			// Toast.makeText(
			// getActivity(),
			// getString(R.string.weibosdk_demo_toast_share_failed)
			// + "Error Message: " + baseResp.errMsg,
			// Toast.LENGTH_LONG).show();
			break;
		}
	}
	
	/**
	 * 当 SSO 授权 Activity 退出时，该函数被调用。
	 * 
	 * @see {@link Activity#onActivityResult}
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		// SSO 授权回调
		// 重要：发起 SSO 登陆的 Activity 必须重写 onActivityResult
		if (shareWeibo.getmSsoHandler() != null) {
			shareWeibo.getmSsoHandler().authorizeCallBack(requestCode,
					resultCode, data);
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		// 从当前应用唤起微博并进行分享后，返回到当前应用时，需要在此处调用该函数
		// 来接收微博客户端返回的数据；执行成功，返回 true，并调用
		// {@link IWeiboHandler.Response#onResponse}；失败返回 false，不调用上述回调
		// mWeiboShareAPI.handleWeiboResponse(intent, getActivity());
		shareWeibo.getmWeiboShareAPI().handleWeiboResponse(intent, this);
	}
	
}
