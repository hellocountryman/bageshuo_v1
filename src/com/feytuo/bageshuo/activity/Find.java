package com.feytuo.bageshuo.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.feytuo.bageshuo.R;
import com.feytuo.bageshuo.adapter.FindbsAdapter;
import com.feytuo.bageshuo.model.FindCardModel;
import com.feytuo.bageshuo.widget.FindCardContainer;

/**
 * 发现模块的功能
 * 
 * @version v1.0
 * 
 * @data 2015-03-24
 * 
 * @author tangpeng
 * 
 */
public class Find extends Activity {
	private FindCardContainer mCardContainer;
	private ArrayList<FindCardModel> dlist;
	private FindbsAdapter bsapt;
	private Button likeBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fg3);
		initView();
	}

	private void initView() {
		mCardContainer = (FindCardContainer) findViewById(R.id.find_card_view);
		likeBtn = (Button) findViewById(R.id.find_like_btn);
		likeBtn.setOnClickListener(new listener());
		Resources r = getResources();
		dlist = new ArrayList<FindCardModel>();
		for (int j = 0; j < 15; j++) {
			dlist.add(new FindCardModel("Title" + j, "这是一段话哦", r
					.getDrawable(R.drawable.recordbtn_10)));
		}
		bsapt = new FindbsAdapter(this, dlist);
		mCardContainer.setAdapter(bsapt);
	}

	class listener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
			switch (v.getId()) {
			case R.id.find_like_btn:
				FindCardContainer cardcontainer = new FindCardContainer(
						Find.this);
				int i = cardcontainer.getposition();
				Toast.makeText(Find.this, "不知道会不会成功" + i, Toast.LENGTH_SHORT)
						.show();
				break;

			default:
				break;
			}
			startActivity(intent);
		}

	}

}
