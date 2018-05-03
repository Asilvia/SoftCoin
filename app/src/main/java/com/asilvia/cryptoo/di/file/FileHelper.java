package com.asilvia.cryptoo.di.file;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

/**
 * Created by asilvia on 27/04/2018.
 */

public interface FileHelper {
    public String writeBmpToFile(Bitmap bitmap, String name);
    public Uri getFileUri(String name);
}
