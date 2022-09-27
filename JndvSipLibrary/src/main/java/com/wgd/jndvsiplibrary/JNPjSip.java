package com.wgd.jndvsiplibrary;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.Surface;
import android.widget.Toast;

import com.ehome.sipservice.BroadcastEventReceiver;
import com.ehome.sipservice.SipAccountData;
import com.ehome.sipservice.SipService;
import com.ehome.sipservice.SipServiceCommand;
import com.wgd.jndvsiplibrary.utils.JNLogUtil;
import com.wgd.jndvsiplibrary.utils.JNPjSipConstants;
import com.wgd.jndvsiplibrary.utils.JNSpUtils;

import org.pjsip.pjsua2.VideoPreview;
import org.pjsip.pjsua2.VideoWindow;
import org.pjsip.pjsua2.pjsip_inv_state;
import org.pjsip.pjsua2.pjsip_status_code;

/**
 * pjsip相关工具类
 * */
public class JNPjSip {
    private static final String TAG = "PjSip";
    private static JNPjSip instance = null;

    private String psw;
    private String host;
    private String sipRealm;
    private long port = 0;
    private String user;
    private SipAccountData mSipAccount = null;
    private static final String KEY_SIP_ACCOUNT = "sip_account";
    private int mCurrentCallId = -1; // -1 means no call
    private String mAccountId = null;
    private Context mContext;
    private PjSipListener mPjSipListener = null;
    private PjSipMessageListener messageListener = null;
    private PjSipCallStateListener mCallStateListener = null;

    private WindowListener mWindowListener = null;
    private boolean isOuting = true;//标记是拨出还是打入

