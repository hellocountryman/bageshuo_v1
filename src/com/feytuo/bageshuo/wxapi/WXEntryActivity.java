package com.feytuo.bageshuo.wxapi;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.feytuo.bageshuo.Global;
import com.feytuo.bageshuo.activity.UserLogin;
import com.feytuo.bageshuo.share_qq.Util;
import com.feytuo.bageshuo.util.ThreeLoginUtil;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

	// IWXAPI 是第三方app和微信通信的openapi接口
	private IWXAPI api;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i("handwechat", "已经执行");
		// 通过WXAPIFactory工厂，获取IWXAPI的实例
		api = WXAPIFactory.createWXAPI(this, Global.WEIXIN_APPID, false);
		api.handleIntent(getIntent(), this);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		api.handleIntent(intent, this);
	}

	// 微信发送请求到第三方应用时，会回调到该方法
	@Override
	public void onReq(BaseReq req) {
	}

	// 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
	@Override
	public void onResp(BaseResp resp) {
		String result = "";

		if (resp.errCode == BaseResp.ErrCode.ERR_OK) {
			result = "发送成功";
			Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
			Log.i("handwechat", "成功:" + resp.openId);
			Log.i("handwechat", "成功:" + ((SendAuth.Resp) resp).code);
			final String code = ((SendAuth.Resp) resp).code;
			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						String response = new WXNetUtil("/oauth2/access_token")
								.get("appid=" + Global.WEIXIN_APPID
										+ "&secret=" + Global.WEIXIN_APPSECRET
										+ "&code=" + code
										+ "&grant_type=authorization_code");
						JSONObject jObject = new JSONObject(response);
						String accessToken = jObject.getString("access_token");
						final String openId = jObject.getString("openid");
						String userBaseInfo = new WXNetUtil("/userinfo")
								.get("access_token=" + accessToken + "&openid="
										+ openId);

						Log.i("handwechat", "userBaseInfo:"
								+ new String(userBaseInfo.getBytes(), "UTF-8"));
						JSONObject userObject = new JSONObject(userBaseInfo);
						final String nickName = new String(userObject
								.getString("nickname").getBytes(), "UTF-8");
//						final Bitmap headBmp = Util.getbitmap(userObject
//								.getString("headimgurl"));
						final String headBmpUrl = userObject
								.getString("headimgurl");
						// Message msg = UserLogin.handler.obtainMessage();
						// Bundle bundle = new Bundle();
						// bundle.putString("nick_name", nickName);
						// bundle.putParcelable("head_bmp", headBmp);
						// msg.setData(bundle);
						// UserLogin.handler.sendMessage(msg);
						runOnUiThread(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								// ((UserLogin)context).threeLoginSuccess(openId,nickName,headBmp,"QQ");
								new ThreeLoginUtil(WXEntryActivity.this)
										.threeLoginSuccess(openId, nickName,
												headBmpUrl, "Wechat");
							}
						});
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}).start();
		} else {
			if (resp.errCode == BaseResp.ErrCode.ERR_USER_CANCEL) {
				result = "发送取消";
			} else if (resp.errCode == BaseResp.ErrCode.ERR_AUTH_DENIED) {
				result = "发送被拒绝";
			} else {
				result = "发送返回";
			}
			Intent it = new Intent();
			it.setClass(WXEntryActivity.this, UserLogin.class);
			startActivity(it);
			finish();
			Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
		}
	}

}