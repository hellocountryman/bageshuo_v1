package com.feytuo.bageshuo.activity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.feytuo.bageshuo.R;
import com.feytuo.bageshuo.adapter.InvitationDetailsAdapter;
import com.feytuo.bageshuo.util.GetSystemDateTime;
import com.feytuo.bageshuo.util.SDcardTools;
import com.feytuo.bageshuo.util.StringTools;
import com.feytuo.bageshuo.widget.XListView;
import com.feytuo.bageshuo.widget.XListView.IXListViewListener;

/**
 * 帖子的详细信息
 * 
 * @date 2015-03-17
 * 
 * @version v1.0
 * 
 * @author tangpeng
 * 
 */
public class InvitationDetails extends Activity implements IXListViewListener {

	private XListView invitationDetailsXlv;
	private Handler mHandler;
	private ArrayList<Map<String, Object>> dlist;
	private InvitationDetailsAdapter adapter;
	private String[] arraytitle = { "从女性的角度来看，一件喜欢的商品打折了从女性的角度来看从女性的角度来看",
			"tangxiao", "朋友跟我哭诉，说因为太穷而经常失恋", "朋友跟我哭诉，说因为太穷而经常失恋", "tangxiao" };
	private int[] arrayuserhead = { R.drawable.lunbo, R.drawable.lunbo,
			R.drawable.lunbo, R.drawable.lunbo, R.drawable.lunbo };

	private LinearLayout partCommentLl;// 评论模块
	private Button commentAddBtn;// 点击添加录音的按钮
	private EditText commentTextEdit;// 输入评论的文字
	private Button commentSendBtn;// 发送评论按钮
	private LinearLayout commentRecordLl;// 可以输入录音的布局
	private Button commentDeleteBtn;// 删除已经录好的声音
	private Button commentRecordBtn;// 录音按钮
	private Button commentAuditionBtn;// 试听按钮
	private TextView commentRecordTimeTv;// 录音的时间

