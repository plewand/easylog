package com.codigine.easylog;


import android.os.Environment;

public class EnvUtil {
    boolean externalStorageMounded() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    String externalStorageDirectory() {
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }
}
