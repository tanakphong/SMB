package com.deverdie.smb;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * Created by USER275 on 9/29/2017.
 */

public class Utils {
    public static String GetAppPath(Context context){
        String extStoreage = Environment.getExternalStorageDirectory().getAbsolutePath();
        String packageName = context.getPackageName();
        return  extStoreage + "/Android/data/" + packageName;
    }
    public static void CreateDirectory(File fileOrFolder){
//        File fileOrFolder=new File(path);
        if (fileOrFolder.isDirectory())
        {
            String[] children = fileOrFolder.list();
            for (int i = 0; i < children.length; i++)
            {
                new File(fileOrFolder, children[i]).delete();
            }
        }
        if (!fileOrFolder.exists()) {
            fileOrFolder.mkdirs();
        }
    }
}
