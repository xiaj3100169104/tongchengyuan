package custom.camera2;

import android.graphics.Bitmap;
import android.os.Environment;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;

/**
 * Created by Sikang on 2017/6/5.
 */

public class OcrUtil {
    //字体库路径，必须包含tesseract文件夹
    static final String TESSBASE_PATH = Environment.getExternalStorageDirectory() + File.separator + "Download" + File.separator;
    //识别语言英文
    static final String ENGLISH_LANGUAGE = "eng";
    //识别语言简体中文
    static final String CHINESE_LANGUAGE = "chi_sim";

    private OcrUtil() {
    }


    /**
     * 识别英文
     *
     * @param bmp      需要识别的图片
     * @param callBack 结果回调（携带一个String 参数即可）
     */
    public static void ScanEnglish(final Bitmap bmp, final MyCallBack callBack) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                TessBaseAPI baseApi = new TessBaseAPI();
                //初始化OCR的字体数据，TESSBASE_PATH为路径，ENGLISH_LANGUAGE指明要用的字体库（不用加后缀）
                if (baseApi.init(TESSBASE_PATH, ENGLISH_LANGUAGE)) {
                    //设置识别模式
                    baseApi.setPageSegMode(TessBaseAPI.PageSegMode.PSM_AUTO);
                    //设置要识别的图片
                    baseApi.setImage(bmp);
                    //开始识别
                    String result = baseApi.getUTF8Text();
                    baseApi.clear();
                    baseApi.end();
                    callBack.response(result);
                }

            }
        }).start();
    }

    /**
     * 识别中文
     *
     * @param bmp      需要识别的图片
     * @param callBack 结果回调（携带一个String 参数即可）
     */
    public static void ScanChinese(final Bitmap bmp, final MyCallBack callBack) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                TessBaseAPI baseApi = new TessBaseAPI();
                //初始化OCR的字体数据，TESSBASE_PATH为路径，ENGLISH_LANGUAGE指明要用的字体库（不用加后缀）
                if (baseApi.init(TESSBASE_PATH, CHINESE_LANGUAGE)) {
                    //设置识别模式
                    baseApi.setPageSegMode(TessBaseAPI.PageSegMode.PSM_AUTO);
                    //设置要识别的图片
                    baseApi.setImage(bmp);
                    //开始识别
                    String result = baseApi.getUTF8Text();
                    baseApi.clear();
                    baseApi.end();
                    callBack.response(result);
                }

            }
        }).start();
    }
}
