package com.feytuo.bageshuo.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.feytuo.bageshuo.R;

public class Fragment4 extends Fragment {
	
	

	private final String TAG = "ChatAndContactFragment";
	private Fragment[] fragments;
	private FriendTwoFragment friendTwoFragment;
	private FriendOneFragment friendOneFragment;

	private ViewPager viewPager;
	private ImageView cursorImage;
	private Button friendOneBtn;
	private Button friendTwoBtn;

	private int cursorOffset;// 每一格偏移量
	private int currentOffset;// 当前总偏移量
	private int currentTabInCAC;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fg4, container,false);
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
		initViewPager();
		// 初始化view
		initView();
		// 初始化滑动条
		initCursor();
	}
	
	private void initViewPager() {
		// TODO Auto-generated method stub
		friendOneFragment = new FriendOneFragment();
		friendTwoFragment = new FriendTwoFragment();
		fragments = new Fragment[] { friendOneFragment, friendTwoFragment };
		viewPager = (ViewPager)getActivity().findViewById(R.id.contact_viewpager);
		viewPager.setAdapter(new MyFragmentPagerAdapter(getChildFragmentManager()));
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			private int currentPager;
			@Override
			public void onPageSelected(int arg0) {//当页面改变时候
				if(arg0 == 0){
					leftcolor();
				}else{
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

	@SuppressLint("ResourceAsColor")
	private void initView() {
		// TODO Auto-generated method stub
		friendOneBtn = (Button)getActivity().findViewById(R.id.friend_one_btn);
		friendTwoBtn = (Button) getActivity().findViewById(R.id.friend_two_btn);
		cursorImage = (ImageView)getActivity().findViewById(R.id.cursor);
		// 进入添加好友页
		friendOneBtn.setOnClickListener(listener);
		friendTwoBtn.setOnClickListener(listener);
	}

	/**
	 * viewpager在左边的时候
	 */
	private void leftcolor() {
		friendOneBtn.setTextColor(getResources().getColor(R.color.index_color));
		friendTwoBtn.setTextColor(getResources().getColor(R.color.grey));
	}

	/**
	 * viewpager在右边的时候
	 */
	private void rightcolor() {
		friendOneBtn.setTextColor(getResources().getColor(R.color.grey));
		friendTwoBtn.setTextColor(getResources().getColor(R.color.index_color));
	}

	private void initCursor() {
		// TODO Auto-generated method stub
		// 获取屏幕分辨率宽度
		DisplayMetrics dm = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
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
			case R.id.friend_one_btn: // 会话按钮
				leftcolor();
				setCursor(viewPager.getCurrentItem(), 0);
				viewPager.setCurrentItem(0, false);
				currentTabInCAC = 0;
				break;
			case R.id.friend_two_btn:// 好友列表按钮
				rightcolor();
				setCursor(viewPager.getCurrentItem(), 1);
				viewPager.setCurrentItem(1, false);
				currentTabInCAC = 1;
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
}
