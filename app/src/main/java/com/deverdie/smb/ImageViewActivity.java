package com.deverdie.smb;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;
import java.util.ArrayList;

public class ImageViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);

        final ImageView img = (ImageView) findViewById(R.id.imgView);

        final String AppPath = Utils.GetAppPath(getApplicationContext());
        File folder = new File(AppPath);

        final File file[] = folder.listFiles();

        final PlayList pl = new PlayList(file, "jpg");
//        Log.i("dlg", "Playlist getSize : " + pl.getSize());
//        Log.i("dlg", "Playlist getName : " + pl.Next().getName());
//        Log.i("dlg", "Playlist getName : " + pl.Next().getName());
//        Log.i("dlg", "Playlist getName : " + pl.Next().getName());
//        Log.i("dlg", "Playlist getName : " + pl.Next().getName());
//        Log.i("dlg", "Playlist getName : " + pl.Next().getName());
//        Bitmap bmp = BitmapFactory.decodeFile(AppPath + "/" + pl.Next().getName());
//        img.setImageBitmap(bmp);
//        new Handler().postDelayed(new Runnable() {
//            public void run() {
//                Log.i("dlg", "run: ");
////                Log.i("dlg", "Playlist : " + pl.getCurrent().getName());
////                pl.Next();
//            }
//        }, 3000);

        // Hide after some seconds
        final Handler handler  = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Log.i("dlg", "Playlist getName : " + pl.getCurrent().getName());
                Bitmap bmp = BitmapFactory.decodeFile(AppPath + "/" + pl.Next().getName());
                img.setImageBitmap(bmp);
                handler.postDelayed(this, 3000);
            }
        };

        handler.postDelayed(runnable, 3000);
    }

    class PlayList {
        int index = 0;
        // File playlist[];
        ArrayList<File> playlist = new ArrayList<File>();

        public PlayList(File files[], String ext) {
            for (File file : files) {
                if (file.getName().contains("." + ext)) {
                    this.playlist.add(file);
                }
            }
//            this.playlist = playlist;
        }

        public File Next() {
            int i = index;
            if (index >= playlist.size() - 1) {
                index = 0;
            } else {
                index += 1;
            }
            return playlist.get(i);
        }

        public File getCurrent() {
            return playlist.get(index);
        }

        public int getSize() {
            return playlist.size();
        }
    }
}