    private BroadcastEventReceiver sipEvents = new BroadcastEventReceiver() {
        @Override
        public void onRegistration(String accountID, pjsip_status_code registrationStateCode) {
            Log.d(TAG, "onRegistration, accountID:" + accountID + "," + registrationStateCode);
            if (registrationStateCode == pjsip_status_code.PJSIP_SC_OK) {
                mAccountId = accountID;
                JNSpUtils.setString(mContext,"myAccount",accountID);
                Toast.makeText(mContext, "User:" + accountID + " 注册成功", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "onRegistration: " + "User:" + accountID + " 注册成功！");
                if (mPjSipListener != null)
                    mPjSipListener.onRegistration(true);
            } else {
                Toast.makeText(mContext, "User:" + accountID + "Unregistered", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "onRegistration: " + "User:" + accountID + " 注册失败, code:" + registrationStateCode);
                if (mPjSipListener != null)
                    mPjSipListener.onRegistration(false);
            }
        }

        @Override
        public void onIncomingCall(String accountID, int callID, String displayName, String remoteUri, boolean isVideo) {
            mAccountId = accountID;
            mCurrentCallId = callID;
            isOuting = false;
            //super.onIncomingCall(accountID, callID, displayName, remoteUri);
            //Logger.debug(TAG, "accountId:" + accountID + ", callID:" + callID + ", displayName:" + displayName);
            //mStatusStrings.add("IncomingCall, accountId:" + accountID + ", callID:" + callID + " displayName:" + displayName);
            //mStatusAdapter.notifyDataSetChanged();
            Log.e(TAG, "onIncomingCall: " + "收到来电 ：" + displayName + " mAccountId: " + accountID + " mCurrentCallId: " + callID);
            if (mPjSipListener != null)
                mPjSipListener.onIncomingCall(accountID, callID, displayName, remoteUri, isVideo);
        }

        @Override
        public void onCallState(String accountID, int callID, pjsip_inv_state callStateCode, pjsip_status_code statusCode, long connectTimestamp, boolean isLocalHold, boolean isLocalMute) {
            //super.onCallState(accountID, callID, callStateCode, connectTimestamp, isLocalHold, isLocalMute);
            String log = null;
            if (callStateCode == pjsip_inv_state.PJSIP_INV_STATE_INCOMING) {
                log = "来电";

            } else if (callStateCode == pjsip_inv_state.PJSIP_INV_STATE_CALLING) {
                log = "正在拨号";

            } else if (callStateCode == pjsip_inv_state.PJSIP_INV_STATE_CONFIRMED) {
                log = "开始通话，通话中...";

            } else if (callStateCode == pjsip_inv_state.PJSIP_INV_STATE_CONNECTING) {
                log = "正在建立通话";

            } else if (callStateCode == pjsip_inv_state.PJSIP_INV_STATE_DISCONNECTED) {
                log = "通话结束";
                if (statusCode != null) {
                    if (statusCode == pjsip_status_code.PJSIP_SC_OK) {
                        log = log + " 正常结束";
                    } else if (statusCode == pjsip_status_code.PJSIP_SC_DECLINE) {
                        log = log + " 挂断";
                    } else {
                        log = log + " 不方便接听";
                    }
                }
                mCurrentCallId = -1;
            } else if (callStateCode == pjsip_inv_state.PJSIP_INV_STATE_EARLY) {
                log = "对方已振铃";
            }
            if (mCallStateListener != null)
                mCallStateListener.onCallState(log, callStateCode, accountID, callID, connectTimestamp, isLocalHold, isLocalMute);
            Log.e(TAG, "onCallState: " + log + " - " + callStateCode + ", " + (statusCode != null ? statusCode : "null"));
        }

        @Override
        public void onOutgoingCall(String accountID, int callID, String number, boolean isvideo, boolean one, boolean two) {
            mAccountId = accountID;
            mCurrentCallId = callID;
//            super.onOutgoingCall(accountID, callID, number);
            Log.e(TAG, "onOutgoingCall: " + "正在拨号：" + number + " mAccountId: " + accountID + " mCurrentCallId: " + callID);
            if (mPjSipListener != null)
                mPjSipListener.onOutgoingCall(accountID, callID, number);
        }

        @Override
        public void onStackStatus(boolean started) {
            super.onStackStatus(started);
            Log.e(TAG, "onStackStatus: started=" + started);
            //addLog("Stack Status:" + started);
        }

        @Override
        public void onReceiveDtmf(String accountID, int callID, String dtmfDigit) {
            super.onReceiveDtmf(accountID, callID, dtmfDigit);
            Log.e(TAG, "onReceiveDtmf: " + "收到DTMF:" + dtmfDigit);
        }

        @Override
        public void gainSipCall(VideoWindow videoWindow, VideoPreview videoPreview) {
            super.gainSipCall(videoWindow, videoPreview);
            Log.e(TAG, "gainSipCall: ");
            if (mWindowListener != null)
                mWindowListener.getVideoWindow(videoWindow, videoPreview);
        }

        @Override
        public void onInstantMessage(String msg, String from, String to) {
            super.onInstantMessage(msg, from, to);
            Log.e(TAG, "onInstantMessage: "+msg+"  "+from+"  "+to );
            if (messageListener!=null){
                messageListener.onInstantMessage(msg,from,to);
            }

        }

        @Override
        public void onInstantMessageStatus(String msg, String to,int state) {
            super.onInstantMessageStatus(msg, to, state);
            if (messageListener!=null){
                messageListener.onInstantMessageStatus(msg,to, state);
            }
        }

        @Override
        public void onConnectState(int state) {
            super.onConnectState(state);
            if (null!=mPjSipListener)mPjSipListener.onConnectState(state);
        }
    };

    private JNPjSip(Context context) {
        this.mContext = context;
    }

    public static JNPjSip getInstance(Context context) {
        if (instance == null) {
            synchronized (JNPjSip.class) {
                if (instance == null) {
                    instance = new JNPjSip(context);
                }
            }
        }
        return instance;
    }

