package com.feytuo.bageshuo.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.feytuo.bageshuo.R;
import com.feytuo.bageshuo.R.id;
import com.feytuo.bageshuo.R.layout;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 探索更多热门社区的模块
 * 
 * @date 2015-03-14
 * 
 * @version v1.0
 * 
 * @author tangpeng
 * 
 */
public class HotHome extends Activity implements OnItemClickListener{
	private static final String TAG="HOTHOMEACTIVITY";
	private GridView hotHomeGv;
	private List<Map<String, Object>> hothomeDataList;
	private String[] cityname = new String[] { "北京", "上海", "广州", "深圳", "长沙","湘潭", "天津" };
	private SimpleAdapter hothomeAdapter;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hot_home);

		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub

//		TextView titleTv = (TextView) findViewById(R.id.top_bar_title);
//		titleTv.setText(R.string.attentionhome);// 设置标题；

		hotHomeGv = (GridView) findViewById(R.id.hothome_gv);
		hothomeDataList = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < cityname.length; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("hothometitle", cityname[i]);
			hothomeDataList.add(map);
		}
		hothomeAdapter = new SimpleAdapter(this, hothomeDataList,
				R.layout.hothome_gridview_item,
				new String[] { "hothometitle" },
				new int[] { R.id.hothome_gv_item_btn });
		hotHomeGv.setAdapter(hothomeAdapter);

		hotHomeGv.setOnItemClickListener(this);
	}

	private OnItemClickListener itemlistener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Toast.makeText(
					HotHome.this,
					"你点击了"
							+ hothomeDataList.get(position).get("hothometitle")
									.toString(), Toast.LENGTH_LONG).show();
		}

	};

	public void onBackBtn(View v) {
		finish();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Log.i(TAG,"你点击了"
				+ hothomeDataList.get(position).get("hothometitle")
				.toString() );
		Toast.makeText(this,
				"你点击了"
						+ hothomeDataList.get(position).get("hothometitle")
								.toString(), Toast.LENGTH_LONG).show();
	}

}
