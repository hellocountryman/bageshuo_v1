package com.feytuo.bageshuo.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 *自定义一个gridview，避免与scrollview冲突
 * 
 * @version v1.0
 * 
 * @data 2015-03-13
 * 
 * @author tangpeng
 *
 */
public class HomeGridView extends GridView {

	public HomeGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public HomeGridView(Context context) {
		super(context);
	}

	public HomeGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}

}
