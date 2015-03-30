package com.feytuo.bageshuo.fragment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.EMConnectionListener;
import com.easemob.EMError;
import com.easemob.chat.CmdMessageBody;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMContactListener;
import com.easemob.chat.EMContactManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMMessage.ChatType;
import com.easemob.chat.EMMessage.Type;
import com.easemob.chat.EMNotifier;
import com.easemob.chat.GroupChangeListener;
import com.easemob.chat.TextMessageBody;
import com.easemob.util.EMLog;
import com.easemob.util.HanziToPinyin;
import com.easemob.util.NetUtils;
import com.feytuo.bageshuo.App;
import com.feytuo.bageshuo.R;
import com.feytuo.chat.Constant;
import com.feytuo.chat.activity.AddContactActivity;
import com.feytuo.chat.activity.BaseActivity;
import com.feytuo.chat.activity.ChatActivity;
import com.feytuo.chat.activity.ChatAllHistoryFragment;
import com.feytuo.chat.activity.ContactlistFragment;
import com.feytuo.chat.activity.GroupsActivity;
import com.feytuo.chat.activity.LoginActivity;
import com.feytuo.chat.activity.SettingsFragment;
import com.feytuo.chat.db.InviteMessgeDao;
import com.feytuo.chat.db.UserDao;
import com.feytuo.chat.domain.InviteMessage;
import com.feytuo.chat.domain.InviteMessage.InviteMesageStatus;
import com.feytuo.chat.domain.User;
import com.feytuo.chat.utils.CommonUtils;
import com.umeng.analytics.MobclickAgent;

public class Fragment4 extends Fragment {
	
	

	private final String TAG = "ChatAndContactFragment";
	private Fragment[] fragments;

	private ViewPager viewPager;
	private ImageView cursorImage;
	private Button friendOneBtn;
	private Button friendTwoBtn;

	private int cursorOffset;// 每一格偏移量
	private int currentOffset;// 当前总偏移量
//	private int currentTabInCAC;
	
	
	// 未读消息textview
    private TextView unreadLabel;
    // 未读通讯录textview
    private TextView unreadAddressLable;

    private ContactlistFragment contactListFragment;
    // private ChatHistoryFragment chatHistoryFragment;
    private ChatAllHistoryFragment chatHistoryFragment;
    private SettingsFragment settingFragment;
    // 当前fragment的index
    private int currentTabIndex;
    private NewMessageBroadcastReceiver msgReceiver;
    // 账号在别处登录
    public boolean isConflict = false;
    //账号被移除
    private boolean isCurrentAccountRemoved = false;
    private BaseActivity mainActivity;
    
