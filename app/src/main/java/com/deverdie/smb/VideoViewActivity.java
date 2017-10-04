package com.deverdie.smb;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.MediaController;
import android.widget.VideoView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class VideoViewActivity extends AppCompatActivity {

    private VideoView mVideoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_view);
        String AppPath = Utils.GetAppPath(getApplicationContext());
        File folder = new File(AppPath);
        File file[] = folder.listFiles();
        final List<String> playList = new ArrayList<String>();

        for (int i = 0; i < file.length; i++) {
            if (file[i].getName().toLowerCase().contains(".mp4")) {
                String vdoFileName = AppPath + "/" + file[i].getName();
                playList.add(vdoFileName);
                Log.i("dlg", "VideoView File: " + vdoFileName);
            }
        }
        Log.i("dlg", "VideoView Size: " + playList.size());
        final PlayList pl = new PlayList(playList);
        Log.i("dlg", "Playlist filename: " + pl.getCurrent());
        mVideoView = (VideoView) findViewById(R.id.vidView);
        //setEvent
//        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//            @Override
//            public void onPrepared(MediaPlayer mp) {
//                mp.setLooping(true);
//            }
//        });
        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                if (playList.size() > 0) {
                    Log.i("dlg", "Playlist filename: " + pl.getCurrent());
                    mVideoView.stopPlayback();
                    mVideoView.setVideoPath(pl.Next());
                    mVideoView.setMediaController(new MediaController(VideoViewActivity.this));
                    mVideoView.requestFocus();
                    mVideoView.start();
//                    videoView.stopPlayback();
//                    videourl = pmgr.getNextFile();
//                    video = Uri.parse(videourl);
//                    videoView.setVideoURI(video);
//                    videoView.requestFocus();
//                    videoView.start();
                }
            }
        });

        mVideoView.setVideoPath(pl.Next());
        mVideoView.setMediaController(new MediaController(VideoViewActivity.this));
        mVideoView.requestFocus();
        mVideoView.start();
    }

    class PlayList {
        int index = 0;
        List<String> playlist;

        public PlayList(List<String> playlist) {
            this.playlist = playlist;
        }

        public String Next() {
            int i = index;
            if (index >= playlist.size() - 1) {
                index = 0;
            } else {
                index += 1;
            }
            return playlist.get(i);
        }

        public String getCurrent() {
            return playlist.get(index);
        }

        public int getSize() {
            return playlist.size();
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public List<String> getPlaylist() {
            return playlist;
        }

        public void setPlaylist(List<String> playlist) {
            this.playlist = playlist;
        }
    }
}
