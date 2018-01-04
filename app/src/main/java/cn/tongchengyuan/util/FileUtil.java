package cn.tongchengyuan.util;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by 王者 on 2016/8/19.
 */
public class FileUtil {

    public static InputStream readFile(File file){
        try {
            InputStream in = new BufferedInputStream(new FileInputStream(file));
            return in;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static OutputStream getOutputStream(File file){
        try {
            OutputStream out = new BufferedOutputStream(new FileOutputStream(file));
            return out;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

}
