package com.feytuo.bageshuo.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.feytuo.bageshuo.R;
import com.feytuo.bageshuo.activity.BageshuoTranslate;
import com.feytuo.bageshuo.activity.InvitationDetails;
import com.feytuo.bageshuo.util.ToolAnimation;

/**
 * 八哥说的适配器，注意有2段布局
 * 
 * @date 2015-03-25
 * 
 * @version v1.0
 * 
 * @author tangpeng
 * 
 */
public class BageshuoAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater inflater;
	private int resource;
	private List<? extends Map<String, ?>> list;// 声明List容器对象
	final int VIEW_TYPE = 2;// 一共是使用2个布局
	final int TYPE_1 = 0;
	final int TYPE_2 = 1;
	NewViewHolder1 holder1 = null;
	NewViewHolder2 holder2 = null;

	public BageshuoAdapter(Context context, List<? extends Map<String, ?>> data) {
		this.list = data;
		this.context = context;
		inflater = LayoutInflater.from(context);
		// goodMap = new SparseArray<>();
		// LayoutInflater作用是将layout的xml布局文件实例化为View类对象。
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return list.size() + 1;
	}

	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return VIEW_TYPE;
	}

	// getItemViewType(int) – 根据position返回相应的Item
	public int getItemViewType(int position) {
		int p = position;
		if (p == 0) {
			return TYPE_1;
		} else {
			return TYPE_2;
		}
	}

	@Override
	// 获取listitem下面的view的值
	public View getView(int position, View convertView, ViewGroup parent) {
		int type = getItemViewType(position);// 判断使用的布局
		if (convertView == null) {
			switch (type) {
			case TYPE_1:
				convertView = inflater
						.inflate(R.layout.bageshuo_top_part, null);// 同样是将布局转化成view
				holder1 = new NewViewHolder1();
				holder1.bageshuoSearchTv = (TextView) convertView
						.findViewById(R.id.bageshuo_search_tv);

				convertView.setTag(holder1);
				break;
			case TYPE_2:
				convertView = inflater.inflate(R.layout.bageshuo_item,
						null);
				holder2 = new NewViewHolder2();
				holder2.homeDetailsTimeBottomTv = (TextView) convertView
						.findViewById(R.id.home_details_time_bottom_tv);
				
				holder2.homeDetailsAnswerBtn=(Button)convertView.findViewById(R.id.home_details_answer_btn);
				holder2.homeDetailsUserheadLv = (ImageView) convertView
						.findViewById(R.id.home_details_userhead_iv);

				holder2.HomeDetailsUsernickTv = (TextView) convertView
						.findViewById(R.id.home_details_usernick_tv);

				holder2.homeDetailsUsersexIv = (ImageView) convertView
						.findViewById(R.id.home_details_usersex_iv);

				holder2.homeDetailsInvitationTitleTv = (TextView) convertView
						.findViewById(R.id.home_details_invitation_title_tv);

				holder2.homeDetailsPalyIv = (ImageView) convertView
						.findViewById(R.id.home_details_paly_iv);
				convertView.setTag(holder2);
				break;

			}
			// Tag从本质上来讲是就是相关联的view的额外的信息。它们经常用来存储一些view的数据，这样做非常方便而不用存入另外的单独结构。
		} else {
			switch (type) {
			case TYPE_1:
				holder1 = (NewViewHolder1) convertView.getTag();
				break;
			case TYPE_2:
				holder2 = (NewViewHolder2) convertView.getTag();
				break;
			}
		}
		switch (type) {
		case TYPE_1:

			dealtype1(position);

			break;
		case TYPE_2:
			dealtype2(position, convertView);
			break;
		}

		return convertView;
	}

	/**
	 * 社区logo，介绍，帖子数目，关注处理
	 */
	private void dealtype1(int position) {

		listener1 listen1 = new listener1(holder1, position);
		holder1.bageshuoSearchTv.setOnClickListener(listen1);

	}

	/**
	 * 社区的帖子处理
	 */
	/**
	 * @param position
	 * @param convertView
	 */
	private void dealtype2(int position, View convertView) {
		listener2 listen2 = new listener2(holder2, position);

		holder2.homeDetailsUserheadLv.setImageDrawable(context.getResources()
				.getDrawable(
						Integer.valueOf(list.get(position - 1)
								.get("homeuerhead").toString())));
		holder2.homeDetailsInvitationTitleTv.setText(Html.fromHtml("”今天早上吃饭没“<font color=red>长沙话</font>怎么说？")); 
//		holder2.homeDetailsInvitationTitleTv.setText(list.get(position - 1)
//				.get("hometitle").toString());
		holder2.homeDetailsUserheadLv.setOnClickListener(listen2);
		holder2.homeDetailsInvitationTitleTv.setOnClickListener(listen2);
		holder2.homeDetailsPalyIv.setOnClickListener(listen2);
		holder2.homeDetailsPalyIv.setOnClickListener(listen2);
		convertView.setClickable(true);
		convertView.setOnClickListener(listen2);
	}

	class listener1 implements OnClickListener {

		private int position;
		private NewViewHolder1 holder1;

		public listener1(NewViewHolder1 holder, int position) {
			this.position = position;
			this.holder1 = holder1;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {

			default:
				Intent intent = new Intent();
				intent.setClass(context, BageshuoTranslate.class);
				context.startActivity(intent);

				break;
			}
		}

	}

	class listener2 implements OnClickListener {

		private int position;
		private NewViewHolder2 holder2;

		public listener2(NewViewHolder2 holder, int position) {
			this.position = position;
			this.holder2 = holder;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.home_details_userhead_iv:
				Toast.makeText(context, "你点击了用户的头像第" + position + "个",
						Toast.LENGTH_LONG).show();
				break;
			case R.id.home_details_paly_iv:
				ToolAnimation.animationdeal(context, holder2.homeDetailsPalyIv);
				break;
			default:
				Intent intent = new Intent();
				intent.setClass(context, InvitationDetails.class);
				context.startActivity(intent);

				break;
			}
		}

	}

	/**
	 * 社区logo，介绍，帖子数目，关注
	 */
	class NewViewHolder1 {

		private TextView bageshuoSearchTv;// 搜索的按钮

	}

	/**
	 * 社区的帖子列表
	 */
	class NewViewHolder2 {
		private TextView homeDetailsInvitationTitleTv;// 发布的文字
		private ImageView homeDetailsPalyIv;// 发布的按钮
		private TextView HomeDetailsUsernickTv;// 用户的昵称
		private ImageView homeDetailsUsersexIv;// 用户的性别
		private ImageView homeDetailsUserheadLv;// 用户的头像
		private TextView homeDetailsTimeBottomTv;// 在item底部显示的时间
		private int position;
		private Button homeDetailsAnswerBtn;//我来纠正，我来回答按钮
	}

}
