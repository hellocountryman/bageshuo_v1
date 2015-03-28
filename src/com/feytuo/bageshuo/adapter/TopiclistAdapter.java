package com.feytuo.bageshuo.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.feytuo.bageshuo.R;
import com.feytuo.bageshuo.activity.TopicDetails;

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
public class TopiclistAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater inflater;
	private int resource;
	private List<? extends Map<String, ?>> list;// 声明List容器对象

	public TopiclistAdapter(Context context, List<? extends Map<String, ?>> data) {
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

			convertView = inflater.inflate(R.layout.topic_list_item, null);
			holder2 = new NewViewHolder2();

			holder2.topiclistIv = (ImageView) convertView
					.findViewById(R.id.topiclist_iv);

			holder2.topiclistTitleTv = (TextView) convertView
					.findViewById(R.id.topiclist_title_tv);
			holder2.topiclistTimeTv = (TextView) convertView
					.findViewById(R.id.topiclist_time_tv);
			holder2.topiclistLooknumtv = (TextView) convertView
					.findViewById(R.id.topiclist_looknum_tv);
			convertView.setTag(holder2);

			// Tag从本质上来讲是就是相关联的view的额外的信息。它们经常用来存储一些view的数据，这样做非常方便而不用存入另外的单独结构。
		} else {
			holder2 = (NewViewHolder2) convertView.getTag();

		}

		dealtype2(position, convertView, holder2);

		return convertView;
	}

	/**
	 * 社区的帖子处理
	 */
	private void dealtype2(int position, View convertView,
			NewViewHolder2 holder2) {
		listener2 listen2 = new listener2(holder2, position);
		holder2.topiclistIv.setImageDrawable(context.getResources()
				.getDrawable(
						Integer.valueOf(list.get(position).get("arrayico")
								.toString())));
		holder2.topiclistTitleTv.setText(list.get(position).get("arraytitle")
				.toString());
		holder2.topiclistTitleTv.setOnClickListener(listen2);

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
			default:
				Intent intent = new Intent();
				intent.setClass(context, TopicDetails.class);
				context.startActivity(intent);

				break;
			}
		}

	}

	/**
	 * 社区的帖子列表
	 */
	class NewViewHolder2 {
		private TextView topiclistTitleTv;// 话题列表的标题
		private TextView topiclistTimeTv;// 话题列表的发布时间
		private ImageView topiclistIv;// 话题列表的图片
		private TextView topiclistLooknumtv;// 话题列表的参与人数
		private int position;
	}

}
