package cn.tongchengyuan.chat.ui;

import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;


import com.same.city.love.R;

import java.io.IOException;

/**
 * Created by wangshuai on 2015/4/19.
 */
public class ChatMediaPlayer {
    private String TAG = ChatMediaPlayer.class.getName();
    private MediaPlayer mPlayer;
    private ImageView mVoiceView;
    private int mVoiceShowCnt = 0;
    private static ChatMediaPlayer mChatMediaPlayer= null;
    private boolean mIsOutVoice;
    private volatile boolean mIsRunning = false;

    private Handler mVoicePlayHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (!mIsRunning){
                return;
            }
            Log.d(TAG,"handleMessage SET");

            if (mIsOutVoice){
                int cnt = (msg.what) % (mOutVoiceShowRes.length);
                //view.setVisibility(View.VISIBLE);
                mVoiceView.setImageResource(mOutVoiceShowRes[cnt]);
            }else{
                int cnt = (msg.what) % (mInVoiceShowRes.length);
                //view.setVisibility(View.VISIBLE);
                mVoiceView.setImageResource(mInVoiceShowRes[cnt]);
            }
        }
    };
    private static int[] mInVoiceShowRes = { R.drawable.chatfrom_voice_playing_f1, R.drawable.chatfrom_voice_playing_f2,
            R.drawable.chatfrom_voice_playing_f3};
    private static int[] mOutVoiceShowRes = { R.drawable.chatto_voice_playing_f1, R.drawable.chatto_voice_playing_f2,
            R.drawable.chatto_voice_playing_f3};

    private Runnable mVoiceplayRunnable = new Runnable() {
        @Override
        public void run() {
            while (null !=mPlayer && mPlayer.isPlaying()){
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                mVoicePlayHandler.sendEmptyMessage(mVoiceShowCnt++);
            }
        }
    };

    private ChatMediaPlayer(){
        mPlayer = new MediaPlayer();
    }

    public static ChatMediaPlayer getInstance(){
        if (null == mChatMediaPlayer){
            mChatMediaPlayer = new ChatMediaPlayer();
        }
        return mChatMediaPlayer;
    }

    private void setOnCompleteListener(){
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                //动画结束
                //mChatVoiceShow.onVoiceEndShow();
                mVoiceShowCnt  = 0;
                mIsRunning = false;
                if (mIsOutVoice){
                    mVoiceView.setImageResource(R.drawable.chatto_voice_playing);
                }else{
                    mVoiceView.setImageResource(R.drawable.chatfrom_voice_playing);
                }
            }
        });
    }

    public void stopVoice(){
        if (null != mPlayer){
            if(mPlayer.isPlaying()){
                mPlayer.stop();
            }
            mPlayer.reset();
            mIsRunning = false;
        }
        if (null != mVoiceView){
            if (mIsOutVoice){
                mVoiceView.setImageResource(R.drawable.chatto_voice_playing);
            }else{
                mVoiceView.setImageResource(R.drawable.chatfrom_voice_playing);
            }

        }
    }

    public void setVoiceDir(boolean isOutGoing){
        mIsOutVoice = isOutGoing;
    }

    public void setVoiceView(ImageView voiceView){
        mVoiceView = voiceView;
    }

    public void playVoice(String voicePath){
        try {
            mIsRunning = true;
            if (null == mPlayer){
                mPlayer  = new MediaPlayer();
            }else{
                if(mPlayer.isPlaying()){
                    mPlayer.stop();
                }
            }

            mPlayer.reset();
            String filePath = voicePath;
            mPlayer.setDataSource(filePath);
            mPlayer.prepare();
            mPlayer.start();

            //播放动画启动
            setOnCompleteListener();
            mVoiceShowCnt = 0;
            Thread thread = new Thread(mVoiceplayRunnable);
            thread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void release(){
        if (null != mPlayer){
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
            mIsRunning = false;
        }
        if (null != mVoiceView){
            if (mIsOutVoice){
                mVoiceView.setImageResource(R.drawable.chatto_voice_playing);
            }else{
                mVoiceView.setImageResource(R.drawable.chatfrom_voice_playing);
            }
        }
    }

    public boolean isRunning(){
        return mIsRunning;
    }

}