    //注册
    public void sipRegister() {
        user = JNSpUtils.getString(mContext, JNPjSipConstants.PJSIP_NUMBER,JNPjSipConstants.PJSIP_NUMBER_DEFAULT);
        psw = JNSpUtils.getString(mContext, JNPjSipConstants.PJSIP_PSWD,JNPjSipConstants.PJSIP_PSWD_DEFAULT);
        host = JNSpUtils.getString(mContext, JNPjSipConstants.PJSIP_HOST,JNPjSipConstants.PJSIP_HOST_DEFAULT);
        if(psw.equals("")){
            psw = "1234";
        }
//        sipRealm = SpUtil.getString(mContext, PjSipConstants.PJSIP_REALM);
        sipRealm = host;
        JNLogUtil.e(TAG, "sipRegister:user: " + user);
        JNLogUtil.e(TAG, "sipRegister:sipRealm: " + sipRealm);
        port = 0;//Long.parseLong(TextUtils.equals(JCSpUtils.getString(mContext, JCPjSipConstants.PJSIP_PORT), "") ? "0" : JCSpUtils.getString(mContext, JCPjSipConstants.PJSIP_PORT,JCPjSipConstants.PJSIP_PORT_DEFAULT));
//        port = Long.parseLong(JCPjSipConstants.PJSIP_PORT_DEFAULT);//Long.parseLong(TextUtils.equals(JCSpUtils.getString(mContext, JCPjSipConstants.PJSIP_PORT), "") ? "0" : JCSpUtils.getString(mContext, JCPjSipConstants.PJSIP_PORT,JCPjSipConstants.PJSIP_PORT_DEFAULT));
        try {
            port = Long.parseLong(JNSpUtils.getString(mContext, JNPjSipConstants.PJSIP_PORT, "0"));
        }catch (Exception e){
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(user) || TextUtils.isEmpty(psw) || TextUtils.isEmpty(host)
//                || port == 0
        ) {
            JNLogUtil.e(TAG, "sipRegister: 请确认sip信息正确 user："+user+" psw:"+psw +" host:"+host + " port:" + port);
        } else {
            // we can set sip stack log level to debug
            SipServiceCommand.setSipStackLogLevel(mContext, 5);
            mSipAccount = new SipAccountData();

            mSipAccount.setHost(host)
                    .setPort(port)
                    .setUsername(user)
                    .setPassword(psw)
                    .setRealm(sipRealm)
                    .setAuthenticationType(SipAccountData.AUTH_TYPE_DIGEST);
            if(0!=port)mSipAccount.setPort(port);
            SipServiceCommand.setAccount(mContext, mSipAccount);

            //SipServiceCommand.getCodecPriorities(this);
            JNLogUtil.d(TAG, "register: " + mSipAccount.getRegistrarUri() + ", " + mSipAccount.getIdUri());
        }
    }

    //给好友发送信息
    public static void sendMessageToBuddy(Context context, String content, String accountID, String uri, boolean subscribe){
        Intent intent = new Intent(context, SipService.class);
        intent.setAction(SipServiceCommand.SENDMESSAGETOBUDDY);
        intent.putExtra("content", content);
        intent.putExtra(SipServiceCommand.PARAM_ACCOUNT_ID, accountID);
        intent.putExtra(SipServiceCommand.BUDDY_URI, uri);
        intent.putExtra(SipServiceCommand.BUDDY_SUBSCIBE, subscribe);
        context.startService(intent);
    }

    //呼出
    public void onCall(String number, boolean isVideo) {
        if (mSipAccount == null) {
            Log.e(TAG, "onCall: Add an account and register it first");
            Toast.makeText(mContext, "未注册！", Toast.LENGTH_SHORT).show();
            return;
        } else {
            if (TextUtils.isEmpty(user) || TextUtils.isEmpty(psw) || TextUtils.isEmpty(user) || TextUtils.isEmpty(host) || port == 0) {
                Log.e(TAG, "sipRegister: 请确认sip信息正确" + port);
            }

            if (number.equals(user)) {
                Toast.makeText(mContext, "无法拨打自己的号码！", Toast.LENGTH_SHORT).show();
                return;
            }


            if (number.isEmpty()) {
                number = "*9000";
                //Toast.makeText(this, "Provide a number to call", Toast.LENGTH_SHORT).show();
                //return;
            }

            SipServiceCommand.makeCall(mContext, mSipAccount.getIdUri(), number, isVideo);
            Log.d(TAG, "make call:" + number);

            //AudioManager am = (AudioManager)getSystemService(Service.AUDIO_SERVICE);
            //am.setSpeakerphoneOn(true);
        }


    }

    //挂断
    public void onTerminate() {
//        SipServiceCommand.hangUpActiveCalls(mContext, mSipAccount.getIdUri());
        if (mSipAccount != null)
            SipServiceCommand.hangUpCall(mContext, mSipAccount.getIdUri(), mCurrentCallId);

    }

    //应答
    public void onAnswer() {
        if (mCurrentCallId == -1) {
            Log.e(TAG, "onAnswer: answer call no current call");
            return;
        }
        SipServiceCommand.acceptIncomingCall(mContext, mAccountId, mCurrentCallId);
        //startActivity(new Intent(this, CliActivity.class));
    }

    //拒绝来电
    public void declineIncomingCall() {
        SipServiceCommand.declineIncomingCall(mContext, mAccountId, mCurrentCallId);
    }

