package com.feytuo.bageshuo.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.feytuo.bageshuo.R;
import com.feytuo.bageshuo.model.FindCardModel;

/**
 * 
 * @author feytuo
 * 
 * 
 */
public class FindbsAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater m_Inflater;
	private int resource;
	private ArrayList<FindCardModel> list;// 声明List容器对象

	public FindbsAdapter(Context context,
			ArrayList<FindCardModel> dlist) {
		this.list = dlist;
		this.context = context;
		m_Inflater = LayoutInflater.from(context);
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
		NewViewHolder holder = null;
		if (convertView == null) {
			convertView = m_Inflater.inflate(R.layout.find_card_inner, null);
//			convertView = m_Inflater.inflate(R.layout.item, parent, false);
//			convertView.setBackgroundResource(R.drawable.recordbtn_10);
			holder = new NewViewHolder();

			holder.play = (ImageButton) convertView.findViewById(R.id.play);
			holder.bgimage=(ImageView)convertView.findViewById(R.id.bgimage);
			holder.helloText = (TextView) convertView.findViewById(R.id.title);
			convertView.setTag(holder);
			// Tag从本质上来讲是就是相关联的view的额外的信息。它们经常用来存储一些view的数据，这样做非常方便而不用存入另外的单独结构。
		} else {
			holder = (NewViewHolder) convertView.getTag();
		}
		
		Log.i("tangpeng", "就是看看进来了没有+"+position);
		
		listener listen = new listener(holder, position);

		holder.helloText.setText(list.get(position).getTitle().toString());
		holder.play.setOnClickListener(listen);
		holder.bgimage.setOnClickListener(listen);

		return convertView;
	}

	class listener implements OnClickListener {

		private int position;
		private NewViewHolder holder;

		public listener(NewViewHolder holder, int position) {
			this.position = position;
			this.holder = holder;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.play:
				Toast.makeText(context, "点击了播放按钮"+position, Toast.LENGTH_LONG).show();
				break;
			case R.id.bgimage:
				Toast.makeText(context, "点击背景进入帖子详情", Toast.LENGTH_LONG)
						.show();
				break;

			default:

				break;
			}
		}

	}

	class NewViewHolder {
		private TextView helloText;
		private ImageButton play;//点击播放按钮
		private int position;
		private ImageView bgimage;//背景点击
	}

}
