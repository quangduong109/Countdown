package com.quangduong.countdown.utils;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;

import java.io.File;

/**
 * Created by QuangDuong on 12/14/2015.
 */
public class AudioPlayer {
    public static void audioPlayer(MediaPlayer mp, String path, String fileName){
        //set up MediaPlayer

        try {
            mp.setDataSource(path+ File.separator+fileName);
            mp.prepare();
            mp.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void playFromAssert(MediaPlayer mp, AssetManager assetManager, String fileName){
        //set up MediaPlayer
        try {
            if(mp.isPlaying()){
                mp.stop();
                mp.reset();
            }else{
                mp.reset();
            }
            AssetFileDescriptor afd = assetManager.openFd(fileName);
            mp.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
            mp.prepare();
            mp.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
