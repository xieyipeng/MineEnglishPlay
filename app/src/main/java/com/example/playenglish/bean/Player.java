package com.example.playenglish.bean;

/**
 * Created by 解奕鹏 on 2018/5/13.
 */

import java.io.IOException;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.util.Log;

public class Player implements OnCompletionListener, OnPreparedListener {

    public MediaPlayer mediaPlayer;

    // 初始化播放器
    public Player() {
        super();
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);// 设置媒体流类型
            mediaPlayer.setOnPreparedListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    @SuppressLint("HandlerLeak")
//    Handler handler = new Handler() {
//        public void handleMessage(android.os.Message msg) {
//            int position = mediaPlayer.getCurrentPosition();
//            int duration = mediaPlayer.getDuration();
//            if (duration > 0) {
//                // 计算进度（获取进度条最大刻度*当前音乐播放位置 / 当前音乐时长）
//                long pos = seekBar.getMax() * position / duration;
//                seekBar.setProgress((int) pos);
//            }
//        };
//    };

    public void play() {
        mediaPlayer.start();
    }

    /**
     * @param url url地址
     */
    public void playUrl(String url) {
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(url); // 设置数据源
            mediaPlayer.prepare(); // prepare自动播放
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 暂停
    public void pause() {
        mediaPlayer.pause();
    }

    // 停止
    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    // 播放准备
    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
        Log.e("mediaPlayer", "onPrepared");
    }

    // 播放完成
    @Override
    public void onCompletion(MediaPlayer mp) {
        Log.e("mediaPlayer", "onCompletion");
    }

}
