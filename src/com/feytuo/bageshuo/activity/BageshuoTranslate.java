package com.feytuo.bageshuo.activity;

import java.util.ArrayList;
import java.util.List;

import com.feytuo.bageshuo.R;
import com.feytuo.bageshuo.R.id;
import com.feytuo.bageshuo.R.layout;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 八哥说页面，可选择不同的语音
 * 
 * @date 2015-03-23
 * 
 * @version v1.0
 * 
 * @author tangpeng
 * 
 */
public class BageshuoTranslate extends Activity {
	private Spinner bageshuoSpinner;
	private static String[] cityInfo={"北京","江苏","浙江","上海"};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bageshuo_translate);
		initView();
	}

	private void initView() {

		TextView titleTv = (TextView) findViewById(R.id.top_bar_return_tv);
		titleTv.setText("八哥一下");// 设置标题；
		bageshuoSpinner = (Spinner) findViewById(R.id.bageshuo_spinner);
		bageshuoSpinner.setBackgroundColor(getResources().getColor(R.color.white));
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.spinner_item,R.id.spinner_item_tv,cityInfo);

		// 为spinner添加适配器
		bageshuoSpinner.setAdapter(adapter);
		bageshuoSpinner.setSelection(2,true);  //设置默认选中项，此处为默认选中第3个值
		bageshuoSpinner.setOnItemSelectedListener(new SpinnerListener());
	}

	// 该监听器用于监听用户多spinner的操作
	class SpinnerListener implements OnItemSelectedListener {
		// 当用户选择先拉列表中的选项时会调用这个方法
		/**
		 * 参数说明： 第一个：当前的下拉列表，也就是第三个参数的父view 
		 * 第二个：当前选中的选项 第三个：所选选项的位置 
		 * 第四个： 所选选项的id
		 */
		public void onItemSelected(AdapterView<?> adapterView, View view,
				int position, long id) {
			// 获取用户所选的选项内容
			String selected = "您的选择是："
					+ adapterView.getItemAtPosition(position).toString();
			Toast.makeText(BageshuoTranslate.this, selected, Toast.LENGTH_SHORT)
					.show();
		}

		// 当用户不做选择时调用的该方法
		public void onNothingSelected(AdapterView<?> arg0) {
			Toast.makeText(BageshuoTranslate.this, "您没有选择任何选项", Toast.LENGTH_SHORT)
					.show();
		}
	}
	
	public void onBackBtn(View v)
	{
		finish();
	}

}
