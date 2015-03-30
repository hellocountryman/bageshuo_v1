package com.feytuo.bageshuo.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.feytuo.bageshuo.App;
import com.feytuo.bageshuo.Global;
import com.feytuo.bageshuo.R;
import com.feytuo.bageshuo.city.CityPicker;
import com.feytuo.bageshuo.share_qq.Util;
import com.feytuo.bageshuo.util.AppInfoUtil;
import com.feytuo.bageshuo.util.BitmapUtil;
import com.feytuo.bageshuo.util.SyncHttpTask;
import com.feytuo.bageshuo.util.SyncHttpTask.CallBack;

/**
 * 注册或者第三方登录新用户进入信息设置
 * 
 * @version v1.0
 * 
 * @date 2015-03-19
 * 
 * @author tangpeng
 * 
 */
public class UserSetting extends Activity {
	private static final String TAG = "UserSetting";
	private App app;
	private Button setUserHeadBtn;// 头像
	private TextView userSetHomelandTv;// 家乡
	private TextView userSetSexTv;// 性别
	private EditText userSetNickEt;// 昵称
	private RelativeLayout setingOpenselectCityRl;// 时间选择器的布局
	private RelativeLayout setSelectSexRl;// 选择性别的布局
	private LinearLayout setSelectHeadLl;// 选择头像的布局
	private CityPicker cityPicker;// 时间选择器

	private static final int PHOTO_REQUEST_TAKEPHOTO = 1;
	private static final int PHOTO_REQUEST_GALLERY = 2;
	private static final int PHOTO_REQUEST_CUT = 3;
	
	private Bitmap headBmp;

