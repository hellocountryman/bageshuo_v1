package com.feytuo.bageshuo;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.feytuo.bageshuo.global.Global;

public class App extends Application{

	private SharedPreferences sp;
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		sp = getSharedPreferences(Global.SP_NAME, Context.MODE_PRIVATE);
		super.onCreate();
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
}
