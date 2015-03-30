package com.feytuo.bageshuo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.feytuo.bageshuo.R;
import com.feytuo.chat.widget.PasteEditText;

/**
 * 修改昵称或者签名
 * 
 * @version v1.0
 * 
 * @date 2015-03-30
 * 
 * @author tangpeng
 * 
 */
public class UpdateNickSign extends Activity {
	private TextView titleTypeText;// 显示昵称还是个性签名
	private TextView updateSuccesstv;// 完成修改
	private TextView typeTint;// 文字输入提示
	private PasteEditText mEditText;// 设置昵称的edit
	private RelativeLayout rela;// 设置昵称的底部横线
	private TextView wordnumText;// 提示还可以输入多少字
	private String type;// 判断是修改昵称，还是签名

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.update_person_info_activity);

		initview();

	}

	public void initview() {

		titleTypeText = (TextView) findViewById(R.id.top_bar_title_success_tv);// 标题
		updateSuccesstv = (TextView) findViewById(R.id.top_bar_title_ok_tv);// 完成按钮
		typeTint = (TextView) findViewById(R.id.type_hint);// 昵称或者个性签名
		type = getIntent().getStringExtra("type").toString();
		mEditText = (PasteEditText) findViewById(R.id.person_edit);
		if (type.equals("nick"))// 如果是修改昵称
		{
			titleTypeText.setText("修改昵称");
			typeTint.setText("好名字可以让你的朋友更加容易记住你");
		} else {
			titleTypeText.setText("修改签名");
			typeTint.setText("签名即个性，秀出你的个性吧");
		}
		rela = (RelativeLayout) findViewById(R.id.edittext_rela);
		wordnumText = (TextView) findViewById(R.id.wordnumtext);
		mEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					rela.setBackgroundResource(R.drawable.input_bar_bg_active);
				} else {
					rela.setBackgroundResource(R.drawable.input_bar_bg_normal);
				}
			}
		});

		mEditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				wordnumText.setText(20 - s.length() + "/20");
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
	}

	public void ReturnSuccess(View v) {
		// TODO Auto-generated method stub
		finish();
	}

}
