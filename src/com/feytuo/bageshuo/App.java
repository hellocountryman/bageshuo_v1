package com.feytuo.bageshuo;

import java.util.Map;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.easemob.EMCallBack;
import com.feytuo.chat.DemoHXSDKHelper;
import com.feytuo.chat.domain.User;

public class App extends Application{

	private SharedPreferences sp;
	
	public static Context applicationContext;
	private static App instance;
	// login user name
	public final String PREF_USERNAME = "username";
	
	/**
	 * 当前用户nickname,为了苹果推送不是userid而是昵称
	 */
	public static String currentUserNick = "";
	public static DemoHXSDKHelper hxSDKHelper = new DemoHXSDKHelper();

	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		sp = getSharedPreferences(Global.SP_NAME, Context.MODE_PRIVATE);
		
		applicationContext = this;
        instance = this;
        /**
         * this function will initialize the HuanXin SDK
         * 
         * @return boolean true if caller can continue to call HuanXin related APIs after calling onInit, otherwise false.
         * 
         * 环信初始化SDK帮助函数
         * 返回true如果正确初始化，否则false，如果返回为false，请在后续的调用中不要调用任何和环信相关的代码
         * 
         * for example:
         * 例子：
         * 
         * public class DemoHXSDKHelper extends HXSDKHelper
         * 
         * HXHelper = new DemoHXSDKHelper();
         * if(HXHelper.onInit(context)){
         *     // do HuanXin related work
         * }
         */
        hxSDKHelper.onInit(applicationContext);
	}
	/**
	 * 保存用户id
	 * @param uId
	 */
	public void saveUid(int uId){
		sp.edit().putInt(Global.U_ID, uId).commit();
	}
	/**
	 * 获取用户id
	 * @return
	 */
	public int getUid(){
		return sp.getInt(Global.U_ID, 0);
	}
	/**
	 * 保存用户名
	 * @param uName
	 */
	
	public void saveUname(String uName){
		sp.edit().putString(Global.U_NAME, uName).commit();
	}
	/**
	 * 保存用户密码
	 * @param uPwd
	 */
	public void saveUpwd(String uPwd){
		sp.edit().putString(Global.U_PWD, uPwd).commit();
	}
	
	
	public static App getInstance() {
		return instance;
	}
 
	/**
	 * 获取内存中好友user list
	 *
	 * @return
	 */
	public Map<String, User> getContactList() {
	    return hxSDKHelper.getContactList();
	}

	/**
	 * 设置好友user list到内存中
	 *
	 * @param contactList
	 */
	public void setContactList(Map<String, User> contactList) {
	    hxSDKHelper.setContactList(contactList);
	}

	/**
	 * 获取当前登陆用户名
	 *
	 * @return
	 */
	public String getUserName() {
	    return hxSDKHelper.getHXId();
	}

	/**
	 * 获取密码
	 *
	 * @return
	 */
	public String getPassword() {
		return hxSDKHelper.getPassword();
	}

	/**
	 * 设置用户名
	 *
	 * @param user
	 */
	public void setUserName(String username) {
	    hxSDKHelper.setHXId(username);
	}

	/**
	 * 设置密码 下面的实例代码 只是demo，实际的应用中需要加password 加密后存入 preference 环信sdk
	 * 内部的自动登录需要的密码，已经加密存储了
	 *
	 * @param pwd
	 */
	public void setPassword(String pwd) {
	    hxSDKHelper.setPassword(pwd);
	}

	/**
	 * 退出登录,清空数据
	 */
	public void logout(final EMCallBack emCallBack) {
		// 先调用sdk logout，在清理app中自己的数据
	    hxSDKHelper.logout(emCallBack);
	}
}
