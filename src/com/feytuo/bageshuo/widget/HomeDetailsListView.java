package com.feytuo.bageshuo.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 *自定义一个listview，避免与scrollview冲突
 * 
 * @version v1.0
 * 
 * @data 2015-03-16
 * 
 * @author tangpeng
 *
 */
public class HomeDetailsListView extends ListView {

	public HomeDetailsListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public HomeDetailsListView(Context context) {
		super(context);
	}

	public HomeDetailsListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}

}
