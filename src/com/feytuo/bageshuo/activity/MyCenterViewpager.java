package com.feytuo.bageshuo.activity;

import org.w3c.dom.Text;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.wifi.p2p.WifiP2pManager.UpnpServiceResponseListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.feytuo.bageshuo.R;
import com.feytuo.bageshuo.R.color;
import com.feytuo.bageshuo.R.drawable;
import com.feytuo.bageshuo.R.id;
import com.feytuo.bageshuo.R.layout;
import com.feytuo.bageshuo.fragment.CenterOneFragment;
import com.feytuo.bageshuo.fragment.CenterTwoFragment;
import com.feytuo.bageshuo.util.BitmapUtil;

public class MyCenterViewpager extends FragmentActivity {
	private final String TAG = "ChatAndContactFragment";
	private Fragment[] fragments;
	private CenterOneFragment centerOneFragment;
	private CenterTwoFragment centerTwoFragment;

	private ViewPager viewPager;
	private ImageView cursorImage;//
	private Button centerOneBtn;// 帖子按钮
	private Button centerTwoBtn;// 动态按钮

	private int cursorOffset;// 每一格偏移量
	private int currentOffset;// 当前总偏移量
	private int currentTabInCAC;

	private TextView centerUpdateinfoTv;// 修改个人信息的按钮
	private ImageView mycenterUserHead;// 用户的头像圆角

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_center_viewpager);

		initView();
		initViewPager();
		initCursor();
	}

	private void initView() {

		TextView titleTv = (TextView) findViewById(R.id.top_bar_title_set_tv);
		titleTv.setText("个人信息");// 设置标题；

		// viewpager的滑动页面
		centerOneBtn = (Button) findViewById(R.id.center_one_btn);
		centerTwoBtn = (Button) findViewById(R.id.center_two_btn);
		cursorImage = (ImageView) findViewById(R.id.cursor);

		mycenterUserHead = (ImageView) findViewById(R.id.center_user_head_iv);
		// 将获取到的头像转化成圆形
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.a01);
		Bitmap bitmapround = BitmapUtil.toRoundBitmap(bitmap);
		BitmapDrawable bd = new BitmapDrawable(bitmapround);
		mycenterUserHead.setBackgroundDrawable(bd);

		centerUpdateinfoTv = (TextView) findViewById(R.id.center_updateinfo_tv);

		centerUpdateinfoTv.setOnClickListener(listener);
		centerOneBtn.setOnClickListener(listener);
		centerTwoBtn.setOnClickListener(listener);
	}

	/**
	 * 初始化viewpager
	 */
	private void initViewPager() {
		// TODO Auto-generated method stub
		centerOneFragment = new CenterOneFragment();
		centerTwoFragment = new CenterTwoFragment();
		fragments = new Fragment[] { centerOneFragment, centerTwoFragment };
		viewPager = (ViewPager) findViewById(R.id.contact_viewpager);
		viewPager.setAdapter(new MyFragmentPagerAdapter(
				getSupportFragmentManager()));
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			private int currentPager;

			@Override
			public void onPageSelected(int arg0) {// 当页面改变时候
				if (arg0 == 0) {
					leftcolor();
				} else {
					rightcolor();
				}
			}

			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				if (currentPager != arg0) {
					currentOffset = arg0 * cursorOffset;
				} else {
					final LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) cursorImage
							.getLayoutParams();
					if (arg0 == 0 && arg2 == 0) {
						params.setMargins((int) (currentOffset + cursorOffset
								* arg1), 0, 0, 0);
					} else {
						params.setMargins((int) (currentOffset + cursorOffset
								* arg1) + 1, 0, 0, 0);
					}
					// 首次加载后不会刷新，必须强制放到ui线程刷新ui
					cursorImage.post(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							cursorImage.setLayoutParams(params);
						}
					});
				}
				currentPager = arg0;
				currentTabInCAC = arg0;
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
	}

	/**
	 * viewpager在左边的时候
	 */
	private void leftcolor() {
		centerOneBtn.setTextColor(getResources().getColor(R.color.index_color));
		centerTwoBtn.setTextColor(getResources().getColor(R.color.dynamictext));
	}

	/**
	 * viewpager在右边的时候
	 */
	private void rightcolor() {

		centerOneBtn.setTextColor(getResources().getColor(R.color.dynamictext));
		centerTwoBtn.setTextColor(getResources().getColor(R.color.index_color));
	}

	/**
	 * 初始化滑动条
	 */
	private void initCursor() {
		// TODO Auto-generated method stub
		// 获取屏幕分辨率宽度
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;
		cursorOffset = screenW / 2;

		LayoutParams params = cursorImage.getLayoutParams();
		params.width = cursorOffset;
		cursorImage.setLayoutParams(params);
		currentTabInCAC = 0;
	}

	private OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.center_one_btn: // 会话按钮
				leftcolor();
				setCursor(viewPager.getCurrentItem(), 0);
				viewPager.setCurrentItem(0, false);
				currentTabInCAC = 0;
				break;
			case R.id.center_two_btn:// 好友列表按钮
				rightcolor();
				setCursor(viewPager.getCurrentItem(), 1);
				viewPager.setCurrentItem(1, false);
				currentTabInCAC = 1;
				break;
			case R.id.center_updateinfo_tv:// 好友列表按钮
				Intent intentupdate = new Intent();
				intentupdate.setClass(MyCenterViewpager.this, UpdateInfo.class);
				startActivity(intentupdate);
				break;
			}
		}
	};

	class MyFragmentPagerAdapter extends FragmentPagerAdapter {

		public MyFragmentPagerAdapter(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
		}

		@Override
		public Fragment getItem(int arg0) {
			// TODO Auto-generated method stub
			return fragments[arg0];
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return fragments.length;
		}
	}

	/**
	 * 点击时滑动块移动
	 */
	private void setCursor(int currentNum, int targetNum) {
		int offsetNum = 0;
		offsetNum = targetNum - currentNum;
		currentOffset = currentOffset + offsetNum * cursorOffset;
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) cursorImage
				.getLayoutParams();
		params.setMargins(currentOffset, 0, 0, 0);
		cursorImage.requestLayout();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			this.finish();
		}
		return false;
	}

	public void publishBackSettingBtn(View v) {
		finish();
	}

	public void setting_ok(View v) {

		Intent intentset = new Intent();
		intentset.setClass(MyCenterViewpager.this, AppSetting.class);
		startActivity(intentset);
	}
}
