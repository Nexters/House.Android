package com.nexters.house.thread;

import java.io.InputStream;

import com.nexters.house.utils.ImageManagingHelper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;
    boolean isCrop;
    
    public DownloadImageTask(ImageView bmImage) {
        this.bmImage = bmImage;
    }

    public DownloadImageTask setCrop(boolean isCrop){
    	this.isCrop = isCrop;
    	return this;
    }
    
    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
//            e.printStackTrace();
        }
        return mIcon11;
    }
    
    protected void onPostExecute(Bitmap result) {
    	if(!isCrop && result != null && bmImage != null)
    		bmImage.setImageBitmap(result);
    	if(isCrop && result != null && bmImage != null){
			int width = (int)(bmImage.getLayoutParams().width);
			int height = (int)(bmImage.getLayoutParams().height);

			if(width > 0)
				bmImage.setImageBitmap(ImageManagingHelper.getCroppedBitmap(result, width));
			else if(height > 0)
				bmImage.setImageBitmap(ImageManagingHelper.getCroppedBitmap(result, height));
    	}
    }
}