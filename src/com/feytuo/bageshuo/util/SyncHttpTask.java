package com.feytuo.bageshuo.util;

import java.io.File;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;

/**
 * 异步获取网络数据 必须在主线程中调用
 * 
 * @author hand
 * 
 */
public class SyncHttpTask {

	private ExecutorService executor;
	private CallBack callBack;

	public SyncHttpTask() {
		// TODO Auto-generated method stub
		executor = Executors.newCachedThreadPool();
	}

	public void doGetTask(final String url, final String params,
			final CallBack callBack) {
		this.callBack = callBack;
		executor.execute(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Message msg = handler.obtainMessage();
				try {
					String response = new SyncHttp(url).get(params);
					msg.what = 0;
					msg.obj = response;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					msg.what = 1;
					msg.obj = e.toString();
					e.printStackTrace();
				}
				handler.sendMessage(msg);
			}
		});
	}

	/**
	 * post请求服务器
	 * 
	 * @param url
	 * @param urlParams
	 * @param callBack
	 */
	public void doPostTask(final String url,
			final HashMap<String, Object> urlParams, final CallBack callBack) {
		this.callBack = callBack;
		executor.execute(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Message msg = handler.obtainMessage();
				try {
					String response = new SyncHttp(url).post(urlParams);
					msg.what = 0;
					msg.obj = response;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					msg.what = 1;
					msg.obj = e.toString();
					e.printStackTrace();
				}
				handler.sendMessage(msg);
			}
		});
	}

	/**
	 * ost请求服务器，上传文件
	 * @param url
	 * @param urlParams
	 * @param file 指定上传文件
	 * @param fileKey 上传文件的mapKey
	 * @param callBack
	 */
	public void doPostFileTask(final String url,
			final HashMap<String, Object> urlParams, 
			final File file,final String fileKey, 
			final CallBack callBack) {
		this.callBack = callBack;
		executor.execute(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Message msg = handler.obtainMessage();
				try {
					String response = new SyncHttp(url).filePost(urlParams,
							file, fileKey);
					msg.what = 0;
					msg.obj = response;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					msg.what = 1;
					msg.obj = e.toString();
					e.printStackTrace();
				}
				handler.sendMessage(msg);
			}
		});
	}

	public interface CallBack {
		public void success(String response);

		public void failure(String response);
	}

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (callBack != null) {
				if (msg.what == 0) {
					callBack.success(msg.obj.toString());
				} else {
					callBack.failure(msg.obj.toString());
				}
			}
		};
	};

}
