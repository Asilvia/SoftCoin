package com.android.asilvia.cryptoo.di.file;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.net.Uri;

import java.io.File;
import java.io.FileOutputStream;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * Created by asilvia on 27/04/2018.
 */

public class AppFileHelper implements FileHelper {
    File directory;

    @Inject
    public AppFileHelper(Context context, @FilesInfo.FileInfo String appFileName) {
        ContextWrapper cw = new ContextWrapper(context);
        directory = cw.getDir(appFileName, Context.MODE_PRIVATE);
    }

    @Override
    public String writeBmpToFile(Bitmap bitmap, String name) {
        String fullName = name + ".jpg";

        // Create imageDir
        File mypath=new File(directory,fullName);

        FileOutputStream fos = null;
        try {

            fos = new FileOutputStream(mypath);

            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Timber.d("absolutepath " + directory.getAbsolutePath());
        return directory.getAbsolutePath();
    }

    @Override
    public Uri getFileUri(String name) {
        String fullName = name + ".jpg";
        return Uri.fromFile( new File(directory, fullName));
    }
}
