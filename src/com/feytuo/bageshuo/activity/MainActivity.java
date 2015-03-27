package com.feytuo.bageshuo.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.feytuo.bageshuo.R;
import com.feytuo.bageshuo.R.color;
import com.feytuo.bageshuo.R.drawable;
import com.feytuo.bageshuo.R.id;
import com.feytuo.bageshuo.R.layout;
import com.feytuo.bageshuo.fragment.Fragment1;
import com.feytuo.bageshuo.fragment.Fragment2;
import com.feytuo.bageshuo.fragment.Fragment3;
import com.feytuo.bageshuo.fragment.Fragment4;
import com.feytuo.bageshuo.util.DisplayUtil;

/**
 * app主页
 * 
 * @version v1.0
 * 
 * @data 2015-03-12
 * 
 * @author tangpeng
 * 
 */
@SuppressLint("ResourceAsColor")
public class MainActivity extends FragmentActivity {

	private final static String TAG="MAINACTIVITY";
	// 定义3个Fragment的对象
	private Fragment1 fg1;
	private Fragment2 fg2;
	private Fragment3 fg3;
	private Fragment4 fg4;
	// 帧布局对象,就是用来存放Fragment的容器
	private FrameLayout flayout;
	// 定义底部导航栏的四个布局
	private RelativeLayout indexBottomBommunityRl;
	private RelativeLayout indexBottomBageshuoRl;
	private RelativeLayout indexBottomFindRl;
	private RelativeLayout indexBottomFriendRl;
	// 定义底部导航栏中的ImageView与TextView
	private ImageView indexBottomCommunityIv;
	private ImageView indexBottomBageshuoIv;
	private ImageView indexBottomFindIv;
	private ImageView indexBottomFriendIv;

	private TextView indexBottomCommunityTv;
	private TextView indexBottomBageshuoTv;
	private TextView indexBottomFindTv;
	private TextView indexBottomFriendTv;

	// 定义FragmentManager对象
	FragmentManager fManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		fManager = getSupportFragmentManager();
		initViews();
		
