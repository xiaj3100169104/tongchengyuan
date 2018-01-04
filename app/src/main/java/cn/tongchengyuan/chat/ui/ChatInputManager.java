package cn.tongchengyuan.chat.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.dmcbig.mediapicker.PickerActivity;
import com.dmcbig.mediapicker.PickerConfig;

import cn.tongchengyuan.activity.SendLocationActivity;
import cn.tongchengyuan.chat.adpter.ExpressionPagerAdapter;
import cn.tongchengyuan.chat.voice.CallVoiceBaseActivity;
import cn.tongchengyuan.chat.xmpp.util.SendMessage;
import cn.tongchengyuan.util.ThreadPoolUtil;
import cn.tongchengyuan.view.AudioRecordButton;
import cn.tongchengyuan.util.BitmapUtil;
import cn.tongchengyuan.util.LogUtil;
import cn.tongchengyuan.util.ToastUtil;

import com.same.city.love.R;
import com.same.city.love.databinding.ActivityChatBinding;
import com.style.constant.FileConfig;
import com.style.constant.Skip;
import com.style.lib.media.camera2video.CameraActivity;
import com.style.utils.CommonUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by 王者 on 2016/8/7.
 */
public class ChatInputManager implements View.OnClickListener {

    private ChatActivity mChatActivity;
    private final ActivityChatBinding bd;

    static Handler mHandler = new Handler();


    public ChatInputManager(ChatActivity chatActivity) {
        //View view = chatActivity.getWindow().getDecorView();
        mChatActivity = chatActivity;
        this.bd = mChatActivity.bd;
        closeBottomContainer();

        bd.recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
    }

    public void onCreate() {
        initEmoticonsViewPager();
        setListener();
    }

    public void onResume() {
        closeBottomContainer();
        hiddenSoftInput();
    }

    public void onDestroy() {
        mChatActivity = null;
    }

    private void initEmoticonsViewPager() {
        List<View> views = new ArrayList<>();
        View gv1 = CommonExpressionView.getStickerView(mChatActivity, bd.chatTool.etInputText);
        views.add(gv1);
        bd.chatTool.emoticonsViewPager.setAdapter(new ExpressionPagerAdapter(views));
    }

