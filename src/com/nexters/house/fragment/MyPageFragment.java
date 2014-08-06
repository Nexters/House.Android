package com.nexters.house.fragment;

import android.animation.*;
import android.graphics.*;
import android.os.*;
import android.support.v4.app.*;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;

import com.nexters.house.*;
import com.nexters.house.adapter.*;


public class MyPageFragment extends Fragment {
	
    public static final int REQUEST_TAKE_PHOTO = 1;
    public static final int REQUEST_PICK_FROM_GALLERY = 2;
    public static final int REQUEST_CODE_CROP_IMAGE = 3;
    
    private int mIvPhotoWidth;
    private ImageView mIvPhotoBtn;
	private Button mBtnConfirm;

    
    private Bitmap mImageBitmap;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_mypage, container, false);
		GridView gridview = (GridView) v.findViewById(R.id.gv_mypage);
	    gridview.setAdapter(new MyPageAdapter(getActivity().getApplicationContext()));
	    

	    gridview.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
	            Toast.makeText(v.getContext(), "" + position, Toast.LENGTH_SHORT).show();
	        }
	    });
		
		
		return v;
	}


}