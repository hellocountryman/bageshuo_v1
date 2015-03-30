package com.feytuo.bageshuo.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
import com.feytuo.bageshuo.share_qq.Share_QQ;
import com.feytuo.bageshuo.share_weibo.Share_Weibo;
import com.feytuo.bageshuo.util.AppInfoUtil;
import com.feytuo.bageshuo.util.SyncHttpTask;
import com.feytuo.bageshuo.util.ThreeLoginUtil;
import com.feytuo.bageshuo.util.SyncHttpTask.CallBack;
import com.feytuo.bageshuo.wxapi.Share_weixin;
import com.feytuo.chat.Constant;
import com.feytuo.chat.db.UserDao;
import com.feytuo.chat.domain.User;
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
				finish();
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
						//保存u_id、u_name、u_pwd
						int uId = jsonObject.getJSONObject("data").getInt("u_id");
						app.saveUid(uId);
						app.saveUname(uName);
						app.saveUpwd(uPwd);
						//登录环信服务器
//						loginHX(uName,uPwd,null);
						new ThreeLoginUtil(UserLogin.this).loginHX(uName, uPwd, null);
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
	
//	/**
//	 * 第三方登录后回调方法
//	 * @param openId
//	 * @param nickName
//	 * @param headBmp
//	 * @param type
//	 */
//	public void threeLoginSuccess(final String openId,final String nickName,final Bitmap headBmp,String type){
//		Log.i(TAG, "进入服务器注册过程");
//		//本地服务器注册
//		String params = "u_name="+openId
//				+"&u_pwd="+openId
//				+"&u_type="+type
//				+"&device_id="+AppInfoUtil.getDeviceId(this)
//				+"&u_push_id="+"push_id";
//		new SyncHttpTask().doGetTask(Global.USER_THREE_LOGIN, params, new CallBack() {
//			
//			@Override
//			public void success(String response) {
//				// TODO Auto-generated method stub
//				try {
//					JSONObject jsonObject = new JSONObject(response);
//					int code = jsonObject.getInt("code");
//					if(code == Global.NET_SUCCESS || code == Global.NET_EXIST){//code==100、99
//						//保存u_id、u_name、u_pwd
//						int uId = jsonObject.getJSONObject("data").getInt("u_id");
//						app.saveUid(uId);
//						app.saveUname(openId);
//						app.saveUpwd(openId);
//						Bundle bundle = new Bundle();
//						bundle.putString("nick_name", nickName);
//						bundle.putParcelable("head_bmp", headBmp);
//						//登录环信服务器
//						loginHX(openId,openId,bundle);
//					}else{//code==101
//						Toast.makeText(UserLogin.this, "用户名或密码错误",Toast.LENGTH_SHORT).show();
//					}
//				} catch (JSONException e) {
//					// TODO Auto-generated catch block
//					Toast.makeText(UserLogin.this, "登录失败，服务器问题",Toast.LENGTH_SHORT).show();
//					e.printStackTrace();
//				}
//			}
//			
//			@Override
//			public void failure(String response) {
//				// TODO Auto-generated method stub
//				Toast.makeText(UserLogin.this, "登录失败",Toast.LENGTH_SHORT).show();
//			}
//		});
//		//跳转
//	}
//
//
//	/**
//	 * 登录环信服务器
//	 * @param userName
//	 * @param userPwd
//	 * @param bundle
//	 */
//	private void loginHX(final String userName,final String userPwd,final Bundle bundle) {
//		// TODO Auto-generated method stub
//		Log.i("LoginHX", "开始登录环信");
//		// 调用sdk登陆方法登陆聊天服务器
//		EMChatManager.getInstance().login(userName, userPwd, new EMCallBack() {
//
//			@Override
//			public void onSuccess() {
//				
//				// 登陆成功，保存用户名密码
//				App.getInstance().setUserName(userName);
//				App.getInstance().setPassword(userPwd);
//				try {
//					// ** 第一次登录或者之前logout后再登录，加载所有本地群和回话
//					// ** manually load all local groups and
//					// conversations in case we are auto login
//					EMGroupManager.getInstance().loadAllGroups();
//					EMChatManager.getInstance().loadAllConversations();
//					//处理好友和群组
//					processContactsAndGroups();
//				} catch (Exception e) {
//					e.printStackTrace();
//					//取好友或者群聊失败，不让进入主页面
//					runOnUiThread(new Runnable() {
//                        public void run() {
//                        	App.getInstance().logout(null);
//                            Toast.makeText(getApplicationContext(), R.string.login_failure_failed, 1).show();
//                        }
//                    });
//					return;
//				}
//				//更新当前用户的nickname 此方法的作用是在ios离线推送时能够显示用户nick
//				boolean updatenick = EMChatManager.getInstance().updateCurrentUserNick(App.currentUserNick.trim());
//				if (!updatenick) {
//					Log.e("LoginActivity", "update current user nick fail");
//				}
//				// 进入主页面
//				//页面跳转到用户设置界面
//				Intent intent = new Intent();
//				if(bundle != null){//code == 100新用户
//					intent.setClass(UserLogin.this, UserSetting.class);
//					intent.putExtras(bundle);
//				}else{//code == 99老用户
//					intent.setClass(UserLogin.this, MainActivity.class);
//				}
//				startActivity(intent);
//				finish();
//			}
//
//           
//
//			@Override
//			public void onProgress(int progress, String status) {
//			}
//
//			@Override
//			public void onError(final int code, final String message) {
//				runOnUiThread(new Runnable() {
//					public void run() {
//						Toast.makeText(getApplicationContext(), getString(R.string.Login_failed) + message, Toast.LENGTH_SHORT).show();
//					}
//				});
//			}
//		});
//	}
//	
//	private void processContactsAndGroups() throws EaseMobException {
//        // demo中简单的处理成每次登陆都去获取好友username，开发者自己根据情况而定
//        List<String> usernames = EMContactManager.getInstance().getContactUserNames();
//        System.out.println("----------------"+usernames.toString());
//        EMLog.d("roster", "contacts size: " + usernames.size());
//        Map<String, User> userlist = new HashMap<String, User>();
//        for (String username : usernames) {
//            User user = new User();
//            user.setUsername(username);
//            setUserHearder(username, user);
//            userlist.put(username, user);
//        }
//        // 添加user"申请与通知"
//        User newFriends = new User();
//        newFriends.setUsername(Constant.NEW_FRIENDS_USERNAME);
//        String strChat = getResources().getString(R.string.Application_and_notify);
//        newFriends.setNick(strChat);
//        
//        userlist.put(Constant.NEW_FRIENDS_USERNAME, newFriends);
//        // 添加"群聊"
//        User groupUser = new User();
//        String strGroup = getResources().getString(R.string.group_chat);
//        groupUser.setUsername(Constant.GROUP_USERNAME);
//        groupUser.setNick(strGroup);
//        groupUser.setHeader("");
//        userlist.put(Constant.GROUP_USERNAME, groupUser);
//
//        // 存入内存
//        App.getInstance().setContactList(userlist);
//        // 存入db
//        UserDao dao = new UserDao(UserLogin.this);
//        List<User> users = new ArrayList<User>(userlist.values());
//        dao.saveContactList(users);
//        
//        //获取黑名单列表
//        List<String> blackList = EMContactManager.getInstance().getBlackListUsernamesFromServer();
//        //保存黑名单
//        EMContactManager.getInstance().saveBlackList(blackList);
//
//        // 获取群聊列表(群聊里只有groupid和groupname等简单信息，不包含members),sdk会把群组存入到内存和db中
//        EMGroupManager.getInstance().getGroupsFromServer();
//    }
//
//	/**
//	 * 设置hearder属性，方便通讯中对联系人按header分类显示，以及通过右侧ABCD...字母栏快速定位联系人
//	 * 
//	 * @param username
//	 * @param user
//	 */
//	protected void setUserHearder(String username, User user) {
//		String headerName = null;
//		if (!TextUtils.isEmpty(user.getNick())) {
//			headerName = user.getNick();
//		} else {
//			headerName = user.getUsername();
//		}
//		if (username.equals(Constant.NEW_FRIENDS_USERNAME)) {
//			user.setHeader("");
//		} else if (Character.isDigit(headerName.charAt(0))) {
//			user.setHeader("#");
//		} else {
//			user.setHeader(HanziToPinyin.getInstance().get(headerName.substring(0, 1)).get(0).target.substring(0, 1).toUpperCase());
//			char header = user.getHeader().toLowerCase().charAt(0);
//			if (header < 'a' || header > 'z') {
//				user.setHeader("#");
//			}
//		}
//	}
	public void onBackBtn(View v) {
		finish();
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
