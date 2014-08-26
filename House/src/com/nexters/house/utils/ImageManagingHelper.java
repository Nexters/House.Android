package com.nexters.house.utils;

import android.graphics.*;
import android.view.*;
import android.view.animation.*;
import android.widget.*;

import com.nexters.house.*;
import com.nostra13.universalimageloader.core.*;
import com.nostra13.universalimageloader.core.assist.*;

public class ImageManagingHelper {
    public static void loadAndAttachCroppedImage(final ImageView iv, String url) {
        loadAndAttachCroppedImage(iv, url, null);
    }

    public static void loadAndAttachCroppedImage(final ImageView iv, String url, final Animation startAnim) {
        ImageSize targetSize = new ImageSize(iv.getWidth(), iv.getHeight());
        DisplayImageOptions options = new DisplayImageOptions.Builder().build();
        ImageLoader.getInstance().loadImage(url, targetSize, options, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
            	//로딩 이미지 만들어달라고하기 
                iv.setImageResource(R.drawable.user_profile_background);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                iv.setImageBitmap(getCroppedBitmap(loadedImage, iv.getWidth()));
                if (startAnim != null)
                    iv.startAnimation(startAnim);
            }
        });
    }

    public static Bitmap getCroppedBitmap(Bitmap bitmap, int radius) {
        Bitmap scaledBitmap;

        if (bitmap.getWidth() != radius || bitmap.getHeight() != radius) {
            scaledBitmap = Bitmap.createScaledBitmap(bitmap, radius, radius, false);
        } else {
            scaledBitmap = bitmap;
        }

        Bitmap output = Bitmap.createBitmap(scaledBitmap.getWidth(), scaledBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight());

        // 주의: 순서 변경하면 안 됨
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(Color.parseColor("#BAB399"));
        canvas.drawCircle(scaledBitmap.getWidth() / 2 + 0.7f, scaledBitmap.getHeight() / 2 + 0.7f, scaledBitmap.getWidth() / 2 + 0.1f, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(scaledBitmap, rect, rect, paint);

        return output;
    }
}
