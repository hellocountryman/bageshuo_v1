package com.feytuo.bageshuo.share_qq;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.feytuo.bageshuo.Global;
import com.feytuo.bageshuo.activity.UserLogin;
import com.feytuo.bageshuo.util.ThreeLoginUtil;
import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.connect.share.QQShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

/**
 * QQ和QQ空间分享、登录、注销 1、QQ有几种分享模式，给朋友能够分享图片、文字、音乐 2、QQ空间可以分享图片、文字，不能分享音乐
 * 3、QQ登录，可以获取token、expires、openId（与QQ号和app相关联的id号）
 * 
 * @author feytuo
 * 
 */
public class Share_QQ {

	private Tencent mTencent;
	private Context context;

	public Share_QQ(Context context) {
		// TODO Auto-generated constructor stub
		// 在主文件中也需要修改appid
		this.context = context;
		mTencent = Tencent.createInstance(Global.QQ_APPID,
				context.getApplicationContext());
	}

	/**
	 * 点击登录
	 * 
	 * @param v
	 */
	public void qqLogin() {
		Activity activity = (Activity) context;
		if (mTencent.isSessionValid()) {
			qqLogout(activity);
		}
		mTencent.login(activity, "all", new MyIUiListener(true));
	}

	/**
	 * 点击注销
	 * 
	 * @param v
	 */
	public void qqLogout(Activity activity) {
		mTencent.logout(activity);
	}

