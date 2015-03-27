package com.feytuo.bageshuo.activity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.feytuo.bageshuo.R;
import com.feytuo.bageshuo.R.id;
import com.feytuo.bageshuo.R.layout;
import com.feytuo.bageshuo.city.CityPicker;
import com.feytuo.bageshuo.util.BitmapUtil;

/**
 * 用户进入s设置
 * 
 * @version v1.0
 * 
 * @date 2015-03-19
 * 
 * @author tangpeng
 * 
 */
public class UserSetting extends Activity {
	private static final String TAG = "USERSETTING";
	private Button setUserHeadBtn;// 头像
	private TextView userSetHomelandTv;// 家乡
	private TextView userSetSexTv;// 性别
	private TextView userSetNickEt;// 昵称
	private LinearLayout setingOpenselectCityLl;// 时间选择器的布局
	private LinearLayout setSelectSexLl;// 选择性别的布局
	private LinearLayout setSelectHeadLl;// 选择头像的布局
	private CityPicker cityPicker;// 时间选择器

	private static final int PHOTO_REQUEST_TAKEPHOTO = 1;
	private static final int PHOTO_REQUEST_GALLERY = 2;
	private static final int PHOTO_REQUEST_CUT = 3;

	File tempFile = new File(Environment.getExternalStorageDirectory(),
			getPhotoFileName());// 图片名字
	private int crop = 180;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_seting);
		initView();
	}

	public void initView() {
		TextView titleTv = (TextView) findViewById(R.id.top_bar_title);
		titleTv.setText("我的设置");// 设置标题；

		setUserHeadBtn = (Button) findViewById(R.id.set_user_head_btn);
		cityPicker = (CityPicker) findViewById(R.id.citycitypicker);
		userSetNickEt = (TextView) findViewById(R.id.user_set_nick_et);
		userSetSexTv = (TextView) findViewById(R.id.user_set_sex_tv);
		userSetHomelandTv = (TextView) findViewById(R.id.user_set_homeland_tv);

		setingOpenselectCityLl = (LinearLayout) findViewById(R.id.seting_open_select_city_ll);
		setSelectSexLl = (LinearLayout) findViewById(R.id.set_select_sex_ll);
		setSelectHeadLl = (LinearLayout) findViewById(R.id.set_select_head_ll);

		setUserHeadBtn.setOnClickListener(new listener());
	}

	class listener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.set_user_head_btn:
				setSelectSexLl.setVisibility(View.GONE);//
				softkeyboard();
				setSelectHeadLl.setVisibility(View.VISIBLE);
				setingOpenselectCityLl.setVisibility(View.GONE);
				break;

			default:
				break;
			}
		}

	}

	// 点击性别的布局时
	public void settingSexLl(View v) {
		setingOpenselectCityLl.setVisibility(View.GONE);
		setSelectHeadLl.setVisibility(View.GONE);
		setSelectSexLl.setVisibility(View.VISIBLE);//
		softkeyboard();
	}

	// 点击性别的男
	public void setSelectSexManBtn(View v) {
		setSelectSexLl.setVisibility(View.GONE);//
		userSetSexTv.setText("男");
	}

	// 点击性别的女
	public void setSelectSexWomenBtn(View v) {
		setSelectSexLl.setVisibility(View.GONE);//
		userSetSexTv.setText("女");
	}

	// 点击性别的取消
	public void setSelectSexCancelBtn(View v) {
		setSelectSexLl.setVisibility(View.GONE);//
	}

	// 点击家乡的布局时
	public void settingHomelandLl(View v) {
		softkeyboard();
		setSelectHeadLl.setVisibility(View.GONE);
		setSelectSexLl.setVisibility(View.GONE);//
		setingOpenselectCityLl.setVisibility(View.VISIBLE);

	}

	// 点击选择城市弹出选择器是ok
	public void setingSelectCityOkLv(View v) {
		userSetHomelandTv.setText(cityPicker.getCity_string() + "");
		setingOpenselectCityLl.setVisibility(View.GONE);
	}

	// 点击选择城市弹出选择器是cancal
	public void setingSelectCityCancelTv(View v) {
		setingOpenselectCityLl.setVisibility(View.GONE);
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
			Bitmap photo = bundle.getParcelable("data");
			@SuppressWarnings("deprecation")
			Drawable drawable = new BitmapDrawable(
					BitmapUtil.toRoundBitmap(photo));// 获取到的头像转化成圆形
			setUserHeadBtn.setBackgroundDrawable(drawable);
		}
	}

	// 设置获取到图片的名字
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
		
		Intent intentok=new Intent();
		intentok.setClass(this, MainActivity.class);
		startActivity(intentok);
		finish();
	}
	public void onBackBtn(View v) {
		finish();
	}

}