    /**
     * 检查当前用户是否被删除
     */
    public boolean getCurrentAccountRemoved(){
        return isCurrentAccountRemoved;
    }
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fg4, container,false);
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
		initViewPager();
		
		initView();

		initCursor();
		
		inithuanxin(savedInstanceState);
	
        
	}
	
	
	
	/**
	 * 好友使用环信
	 * @param savedInstanceState
	 */
	private void inithuanxin(Bundle savedInstanceState)
	{
		mainActivity = (BaseActivity) getActivity();
        if(savedInstanceState != null && savedInstanceState.getBoolean(Constant.ACCOUNT_REMOVED, false)){
            // 防止被移除后，没点确定按钮然后按了home键，长期在后台又进app导致的crash
            // 三个fragment里加的判断同理
            App.getInstance().logout(null);
            mainActivity.finish();
            startActivity(new Intent(mainActivity, LoginActivity.class));
            return;
        }else if (savedInstanceState != null && savedInstanceState.getBoolean("isConflict", false)) {
            // 防止被T后，没点确定按钮然后按了home键，长期在后台又进app导致的crash
            // 三个fragment里加的判断同理
            mainActivity.finish();
            startActivity(new Intent(mainActivity, LoginActivity.class));
            return;
        }
        
//      MobclickAgent.setDebugMode( true );
        //--?--
        MobclickAgent.updateOnlineConfig(mainActivity);
        
        if (mainActivity.getIntent().getBooleanExtra("conflict", false) && !isConflictDialogShow){
            showConflictDialog();
        }else if(mainActivity.getIntent().getBooleanExtra(Constant.ACCOUNT_REMOVED, false) && !isAccountRemovedDialogShow){
            showAccountRemovedDialog();
        }
        
        inviteMessgeDao = new InviteMessgeDao(mainActivity);
        userDao = new UserDao(mainActivity);
       
        // 注册一个接收消息的BroadcastReceiver
        msgReceiver = new NewMessageBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter(EMChatManager.getInstance().getNewMessageBroadcastAction());
        intentFilter.setPriority(3);
        mainActivity.registerReceiver(msgReceiver, intentFilter);

        // 注册一个ack回执消息的BroadcastReceiver
        IntentFilter ackMessageIntentFilter = new IntentFilter(EMChatManager.getInstance().getAckMessageBroadcastAction());
        ackMessageIntentFilter.setPriority(3);
        mainActivity.registerReceiver(ackMessageReceiver, ackMessageIntentFilter);
        
        //注册一个透传消息的BroadcastReceiver
        IntentFilter cmdMessageIntentFilter = new IntentFilter(EMChatManager.getInstance().getCmdMessageBroadcastAction());
        cmdMessageIntentFilter.setPriority(3);
        mainActivity.registerReceiver(cmdMessageReceiver, cmdMessageIntentFilter);
        
        

        // 注册一个离线消息的BroadcastReceiver
        // IntentFilter offlineMessageIntentFilter = new
        // IntentFilter(EMChatManager.getInstance()
        // .getOfflineMessageBroadcastAction());
        // registerReceiver(offlineMessageReceiver, offlineMessageIntentFilter);

        // setContactListener监听联系人的变化等
        EMContactManager.getInstance().setContactListener(new MyContactListener());
        // 注册一个监听连接状态的listener
        EMChatManager.getInstance().addConnectionListener(new MyConnectionListener());
        // 注册群聊相关的listener
        EMGroupManager.getInstance().addGroupChangeListener(new MyGroupChangeListener());
        // 通知sdk，UI 已经初始化完毕，注册了相应的receiver和listener, 可以接受broadcast了
        EMChat.getInstance().setAppInited();
        
	}
	/**
	 *  初始化view
	 */
	private void initViewPager() {
		// TODO Auto-generated method stub
		 // 这个fragment只显示好友和群组的聊天记录
        // chatHistoryFragment = new ChatHistoryFragment();
        // 显示所有人消息记录的fragment
        chatHistoryFragment = new ChatAllHistoryFragment();
        contactListFragment = new ContactlistFragment();
        settingFragment = new SettingsFragment();
		fragments = new Fragment[] { chatHistoryFragment, contactListFragment };
		viewPager = (ViewPager)getActivity().findViewById(R.id.contact_viewpager);
		viewPager.setAdapter(new MyFragmentPagerAdapter(getChildFragmentManager()));
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			private int currentPager;
			@Override
			public void onPageSelected(int arg0) {//当页面改变时候
				if(arg0 == 0){
					leftcolor();
				}else{
					rightcolor();
				}
			}
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				if (currentPager != arg0) {
					currentOffset = arg0 * cursorOffset;
				} else {
					final LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) cursorImage
							.getLayoutParams();
					if (arg0 == 0 && arg2 == 0) {
						params.setMargins((int) (currentOffset + cursorOffset
								* arg1), 0, 0, 0);
					} else {
						params.setMargins((int) (currentOffset + cursorOffset
								* arg1) + 1, 0, 0, 0);
					}
					// 首次加载后不会刷新，必须强制放到ui线程刷新ui
					cursorImage.post(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							cursorImage.setLayoutParams(params);
						}
					});
				}
				currentPager = arg0;
			}
			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
	}

	@SuppressLint("ResourceAsColor")
	private void initView() {
		// TODO Auto-generated method stub
		friendOneBtn = (Button)getActivity().findViewById(R.id.friend_one_btn);
		friendTwoBtn = (Button) getActivity().findViewById(R.id.friend_two_btn);
		cursorImage = (ImageView)getActivity().findViewById(R.id.cursor);
		// 进入添加好友页
		friendOneBtn.setOnClickListener(listener);
		friendTwoBtn.setOnClickListener(listener);
		
		unreadLabel = (TextView) getActivity().findViewById(R.id.unread_msg_number);
        unreadAddressLable = (TextView) getActivity().findViewById(R.id.unread_address_number);
        ImageView addContactView = (ImageView) getView().findViewById(R.id.iv_new_contact);
		// 进入添加好友页
		addContactView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(getActivity(), AddContactActivity.class));
			}
		});
	}

	/**
	 * viewpager在左边的时候
	 */
	private void leftcolor() {
		friendOneBtn.setTextColor(getResources().getColor(R.color.index_color));
		friendTwoBtn.setTextColor(getResources().getColor(R.color.grey));
	}

	/**
	 * viewpager在右边的时候
	 */
	private void rightcolor() {
		friendOneBtn.setTextColor(getResources().getColor(R.color.grey));
		friendTwoBtn.setTextColor(getResources().getColor(R.color.index_color));
	}

	private void initCursor() {
		// TODO Auto-generated method stub
		// 获取屏幕分辨率宽度
		DisplayMetrics dm = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;
		cursorOffset = screenW / 2;

		LayoutParams params = cursorImage.getLayoutParams();
		params.width = cursorOffset;
		cursorImage.setLayoutParams(params);
	}

	private OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.friend_one_btn: // 会话按钮
				leftcolor();
				setCursor(viewPager.getCurrentItem(), 0);
				viewPager.setCurrentItem(0, false);
				break;
			case R.id.friend_two_btn:// 好友列表按钮
				rightcolor();
				setCursor(viewPager.getCurrentItem(), 1);
				viewPager.setCurrentItem(1, false);
				break;
			}
		}
	};

	class MyFragmentPagerAdapter extends FragmentPagerAdapter {

		public MyFragmentPagerAdapter(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
		}
		@Override
		public Fragment getItem(int arg0) {
			// TODO Auto-generated method stub
			return fragments[arg0];
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return fragments.length;
		}
	}

	/**
	 * 点击时滑动块移动
	 */
	private void setCursor(int currentNum, int targetNum) {
		int offsetNum = 0;
		offsetNum = targetNum - currentNum;
		currentOffset = currentOffset + offsetNum * cursorOffset;
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) cursorImage
				.getLayoutParams();
		params.setMargins(currentOffset, 0, 0, 0);
		cursorImage.requestLayout();
	}
	
	/**
     * 刷新未读消息数
     */
    public void updateUnreadLabel() {
        int count = getUnreadMsgCountTotal();
        if (count > 0) {
            unreadLabel.setText(String.valueOf(count));
            unreadLabel.setVisibility(View.VISIBLE);
        } else {
            unreadLabel.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 刷新申请与通知消息数
     */
    public void updateUnreadAddressLable() {
        mainActivity.runOnUiThread(new Runnable() {
            public void run() {
                int count = getUnreadAddressCountTotal();
                if (count > 0) {
                    unreadAddressLable.setText(String.valueOf(count));
                    unreadAddressLable.setVisibility(View.VISIBLE);
                } else {
                    unreadAddressLable.setVisibility(View.INVISIBLE);
                }
            }
        });

    }

    /**
     * 获取未读申请与通知消息
     * 
     * @return
     */
    public int getUnreadAddressCountTotal() {
        int unreadAddressCountTotal = 0;
        if (App.getInstance().getContactList().get(Constant.NEW_FRIENDS_USERNAME) != null)
            unreadAddressCountTotal = App.getInstance().getContactList().get(Constant.NEW_FRIENDS_USERNAME).getUnreadMsgCount();
        return unreadAddressCountTotal;
    }

    /**
     * 获取未读消息数
     * 
     * @return
     */
    public int getUnreadMsgCountTotal() {
        int unreadMsgCountTotal = 0;
        unreadMsgCountTotal = EMChatManager.getInstance().getUnreadMsgsCount();
        return unreadMsgCountTotal;
    }

    /**
     * 新消息广播接收者
     * 
     * 
     */
    private class NewMessageBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // 主页面收到消息后，主要为了提示未读，实际消息内容需要到chat页面查看

            String from = intent.getStringExtra("from");
            // 消息id
            String msgId = intent.getStringExtra("msgid");
            EMMessage message = EMChatManager.getInstance().getMessage(msgId);
            // 2014-10-22 修复在某些机器上，在聊天页面对方发消息过来时不立即显示内容的bug
            if (ChatActivity.activityInstance != null) {
                if (message.getChatType() == ChatType.GroupChat) {
                    if (message.getTo().equals(ChatActivity.activityInstance.getToChatUsername()))
                        return;
                } else {
                    if (from.equals(ChatActivity.activityInstance.getToChatUsername()))
                        return;
                }
            }
            
            // 注销广播接收者，否则在ChatActivity中会收到这个广播
            abortBroadcast();
            
            mainActivity.notifyNewMessage(message);  

            // 刷新bottom bar消息未读数
            updateUnreadLabel();
            if (currentTabIndex == 0) {
                // 当前页面如果为聊天历史页面，刷新此页面
                if (chatHistoryFragment != null) {
                    chatHistoryFragment.refresh();
                }
            }

        }
    }

    /**
     * 消息回执BroadcastReceiver
     */
    private BroadcastReceiver ackMessageReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            abortBroadcast();
            
            String msgid = intent.getStringExtra("msgid");
            String from = intent.getStringExtra("from");

            EMConversation conversation = EMChatManager.getInstance().getConversation(from);
            if (conversation != null) {
                // 把message设为已读
                EMMessage msg = conversation.getMessage(msgid);

                if (msg != null) {

                    // 2014-11-5 修复在某些机器上，在聊天页面对方发送已读回执时不立即显示已读的bug
                    if (ChatActivity.activityInstance != null) {
                        if (msg.getChatType() == ChatType.Chat) {
                            if (from.equals(ChatActivity.activityInstance.getToChatUsername()))
                                return;
                        }
                    }

                    msg.isAcked = true;
                }
            }
            
        }
    };
    
    
    
    /**
     * 透传消息BroadcastReceiver
     */
    private BroadcastReceiver cmdMessageReceiver = new BroadcastReceiver() {
        
        @Override
        public void onReceive(Context context, Intent intent) {
            abortBroadcast();
            EMLog.d(TAG, "收到透传消息");
            //获取cmd message对象
            String msgId = intent.getStringExtra("msgid");
            EMMessage message = intent.getParcelableExtra("message");
            //获取消息body
            CmdMessageBody cmdMsgBody = (CmdMessageBody) message.getBody();
            String action = cmdMsgBody.action;//获取自定义action
            
            //获取扩展属性 此处省略
//          message.getStringAttribute("");
            EMLog.d(TAG, String.format("透传消息：action:%s,message:%s", action,message.toString()));
            String st9 = getResources().getString(R.string.receive_the_passthrough);
            Toast.makeText(mainActivity, st9+action, Toast.LENGTH_SHORT).show();
        }
    };

    /**
     * 离线消息BroadcastReceiver sdk 登录后，服务器会推送离线消息到client，这个receiver，是通知UI
     * 有哪些人发来了离线消息 UI 可以做相应的操作，比如下载用户信息
     */
    // private BroadcastReceiver offlineMessageReceiver = new
    // BroadcastReceiver() {
    //
    // @Override
    // public void onReceive(Context context, Intent intent) {
    // String[] users = intent.getStringArrayExtra("fromuser");
    // String[] groups = intent.getStringArrayExtra("fromgroup");
    // if (users != null) {
    // for (String user : users) {
    // System.out.println("收到user离线消息：" + user);
    // }
    // }
    // if (groups != null) {
    // for (String group : groups) {
    // System.out.println("收到group离线消息：" + group);
    // }
    // }
    // }
    // };

    private InviteMessgeDao inviteMessgeDao;
    private UserDao userDao;

    /***
     * 好友变化listener
     * 
     */
    private class MyContactListener implements EMContactListener {

        @Override
        public void onContactAdded(List<String> usernameList) {
            // 保存增加的联系人
            Map<String, User> localUsers = App.getInstance().getContactList();
            Map<String, User> toAddUsers = new HashMap<String, User>();
            for (String username : usernameList) {
                User user = setUserHead(username);
                // 添加好友时可能会回调added方法两次
                if (!localUsers.containsKey(username)) {
                    userDao.saveContact(user);
                }
                toAddUsers.put(username, user);
            }
            localUsers.putAll(toAddUsers);
            // 刷新ui
            if (currentTabIndex == 1)
                contactListFragment.refresh();

        }

        @Override
        public void onContactDeleted(final List<String> usernameList) {
            // 被删除
            Map<String, User> localUsers = App.getInstance().getContactList();
            for (String username : usernameList) {
                localUsers.remove(username);
                userDao.deleteContact(username);
                inviteMessgeDao.deleteMessage(username);
            }
            mainActivity.runOnUiThread(new Runnable() {
                public void run() {
                    // 如果正在与此用户的聊天页面
                    String st10 = getResources().getString(R.string.have_you_removed);
                    if (ChatActivity.activityInstance != null && usernameList.contains(ChatActivity.activityInstance.getToChatUsername())) {
                        Toast.makeText(mainActivity, ChatActivity.activityInstance.getToChatUsername() + st10, Toast.LENGTH_SHORT).show();
                        ChatActivity.activityInstance.finish();
                    }
                    updateUnreadLabel();
                    // 刷新ui
                    if (currentTabIndex == 1)
                        contactListFragment.refresh();
                    else if(currentTabIndex == 0)
                        chatHistoryFragment.refresh();
                }
            });
        
        }

        @Override
        public void onContactInvited(String username, String reason) {
            // 接到邀请的消息，如果不处理(同意或拒绝)，掉线后，服务器会自动再发过来，所以客户端不需要重复提醒
            List<InviteMessage> msgs = inviteMessgeDao.getMessagesList();

            for (InviteMessage inviteMessage : msgs) {
                if (inviteMessage.getGroupId() == null && inviteMessage.getFrom().equals(username)) {
                    inviteMessgeDao.deleteMessage(username);
                }
            }
            // 自己封装的javabean
            InviteMessage msg = new InviteMessage();
            msg.setFrom(username);
            msg.setTime(System.currentTimeMillis());
            msg.setReason(reason);
            Log.d(TAG, username + "请求加你为好友,reason: " + reason);
            // 设置相应status
            msg.setStatus(InviteMesageStatus.BEINVITEED);
            notifyNewIviteMessage(msg);

        }

        @Override
        public void onContactAgreed(String username) {
            List<InviteMessage> msgs = inviteMessgeDao.getMessagesList();
            for (InviteMessage inviteMessage : msgs) {
                if (inviteMessage.getFrom().equals(username)) {
                    return;
                }
            }
            // 自己封装的javabean
            InviteMessage msg = new InviteMessage();
            msg.setFrom(username);
            msg.setTime(System.currentTimeMillis());
            Log.d(TAG, username + "同意了你的好友请求");
            msg.setStatus(InviteMesageStatus.BEAGREED);
            notifyNewIviteMessage(msg);

        }

        @Override
        public void onContactRefused(String username) {
            // 参考同意，被邀请实现此功能,demo未实现
            Log.d(username, username + "拒绝了你的好友请求");
        }

    }

    /**
     * 保存提示新消息
     * 
     * @param msg
     */
    private void notifyNewIviteMessage(InviteMessage msg) {
        saveInviteMsg(msg);
        // 提示有新消息
        EMNotifier.getInstance(mainActivity.getApplicationContext()).notifyOnNewMsg();

        // 刷新bottom bar消息未读数
        updateUnreadAddressLable();
        // 刷新好友页面ui
        if (currentTabIndex == 1)
            contactListFragment.refresh();
    }

    /**
     * 保存邀请等msg
     * 
     * @param msg
     */
    private void saveInviteMsg(InviteMessage msg) {
        // 保存msg
        inviteMessgeDao.saveMessage(msg);
        // 未读数加1
        User user = App.getInstance().getContactList().get(Constant.NEW_FRIENDS_USERNAME);
        if (user.getUnreadMsgCount() == 0)
            user.setUnreadMsgCount(user.getUnreadMsgCount() + 1);
    }

    /**
     * set head
     * 
     * @param username
     * @return
     */
    User setUserHead(String username) {
        User user = new User();
        user.setUsername(username);
        String headerName = null;
        if (!TextUtils.isEmpty(user.getNick())) {
            headerName = user.getNick();
        } else {
            headerName = user.getUsername();
        }
        if (username.equals(Constant.NEW_FRIENDS_USERNAME)) {
            user.setHeader("");
        } else if (Character.isDigit(headerName.charAt(0))) {
            user.setHeader("#");
        } else {
            user.setHeader(HanziToPinyin.getInstance().get(headerName.substring(0, 1)).get(0).target.substring(0, 1).toUpperCase());
            char header = user.getHeader().toLowerCase().charAt(0);
            if (header < 'a' || header > 'z') {
                user.setHeader("#");
            }
        }
        return user;
    }

    /**
     * 连接监听listener
     * 
     */
    private class MyConnectionListener implements EMConnectionListener {

        @Override
        public void onConnected() {
            mainActivity.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    chatHistoryFragment.errorItem.setVisibility(View.GONE);
                }

            });
        }

        @Override
        public void onDisconnected(final int error) {
            final String st1 = getResources().getString(R.string.Less_than_chat_server_connection);
            final String st2 = getResources().getString(R.string.the_current_network);
            mainActivity.runOnUiThread(new Runnable() {
                
                @Override
                public void run() {
                    if(error == EMError.USER_REMOVED){
                        // 显示帐号已经被移除
                        showAccountRemovedDialog();
                    }else if (error == EMError.CONNECTION_CONFLICT) {
                        // 显示帐号在其他设备登陆dialog
                        showConflictDialog();
                    } else {
                        chatHistoryFragment.errorItem.setVisibility(View.VISIBLE);
                        if (NetUtils.hasNetwork(mainActivity))
                            chatHistoryFragment.errorText.setText(st1);
                        else
                            chatHistoryFragment.errorText.setText(st2);

                    }
                }

            });
        }
    }

    /**
     * MyGroupChangeListener
     */
    private class MyGroupChangeListener implements GroupChangeListener {

        @Override
        public void onInvitationReceived(String groupId, String groupName, String inviter, String reason) {
            boolean hasGroup = false;
            for (EMGroup group : EMGroupManager.getInstance().getAllGroups()) {
                if (group.getGroupId().equals(groupId)) {
                    hasGroup = true;
                    break;
                }
            }
            if (!hasGroup)
                return;

            // 被邀请
            String st3 = getResources().getString(R.string.Invite_you_to_join_a_group_chat);
            EMMessage msg = EMMessage.createReceiveMessage(Type.TXT);
            msg.setChatType(ChatType.GroupChat);
            msg.setFrom(inviter);
            msg.setTo(groupId);
            msg.setMsgId(UUID.randomUUID().toString());
            msg.addBody(new TextMessageBody(inviter + st3));
            // 保存邀请消息
            EMChatManager.getInstance().saveMessage(msg);
            // 提醒新消息
            EMNotifier.getInstance(mainActivity.getApplicationContext()).notifyOnNewMsg();

            mainActivity.runOnUiThread(new Runnable() {
                public void run() {
                    updateUnreadLabel();
                    // 刷新ui
                    if (currentTabIndex == 0)
                        chatHistoryFragment.refresh();
                    if (CommonUtils.getTopActivity(mainActivity).equals(GroupsActivity.class.getName())) {
                        GroupsActivity.instance.onResume();
                    }
                }
            });

        }

        @Override
        public void onInvitationAccpted(String groupId, String inviter, String reason) {

        }

        @Override
        public void onInvitationDeclined(String groupId, String invitee, String reason) {

        }

        @Override
        public void onUserRemoved(String groupId, String groupName) {
            // 提示用户被T了，demo省略此步骤
            // 刷新ui
            mainActivity.runOnUiThread(new Runnable() {
                public void run() {
                    try {
                        updateUnreadLabel();
                        if (currentTabIndex == 0)
                            chatHistoryFragment.refresh();
                        if (CommonUtils.getTopActivity(mainActivity).equals(GroupsActivity.class.getName())) {
                            GroupsActivity.instance.onResume();
                        }
                    } catch (Exception e) {
                        EMLog.e(TAG, "refresh exception " + e.getMessage());
                    }
                }
            });
        }

        @Override
        public void onGroupDestroy(String groupId, String groupName) {
            // 群被解散
            // 提示用户群被解散,demo省略
            // 刷新ui
            mainActivity.runOnUiThread(new Runnable() {
                public void run() {
                    updateUnreadLabel();
                    if (currentTabIndex == 0)
                        chatHistoryFragment.refresh();
                    if (CommonUtils.getTopActivity(mainActivity).equals(GroupsActivity.class.getName())) {
                        GroupsActivity.instance.onResume();
                    }
                }
            });

        }

        @Override
        public void onApplicationReceived(String groupId, String groupName, String applyer, String reason) {
            // 用户申请加入群聊
            InviteMessage msg = new InviteMessage();
            msg.setFrom(applyer);
            msg.setTime(System.currentTimeMillis());
            msg.setGroupId(groupId);
            msg.setGroupName(groupName);
            msg.setReason(reason);
            Log.d(TAG, applyer + " 申请加入群聊：" + groupName);
            msg.setStatus(InviteMesageStatus.BEAPPLYED);
            notifyNewIviteMessage(msg);
        }

        @Override
        public void onApplicationAccept(String groupId, String groupName, String accepter) {
            String st4 = getResources().getString(R.string.Agreed_to_your_group_chat_application);
            // 加群申请被同意
            EMMessage msg = EMMessage.createReceiveMessage(Type.TXT);
            msg.setChatType(ChatType.GroupChat);
            msg.setFrom(accepter);
            msg.setTo(groupId);
            msg.setMsgId(UUID.randomUUID().toString());
            msg.addBody(new TextMessageBody(accepter + st4));
            // 保存同意消息
            EMChatManager.getInstance().saveMessage(msg);
            // 提醒新消息
            EMNotifier.getInstance(mainActivity.getApplicationContext()).notifyOnNewMsg();

            mainActivity.runOnUiThread(new Runnable() {
                public void run() {
                    updateUnreadLabel();
                    // 刷新ui
                    if (currentTabIndex == 0)
                        chatHistoryFragment.refresh();
                    if (CommonUtils.getTopActivity(mainActivity).equals(GroupsActivity.class.getName())) {
                        GroupsActivity.instance.onResume();
                    }
                }
            });
        }

        @Override
        public void onApplicationDeclined(String groupId, String groupName, String decliner, String reason) {
            // 加群申请被拒绝，demo未实现
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isConflict||!isCurrentAccountRemoved) {
            updateUnreadLabel();
            updateUnreadAddressLable();
            EMChatManager.getInstance().activityResumed();
        }

    }
    
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("isConflict", isConflict);
        outState.putBoolean(Constant.ACCOUNT_REMOVED, isCurrentAccountRemoved);
        super.onSaveInstanceState(outState);
    }


    private android.app.AlertDialog.Builder conflictBuilder;
    private android.app.AlertDialog.Builder accountRemovedBuilder;
    private boolean isConflictDialogShow;
    private boolean isAccountRemovedDialogShow;

    /**
     * 显示帐号在别处登录dialog
     */
    private void showConflictDialog() {
        isConflictDialogShow = true;
        App.getInstance().logout(null);
        String st = getResources().getString(R.string.Logoff_notification);
        if (!mainActivity.isFinishing()) {
            // clear up global variables
            try {
                if (conflictBuilder == null)
                    conflictBuilder = new android.app.AlertDialog.Builder(mainActivity);
                conflictBuilder.setTitle(st);
                conflictBuilder.setMessage(R.string.connect_conflict);
                conflictBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        conflictBuilder = null;
                        mainActivity.finish();
                        startActivity(new Intent(mainActivity, LoginActivity.class));
                    }
                });
                conflictBuilder.setCancelable(false);
                conflictBuilder.create().show();
                isConflict = true;
            } catch (Exception e) {
                EMLog.e(TAG, "---------color conflictBuilder error" + e.getMessage());
            }

        }

    }
    
    
    
    /**
     * 帐号被移除的dialog
     */
    private void showAccountRemovedDialog() {
        isAccountRemovedDialogShow = true;
        App.getInstance().logout(null);
        String st5 = getResources().getString(R.string.Remove_the_notification);
        if (!mainActivity.isFinishing()) {
            // clear up global variables
            try {
                if (accountRemovedBuilder == null)
                    accountRemovedBuilder = new android.app.AlertDialog.Builder(mainActivity);
                accountRemovedBuilder.setTitle(st5);
                accountRemovedBuilder.setMessage(R.string.em_user_remove);
                accountRemovedBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        accountRemovedBuilder = null;
                        mainActivity.finish();
                        startActivity(new Intent(mainActivity, LoginActivity.class));
                    }
                });
                accountRemovedBuilder.setCancelable(false);
                accountRemovedBuilder.create().show();
                isCurrentAccountRemoved = true;
            } catch (Exception e) {
                EMLog.e(TAG, "---------color userRemovedBuilder error" + e.getMessage());
            }

        }

    }

	
	@Override
    public void onDestroy() {
        super.onDestroy();
        // 注销广播接收者
        try {
            mainActivity.unregisterReceiver(msgReceiver);
        } catch (Exception e) {
        }
        try {
            mainActivity.unregisterReceiver(ackMessageReceiver);
        } catch (Exception e) {
        }
        try {
            mainActivity.unregisterReceiver(cmdMessageReceiver);
        } catch (Exception e) {
        }
        
        // try {
        // unregisterReceiver(offlineMessageReceiver);
        // } catch (Exception e) {
        // }

        if (conflictBuilder != null) {
            conflictBuilder.create().dismiss();
            conflictBuilder = null;
        }

    }
}