	/**
	 * 分享给QQ好友或者分享到QQ空间
	 * 
	 * @param title
	 *            小标题
	 * @param summary
	 *            简要介绍 url一定要加上“http://”
	 * @param targetUrl
	 *            整体点击跳转url，可以是app下载
	 * @param imageUrl
	 *            图片url
	 * @param audioUrl
	 *            语音的url，QQ空间分享音乐可以将audioUrl加到简要介绍里面
	 * @param QorQzone
	 *            选择分享，1代表分享到QQ空间，2代表分享给好友
	 */
	public void shareToQQOrQzone(String title, String summary,
			String targetUrl, String imageUrl, String audioUrl, int QorQzone) {
		final Bundle params = new Bundle();
		params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE,
				QQShare.SHARE_TO_QQ_TYPE_AUDIO);
		params.putString(QQShare.SHARE_TO_QQ_TITLE, title);
		params.putString(QQShare.SHARE_TO_QQ_SUMMARY, summary);
		params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, targetUrl);
		params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, imageUrl);
		params.putString(QQShare.SHARE_TO_QQ_AUDIO_URL, audioUrl);
		params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "乡乡");
		params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QorQzone);
		doShareToQQ(params);
	}

	// /**
	// * 分享给QQ好友
	// */
	// public void shareToQQ() {
	// final Bundle params = new Bundle();
	// params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE,
	// QQShare.SHARE_TO_QQ_TYPE_DEFAULT);// 分享方式
	// params.putString(QQShare.SHARE_TO_QQ_TITLE, "要分享的标题");// 分享标题
	// params.putString(QQShare.SHARE_TO_QQ_SUMMARY, "要分享的摘要");// 分享内容摘要
	// params.putString(QQShare.SHARE_TO_QQ_TARGET_URL,// 点击链接
	// "http://www.qq.com/news/1.html");
	// params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL,// 图片url
	// "http://imgcache.qq.com/qzone/space_item/pre/0/66768.gif");
	// params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "我的QQ好友app");
	// params.putInt(QQShare.SHARE_TO_QQ_EXT_INT,
	// QQShare.SHARE_TO_QQ_FLAG_QZONE_ITEM_HIDE);// 分享不弹出分享到qq空间分享窗
	// doShareToQQ(params);
	// }
	//
	// /**
	// * 分享到QQ空间1，在好友列表弹出分享到QQ空间弹窗
	// */
	// public void shareToQzone1() {
	// final Bundle params = new Bundle();
	// params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE,
	// QQShare.SHARE_TO_QQ_TYPE_AUDIO);
	// params.putString(QQShare.SHARE_TO_QQ_TITLE, "分享到QQ空间的标题");
	// params.putString(QQShare.SHARE_TO_QQ_SUMMARY, "分享的文字内容");
	// params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, "http://www.baidu.com");
	// params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL,
	// "http://img3.cache.netease.com/photo/0005/2013-03-07/8PBKS8G400BV0005.jpg");
	// params.putString(QQShare.SHARE_TO_QQ_AUDIO_URL,
	// "http://staff2.ustc.edu.cn/~wdw/softdown/index.asp/0042515_05.ANDY.mp3");
	// params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "我的空间分享app");
	// params.putInt(QQShare.SHARE_TO_QQ_EXT_INT,
	// QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);
	// doShareToQQ(params);
	// }
	//
	// /**
	// * 直接分享到QQ空间方式2
	// */
	// public void shareToQzone2() {
	// final Bundle params = new Bundle();
	// params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE,
	// QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
	// params.putString(QzoneShare.SHARE_TO_QQ_TITLE, "分享到QQ空间的标题2");
	// params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, "分享的文字内容2");
	// params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL,
	// "http://www.hao123.com");
	// // params.putString(QzoneShare.SHARE_TO_QQ_IMAGE_URL,
	// //
	// "http://img3.cache.netease.com/photo/0005/2013-03-07/8PBKS8G400BV0005.jpg");
	// // 支持传多个imageUrl
	// ArrayList<String> imageUrls = new ArrayList<String>();
	// for (int i = 0; i < 1; i++) {
	// imageUrls
	// .add("http://img3.cache.netease.com/photo/0005/2013-03-07/8PBKS8G400BV0005.jpg");
	// }
	// params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, imageUrls);
	// doShareToQzone(params);
	// }

	/**
	 * 用异步方式启动分享到QQ
	 * 
	 * @param params
	 */
	private void doShareToQQ(final Bundle params) {
		final Activity activity = (Activity) context;
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				mTencent.shareToQQ(activity, params, new MyIUiListener(false));
			}
		}).start();
	}

	// /**
	// * 用异步方式启动分享到QQ空间
	// *
	// * @param params
	// */
	// private void doShareToQzone(final Bundle params) {
	// final Activity activity = (Activity) context;
	// new Thread(new Runnable() {
	//
	// @Override
	// public void run() {
	// // TODO Auto-generated method stub
	// mTencent.shareToQzone(activity, params,
	// new MyIUiListener(false));
	// }
	// }).start();
	// }

	class MyIUiListener implements IUiListener {

		private boolean isLogin = false;

		public MyIUiListener(boolean isLogin) {
			this.isLogin = isLogin;
		}

		@Override
		public void onCancel() {
			// Util.toastMessage((Activity) context, "取消");
		}

		@Override
		public void onError(UiError e) {
			// TODO Auto-generated method stub
			// Util.toastMessage((Activity) context, "错误："
			// + e.errorMessage, "e");
		}

		@Override
		public void onComplete(Object response) {
			// TODO Auto-generated method stub
			// Util.toastMessage((Activity) context,
			// "完成: " + response.toString());
			Log.i("qqresponse", response.toString());

			initOpenidAndToken((JSONObject) response, isLogin);
		}
	};

	/**
	 * 返回监听结果可以得到openId
	 * 
	 * @param jsonObject
	 */
	private void initOpenidAndToken(JSONObject jsonObject, boolean isLogin) {
		try {
			String token = jsonObject.getString(Constants.PARAM_ACCESS_TOKEN);
			String expires = jsonObject.getString(Constants.PARAM_EXPIRES_IN);
			String openId = jsonObject.getString(Constants.PARAM_OPEN_ID);
			if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(expires)
					&& !TextUtils.isEmpty(openId)) {
				mTencent.setAccessToken(token, expires);
				mTencent.setOpenId(openId);
				// 判断是否是登录
				if (isLogin) {
					getUserQQInfo(openId);
				}
			}
		} catch (Exception e) {
		}
	}

	private void getUserQQInfo(final String openId) {
		if (mTencent != null && mTencent.isSessionValid()) {// 如果已经登录并且token可用
			IUiListener userInfoListener = new IUiListener() {
				@Override
				public void onError(UiError e) {}
				@Override
				public void onComplete(final Object response) {
					new Thread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							JSONObject json = (JSONObject) response;
							Log.i("QQlogin", json.toString());
							if (json.has("figureurl") && json.has("nickname")) {// 获取头像
								try {
									final String nickName = json.getString("nickname");
//									final Bitmap headBmp = Util.getbitmap(json.getString("figureurl_qq_2"));
									final String headBmpUrl = json.getString("figureurl_qq_2");
									if(context instanceof UserLogin){
										((UserLogin) context).runOnUiThread(new Runnable() {
											
											@Override
											public void run() {
												// TODO Auto-generated method stub
//												((UserLogin)context).threeLoginSuccess(openId,nickName,headBmp,"QQ");
												new ThreeLoginUtil(context).threeLoginSuccess(openId,nickName,headBmpUrl,"QQ");
											}
										});
									}
								} catch (JSONException e) {
									e.printStackTrace();
								}
							}
						}
					}).start();
					
				}
				@Override
				public void onCancel() {}
			};
			UserInfo mInfo = new UserInfo(context, mTencent.getQQToken());
			mInfo.getUserInfo(userInfoListener);
		} else {// 如果未登录或者token不可用
			// 设置未登录状态提醒重新登录
		}
	}

	
}
