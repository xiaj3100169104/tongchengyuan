package com.juns.wechat.xmpp.util;



import com.juns.wechat.common.BASE64;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


/**
 * Created by 王者 on 2015/1/26.
 */
public class MsgCode {
    private String TAG = MsgCode.class.getName();

    public String encode(String filePath){
        byte[] data = null;

        // 读取图片字节数组
        try {

            InputStream in = new FileInputStream(filePath);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 对字节数组Base64编码=1

        BASE64 encoder = new BASE64();
        return encoder.encode(data);// 返回Base64编码过的字节数组字符串*/
    }

    /**
     * 将字符串解码并存入指定文件中
     * @param encodedStr
     * @param filePath
     * @return
     */
    public boolean decode(String encodedStr, String filePath){
        if (encodedStr == null) // 图像数据为空
            return false;
        try {
            BASE64 decoder = new BASE64();
            byte[] decodedData = decoder.decode(encodedStr);

            OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(filePath));
            outputStream.write(decodedData);
            outputStream.flush();
            outputStream.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

          /*  for (int i = 0; i < bytes.length; ++i) {
                if (bytes[i] < 0) {// 调整异常数据
                    bytes[i] += 256;
                }
            }*/
        return false;
    }
}
