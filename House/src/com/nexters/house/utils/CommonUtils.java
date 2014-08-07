package com.nexters.house.utils;

import android.content.*;
import android.util.*;
// 뷰의 미니멈 높이를 설정해두었는데 px로 들어가서 모든 기기마다 많이 차이나는거..그래서 고치려고 이 함수 복붙.
public class CommonUtils {
	public static int dpToPx(Context context, int dp) {
	    DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
	    int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));       
	    return px;
	}
	 
	public static int pxToDp(Context context, int px) {
	    DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
	    int dp = Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
	    return dp;
	}
}