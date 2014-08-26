package com.nexters.house.utils;


import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;

public class TempFileManager {

    private static final String IMAGE_FILE_NAME = "house.jpg";

    private static final String basePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/.House/";
    private static final String imageFilePath = basePath + IMAGE_FILE_NAME;

    public static File saveBitmapToImageFile(Bitmap bitmap) {
        File file = new File(getImageFilePath());
        OutputStream out;
        try {
            file.createNewFile();
            out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    public static Uri getImageFileUri() {
        return Uri.fromFile(getImageFile());
    }

    public static String getImageFilePath() {
        File file = new File(basePath);
        if(!file.exists())
            file.mkdirs();
        return imageFilePath;
    }

    public static File getImageFile() {
        return new File(imageFilePath);
    }

    public static void deleteImageFile() {
        File file = getImageFile();
        if (file.exists())
            file.delete();
    }

    public static boolean isExistImageFile() {
        File file = getImageFile();
        return file.exists();
    }
}