	private MediaPlayer mp = new MediaPlayer();
	private String fileAudioName; // 保存的音频文件的名字
	private MediaRecorder mediaRecorder; // 录音控制
	private String filePath; // 音频保存的文件路径
	private List<String> listAudioFileName; // 音频文件列表
	private boolean isLuYin; // 是否在录音 true 是 false否
	private File fileAudio; // 录音文件
	private File fileAudioList; // 列表中的 录音文件
	File dir; // 录音文件
	private CountDownTimer mCountDownTimer;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_invitation_details);

		TextView titleTv = (TextView) findViewById(R.id.top_bar_title);
		titleTv.setText("北京社区");// 设置标题；
		initview();
		initViewxlistview();
		initviewrecord();
	}

	private void initviewrecord() {
		if (!SDcardTools.isHaveSDcard()) {
			Toast.makeText(InvitationDetails.this, "请插入SD卡以便存储录音",
					Toast.LENGTH_LONG).show();
			return;
		}

		// 要保存的文件的路径
		filePath = SDcardTools.getSDPath() + "/" + "myAudio";
		// 实例化文件夹
		dir = new File(filePath);
		if (!dir.exists()) {
			// 如果文件夹不存在 则创建文件夹
			dir.mkdir();
		}
		Log.i("test", "要保存的录音的文件名为" + fileAudioName + "路径为" + filePath);
		listAudioFileName = SDcardTools.getFileFormSDcard(dir, ".mar");
	}

	private void initview() {
		// TODO Auto-generated method stub
		commentAddBtn = (Button) findViewById(R.id.comment_add_btn);
		commentTextEdit = (EditText) findViewById(R.id.comment_text_edit);
		commentSendBtn = (Button) findViewById(R.id.comment_send_btn);
		commentRecordLl = (LinearLayout) findViewById(R.id.comment_record_ll);
		partCommentLl = (LinearLayout) findViewById(R.id.part_comment_ll);
		commentDeleteBtn = (Button) findViewById(R.id.comment_delete_btn);
		commentRecordBtn = (Button) findViewById(R.id.comment_record_btn);
		commentAuditionBtn = (Button) findViewById(R.id.comment_audition_btn);
		commentRecordTimeTv = (TextView) findViewById(R.id.comment_record_time_tv);

		listener listen = new listener();
		commentTextEdit.setOnClickListener(listen);
		commentAddBtn.setOnClickListener(listen);
		commentDeleteBtn.setOnClickListener(listen);
		commentAuditionBtn.setOnClickListener(listen);
		commentRecordBtn.setOnClickListener(listen);
		commentRecordBtn.setOnTouchListener(new OnToucher());

	}

	class listener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {

			case R.id.comment_text_edit:
				commentRecordLl.setVisibility(View.GONE);

				break;
			case R.id.comment_add_btn:
				softkeyboard();
				if (commentRecordLl.getVisibility() == View.GONE) {
					commentRecordLl.setVisibility(View.VISIBLE);
					// showcomment();
				} else {
					commentRecordLl.setVisibility(View.GONE);
				}

				break;
			case R.id.comment_delete_btn:

				deleteAudion();
				commentRecordTimeTv.setVisibility(View.INVISIBLE);
				commentRecordBtn
						.setBackgroundResource(R.drawable.bt_comment_record_1);
				commentAuditionBtn.setVisibility(View.INVISIBLE);
				break;
			case R.id.comment_audition_btn:
				palyAudion();

				break;
			case R.id.comment_record_btn:
				Toast.makeText(InvitationDetails.this, "亲，按住录音哦",
						Toast.LENGTH_SHORT).show();
				break;

			default:
				break;
			}
		}

	}

	/**
	 * 
	 * 按住事件
	 */
	class OnToucher implements OnTouchListener {
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				commentRecordBtn
						.setBackgroundResource(R.drawable.bt_comment_record_2);
				startAudio();
				break;
			case MotionEvent.ACTION_UP:

				stopAudion();

				break;

			default:
				break;
			}
			return true;
		}
	}

	/**
	 * listview 的实例化
	 */
	private void initViewxlistview() {
		// TODO Auto-generated method stub
		invitationDetailsXlv = (XListView) findViewById(R.id.invitation_details_xlv);// 你这个listview是在这个layout里面
		invitationDetailsXlv.setPullLoadEnable(true);// 设置让它上拉，FALSE为不让上拉，便不加载更多数据

		adapter = new InvitationDetailsAdapter(InvitationDetails.this,
				getData());

		invitationDetailsXlv.setAdapter(adapter);
		invitationDetailsXlv.setXListViewListener(this);
		mHandler = new Handler();

		invitationDetailsXlv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switch (view.getId()) {

				default:
					commentRecordLl.setVisibility(View.GONE);
					break;
				}
			}

		});
	}

	private ArrayList<Map<String, Object>> getData() {
		dlist = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < arraytitle.length; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("hometitle", arraytitle[i]);
			map.put("homeuerhead", arrayuserhead[i]);
			dlist.add(map);
		}
		return dlist;
	}

	/** 停止刷新， */
	private void onLoad() {
		invitationDetailsXlv.stopRefresh();
		invitationDetailsXlv.stopLoadMore();
		invitationDetailsXlv.setRefreshTime("刚刚");
	}

	// 刷新
	@Override
	public void onRefresh() {
		mHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				getData();
				invitationDetailsXlv.setAdapter(adapter);
				onLoad();
			}
		}, 2000);
	}

	// 加载更多
	@Override
	public void onLoadMore() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				getData();
				adapter.notifyDataSetChanged();
				onLoad();
			}
		}, 2000);
	}

	private int recorderTime;
	private Timer recorderTimer;

	/* ****************************************************************
	 * 
	 * 开始录音
	 */
	private void startAudio() {

		recorderTime = 0;
		recorderTimer = new Timer();
		recorderTimer.schedule(new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				recorderTime++;
			}
		}, 1000l, 1000l);
		// 创建录音频文件
		// 这种创建方式生成的文件名是随机的
		fileAudioName = "audio" + GetSystemDateTime.now()
				+ StringTools.getRandomString(2) + ".mar";
		mediaRecorder = new MediaRecorder();
		// 设置录音的来源为麦克风
		mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
		mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
		mediaRecorder.setOutputFile(filePath + "/" + fileAudioName);
		try {
			mediaRecorder.prepare();
			mediaRecorder.start();

			fileAudio = new File(filePath + "/" + fileAudioName);
			isLuYin = true;
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/* ******************************************************
	 * 
	 * 停止录制
	 */
	private void stopAudion() {

		if (recorderTime <= 1) {
			Toast.makeText(this, "太短啦", Toast.LENGTH_SHORT).show();
			recordAudio();
		} else {
			if (null != mediaRecorder) {
				// 停止录音
				commentRecordBtn.setText("");
				mediaRecorder.stop();

				commentRecordBtn
						.setBackgroundResource(R.drawable.bt_comment_record_4);
				commentRecordTimeTv.setVisibility(View.VISIBLE);
				commentDeleteBtn.setVisibility(View.VISIBLE);
				commentAuditionBtn.setVisibility(View.VISIBLE);
			}
		}
		recorderTimer.cancel();
		mediaRecorder.reset();
		mediaRecorder.release();
		mediaRecorder = null;

	}

	/**
	 * 
	 * 重新录音
	 */
	private void recordAudio() {
		if (mp != null && mp.isPlaying()) {
			mp.stop();
		}
		if (fileAudio != null) {
			fileAudio.delete();// 文件删除
			fileAudio = null;
		}
		commentRecordTimeTv.setVisibility(View.GONE);
		commentRecordBtn.setBackgroundResource(R.drawable.bt_comment_record_1);
		commentAuditionBtn.setVisibility(View.INVISIBLE);// 隐藏播放按钮
		commentDeleteBtn.setVisibility(View.INVISIBLE);// 隐藏删除按钮

	}

	protected void onDestroy() {
		super.onDestroy();
		if (mp != null) {
			mp.release();
		}
		if (null != mediaRecorder && isLuYin) {
			mediaRecorder.release();
		}
		if (fileAudio != null) {
			fileAudio.delete();
		}

	}

	private void deleteAudion() {
		if (mp != null && mp.isPlaying()) {
			mp.stop();
		}
		if (fileAudio != null) {
			fileAudio.delete();// 文件删除
			fileAudio = null;
		}

	}

	private void palyAudion() {
		// mp.setDataSource(filePath + "/" + fileAudioNameList);
		try {

			mp.reset();
			mp.setDataSource(filePath + "/" + fileAudioName);
			mp.prepare();
			mp.seekTo(0);
			mp.start();

		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// //实现倒计时
		mCountDownTimer = new MyCount(mp.getDuration(), 1000);
		mCountDownTimer.start();

	}

	/* 定义一个倒计时的内部类 */
	class MyCount extends CountDownTimer {
		public MyCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onFinish() {
			commentRecordTimeTv.setText("");
			commentAuditionBtn.setVisibility(View.VISIBLE);// 显示播放按钮
			commentAuditionBtn.setVisibility(View.VISIBLE);// 显示播放按钮
			commentDeleteBtn.setVisibility(View.VISIBLE);// 显示删除按钮
		}

		@Override
		public void onTick(long millisUntilFinished) {

			commentAuditionBtn.setVisibility(View.INVISIBLE);// 隐藏播放按钮
			commentDeleteBtn.setVisibility(View.INVISIBLE);// 隐藏删除按钮
			commentRecordTimeTv.setText(millisUntilFinished / 1000 + "");
			// Toast.makeText(InvitationDetailsActivity.this,
			// millisUntilFinished / 1000 + "",
			// Toast.LENGTH_LONG).show();// toast有显示时间延迟
		}
	}

	/**
	 * Handler消息处理
	 */
	private Handler aumHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0) {
				finish();
			}
			super.handleMessage(msg);
		}
	};

	/**
	 * 隐藏输入法
	 */
	public void softkeyboard() {
		((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
				.hideSoftInputFromWindow(InvitationDetails.this
						.getCurrentFocus().getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			this.finish();
		}
		return false;
	}

	/**
	 * 动画，显评论
	 */
	public void showcomment() {
		Animation animation = AnimationUtils.loadAnimation(
				InvitationDetails.this, R.anim.translate_record_show);
		partCommentLl.startAnimation(animation);
	}

	public void onBackBtn(View v) {
		finish();
	}

}
