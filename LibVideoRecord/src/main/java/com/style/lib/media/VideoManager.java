package com.style.lib.media;

/**
 * Created by xiajun on 2017/4/2.
 */

public class VideoManager {

    private static VideoManager instance;
    public String videoPathDir;


    public static VideoManager getInstance() {
        if (instance == null)
            instance = new VideoManager();
        return instance;
    }

}
