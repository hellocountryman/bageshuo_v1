package com.feytuo.bageshuo.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.feytuo.bageshuo.R;
import com.feytuo.bageshuo.activity.InvitationDetails;
import com.feytuo.bageshuo.util.BitmapUtil;
import com.feytuo.bageshuo.util.ToolAnimation;

/**
 * 个人中心适配器
 * 
 * @date 2015-03-25
 * 
 * @version v1.0
 * 
 * @author tangpeng
 * 
 */
public class MyCenterAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater inflater;
	private int resource;
	private List<? extends Map<String, ?>> list;// 声明List容器对象
	public MyCenterAdapter(Context context, List<? extends Map<String, ?>> data) {
		this.list = data;
		this.context = context;
		inflater = LayoutInflater.from(context);
		// goodMap = new SparseArray<>();
		// LayoutInflater作用是将layout的xml布局文件实例化为View类对象。
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	// 获取listitem下面的view的值
	public View getView(int position, View convertView, ViewGroup parent) {
		NewViewHolder2 holder2 = null;
		if (convertView == null) {

			convertView = inflater.inflate(R.layout.home_invitation_item, null);
			holder2 = new NewViewHolder2();

			holder2.homeDetailsUserheadLv = (ImageView) convertView
					.findViewById(R.id.home_details_userhead_iv);

			holder2.HomeDetailsUsernickTv = (TextView) convertView
					.findViewById(R.id.home_details_usernick_tv);

			holder2.homeDetailsUsersexIv = (ImageView) convertView
					.findViewById(R.id.home_details_usersex_iv);
			holder2.homeDetailsTimeTv = (TextView) convertView
					.findViewById(R.id.home_details_time_tv);

			holder2.homeDetailsInvitationTitleTv = (TextView) convertView
					.findViewById(R.id.home_details_invitation_title_tv);

			holder2.homeDetailsPalyIv = (ImageView) convertView
					.findViewById(R.id.home_details_paly_iv);
			convertView.setTag(holder2);

			// Tag从本质上来讲是就是相关联的view的额外的信息。它们经常用来存储一些view的数据，这样做非常方便而不用存入另外的单独结构。
		} else {
			holder2 = (NewViewHolder2) convertView.getTag();

		}

		dealtype2(position, convertView,holder2);

		return convertView;
	}

	/**
	 * 社区的帖子处理
	 */
	private void dealtype2(int position, View convertView,NewViewHolder2 holder2) {
		listener2 listen2 = new listener2(holder2, position);
		holder2.homeDetailsUserheadLv.setImageDrawable(context.getResources()
				.getDrawable(
						Integer.valueOf(list.get(position).get("homeuerhead")
								.toString())));
		holder2.homeDetailsInvitationTitleTv.setText(list.get(position)
				.get("hometitle").toString());
		holder2.homeDetailsUserheadLv.setOnClickListener(listen2);
		holder2.homeDetailsInvitationTitleTv.setOnClickListener(listen2);
		holder2.homeDetailsPalyIv.setOnClickListener(listen2);
		holder2.homeDetailsPalyIv.setOnClickListener(listen2);
		convertView.setClickable(true);
		convertView.setOnClickListener(listen2);
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
	 * 社区的帖子列表
	 */
	class NewViewHolder2 {
		private TextView homeDetailsTimeTv;// 发布的时间
		private TextView homeDetailsInvitationTitleTv;// 发布的文字
		private ImageView homeDetailsPalyIv;// 发布的按钮
		private TextView HomeDetailsUsernickTv;// 用户的昵称
		private ImageView homeDetailsUsersexIv;// 用户的性别
		private ImageView homeDetailsUserheadLv;// 用户的头像
		private int position;
	}

}
