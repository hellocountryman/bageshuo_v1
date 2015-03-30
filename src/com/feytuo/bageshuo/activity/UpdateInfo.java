package com.feytuo.bageshuo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.feytuo.bageshuo.R;
import com.feytuo.bageshuo.city.CityPicker;

/**
 * 修改个人信息
 * 
 * @version v1.0
 * 
 * @date 2015-03-30
 * 
 * @author tangpeng
 * 
 */
public class UpdateInfo extends Activity {
	private TextView userSetHomelandTv;// 家乡
	private TextView userSetSexTv;// 性别
	private TextView userUpdateSexTv;// 昵称
	private TextView userSetSignTv;// 个性签名

	private RelativeLayout setingOpenselectCityRl;// 时间选择器的布局
	private RelativeLayout setSelectSexRl;// 选择性别的布局
	private CityPicker cityPicker;// 时间选择器

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.update_info);
		initView();
	}

	private void initView() {
		TextView titleTv = (TextView) findViewById(R.id.top_bar_title);
		titleTv.setText("修改信息");// 设置标题；

		userSetSexTv = (TextView) findViewById(R.id.user_set_sex_tv);
		userUpdateSexTv = (TextView) findViewById(R.id.user_update_nick_et);
		userSetHomelandTv = (TextView) findViewById(R.id.user_set_homeland_tv);
		userSetSignTv = (TextView) findViewById(R.id.user_set_sign_tv);
		cityPicker = (CityPicker) findViewById(R.id.citycitypicker);
		listener listen = new listener();
		userSetSexTv.setOnClickListener(listen);
		userUpdateSexTv.setOnClickListener(listen);
		userSetHomelandTv.setOnClickListener(listen);
		userSetSignTv.setOnClickListener(listen);
		
		setingOpenselectCityRl = (RelativeLayout) findViewById(R.id.seting_open_select_city_Rl);
		setSelectSexRl = (RelativeLayout) findViewById(R.id.set_select_sex_Rl);

	}

	class listener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.user_set_sex_tv:
				setingOpenselectCityRl.setVisibility(View.GONE);
				setSelectSexRl.setVisibility(View.VISIBLE);//
				break;
			case R.id.user_update_nick_et:
				Intent intentnick = new Intent();
				intentnick.putExtra("type","nick");
				intentnick.setClass(UpdateInfo.this, UpdateNickSign.class);
				startActivity(intentnick);
				break;
			case R.id.user_set_homeland_tv:
				setSelectSexRl.setVisibility(View.GONE);//
				setingOpenselectCityRl.setVisibility(View.VISIBLE);
				break;
			case R.id.user_set_sign_tv:
				Intent intentsign = new Intent();
				intentsign.putExtra("type", "sign");
				intentsign.setClass(UpdateInfo.this, UpdateNickSign.class);
				startActivity(intentsign);
				break;
			default:
				break;
				
			}

		}

	}

	// 点击性别的男
	public void setSelectSexManBtn(View v) {
		setSelectSexRl.setVisibility(View.GONE);//
		userSetSexTv.setText("男");
	}

	// 点击性别的女
	public void setSelectSexWomenBtn(View v) {
		setSelectSexRl.setVisibility(View.GONE);//
		userSetSexTv.setText("女");
	}
	// 点击性别的取消
	public void setSelectSexCancelBtn(View v) {
		setSelectSexRl.setVisibility(View.GONE);//
	}

	// 点击选择城市弹出选择器是ok
	public void setingSelectCityOkLv(View v) {
		userSetHomelandTv.setText(cityPicker.getCity_string() + "");
		setingOpenselectCityRl.setVisibility(View.GONE);
	}

	// 点击选择城市弹出选择器是cancal
	public void setingSelectCityCancelTv(View v) {
		setingOpenselectCityRl.setVisibility(View.GONE);
	}

	public void onBackBtn(View v) {
		finish();
	}

}
