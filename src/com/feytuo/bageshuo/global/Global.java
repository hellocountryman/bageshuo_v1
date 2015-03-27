package com.feytuo.bageshuo.global;

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
	public final static String BASE_URL = "http://192.168.2.100:8080/bageshuo"; 
	public final static String USER_REGISTER = "/user/Register";
	public final static String USER_NORMAL_LOGIN = "/user/NormalLogin";
	
	
	public final static int NET_SUCCESS = 100;
	public final static int NET_FAILURE = 101;
	
	/**
	 * 第三方appkey
	 */
	// 填写从短信SDK应用后台注册得到的APPKEY
	public final static String SMS_APPKEY = "63fe946784fe";
	// 填写从短信SDK应用后台注册得到的APPSECRET
	public final static String SMS_APPSECRET = "edc4fc8eb4a276926d86f885afd87c48";
}
