package com.feytuo.bageshuo.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.widget.ImageView;
import android.widget.Toast;
import com.feytuo.bageshuo.MainActivity;

public class ToolAnimation {
	
	private static AnimationDrawable frameAnim;
	
	/**
	 * 开始播放
	 */
	protected void start(Context context) {
		if (frameAnim != null && !frameAnim.isRunning()) {
			frameAnim.start();
			Toast.makeText(context, "开始播放", 0).show();
		}
	}

	/**
	 * 停止播放
	 */
	public static void stop( Context context) {
		if (frameAnim != null && frameAnim.isRunning()) {
			frameAnim.stop();
			Toast.makeText(context, "停止播放", 0).show();
		}
	}

	
	@SuppressLint("NewApi")
	public static void animationdeal( Context context,ImageView imageview) {
		frameAnim = new AnimationDrawable();
		// 为AnimationDrawable添加动画帧
		for (int i = 0; i < 10; i++) {
			frameAnim.addFrame(
					context.getResources().getDrawable(
							context.getResources().getIdentifier(
									"recordbtn_" + (i + 1), "drawable",
									context.getPackageName())), 50);
		}
		frameAnim.setOneShot(false);
		// 设置ImageView的背景为AnimationDrawable
		imageview.setBackground(frameAnim);

		frameAnim.start();
	}

}
