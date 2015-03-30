package com.feytuo.bageshuo.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMContactManager;
import com.easemob.chat.EMGroupManager;
import com.easemob.exceptions.EaseMobException;
import com.easemob.util.EMLog;
import com.easemob.util.HanziToPinyin;
import com.feytuo.bageshuo.App;
import com.feytuo.bageshuo.Global;
import com.feytuo.bageshuo.R;
import com.feytuo.bageshuo.util.AppInfoUtil;
import com.feytuo.bageshuo.util.SyncHttpTask;
import com.feytuo.bageshuo.util.SyncHttpTask.CallBack;
import com.feytuo.chat.Constant;
import com.feytuo.chat.db.UserDao;
import com.feytuo.chat.domain.User;

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

	private boolean isRegister = false;//是注册or忘记密码
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
		isRegister = getIntent().getBooleanExtra("isRegister", false);
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
		if(isRegister){
			titleTv.setText("注册");// 设置标题
		}else{
			titleTv.setText("找回密码");// 设置标题
		}
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
			Toast.makeText(this, "新密码不能为空",Toast.LENGTH_SHORT).show();
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
					if(isRegister){
						//用户注册
						UserRegister();
					}else{
						//用户修改密码
						UserUpdatePwd();
					}
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

	private void UserUpdatePwd() {
		// TODO Auto-generated method stub
		final String uPwd = registerVerivationvPasswordEt.getText().toString();
		String params = "u_name="+phoneNumber
				+"&u_pwd="+uPwd
				+"&device_id="+AppInfoUtil.getDeviceId(this);
		new SyncHttpTask().doGetTask(Global.USER_UPDATE_PWD, params,new CallBack() {
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
						finish();
					}else{//code==101
						Toast.makeText(UserRegistVerification.this, "找回密码失败",Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			@Override
			public void failure(String response) {
				// TODO Auto-generated method stub
				Toast.makeText(UserRegistVerification.this, "找回密码失败",Toast.LENGTH_SHORT).show();
			}
		});
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
						//保存u_id、u_name、u_pwd
						int uId = jsonObject.getJSONObject("data").getInt("u_id");
						app.saveUid(uId);
						app.saveUname(phoneNumber);
						app.saveUpwd(uPwd);
						//登录环信服务器
						loginHX(phoneNumber, uPwd);
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

	private void loginHX(final String userName,final String userPwd) {
		// TODO Auto-generated method stub
		// 调用sdk登陆方法登陆聊天服务器
		Log.i("LoginHX", "开始登录环信");
		EMChatManager.getInstance().login(userName, userPwd, new EMCallBack() {

			@Override
			public void onSuccess() {
				
				// 登陆成功，保存用户名密码
				App.getInstance().setUserName(userName);
				App.getInstance().setPassword(userPwd);
				try {
					// ** 第一次登录或者之前logout后再登录，加载所有本地群和回话
					// ** manually load all local groups and
					// conversations in case we are auto login
					EMGroupManager.getInstance().loadAllGroups();
					EMChatManager.getInstance().loadAllConversations();
					//处理好友和群组
					processContactsAndGroups();
				} catch (Exception e) {
					e.printStackTrace();
					//取好友或者群聊失败，不让进入主页面
					runOnUiThread(new Runnable() {
                        public void run() {
                        	App.getInstance().logout(null);
                            Toast.makeText(getApplicationContext(), R.string.login_failure_failed, 1).show();
                        }
                    });
					return;
				}
				//更新当前用户的nickname 此方法的作用是在ios离线推送时能够显示用户nick
				boolean updatenick = EMChatManager.getInstance().updateCurrentUserNick(App.currentUserNick.trim());
				if (!updatenick) {
					Log.e("LoginActivity", "update current user nick fail");
				}
				//页面跳转到用户设置界面
				Intent intent = new Intent();
				intent.setClass(UserRegistVerification.this, UserSetting.class);
				startActivity(intent);
				finish();
			}

           

			@Override
			public void onProgress(int progress, String status) {
			}

			@Override
			public void onError(final int code, final String message) {
				runOnUiThread(new Runnable() {
					public void run() {
						Toast.makeText(getApplicationContext(), getString(R.string.Login_failed) + message, Toast.LENGTH_SHORT).show();
					}
				});
			}
		});
	}
	
	private void processContactsAndGroups() throws EaseMobException {
        // demo中简单的处理成每次登陆都去获取好友username，开发者自己根据情况而定
        List<String> usernames = EMContactManager.getInstance().getContactUserNames();
        System.out.println("----------------"+usernames.toString());
        EMLog.d("roster", "contacts size: " + usernames.size());
        Map<String, User> userlist = new HashMap<String, User>();
        for (String username : usernames) {
            User user = new User();
            user.setUsername(username);
            setUserHearder(username, user);
            userlist.put(username, user);
        }
        // 添加user"申请与通知"
        User newFriends = new User();
        newFriends.setUsername(Constant.NEW_FRIENDS_USERNAME);
        String strChat = getResources().getString(R.string.Application_and_notify);
        newFriends.setNick(strChat);
        
        userlist.put(Constant.NEW_FRIENDS_USERNAME, newFriends);
        // 添加"群聊"
        User groupUser = new User();
        String strGroup = getResources().getString(R.string.group_chat);
        groupUser.setUsername(Constant.GROUP_USERNAME);
        groupUser.setNick(strGroup);
        groupUser.setHeader("");
        userlist.put(Constant.GROUP_USERNAME, groupUser);

        // 存入内存
        App.getInstance().setContactList(userlist);
        // 存入db
        UserDao dao = new UserDao(UserRegistVerification.this);
        List<User> users = new ArrayList<User>(userlist.values());
        dao.saveContactList(users);
        
        //获取黑名单列表
        List<String> blackList = EMContactManager.getInstance().getBlackListUsernamesFromServer();
        //保存黑名单
        EMContactManager.getInstance().saveBlackList(blackList);

        // 获取群聊列表(群聊里只有groupid和groupname等简单信息，不包含members),sdk会把群组存入到内存和db中
        EMGroupManager.getInstance().getGroupsFromServer();
    }

	/**
	 * 设置hearder属性，方便通讯中对联系人按header分类显示，以及通过右侧ABCD...字母栏快速定位联系人
	 * 
	 * @param username
	 * @param user
	 */
	protected void setUserHearder(String username, User user) {
		String headerName = null;
		if (!TextUtils.isEmpty(user.getNick())) {
			headerName = user.getNick();
		} else {
			headerName = user.getUsername();
		}
		if (username.equals(Constant.NEW_FRIENDS_USERNAME)) {
			user.setHeader("");
		} else if (Character.isDigit(headerName.charAt(0))) {
			user.setHeader("#");
		} else {
			user.setHeader(HanziToPinyin.getInstance().get(headerName.substring(0, 1)).get(0).target.substring(0, 1).toUpperCase());
			char header = user.getHeader().toLowerCase().charAt(0);
			if (header < 'a' || header > 'z') {
				user.setHeader("#");
			}
		}
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
