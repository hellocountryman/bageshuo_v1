package com.feytuo.bageshuo.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.feytuo.bageshuo.R;

public class InvitationDetailsAdapter extends BaseAdapter {
	private final static String TAG = "INVITATIONDETAILSADAPTER";
	private Context context;
	private List<Map<String, Object>> data;
	private LayoutInflater inflater;
	final int VIEW_TYPE = 2;// 一共是使用2个布局
	final int TYPE_1 = 0;
	final int TYPE_2 = 1;
	viewHolder1 holder1 = null;
	viewHolder2 holder2 = null;

	public InvitationDetailsAdapter(Context context,
			List<Map<String, Object>> data) {
		// TODO Auto-generated constructor stub
		this.data = data;
		this.context = context;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size() + 1;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return data.get(position);
	}

	// getViewTypeCount() – 该方法返回多少个不同的布局

	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return VIEW_TYPE;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
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
	public View getView(int position, View convertView, ViewGroup parent) {
		int type = getItemViewType(position);// 判断使用的布局

		if (convertView == null) {
			Log.i(TAG, "if" + convertView);
			switch (type) {
			case TYPE_1:
				convertView = inflater.inflate(R.layout.home_invitation_item,
						null);// 同样是将布局转化成view
				holder1 = new viewHolder1();
				holder1.homeDetailsUserheadIv = (ImageView) convertView
						.findViewById(R.id.home_details_userhead_iv);
				holder1.HomeDetailsUsernickTv = (TextView) convertView
						.findViewById(R.id.home_details_usernick_tv);
				holder1.homeDetailsUsersexIv = (ImageView) convertView
						.findViewById(R.id.home_details_usersex_iv);
				holder1.homeDetailsTimeTv = (TextView) convertView
						.findViewById(R.id.home_details_time_tv);
				holder1.homeDetailsInvitationTitleTv = (TextView) convertView
						.findViewById(R.id.home_details_invitation_title_tv);
				holder1.homeDetailsPalyIv = (ImageView) convertView
						.findViewById(R.id.home_details_paly_iv);
				convertView.setTag(holder1);
				break;
			case TYPE_2:
				convertView = inflater.inflate(
						R.layout.home_invitation_comment_item, null);
				// 实例化holder以及控件

				holder2 = new viewHolder2();
				holder2.invitationCommentUserHeadIv = (ImageView) convertView
						.findViewById(R.id.home_details_userhead_iv);
				holder2.invitationCommentNickTv = (TextView) convertView
						.findViewById(R.id.home_details_usernick_tv);
				holder2.invitationCommentSexIv = (ImageView) convertView
						.findViewById(R.id.home_details_usersex_iv);
				holder2.inivtationCommentTitleTv = (TextView) convertView
						.findViewById(R.id.home_details_invitation_title_tv);
				holder2.invitationCommentPlayBtn = (ImageView) convertView
						.findViewById(R.id.home_details_paly_iv);
				convertView.setTag(holder2);

				break;
			}
		} else {
			Log.i(TAG, "else" + convertView);
			switch (type) {
			case TYPE_1:
				holder1 = (viewHolder1) convertView.getTag();
				break;
			case TYPE_2:
				holder2 = (viewHolder2) convertView.getTag();
				break;
			}

		}
		// 可以对控件进行操作
		switch (type) {
		case TYPE_1:

			dealtype1(position);

			break;
		case TYPE_2:
			dealtype2(position);

			break;

		}

		return convertView;

	}

	
	/**
	 * 给帖子赋值
	 * @param position
	 */
	private void dealtype1(int position) {
		// TODO Auto-generated method stub
		holder1.homeDetailsInvitationTitleTv.setText(data.get(position)
				.get("hometitle").toString());
		holder1.homeDetailsUserheadIv.setImageDrawable(context.getResources()
				.getDrawable(
						Integer.valueOf(data.get(position).get("homeuerhead")
								.toString())));
	}

	/**
	 * 给帖子的评论赋值
	 * @param position
	 */
	private void dealtype2(int position) {
		// TODO Auto-generated method stub
		holder2.inivtationCommentTitleTv.setText(data.get(position - 1)
				.get("hometitle").toString());
		holder2.invitationCommentUserHeadIv.setImageDrawable(context
				.getResources().getDrawable(
						Integer.valueOf(data.get(position - 1)
								.get("homeuerhead").toString())));
	}

	
	class viewHolder1 {
		private TextView homeDetailsTimeTv;// 发布的时间
		private TextView homeDetailsInvitationTitleTv;// 发布的文字
		private ImageView homeDetailsPalyIv;// 录音的播放
		private TextView HomeDetailsUsernickTv;// 用户的昵称
		private ImageView homeDetailsUsersexIv;// 用户的性别
		private ImageView homeDetailsUserheadIv;// 用户的头像
	}

	class viewHolder2 {
		private ImageView invitationCommentUserHeadIv;// 评论人的头像
		private TextView invitationCommentNickTv;// 评论人的昵称
		private ImageView invitationCommentSexIv;// 评论人的性别
		private TextView inivtationCommentTitleTv;// 评论的内容
		private TextView inivtationCommentTimeTv;// 评论的时间
		private ImageView invitationCommentPlayBtn;// 评论的语音按钮
		private TextView invitationCommentPlaytv;// 评论语音的时长
	}

}