    private void setListener() {

        // 监听文字框
        bd.chatTool.etInputText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    bd.chatTool.btnSend.setEnabled(true);
                } else {
                    bd.chatTool.btnSend.setEnabled(false);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        bd.chatTool.btnRecord.setAudioFinishRecorderListener(new AudioRecordButton.AudioFinishRecorderListener() {
            @Override
            public void onFinished(float seconds, String filePath) {
                sendVoice(mChatActivity.getContactUserId(), (int) seconds, filePath);
            }
        });

        bd.recyclerView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                closeBottomContainer();
                hiddenSoftInput();
                return false;
            }
        });
        bd.chatTool.etInputText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //viewMore.setChecked(false);
                closeBottomContainer();
                showSoftInput();
                return false;
            }
        });
        bd.chatTool.btnSend.setOnClickListener(this);

        bd.chatTool.viewCamera.setOnClickListener(this);
        bd.chatTool.viewPicture.setOnClickListener(this);
        bd.chatTool.viewLocation.setOnClickListener(this);
        bd.chatTool.viewEmotion.setOnClickListener(this);
        bd.chatTool.viewAudioRecord.setOnClickListener(this);

        //mChatActivity.findViewById(R.id.view_video_record).setOnClickListener(this);
        //mChatActivity.findViewById(R.id.view_audio).setOnClickListener(this);
        //mChatActivity.findViewById(R.id.view_video_call).setOnClickListener(this);
    }

    public boolean isBottomContainerVisibility() {
        if (bd.chatTool.layoutBottomContainer.getVisibility() == View.VISIBLE) {
            closeBottomContainer();
            return true;
        }
        return false;
    }


    @Override
    public void onClick(View view) {
        String otherUserName = mChatActivity.getContactUserId();
        int id = view.getId();
        switch (id) {
            case R.id.view_picture:
                selectPicFromLocal(); // 点击图片图标
                hiddenSoftInput();
                break;
            case R.id.view_camera:
                takeCamera();         // 点击照相图标
                hiddenSoftInput();
                break;
            case R.id.view_location:
                openMap();       //点击位置图标
                hiddenSoftInput();
                break;
            case R.id.view_emotion:
                closeRecordContainer();
                openEmotionContainer();
                mChatActivity.scrollToBottom(true);
                break;
            case R.id.view_audio_record:
                closeEmotionContainer();
                openRecordContainer();
                mChatActivity.scrollToBottom(true);

                break;
            /*case R.id.view_video_record:
                //mChatActivity.showToast("正在努力开发中");
                recordVideo();       //录制视频
                break;
            case R.id.view_audio_call:
                makeAudioCall(); //语音通话
                break;
            case R.id.view_video_call:// 视频通话
                break;*/
            /*case R.id.view_more:
                onClickMoreView(view);
                break;*/
            case R.id.btn_send:
                // 点击发送按钮(发文字和表情)
                sendText(otherUserName);
                closeBottomContainer();
                hiddenSoftInput();
                break;
        }
    }

    private void closeBottomContainer() {
        bd.chatTool.layoutBottomContainer.setVisibility(View.GONE);
        closeRecordContainer();
        closeEmotionContainer();
    }

    //先打开最外层容器
    private void openBottomContainer() {
        bd.chatTool.layoutBottomContainer.setVisibility(View.VISIBLE);
        hiddenSoftInput();
    }

    private void closeEmotionContainer() {
        bd.chatTool.layoutEmoticonContainer.setVisibility(View.GONE);
        bd.chatTool.viewEmotion.setSelected(false);
    }

    private void openEmotionContainer() {
        hiddenSoftInput();
        //先隐藏输入法,然后延迟显示
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                openBottomContainer();
                bd.chatTool.layoutEmoticonContainer.setVisibility(View.VISIBLE);
                bd.chatTool.viewEmotion.setSelected(true);
            }
        }, 100);
    }

    private void closeRecordContainer() {
        bd.chatTool.layoutRecordContainer.setVisibility(View.GONE);
        bd.chatTool.viewAudioRecord.setSelected(false);
    }

    private void openRecordContainer() {
        hiddenSoftInput();
        //先隐藏输入法,然后延迟显示
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                openBottomContainer();
                bd.chatTool.layoutRecordContainer.setVisibility(View.VISIBLE);
                bd.chatTool.viewAudioRecord.setSelected(true);
            }
        }, 100);
    }

    public void hiddenSoftInput() {
        CommonUtil.hiddenSoftInput(mChatActivity);
    }

    public void showSoftInput() {
        CommonUtil.showSoftInput(mChatActivity, bd.chatTool.etInputText);
    }
    /**
     * 从图库获取图片
     */
    private void selectPicFromLocal() {
        Intent intent = new Intent(mChatActivity, PickerActivity.class);
        intent.putExtra(PickerConfig.SELECT_MODE, PickerConfig.PICKER_IMAGE);//default image and video (Optional)
        long maxSize = 188743680L;//long long long
        intent.putExtra(PickerConfig.MAX_SELECT_SIZE, maxSize); //default 180MB (Optional)
        intent.putExtra(PickerConfig.MAX_SELECT_COUNT, 9);  //default 40 (Optional)
        //intent.putExtra(PickerConfig.DEFAULT_SELECTED_LIST, null); // (Optional)
        mChatActivity.startActivityForResult(intent, PickerConfig.CODE_TAKE_ALBUM);

    }

    private void takeCamera() {
        mChatActivity.cameraFile = CommonUtil.takePhoto(mChatActivity, FileConfig.DIR_IMAGE + File.separator + String.valueOf(System.currentTimeMillis()) + ".jpg");
    }

    private void recordVideo() {
        mChatActivity.startActivityForResult(new Intent(mChatActivity, CameraActivity.class), Skip.CODE_RECORD_VIDEO);
    }

    private void openMap() {
        mChatActivity.startActivityForResult(new Intent(mChatActivity, SendLocationActivity.class), Skip.CODE_MAP);
    }

    private void sendText(String otherUserName) {
        String content = bd.chatTool.etInputText.getText().toString();
        if (!TextUtils.isEmpty(content)) {
            SendMessage.sendTextMsg(otherUserName, content);
            bd.chatTool.etInputText.getText().clear();
        }
    }

    public void sendLocation(String contactName, double latitude, double longitude, String address) {
        SendMessage.sendLocationMsg(contactName, latitude, longitude, address);
        bd.chatTool.etInputText.getText().clear();
    }

    private void sendVoice(String otherUserName, int seconds, String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            LogUtil.e("filePath is invalid!");
            return;
        }
        SendMessage.sendVoiceMsg(otherUserName, seconds, filePath);
    }

    public void sendPicture(final String otherUserName, final ArrayList<String> filePaths) {
        if (filePaths == null || filePaths.isEmpty()) {
            throw new NullPointerException("filePaths should not be empty");
        }

        for (final String filePath : filePaths) {
            ThreadPoolUtil.execute(new Runnable() {
                @Override
                public void run() {
                    Bitmap compressedBitmap = BitmapUtil.compressImage(filePath);
                    if (compressedBitmap == null) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.showToast("选择图片无效，请重新选择", Toast.LENGTH_SHORT);
                            }
                        });
                        return;
                    }
                    String fileName = FileConfig.getUniqueFileName();
                    String filePath = FileConfig.DIR_CACHE + File.separator + fileName;
                    try {
                        com.style.utils.BitmapUtil.saveBitmap(filePath, compressedBitmap, 100);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    int width = compressedBitmap.getWidth();
                    int height = compressedBitmap.getHeight();
                    compressedBitmap.recycle();
                    SendMessage.sendPictureMsg(otherUserName, filePath, width, height);
                }
            });
        }

    }

    public void sendOfflineVideo(final String otherUserName, final String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            throw new NullPointerException("filePaths should not be empty");
        }
        ThreadPoolUtil.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                    retriever.setDataSource(filePath);
                    Bitmap bitmap = retriever.getFrameAtTime(1000 * 1000, MediaMetadataRetriever.OPTION_CLOSEST);
                    //ivVideoPreview.setImageBitmap(bitmap);
                    com.style.utils.BitmapUtil.saveBitmap(filePath + ".image", bitmap, 100);
                    int width = bitmap.getWidth();
                    int height = bitmap.getHeight();
                    SendMessage.sendOfflineVideoMsg(otherUserName, new File(filePath), width, height);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 拨打语音电话
     */
    private void makeAudioCall() {
        Intent intent = new Intent(mChatActivity, CallVoiceBaseActivity.class);
        intent.putExtra(Skip.KEY_USER_NAME, mChatActivity.getContactUserId());
        mChatActivity.startActivity(intent);
    }

}
