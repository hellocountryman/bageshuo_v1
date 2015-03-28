package com.feytuo.bageshuo;

import android.os.Environment;

public interface Global {

	/**
	 * sharePreference名
	 */
	public final static String SP_NAME = "bageshuo";
	public final static String U_NAME = "user_name";
	public final static String U_ID = "user_id";
	public final static String U_PWD = "user_password";
	
	/**
	 * 网络服务器请求
	 */
	public final static String BASE_URL = "http://192.168.2.102:8080/bageshuo"; 
//	public final static String BASE_URL = "http://www.feytuo.com/bageshuo"; 
	public final static String USER_REGISTER = "/user/Register";
	public final static String USER_NORMAL_LOGIN = "/user/NormalLogin";
	public final static String USER_THREE_LOGIN = "/user/ThreeLogin";
	public final static String USER_UPDATE_PWD = "/user/UpdatePwd";
	public final static String USER_SET_USERINFO = "/user/SetUserInfo";
	
	public final static int NET_EXIST = 99;
	public final static int NET_SUCCESS = 100;
	public final static int NET_FAILURE = 101;
	
	/**
	 * 第三方appkey
	 */
	// 填写从短信SDK应用后台注册得到的APPKEY
	public final static String SMS_APPKEY = "63fe946784fe";
	// 填写从短信SDK应用后台注册得到的APPSECRET
	public final static String SMS_APPSECRET = "edc4fc8eb4a276926d86f885afd87c48";
	
	//QQappid
	public final static String QQ_APPID = "1104422975"; 
	//微博appid
	public final static String WEIBO_APPID = "2223267839"; 
	//微信appid
	public final static String WEIXIN_APPID = "wxb75f15ceacea5ac1"; 
	//微信appsecret
	public final static String WEIXIN_APPSECRET = "11b148f4bea3b8f19942fc307b15bd41"; 
	
	
	
	
	//头像临时存放目录
	public final static String HEAD_IMG_TEMP_DIR = Environment.getExternalStorageDirectory()+"/bageshuo/headimg";
}
