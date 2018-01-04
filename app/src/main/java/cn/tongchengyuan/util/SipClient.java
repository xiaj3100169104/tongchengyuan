package cn.tongchengyuan.util;

import android.app.PendingIntent;
import android.content.Intent;
import android.net.sip.SipAudioCall;
import android.net.sip.SipException;
import android.net.sip.SipManager;
import android.net.sip.SipProfile;
import android.net.sip.SipRegistrationListener;

import cn.tongchengyuan.manager.AccountManager;
import cn.tongchengyuan.app.App;
import cn.tongchengyuan.bean.UserBean;
import cn.tongchengyuan.chat.config.ConfigUtil;

import java.text.ParseException;

/**
 * Created by 王者 on 2016/12/2.
 */

public class SipClient {
    private static SipClient mInstance;

    private SipManager sipManager;
    private SipProfile myProfile;
    private boolean mSessionOpened = false;
    private SipAudioCall call;
    private static final UserBean myself = AccountManager.getInstance().getUser();
    private static final String xmppServer = ConfigUtil.getXmppServer();

    public static SipClient getInstance(){
        if(mInstance == null){
            mInstance = new SipClient();
        }
        return mInstance;
    }

    private SipClient(){
    }

    public void initSession(){
        if(sipManager == null){
            sipManager = SipManager.newInstance(App.getInstance());
        }
        if(sipManager == null){  //不支持sip
            return;
        }
        if (myProfile != null) {
            closeLocalProfile();
        }
        try {
            SipProfile.Builder builder = new SipProfile.Builder(myself.getUserName(), xmppServer);
            builder.setPassword(myself.getPassWord());
            myProfile = builder.build();
            LogUtil.i(myProfile.getUriString());

            Intent i = new Intent();
            i.setAction("android.SipDemo.INCOMING_CALL");
            PendingIntent pi = PendingIntent.getBroadcast(App.getInstance(), 0, i, Intent.FILL_IN_DATA);
            sipManager.open(myProfile, pi, null);

            sipManager.setRegistrationListener(myProfile.getUriString(), new SipRegistrationListener() {
                public void onRegistering(String localProfileUri) {
                    LogUtil.i("sip registering");
                }

                public void onRegistrationDone(String localProfileUri, long expiryTime) {
                    LogUtil.i("sip register done!");
                    mSessionOpened = true;
                }

                public void onRegistrationFailed(String localProfileUri, int errorCode,
                                                 String errorMessage) {;
                    LogUtil.i("sip register failed");
                }
            });

        } catch (ParseException e) {
            e.printStackTrace();
        } catch (SipException e) {
            e.printStackTrace();
        }

    }

    public void makeAudioCall(String toUserName){
        if(!mSessionOpened){
            initSession(); //重新初始化session
        }
        try {
            SipAudioCall.Listener listener = new SipAudioCall.Listener() {
                @Override
                public void onCallEstablished(SipAudioCall call) {
                    call.startAudio();
                    call.setSpeakerMode(true);
                    call.toggleMute();
                }

                @Override
                public void onCallEnded(SipAudioCall call) {

                }
            };

            String sipAddress = toUserName + "@" + xmppServer;
            call = sipManager.makeAudioCall(myProfile.getUriString(), sipAddress, listener, 30);

        }
        catch (Exception e) {
            LogUtil.e(e.getMessage());
            closeLocalProfile();
            if (call != null) {
                call.close();
            }
        }
    }

    public void endCall(){
        if(call != null){
            call.close();
        }
    }

    public void destroySession(){
        closeLocalProfile();
    }

    /**
     * Closes out your local profile, freeing associated objects into memory
     * and unregistering your device from the server.
     */
    private void closeLocalProfile() {
        mSessionOpened = false;
        if (sipManager == null) {
            return;
        }
        try {
            if (myProfile != null) {
                sipManager.close(myProfile.getUriString());
            }
        } catch (Exception ee) {
            LogUtil.e(ee.getMessage());
        }
        if (call != null) {
            call.close();
        }
    }

}
