package com.feytuo.bageshuo.activity;

import com.feytuo.bageshuo.R;
import com.feytuo.bageshuo.R.anim;
import com.feytuo.bageshuo.R.drawable;
import com.feytuo.bageshuo.R.id;
import com.feytuo.bageshuo.R.layout;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 发布的模块
 * 
 * @verson:v1.0
 * 
 * @date:2015-03-18
 * 
 * @author tangpeng
 * 
 * 
 */
public class PublishFirst extends Activity {
	private ImageView publishRecordIv;// 录音的按钮
	private ImageView publishRotateIv;// 按住录音是出现的动画
	private ImageView PublishHeadphoneIv;// 录完音后显示的耳机图标
	private TextView PublishRecordTv;// 录完音后的录音时间
	private AnimationDrawable animationDrawable;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_publish);
		initView();
	}

	private void initView() {
		PublishHeadphoneIv = (ImageView) findViewById(R.id.publish_headphone_iv);
		publishRotateIv = (ImageView) findViewById(R.id.publish_rotate_iv);
		PublishRecordTv = (TextView) findViewById(R.id.publish_recordtime_tv);
		publishRecordIv = (ImageView) findViewById(R.id.publish_record_iv);
		publishRecordIv.setOnTouchListener(new OnToucher());
	}
	/**
	 * 
	 * 按住事件
	 */
	class OnToucher implements OnTouchListener {
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				publishRecordIv.setImageResource(R.drawable.ic_volume_press);
				PublishHeadphoneIv.setVisibility(View.GONE);
				PublishRecordTv.setVisibility(View.GONE);
				setrotateanimation();
				break;
			case MotionEvent.ACTION_UP:
				publishRecordIv.setImageResource(R.drawable.ic_volume_normal2);
				PublishHeadphoneIv.setVisibility(View.VISIBLE);
				PublishRecordTv.setVisibility(View.VISIBLE);
				 animationDrawable.stop();
				break;

			default:
				break;
			}
			return true;
		}
	}

	/**
	 * 按住录音时的出现的动画效果
	 */
	private void setrotateanimation() {
		 publishRotateIv.setVisibility(View.VISIBLE);
		publishRotateIv.setBackgroundResource(R.anim.publish_record_anim);
		animationDrawable = (AnimationDrawable)
        		publishRotateIv.getBackground();
        animationDrawable.start();

	}

	public void publishDeleteBtn(View v) {
		finish();
		// publishRecordIv.setImageResource(R.drawable.ic_volume_normal);
		// PublishHeadphoneIv.setVisibility(View.GONE);
		// PublishRecordTv.setVisibility(View.GONE);
	}

	public void publishEditTextBtn(View v) {
		Intent intent = new Intent();
		intent.setClass(this, PublishSecond.class);
		startActivity(intent);
	}

}
