package com.deverdie.smb;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;

import static com.deverdie.smb.R.id.coordinatorLayout;

public class MainActivity extends AppCompatActivity {

    private ConstraintLayout coodinator;
    private File dir;
    private Button PlayImage;
    private Button PlayVideo;

    class SmbTask extends AsyncTask<String, Void, String> {
        private ProgressDialog dialog = new ProgressDialog(MainActivity.this);

        @Override
        protected void onPreExecute() {
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setMessage("Please wait , application download file.");
            dialog.show();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();
            Snackbar
                    .make(coodinator, "Download complete.", Snackbar.LENGTH_LONG)
                    .setAction("List", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            file = new File( root_sd + "/external_sd" ) ;
                            File list[] = dir.listFiles();

                            for (int i = 0; i < list.length; i++) {
                                Log.i("dlg", "File: " + list[i].getName());
                            }
                        }
                    })
                    .show();
        }

        @Override
        protected String doInBackground(String... strings) {

            UpdateSource("smb://192.168.3.222/Shared/", "USER275", "275", strings[0]);
//            SMBClient client = new SMBClient();

//            try (Connection connection = client.connect("192.168.3.222")) {
//                AuthenticationContext ac = new AuthenticationContext("USER275", "275".toCharArray(),null);
//                Session session = connection.authenticate(ac);
//
//                // Connect to Share
//                try (DiskShare share = (DiskShare) session.connectShare("Public")) {
//                    for (FileIdBothDirectoryInformation f : share.list("Marquee", "*.txt")) {
//                        Log.i("dlg", "File: "+f.getFileName());
//                    }
//                }catch (IOException aE){
//                    Log.i("dlg", "IOException: "+aE);
//                }
//            }catch (IOException aE){
//                Log.i("dlg", "IOException: "+aE);
//            }catch (Exception aE){
//                Log.i("dlg", "Exception: "+aE);
//            }
            return null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        coodinator = (ConstraintLayout) findViewById(coordinatorLayout);

        String extStoreage = Environment.getExternalStorageDirectory().getAbsolutePath();
        String packageName = this.getPackageName();
        String appDataPath = extStoreage + "/Android/data/" + packageName;
        dir = new File(appDataPath);


        PlayImage = (Button) findViewById(R.id.btnPlayImage);
        PlayImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ImageViewActivity.class);
                startActivity(i);
            }
        });

        PlayVideo = (Button) findViewById(R.id.btnPlayVideo);
        PlayVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), VideoViewActivity.class);
                startActivity(i);
            }
        });

        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                new File(dir, children[i]).delete();
            }
        }

        if (!dir.exists()) {
            dir.mkdirs();
        }

        new SmbTask().execute(appDataPath);


//        UpdateSource("smb://192.168.3.222/Shared","USER275","275");

    }


    public void UpdateSource(String strSMBUrl, String strSMBUser, String strSMBPass, String strDir) {
        try {
            SmbFile SremoteFile;
            if (strSMBUser.trim() == "") {
                SremoteFile = new SmbFile(strSMBUrl);
            } else {
                NtlmPasswordAuthentication Sauth = new NtlmPasswordAuthentication(null, strSMBUser, strSMBPass);
                SremoteFile = new SmbFile(strSMBUrl, Sauth);
            }
            SmbFile[] files = SremoteFile.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].getName().toLowerCase().contains(".")) {
                    Log.i("dlg", "Filename : " + files[i].getName());

                    InputStream is = files[i].getInputStream();
                    BufferedInputStream bis = new BufferedInputStream(is);
                    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                    //We create an array of bytes
                    byte[] data = new byte[250];
                    int current = 0;

                    while ((current = bis.read(data, 0, data.length)) != -1) {
                        buffer.write(data, 0, current);
                    }
                    File file = new File(strDir, files[i].getName());
                    FileOutputStream fos = new FileOutputStream(file);
                    fos.write(buffer.toByteArray());
                    fos.flush();
                    fos.close();
                    buffer.flush();
                    buffer.close();
                    bis.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
