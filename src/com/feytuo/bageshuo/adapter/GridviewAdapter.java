package com.feytuo.bageshuo.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.sax.StartElementListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.feytuo.bageshuo.R;
import com.feytuo.bageshuo.activity.HomeDetails;
/**
 * 自定义GridviewAdapter ，方便随时更新新的消息
 * 
 * @version v1.0
 * 
 * @data 2015-03-12
 * 
 * @author tangpeng
 *
 */
public class GridviewAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater m_Inflater;
	private int resource;
	private List<? extends Map<String, ?>> list;// 声明List容器对象

	public GridviewAdapter(Context context,
			List<? extends Map<String, ?>> data) {
		this.list = data;
		this.context = context;
		m_Inflater = LayoutInflater.from(context);
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
		// ViewHolder不是Android的开发API，而是一种设计方法，就是设计个静态类，缓存一下，省得Listview更新的时候，还要重新操作。
		if (convertView == null) {

			convertView = m_Inflater.inflate(R.layout.home_gridview_item, null);
			holder = new NewViewHolder();
			holder.gridviewHomenumBtn=(Button) convertView.findViewById(R.id.gridview_homenum_btn);
			holder.gridviewHomenameTv=(TextView) convertView.findViewById(R.id.gridview_homename_tv);
			
			convertView.setTag(holder);
			// Tag从本质上来讲是就是相关联的view的额外的信息。它们经常用来存储一些view的数据，这样做非常方便而不用存入另外的单独结构。
		} else {
			holder = (NewViewHolder) convertView.getTag();
		}
		holder.gridviewHomenumBtn.setText(list.get(position).get("homenum").toString());
		holder.gridviewHomenameTv.setText(list.get(position).get("hometitle").toString());
		convertView.setOnClickListener(new listener(holder, position));
		return convertView;
	}

	class NewViewHolder {
		private TextView gridviewHomenameTv;// 
		private Button gridviewHomenumBtn;//
		private int position;
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
		
			default:
				Toast.makeText(context, "进入社区详情",Toast.LENGTH_LONG).show();
				Intent intent=new Intent();
				intent.setClass(context, HomeDetails.class);
				context.startActivity(intent);
				break;
			}
		}

	}
}