    //切换通话静音状态
    public void toggleCallMute() {
        SipServiceCommand.toggleCallMute(mContext, mAccountId, mCurrentCallId);
    }

    //旋转镜头
    public void changeVideoOrientation(int orientation) {
        SipServiceCommand.changeVideoOrientation(mContext, mAccountId, mCurrentCallId, orientation);
    }

    //通话保持，视频及音频双方都停止了
    public void setCallHold(boolean isHold) {
        SipServiceCommand.setCallHold(mContext, mAccountId, mCurrentCallId, isHold);
    }

    //
    public void setVideoNoFlow(boolean isFlow) {
        SipServiceCommand.setVideoNoFlow(mContext, mAccountId, mCurrentCallId, isFlow);
    }

    //停止通话预览
    public void stopPreview() {
        SipServiceCommand.stopVideoPreview(mContext, mAccountId, mCurrentCallId);
    }

    //启动呼叫预览
    public void startPreview(Surface surface) {
        SipServiceCommand.startVideoPreview(mContext, mAccountId, mCurrentCallId, surface);
    }

    //切换前后摄像头
    public void switchVideoCaptureDevice() {
        SipServiceCommand.switchVideoCaptureDevice(mContext, mAccountId, mCurrentCallId);
    }

    /**
     * 视频静音，就是我的视频画面停止，即我这边不预览不推流
     * @param isMute true=不推；false=推流
     */
    public void setVideoMute(boolean isMute) {
        SipServiceCommand.setVideoMute(mContext, mAccountId, mCurrentCallId, isMute);
    }

    //设置传入视频源
    public void setupIncomingVideoFeed(Surface surface) {
        SipServiceCommand.setupIncomingVideoFeed(mContext, mAccountId, mCurrentCallId, surface);
    }

    //添加好友
    public void addBuddy(String uri, boolean subscribe) {
        SipServiceCommand.addBuddy(mContext, mAccountId, uri, subscribe);
    }

    //向好友发送信息
    public void sendMessageToBuddy() {
        SipServiceCommand.sendMessageToBuddy(mContext);
    }




    //
    public void sendDTMF(String dtmfNumber) {
        if (mCurrentCallId == -1) {
            Log.e(TAG, "sendDTMF: sendDtmf no current call");
            return;
        }
        //Toast.makeText(this, "sendDtmf", Toast.LENGTH_LONG).show();
        SipServiceCommand.sendDTMF(mContext, mAccountId, mCurrentCallId, dtmfNumber.trim());
    }

    //注册广播
    public void sipBroadcastRegister() {
        sipEvents.register(mContext);
    }

    //注销广播
    public void sipBroadcastUnRegister() {
        sipEvents.unregister(mContext);
    }

    public void setPjSipListener(PjSipListener listener) {
        this.mPjSipListener = listener;
    }

    public void setMessageListener(PjSipMessageListener messageListener) {
        this.messageListener = messageListener;
    }

    public void setCallStateListener(PjSipCallStateListener callStateListener) {
        this.mCallStateListener = callStateListener;
    }

    public void setmWindowListener(WindowListener windowListener) {
        this.mWindowListener = windowListener;
    }

    public interface PjSipListener {
        void onRegistration(boolean isSuccessed);

        void onIncomingCall(String accountID, int callID, String displayName, String remoteUri, boolean isVideo);
        //        void onCallState(String accountID, int callID, pjsip_inv_state callStateCode, pjsip_status_code statusCode, long connectTimestamp, boolean isLocalHold, boolean isLocalMute);

        void onOutgoingCall(String accountID, int callID, String number);

        void onConnectState(int state);


    }

    public interface PjSipCallStateListener {
        void onCallState(String status, pjsip_inv_state code, String accountID, int callID, long connectTimestamp, boolean isLocalHold, boolean isLocalMute);
    }

    public interface PjSipMessageListener {
        void onInstantMessage(String msg, String from_uri, String to_uri);
        void onInstantMessageStatus(String msg, String to_uri, int state);
    }


    public interface WindowListener {
        void getVideoWindow(VideoWindow videoWindow, VideoPreview videoPreview);
    }

    //停止震动
    public void stopRingtoneCommand() {
        SipServiceCommand.stopRingtoneCommand(mContext, mAccountId, mCurrentCallId);
    }

    //开始震动
    public void startRingtoneCommand() {
        SipServiceCommand.startRingtoneCommand(mContext, mAccountId, mCurrentCallId);
    }

}
