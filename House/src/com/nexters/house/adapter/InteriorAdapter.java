package com.nexters.house.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.nexters.house.R;
import com.nexters.house.activity.ContentDetailActivity;
import com.nexters.house.activity.EditActivity;
import com.nexters.house.activity.MainActivity;
import com.nexters.house.entity.InteriorEntity;
import com.nexters.house.utils.CommonUtils;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;

public class InteriorAdapter extends BaseAdapter {
	private Context mContext;
	private MainActivity mMainActivity;
	
	private ArrayList<InteriorEntity> mInteriorItemArrayList;
	private LayoutInflater mLayoutInflater;
	private int resource;
	private CommonUtils mUtil;

	public InteriorAdapter(Context context, ArrayList<InteriorEntity> mInteriorItemArrayList, int resource, MainActivity mainActivity) {
		mMainActivity = mainActivity;
		mContext = context;
		mUtil = new CommonUtils();
		this.mInteriorItemArrayList = mInteriorItemArrayList;
		this.mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.resource = resource;
	}
	
	@Override
	public int getCount() {
		return mInteriorItemArrayList.size();
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		Holder holder = null;
		
		if(convertView != null)
			holder = (Holder) convertView.getTag();
		if (convertView == null || (holder != null && holder.position != position)) {
			final View createView;
			createView = convertView = mLayoutInflater.inflate(resource, null);
			holder = new Holder();
			
			// get interior
			final long no = mInteriorItemArrayList.get(position).no;
			String id = mInteriorItemArrayList.get(position).id;
			String content = mInteriorItemArrayList.get(position).content;
			int nLike = mInteriorItemArrayList.get(position).like;
			int nReply = mInteriorItemArrayList.get(position).comment;
			List<String> imageUrls = mInteriorItemArrayList.get(position).imageUrls;
			
			// find resource
			holder.position = position;
			
			holder.houseId = (TextView) convertView.findViewById(R.id.house_id);
			holder.interiorContent = (TextView) convertView.findViewById(R.id.interior_content);
			holder.rlContents = (RelativeLayout) convertView.findViewById(R.id.rl_interior_custom_view);
			holder.slider = (SliderLayout) convertView.findViewById(R.id.interior_slider);
			holder.btnDown = (ImageView) convertView.findViewById(R.id.icon_down);
			holder.btnEdit = (ImageView) convertView.findViewById(R.id.icon_edit);
			holder.btnDelete = (ImageView) convertView.findViewById(R.id.icon_delete);
			
			holder.houseProfile = (ImageView) convertView.findViewById(R.id.house_profile);
			holder.interiorLikes = (TextView)convertView.findViewById(R.id.interior_likes_cnt);
			holder.interiorReplies = (TextView)convertView.findViewById(R.id.interior_reply_cnt);
			
			// set
			holder.houseId.setText(id + " = " + position);
			holder.interiorContent.setText(content);
			holder.interiorLikes.setText(Integer.toString(nLike));
			holder.interiorReplies.setText(Integer.toString(nReply));

			SliderLayout slider = (SliderLayout) convertView.findViewById(R.id.interior_slider);

			for (String url : imageUrls) {
				TextSliderView textSliderView = new TextSliderView(
						convertView.getContext());
				// initialize a SliderLayout
				textSliderView.image(url);
				textSliderView.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener(){
					@Override
					public void onSliderClick(BaseSliderView slider) {
						Intent intent = new Intent(mContext,ContentDetailActivity.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						intent.putExtra("brdNo", no);
						mContext.startActivity(intent);
					}
				});
				// .setScaleType(BaseSliderView.ScaleType.Fit);
				slider.addSlider(textSliderView);
			}
			
			// set listener
			View.OnClickListener convertOnClickListener = new View.OnClickListener() {
				Boolean clicked = false;
				@Override
				public void onClick(View v) {
					View rootView = createView;
					Animation showDown = AnimationUtils.loadAnimation(mContext, R.anim.show_down);
					Animation hideUp	= AnimationUtils.loadAnimation(mContext, R.anim.hide_up);
					
					ImageView btnEdit = (ImageView) rootView.findViewById(R.id.icon_edit);
					ImageView btnDelete = (ImageView) rootView.findViewById(R.id.icon_delete);
					
					Intent intent = null;
					
					switch(v.getId()){
					case R.id.icon_down :
						//렐러티브레이아웃이 이미지 슬라이더에 가려져서 버튼들이 가려지게됨. 그래서 레이아웃을 맨 위로 올려줌!
						RelativeLayout test = (RelativeLayout)rootView.findViewById(R.id.rl_test);
						test.bringToFront();

						if(!clicked){
							clicked = true;
							btnEdit.startAnimation(showDown);
							btnDelete.startAnimation(showDown);
							
							btnEdit.setClickable(true);
							btnDelete.setClickable(true);
							
						}else{

							clicked = false;
							btnEdit.startAnimation(hideUp);
							btnDelete.startAnimation(hideUp);	
							btnEdit.setClickable(false);
							btnDelete.setClickable(false);
						}
						//Log.d("Click Click", "down onClick : " + showDown + " : ");
						break;
					case R.id.rl_interior_custom_view :
						//mMainActivity.changeFragment(MainActivity.FRAGMENT_DETAIL_INTERIOR);
						intent = new Intent(mContext,ContentDetailActivity.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						intent.putExtra("brdNo", no);
						mContext.startActivity(intent);
						break;
					case R.id.icon_edit:
						intent = new Intent(mContext,EditActivity.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						mContext.startActivity(intent);
						break;
					case R.id.icon_delete:						
						mMainActivity.showDialog(3378);
						break;
					}
				}
			};
			holder.btnDown.setOnClickListener(convertOnClickListener);
			holder.btnEdit.setOnClickListener(convertOnClickListener);
			holder.btnDelete.setOnClickListener(convertOnClickListener);
			holder.rlContents.setOnClickListener(convertOnClickListener);
			
			convertView.setTag(holder);
			// 리스트뷰안의 아이템 높이 설정하는 메소드
			//convertView.setMinimumHeight(minHeight);
		}
		return convertView;
	}

	private class Holder {
		int position;
		ImageView houseProfile;
		LinearLayout tvContents;
		RelativeLayout rlContents;
		TextView houseId, interiorContent, interiorCategory;
		TextView interiorLikes, interiorReplies;
		ImageView btnDown, btnEdit, btnDelete;
		SliderLayout slider;
	}
	
	@SuppressWarnings("serial")
	public void add(long no, String usrId, String content, List<String> imgUrls, int likeCnt, int commentCnt){
		InteriorEntity e = new InteriorEntity();
		e.no = no;
		e.badge = 1;
//		e.category = "new";
		Log.d("content :", "content : " + content);
		e.content = content;
		e.id = usrId;
		e.imageUrls = imgUrls; 

		e.comment = commentCnt;
		e.like = likeCnt;
		
		mInteriorItemArrayList.add(e);
	}
}