package com.feytuo.bageshuo.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import com.feytuo.bageshuo.activity.MainActivity;
import com.feytuo.bageshuo.activity.UserSetting;
import com.feytuo.bageshuo.util.SyncHttpTask.CallBack;
import com.feytuo.chat.Constant;
import com.feytuo.chat.db.UserDao;
import com.feytuo.chat.domain.User;

public class ThreeLoginUtil {

	
	private final String TAG= "ThreeLogin";
	private Context context;
	private App app;
	public ThreeLoginUtil(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
		app = (App) context.getApplicationContext();
	}
	/**
	 * 第三方登录后回调方法
	 * @param openId
	 * @param nickName
	 * @param headBmp
	 * @param type
	 */
	public void threeLoginSuccess(final String openId,final String nickName,final String headBmpUrl,String type){
		Log.i(TAG, "进入服务器注册过程");
		//本地服务器注册
		String params = "u_name="+openId
				+"&u_pwd="+openId
				+"&u_type="+type
				+"&device_id="+AppInfoUtil.getDeviceId(context)
				+"&u_push_id="+"push_id";
		new SyncHttpTask().doGetTask(Global.USER_THREE_LOGIN, params, new CallBack() {
			
			@Override
			public void success(String response) {
				// TODO Auto-generated method stub
				try {
					JSONObject jsonObject = new JSONObject(response);
					int code = jsonObject.getInt("code");
					if(code == Global.NET_SUCCESS || code == Global.NET_EXIST){//code==100、99
						//保存u_id、u_name、u_pwd
						Log.i(TAG, "服务器注册成功:"+code);
						int uId = jsonObject.getJSONObject("data").getInt("u_id");
						app.saveUid(uId);
						app.saveUname(openId);
						app.saveUpwd(openId);
						Bundle bundle = new Bundle();
						bundle.putString("nick_name", nickName);
						bundle.putString("head_bmp_url", headBmpUrl);
						//登录环信服务器
						loginHX(openId,openId,bundle);
					}else{//code==101
						Toast.makeText(context, "用户名或密码错误",Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					Toast.makeText(context, "登录失败，服务器问题",Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				}
			}
			
			@Override
			public void failure(String response) {
				// TODO Auto-generated method stub
				Toast.makeText(context, "登录失败",Toast.LENGTH_SHORT).show();
			}
		});
		//跳转
	}


	/**
	 * 登录环信服务器
	 * @param userName
	 * @param userPwd
	 * @param bundle
	 */
	public void loginHX(final String userName,final String userPwd,final Bundle bundle) {
		// TODO Auto-generated method stub
		Log.i(TAG, "开始登录环信");
		// 调用sdk登陆方法登陆聊天服务器
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
					((Activity)context).runOnUiThread(new Runnable() {
                        public void run() {
                        	App.getInstance().logout(null);
                            Toast.makeText(app, R.string.login_failure_failed, 1).show();
                        }
                    });
					return;
				}
				//更新当前用户的nickname 此方法的作用是在ios离线推送时能够显示用户nick
				boolean updatenick = EMChatManager.getInstance().updateCurrentUserNick(App.currentUserNick.trim());
				if (!updatenick) {
					Log.e("LoginActivity", "update current user nick fail");
				}
				// 进入主页面
				//页面跳转到用户设置界面
				Intent intent = new Intent();
				if(bundle != null){//code == 100新用户
					Log.i(TAG, "登录成功，新用户");
					intent.setClass(context, UserSetting.class);
					intent.putExtras(bundle);
				}else{//code == 99老用户
					Log.i(TAG, "登录成功，老用户");
					intent.setClass(context, MainActivity.class);
				}
				context.startActivity(intent);
				((Activity)context).finish();
			}

           

			@Override
			public void onProgress(int progress, String status) {
			}

			@Override
			public void onError(final int code, final String message) {
				((Activity)context).runOnUiThread(new Runnable() {
					public void run() {
						Toast.makeText(app, context.getString(R.string.Login_failed) + message, Toast.LENGTH_SHORT).show();
					}
				});
			}
		});
	}
	
	private void processContactsAndGroups() throws EaseMobException {
        // demo中简单的处理成每次登陆都去获取好友username，开发者自己根据情况而定
		Log.i(TAG, "获取环信的好友");
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
        String strChat = context.getResources().getString(R.string.Application_and_notify);
        newFriends.setNick(strChat);
        
        userlist.put(Constant.NEW_FRIENDS_USERNAME, newFriends);
        // 添加"群聊"
        User groupUser = new User();
        String strGroup = context.getResources().getString(R.string.group_chat);
        groupUser.setUsername(Constant.GROUP_USERNAME);
        groupUser.setNick(strGroup);
        groupUser.setHeader("");
        userlist.put(Constant.GROUP_USERNAME, groupUser);

        // 存入内存
        App.getInstance().setContactList(userlist);
        // 存入db
        UserDao dao = new UserDao(context);
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
}
