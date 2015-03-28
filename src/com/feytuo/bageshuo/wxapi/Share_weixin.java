package com.feytuo.bageshuo.wxapi;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.feytuo.bageshuo.Global;
import com.feytuo.bageshuo.R;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXMusicObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class Share_weixin {

	
	
	private IWXAPI api;
	private Context context;
	
	public Share_weixin(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
		api = WXAPIFactory.createWXAPI(context, Global.WEIXIN_APPID, false);
		api.registerApp(Global.WEIXIN_APPID);
	}
	
	/**
	 * 发送音乐到微信好友或者朋友圈
	 * @param isCirle true分享到朋友圈，false分享给好友
	 */
	public void sendToWechat(boolean isCirle){
		WXMusicObject music = new WXMusicObject();
		//音频数据url
		music.musicDataUrl = "http://staff2.ustc.edu.cn/~wdw/softdown/index.asp/0042515_05.ANDY.mp3";
		//音频网页url
		music.musicUrl= "http://www.baidu.com";
		WXMediaMessage msg = new WXMediaMessage();
		msg.mediaObject = music;
		msg.title = "标题";
		msg.description = "介绍";

		Bitmap thumb = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher);
		msg.setThumbImage(thumb);//缩略图设置
		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = buildTransaction("music");
		req.message = msg;
		req.scene = isCirle ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
		api.sendReq(req);
	}
	
	
	private String buildTransaction(final String type) {
		return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
	}
	
	/**
	 * 微信登录
	 */
	public void loginWechat(){
		SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "com.feytuo.bageshuo";
        api.sendReq(req);
	}
}