		 int sp=DisplayUtil.px2sp(this, 20);
//		int sp=DisplayUtil.px2sp(this, 30);==15
//		int sp=DisplayUtil.px2sp(this, 30);==15
		 Toast.makeText(this, sp+"lala", Toast.LENGTH_LONG).show();
	}
	

	/**
	 * 完成组件的初始化
	 */
	public void initViews() {
		indexBottomCommunityIv = (ImageView) findViewById(R.id.index_bottom_community_iv);
		indexBottomBageshuoIv = (ImageView) findViewById(R.id.index_bottom_bageshuo_iv);
		indexBottomFindIv = (ImageView) findViewById(R.id.index_bottom_find_iv);
		indexBottomFriendIv = (ImageView) findViewById(R.id.index_bottom_friend_iv);

		indexBottomCommunityTv = (TextView) findViewById(R.id.index_bottom_community_tv);
		indexBottomBageshuoTv = (TextView) findViewById(R.id.index_bottom_bageshuo_tv);
		indexBottomFindTv = (TextView) findViewById(R.id.index_bottom_find_tv);
		indexBottomFriendTv = (TextView) findViewById(R.id.index_bottom_friend_tv);

		indexBottomBommunityRl = (RelativeLayout) findViewById(R.id.index_bottom_community_rl);
		indexBottomBageshuoRl = (RelativeLayout) findViewById(R.id.index_bottom_bageshuo_rl);
		indexBottomFindRl = (RelativeLayout) findViewById(R.id.index_bottom_find_rl);
		indexBottomFriendRl = (RelativeLayout) findViewById(R.id.index_bottom_friend_rl);

		indexBottomBommunityRl.setOnClickListener(btnListener);
		indexBottomBageshuoRl.setOnClickListener(btnListener);
		indexBottomFindRl.setOnClickListener(btnListener);
		indexBottomFriendRl.setOnClickListener(btnListener);
		
		setChioceItem(0);//默认进入社区版块
	}

	
	
	
	/**
	 * 按钮的监听事件
	 */
	private OnClickListener btnListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.index_bottom_community_rl:
				setChioceItem(0);
				break;
			case R.id.index_bottom_bageshuo_rl:
				setChioceItem(1);
				break;
			case R.id.index_bottom_find_rl:
				setChioceItem(2);
				break;
			case R.id.index_bottom_friend_rl:
				setChioceItem(3);
				break;

			default:
				break;
			}
		}
	};

	/**
	 * 定义一个选中一个item后的处理
	 */
	public void setChioceItem(int index) {
		// 重置选项+隐藏所有Fragment
		FragmentTransaction transaction = fManager.beginTransaction();
		clearChioce();
		hideFragments(transaction);
		switch (index) {
		case 0:

			indexBottomCommunityIv.setImageResource(R.drawable.ic_home_press);
			indexBottomCommunityTv.setTextColor(this.getResources().getColor(R.color.index_botton_text_press));
			if (fg1 == null) {
				// 如果fg1为空，则创建一个并添加到界面上
				fg1 = new Fragment1();
				transaction.add(R.id.content, fg1);
			} else {
				// 如果MessageFragment不为空，则直接将它显示出来
				transaction.show(fg1);
			}
			break;

		case 1:
			
			indexBottomBageshuoIv.setImageResource(R.drawable.ic_chat_press);
			indexBottomBageshuoTv.setTextColor(this.getResources().getColor(R.color.index_botton_text_press));

			if (fg2 == null) {
				// 如果fg1为空，则创建一个并添加到界面上
				fg2 = new Fragment2();
				transaction.add(R.id.content, fg2);
			} else {
				// 如果MessageFragment不为空，则直接将它显示出来
				transaction.show(fg2);
			}
			break;

		case 2:
			
			indexBottomFindIv.setImageResource(R.drawable.ic_detect_press);
			indexBottomFindTv.setTextColor(this.getResources().getColor(R.color.index_botton_text_press));
			if (fg3 == null) {
				// 如果fg1为空，则创建一个并添加到界面上
				fg3 = new Fragment3();
				transaction.add(R.id.content, fg3);
			} else {
				// 如果MessageFragment不为空，则直接将它显示出来
				transaction.show(fg3);
			}
			break;
			
		case 3:

			indexBottomFriendIv.setImageResource(R.drawable.ic_friends_press);
			indexBottomFriendTv.setTextColor(this.getResources().getColor(R.color.index_botton_text_press));

			if (fg4 == null) {
				// 如果fg1为空，则创建一个并添加到界面上
				fg4 = new Fragment4();
				transaction.add(R.id.content, fg4);
			} else {
				// 如果MessageFragment不为空，则直接将它显示出来
				transaction.show(fg4);
			}
			break;
		}
		transaction.commit();
	}

	/**
	 * 隐藏所有的Fragment,避免fragment混乱
	 */
	private void hideFragments(FragmentTransaction transaction) {
		if (fg1 != null) {
			transaction.hide(fg1);
		}
		if (fg2 != null) {
			transaction.hide(fg2);
		}
		if (fg3 != null) {
			transaction.hide(fg3);
		}
		if (fg4 != null) {
			transaction.hide(fg4);
		}
	}

	/**
	 * 定义一个重置所有选项的方法
	 */
	public void clearChioce() {

		indexBottomCommunityIv.setImageResource(R.drawable.ic_home_normal);
		indexBottomCommunityTv.setTextColor(R.color.index_botton_text_normal);

		indexBottomBageshuoIv.setImageResource(R.drawable.ic_chat_normal);
		indexBottomBageshuoTv.setTextColor(R.color.index_botton_text_normal);
		
		indexBottomFindIv.setImageResource(R.drawable.ic_detect_normal);
		indexBottomFindTv.setTextColor(R.color.index_botton_text_normal);

		indexBottomFriendIv.setImageResource(R.drawable.ic_friends_normal);
		indexBottomFriendTv.setTextColor(R.color.index_botton_text_normal);
	}

}