	private File tempFile;// 图片名字
	private int crop = 180;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		app = (App) getApplication();
		setContentView(R.layout.user_seting);
		Bundle bundle = getIntent().getExtras();
		initFile();
		initView(bundle);
	}

	private void initFile() {
		// TODO Auto-generated method stub
		File fileDir = new File(Global.HEAD_IMG_TEMP_DIR);
		if(!fileDir.exists()){
			fileDir.mkdirs();
		}
		tempFile = new File(Global.HEAD_IMG_TEMP_DIR,
				getPhotoFileName());
	}

	public void initView(Bundle bundle) {
		TextView titleTv = (TextView) findViewById(R.id.top_bar_title);
		titleTv.setText("我的设置");// 设置标题；

		setUserHeadBtn = (Button) findViewById(R.id.set_user_head_btn);
		cityPicker = (CityPicker) findViewById(R.id.citycitypicker);
		userSetNickEt = (EditText) findViewById(R.id.user_set_nick_et);
		userSetSexTv = (TextView) findViewById(R.id.user_set_sex_tv);
		userSetHomelandTv = (TextView) findViewById(R.id.user_set_homeland_tv);

		setingOpenselectCityRl = (RelativeLayout) findViewById(R.id.seting_open_select_city_Rl);
		setSelectSexRl = (RelativeLayout) findViewById(R.id.set_select_sex_Rl);
		setSelectHeadLl = (LinearLayout) findViewById(R.id.set_select_head_ll);

		setUserHeadBtn.setOnClickListener(new listener());
		userSetSexTv.setOnClickListener(new listener());
		userSetHomelandTv.setOnClickListener(new listener());
		
		if (bundle != null) {
			userSetNickEt.setText(bundle.getString("nick_name"));
			final String headBmpUrl = bundle.getString("head_bmp_url");
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					headBmp = Util.getbitmap(headBmpUrl);
					handler.sendEmptyMessage(0);
				}
			}).start();
			
			
		}
	}
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler(){
		@SuppressWarnings("deprecation")
		public void handleMessage(android.os.Message msg) {
			Drawable drawable = new BitmapDrawable(
					BitmapUtil.toRoundBitmap(headBmp));// 获取到的头像转化成圆形
			setUserHeadBtn.setBackgroundDrawable(drawable);
		};
	};

	class listener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.set_user_head_btn:
				setSelectSexRl.setVisibility(View.GONE);//
				softkeyboard();
				setSelectHeadLl.setVisibility(View.VISIBLE);
				setingOpenselectCityRl.setVisibility(View.GONE);
				break;
			case R.id.user_set_sex_tv:
				settingSexLl();
				break;
			case R.id.user_set_homeland_tv:
				settingHomelandLl();
				break;

				
			default:
				break;
			}
		}

	}

	/**
	 * 点击性别的布局时
	 */
	private void settingSexLl() {
		setingOpenselectCityRl.setVisibility(View.GONE);
		setSelectHeadLl.setVisibility(View.GONE);
		setSelectSexRl.setVisibility(View.VISIBLE);//
		softkeyboard();
	}

	/**
	 *  点击性别的男
	 * @param v
	 */
	public void setSelectSexManBtn(View v) {
		setSelectSexRl.setVisibility(View.GONE);//
		userSetSexTv.setText("男");
	}

	/**
	 * 点击性别的女
	 * @param v
	 */
	public void setSelectSexWomenBtn(View v) {
		setSelectSexRl.setVisibility(View.GONE);//
		userSetSexTv.setText("女");
	}

	/**
	 *  点击性别的取消
	 * @param v
	 */
	public void setSelectSexCancelBtn(View v) {
		setSelectSexRl.setVisibility(View.GONE);//
	}

	/**
	 *  点击家乡的布局时
	 */
	private void settingHomelandLl() {
		softkeyboard();
		setSelectHeadLl.setVisibility(View.GONE);
		setSelectSexRl.setVisibility(View.GONE);//
		setingOpenselectCityRl.setVisibility(View.VISIBLE);

	}

	/**
	 *  点击选择城市弹出选择器是ok
	 * @param v
	 */
	public void setingSelectCityOkLv(View v) {
		userSetHomelandTv.setText(cityPicker.getCity_string() + "");
		setingOpenselectCityRl.setVisibility(View.GONE);
	}

	/**
	 * 点击选择城市弹出选择器是cancal
	 * @param v
	 */
	public void setingSelectCityCancelTv(View v) {
		setingOpenselectCityRl.setVisibility(View.GONE);
	}

	// 如果想在Activity中得到新打开Activity 关闭后返回的数据，需要使用系统提供的
	// startActivityForResult(Intent intent, int requestCode)方法打开新的Activity，
	// 新的Activity 关闭后会向前面的Activity传回数据，为了得到传回的数据，
	// 必须在前面的Activity中重写onActivityResult(int requestCode, int resultCode, Intent
	// data)方法。

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub

		switch (requestCode) {
		case PHOTO_REQUEST_TAKEPHOTO:
			startPhotoZoom(Uri.fromFile(tempFile), 150);
			break;

		case PHOTO_REQUEST_GALLERY:
			if (data != null)
				startPhotoZoom(data.getData(), 150);
			break;

		case PHOTO_REQUEST_CUT:
			Log.e("zoom", "begin2");
			if (data != null)
				setPicToView(data);
			break;
		}

		super.onActivityResult(requestCode, resultCode, data);

	}

	// 点击头像用相机时
	public void setSelectImgfromcameraBtn(View v) {
		setSelectHeadLl.setVisibility(View.GONE);
		Intent intent = new Intent(Intent.ACTION_PICK, null);
		intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				"image/*");
		startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
	}

	// 点击头像用本地照片时
	public void setSelectImgfromphoneBtn(View v) {

		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));// 从文件里吗获取文件
		Log.e("file", tempFile.toString());
		startActivityForResult(intent, PHOTO_REQUEST_TAKEPHOTO);

		setSelectHeadLl.setVisibility(View.GONE);
	}

	// 点击头像用相机时取消选择
	public void setSelectImgCancelBtn(View v) {
		setSelectHeadLl.setVisibility(View.GONE);
	}

	// 将图片显示到布局中
	@SuppressWarnings("deprecation")
	private void setPicToView(Intent picdata) {
		Bundle bundle = picdata.getExtras();
		if (bundle != null) {
			headBmp = bundle.getParcelable("data");
			Drawable drawable = new BitmapDrawable(
					BitmapUtil.toRoundBitmap(headBmp));// 获取到的头像转化成圆形
			setUserHeadBtn.setBackgroundDrawable(drawable);
		}
	}

	// 设置获取到图片的名字
	@SuppressLint("SimpleDateFormat")
	private String getPhotoFileName() {
		Date date = new Date(System.currentTimeMillis());// 获取当前的系统的时间
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"'IMG'_yyyyMMdd_HHmmss");
		return dateFormat.format(date) + ".jpg";
	}

	// 使用默认的裁切工具把文件裁切
	private void startPhotoZoom(Uri uri, int size) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);// 裁剪框比例
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", crop);// 输出图片大小
		intent.putExtra("outputY", crop);
		intent.putExtra("return-data", true);
		Log.e("zoom", "begin1");
		// 如果是调用android自带的intent，就不需要finish。不然要是要finish的
		startActivityForResult(intent, PHOTO_REQUEST_CUT);
	}

	/**
	 * 隐藏输入法
	 */
	public void softkeyboard() {
		((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
				.hideSoftInputFromWindow(UserSetting.this.getCurrentFocus()
						.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}

	
	
	/**
	 * 点击确定
	 * @param v
	 */
	public void UserSettingOk(View v) {
		if(TextUtils.isEmpty(userSetHomelandTv.getText())){
			Toast.makeText(this, "请选择家乡", Toast.LENGTH_SHORT).show();
			return;
		}
		if(TextUtils.isEmpty(userSetSexTv.getText())){
			Toast.makeText(this, "请选择性别", Toast.LENGTH_SHORT).show();
			return;
		}
		if(TextUtils.isEmpty(userSetNickEt.getText())){
			Toast.makeText(this, "请填写昵称", Toast.LENGTH_SHORT).show();
			return;
		}
		if(headBmp == null || setUserHeadBtn.getBackground() == null){
			Toast.makeText(this, "请选择头像", Toast.LENGTH_SHORT).show();
			return;
		}
		getHeadBmpFile();
		//设置用户个人信息
		setUserInfo();
	}

	private void getHeadBmpFile() {
		// TODO Auto-generated method stub
		FileOutputStream ous;
		try {
			ous = new FileOutputStream(tempFile);
			headBmp.compress(CompressFormat.JPEG, 100, ous);
			ous.flush();
			ous.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	/**
	 * 将用户信息保存到服务器
	 */
	private void setUserInfo() {
		// TODO Auto-generated method stub
		HashMap<String, Object> urlParams = new HashMap<String, Object>();
		urlParams.put("u_id", app.getUid());
		urlParams.put("device_id", AppInfoUtil.getDeviceId(this));
		urlParams.put("u_nick", userSetNickEt.getText());
		urlParams.put("u_sex", userSetSexTv.getText());
		urlParams.put("u_home", userSetHomelandTv.getText());
		Log.i(TAG, app.getUid()+""+AppInfoUtil.getDeviceId(this)+""
				+userSetNickEt.getText()
				+userSetSexTv.getText()+""
				+userSetHomelandTv.getText()+"");
		new SyncHttpTask().doPostFileTask(Global.USER_SET_USERINFO, urlParams, tempFile, "u_head", new CallBack() {
			
			@Override
			public void success(String response) {
				// TODO Auto-generated method stub
				try {
					JSONObject jsonObject = new JSONObject(response);
					int code = jsonObject.getInt("code");
					Log.i(TAG, "code:"+code);
					if(code == Global.NET_SUCCESS){//code==100
						//页面跳转到用户设置界面
						Intent intent = new Intent();
						intent.setClass(UserSetting.this, MainActivity.class);
						startActivity(intent);
						finish();
						Toast.makeText(UserSetting.this, "用户信息保存成功",Toast.LENGTH_SHORT).show();
					}else if(code == Global.NET_FAILURE){//code==101
						Toast.makeText(UserSetting.this, "用户信息保存失败",Toast.LENGTH_SHORT).show();
					}else{
						Toast.makeText(UserSetting.this, "用户信息保存失败,上传头像文件过大",Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					Toast.makeText(UserSetting.this, "用户信息保存失败，服务器问题",Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				}
			}
			
			@Override
			public void failure(String response) {
				// TODO Auto-generated method stub
				Toast.makeText(UserSetting.this, "用户信息保存失败",Toast.LENGTH_SHORT).show();
			}
		});
	}

	public void onBackBtn(View v) {
		finish();
	}

}
