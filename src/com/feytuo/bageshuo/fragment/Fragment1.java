package com.feytuo.bageshuo.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.feytuo.bageshuo.R;
import com.feytuo.bageshuo.activity.HomePersonSearch;
import com.feytuo.bageshuo.activity.HotHome;
import com.feytuo.bageshuo.activity.MyCenterViewpager;
import com.feytuo.bageshuo.activity.PersonsList;
import com.feytuo.bageshuo.activity.TopicList;
import com.feytuo.bageshuo.adapter.GridviewAdapter;
import com.feytuo.bageshuo.widget.HomeGridView;

/**
 * 社区模块的功能
 * 
 * @version v1.0
 * 
 * @data 2015-03-12
 * 
 * @author tangpeng
 * 
 */
public class Fragment1 extends Fragment {

	private ViewPager mViewPager;
	private List<ImageView> imageViewList;
	private TextView descriptionTv;// ViewPager的文字描述
	private LinearLayout pointsLl;// ViewPager的第几页..
	private String[] imageDescriptions;// 获取图片的信息
	private int previousSelectPosition = 0;
	private boolean isLoop = true;
	private HomeGridView mygridview;
	private List<Map<String, Object>> data_list;
	private GridviewAdapter gridviewadapter;
	private ImageView homeUserHead;// 用户头像
	private ImageView homeSearchIv;//点击搜索人获取社区
	private LinearLayout homeHothomeLl;// 探索更多感兴趣的社区布局
	private LinearLayout homeTopicPartLL;// 点击进入话题的布局
	private LinearLayout homePersonsPartLL;// 探索更多感兴趣的社区布局

	// 图片封装为一个数组
	private String[] icon = { "18", "99+", "2" };
	private String[] iconName = { "北京社区", "上海社区", "长沙社区" };

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fg1, container, false);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		initView();
		initViewViewpage();
		initViewGridview();
		super.onActivityCreated(savedInstanceState);
	}

	/**
	 * 普通控件的处理
	 */
	private void initView() {
		homeHothomeLl = (LinearLayout) getActivity().findViewById(
				R.id.home_hothome_ll);
		homeTopicPartLL = (LinearLayout) getActivity().findViewById(
				R.id.home_topic_part_ll);
		homePersonsPartLL = (LinearLayout) getActivity().findViewById(
				R.id.home_persons_part_ll);
		homeUserHead = (ImageView) getActivity().findViewById(
				R.id.home_user_head);
		homeSearchIv= (ImageView) getActivity().findViewById(
				R.id.home_search_iv);
		listener listen = new listener();
		homeHothomeLl.setOnClickListener(listen);
		homeTopicPartLL.setOnClickListener(listen);
		homePersonsPartLL.setOnClickListener(listen);
		homeUserHead.setOnClickListener(listen);
		homeSearchIv.setOnClickListener(listen);
	}

	class listener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
			switch (v.getId()) {
			case R.id.home_hothome_ll:
				intent.setClass(getActivity(), HotHome.class);
				break;
			case R.id.home_topic_part_ll:
				intent.setClass(getActivity(), TopicList.class);
				break;
			case R.id.home_persons_part_ll:
				intent.setClass(getActivity(), PersonsList.class);
				break;
			case R.id.home_user_head:
//				intent.setClass(getActivity(), ExcessiveViewPager.class);
				intent.setClass(getActivity(), MyCenterViewpager.class);
				break;
			case R.id.home_search_iv:
				intent.setClass(getActivity(), HomePersonSearch.class);
//				intent.setClass(getActivity(), MyCenterViewpager.class);
				break;

			default:
				break;
			}
			getActivity().startActivity(intent);
		}

	}

	/**
	 * gridview控件的处理
	 */
	private void initViewGridview() {
		// TODO Auto-generated method stub
		mygridview = (HomeGridView) getActivity().findViewById(R.id.home_gv);
		mygridview.setPadding(0, -10, 0, -10);
		// 新建List
		data_list = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < icon.length; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("homenum", icon[i]);
			map.put("hometitle", iconName[i]);
			data_list.add(map);
		}
		gridviewadapter = new GridviewAdapter(getActivity(), data_list);
		mygridview.setAdapter(gridviewadapter);

	}

	/**
	 * 使用一个viewpager轮播，自动切换页面功能.开启一个线程
	 */
	private void initViewViewpage() {

		new Thread(new Runnable() {
			@Override
			public void run() {
				while (isLoop) {
					SystemClock.sleep(10000);// 多少秒执行一次
					handler.sendEmptyMessage(0);
				}
			}
		}).start();

		mViewPager = (ViewPager) getActivity().findViewById(R.id.viewpager);
		descriptionTv = (TextView) getActivity().findViewById(
				R.id.description_tv);
		pointsLl = (LinearLayout) getActivity().findViewById(R.id.points_ll);

		prepareData();

		ViewPagerAdapter adapter = new ViewPagerAdapter();
		mViewPager.setAdapter(adapter);
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int arg0) {
				descriptionTv.setText(imageDescriptions[arg0
						% imageViewList.size()]);
				// 切换选中的点
				pointsLl.getChildAt(previousSelectPosition).setEnabled(false); // 把前一个点置为normal状态
				pointsLl.getChildAt(arg0 % imageViewList.size()).setEnabled(
						true); // 把当前选中的position对应的点置为enabled状态
				previousSelectPosition = arg0 % imageViewList.size();
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});

		descriptionTv.setText(imageDescriptions[previousSelectPosition]);
		pointsLl.getChildAt(previousSelectPosition).setEnabled(true);

		/**
		 * 2147483647 / 2 = 1073741820 - 1
		 */
		int n = Integer.MAX_VALUE / 2 % imageViewList.size();
		int itemPosition = Integer.MAX_VALUE / 2 - n;

		mViewPager.setCurrentItem(itemPosition);
	}

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
		}
	};

	/**
	 * viewpager中// 添加点view对象
	 */
	@SuppressWarnings("deprecation")
	private void prepareData() {
		imageViewList = new ArrayList<ImageView>();
		int[] imageResIDs = getImageResIDs();
		imageDescriptions = getImageDescription();

		ImageView iv;
		View view;
		for (int i = 0; i < imageResIDs.length; i++) {
			iv = new ImageView(getActivity());
			iv.setBackgroundResource(imageResIDs[i]);
			imageViewList.add(iv);

			// 添加点view对象
			view = new View(getActivity());
			view.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.point_background));
			LayoutParams lp = new LayoutParams(5, 5);
			lp.leftMargin = 10;
			view.setLayoutParams(lp);
			view.setEnabled(false);
			pointsLl.addView(view);
		}
	}

	class ViewPagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return Integer.MAX_VALUE;
		}

		/**
		 * 判断出去的view是否等于进来的view 如果为true直接复用
		 */
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		/**
		 * 销毁预加载以外的view对象, 会把需要销毁的对象的索引位置传进来就是position
		 */
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(imageViewList.get(position
					% imageViewList.size()));
		}

		/**
		 * 创建一个view
		 */
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			container
					.addView(imageViewList.get(position % imageViewList.size()));
			return imageViewList.get(position % imageViewList.size());
		}

	}

	private int[] getImageResIDs() {
		return new int[] { R.drawable.lunbo, R.drawable.lunbo,
				R.drawable.lunbo, R.drawable.lunbo, R.drawable.lunbo };
	}

	private String[] getImageDescription() {
		return new String[] { "一", "二", "三", "四", "五" };
	}
}
