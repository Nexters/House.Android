package com.nexters.house.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.http.MediaType;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.nexters.house.R;
import com.nexters.house.activity.ContentDetailActivity;
import com.nexters.house.activity.EditActivity;
import com.nexters.house.activity.MainActivity;
import com.nexters.house.core.SessionManager;
import com.nexters.house.entity.APICode;
import com.nexters.house.entity.CodeType;
import com.nexters.house.entity.InteriorEntity;
import com.nexters.house.entity.reqcode.AP0007;
import com.nexters.house.handler.TransHandler;
import com.nexters.house.thread.DownloadImageTask;
import com.nexters.house.thread.PostMessageTask;
import com.nexters.house.utils.JacksonUtils;

public class InteriorAdapter extends BaseAdapter {
	private Context mContext;
	private MainActivity mMainActivity;
	private ListView mListView;
	
	private ArrayList<InteriorEntity> mInteriorItemArrayList;
	private LayoutInflater mLayoutInflater;

	private PostMessageTask mPostTask;
	
	private String usrId;
	private int refreshCnt;
	private int selectedPosition;
	
	public InteriorAdapter(Context context,
			ArrayList<InteriorEntity> mInteriorItemArrayList,
			MainActivity mainActivity, ListView listView) {
		mListView = listView;
		mMainActivity = mainActivity;
		mContext = context;
		this.mInteriorItemArrayList = mInteriorItemArrayList;
		this.mLayoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		usrId = SessionManager.getInstance(mMainActivity).getUserDetails().get(SessionManager.KEY_EMAIL);
		refreshCnt = 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		Holder holder = null;
		
		if(convertView != null)
			holder = (Holder) convertView.getTag();
		if (convertView == null || holder.position != position || holder.refresh != refreshCnt) {
			final View createView;
			createView = convertView = mLayoutInflater.inflate(R.layout.custom_view_interior, null);
			holder = new Holder();
			
			// get interior
			final long no = mInteriorItemArrayList.get(position).no;
			String name = mInteriorItemArrayList.get(position).name;
			String id = mInteriorItemArrayList.get(position).id;
			String profileImg = mInteriorItemArrayList.get(position).profileImg;
			String created = mInteriorItemArrayList.get(position).created;
			String content = mInteriorItemArrayList.get(position).content;
			List<String> imageUrls = mInteriorItemArrayList.get(position).imageUrls;
			int nLike = mInteriorItemArrayList.get(position).like;
			int nReply = mInteriorItemArrayList.get(position).comment;
			
			// find resource
			holder.position = position;
			holder.refresh = refreshCnt;
			holder.houseId = (TextView) convertView.findViewById(R.id.house_id);
			holder.houseProfile = (ImageView) convertView.findViewById(R.id.house_profile);
//			holder.created = (TextView) convertView.findViewById(R.id.)
			holder.interiorContent = (TextView) convertView.findViewById(R.id.interior_content);
			holder.interiorLikes = (TextView)convertView.findViewById(R.id.interior_likes_cnt);
			holder.interiorReplies = (TextView)convertView.findViewById(R.id.interior_reply_cnt);
			
			holder.slider = (SliderLayout) convertView.findViewById(R.id.interior_slider);
			
			holder.rlMenu = (RelativeLayout) convertView.findViewById(R.id.rl_menu);
			holder.rlContents = (RelativeLayout) convertView.findViewById(R.id.rl_interior_custom_view);
			holder.btnDown = (ImageView) convertView.findViewById(R.id.icon_down);
			holder.btnEdit = (ImageView) convertView.findViewById(R.id.icon_edit);
			holder.btnDelete = (ImageView) convertView.findViewById(R.id.icon_delete);
			
			// set
			if(!id.equals(usrId))
				holder.rlMenu.setVisibility(View.GONE);
			holder.houseId.setText(name + " = " + no);
			if(profileImg != null){
				new DownloadImageTask(holder.houseProfile).setCrop(true).execute(mMainActivity.getString(R.string.base_uri) + profileImg);
			}
			holder.interiorContent.setText(content);
			holder.interiorLikes.setText(Integer.toString(nLike));
			holder.interiorReplies.setText(Integer.toString(nReply));

			SliderLayout slider = (SliderLayout) convertView.findViewById(R.id.interior_slider);
			
			slider.setVisibility(View.GONE);
			if(imageUrls != null && imageUrls.size() > 0)
				slider.setVisibility(View.VISIBLE);
			
			for (String url : imageUrls) {
				TextSliderView textSliderView = new TextSliderView(
						convertView.getContext());
				// initialize a SliderLayout
				textSliderView.image(url);
				textSliderView.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener(){
					@Override
					public void onSliderClick(BaseSliderView slider) {
						selectedPosition = position;
//						Log.d("curPosition", "curPosition : " + curPosition);
						Intent intent = new Intent(mContext,ContentDetailActivity.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						intent.putExtra("brdNo", no);
						intent.putExtra("brdType", CodeType.INTERIOR_TYPE);
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
						RelativeLayout menuLayout = (RelativeLayout)rootView.findViewById(R.id.rl_menu);
						LinearLayout bodyLayout = (LinearLayout)rootView.findViewById(R.id.interior_body);
						menuLayout.bringChildToFront(bodyLayout);
						
						if(!clicked){
							clicked = true;
							btnEdit.startAnimation(showDown);
							btnDelete.startAnimation(showDown);
						} else{
							clicked = false;
							btnEdit.startAnimation(hideUp);
							btnDelete.startAnimation(hideUp);					
						}
						//Log.d("Click Click", "down onClick : " + showDown + " : ");
						break;
					case R.id.rl_interior_custom_view :
						intent = new Intent(mContext,ContentDetailActivity.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						intent.putExtra("brdNo", no);
						mContext.startActivity(intent);
						break;
					case R.id.icon_edit:
						intent = new Intent(mContext,EditActivity.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						intent.putExtra("brdNo", no);
						mContext.startActivity(intent);
						break;
					case R.id.icon_delete:	
						AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mMainActivity);
						// set title
						alertDialogBuilder.setTitle("Your Title");
				 
						// set dialog message
						alertDialogBuilder
							.setMessage("Click yes to exit!")
							.setCancelable(false)
							.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,int id) {
									deleteInterior(no, position);
								}
							  })
							.setNegativeButton("No",new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,int id) {
									dialog.cancel();
								}
							});
						AlertDialog alertDialog = alertDialogBuilder.create();
		 
						// show it
						alertDialog.show();
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

	public void deleteInterior(long interiorNo, final int position){
		if(mPostTask != null && mPostTask.getStatus() != mPostTask.getStatus().FINISHED)
			return ;
		AP0007 ap = new AP0007();
		ap.setType(CodeType.INTERIOR_TYPE);
		ap.setBrdId(usrId);
		ap.setBrdNo(interiorNo);
		
		TransHandler.Handler handler = new TransHandler.Handler() {
			public void handle(APICode resCode) {
				AP0007 ap = JacksonUtils.hashMapToObject((HashMap)resCode.getTranData().get(0), AP0007.class);
				mInteriorItemArrayList.remove(position);
				if(position > 0)
					mListView.setSelection(position - 1);
				notifyDataSetChanged();
			}
		};
		TransHandler<AP0007> articleHandler = new TransHandler<AP0007>("AP0007", handler, ap);
		
		mPostTask = new PostMessageTask(mMainActivity, articleHandler);
		mPostTask.execute(MediaType.APPLICATION_JSON);
	}
	
	public int getSelectedPosition(){
		return selectedPosition;
	}
	
	private class Holder {
		int refresh;
		int position;
		ImageView houseProfile;
		LinearLayout tvContents;
		RelativeLayout rlContents, rlMenu;
		TextView houseId, created, interiorContent;
		TextView interiorLikes, interiorReplies;
		ImageView btnDown, btnEdit, btnDelete;
		SliderLayout slider;
	}

	@SuppressWarnings("serial")
	public void add(int index, InteriorEntity e) {
		mInteriorItemArrayList.add(index, e);
	}
	
	public void add(InteriorEntity e) {
		mInteriorItemArrayList.add(e);
	}
	
	@Override
	public void notifyDataSetChanged() {
		refreshCnt += 1;
		super.notifyDataSetChanged();
	}

	public void clear() {
		mInteriorItemArrayList.clear();
	}

	public void add() {
		InteriorEntity e = new InteriorEntity();
		e.badge = 1;
		e.content = "인테리어 입니다 ~~~~";
		e.id = "newId";
		e.imageUrls = new ArrayList<String>(){{
			add("https://fbcdn-sphotos-a-a.akamaihd.net/hphotos-ak-xfp1/v/t1.0-9/10402950_570244953084785_2207844659246242948_n.jpg?oh=b19d78a504af3a54501e629f0383da87&oe=5448A4C6&__gda__=1413348687_974d19e8b5ddcf217cb99b77b0186685");
			add("https://fbcdn-sphotos-g-a.akamaihd.net/hphotos-ak-xfa1/t1.0-9/10487348_570244969751450_1480175892860352468_n.jpg");
			add("https://fbcdn-sphotos-f-a.akamaihd.net/hphotos-ak-xpa1/t1.0-9/1546376_570244983084782_3616217572638065925_n.jpg");
			add("https://fbcdn-sphotos-d-a.akamaihd.net/hphotos-ak-xpf1/t1.0-9/10487342_570244993084781_3890212537564580615_n.jpg");
			add("https://fbcdn-sphotos-b-a.akamaihd.net/hphotos-ak-xpa1/v/t1.0-9/10524374_570245013084779_7454008372005256632_n.jpg?oh=4761db9f33b72709585016c2649c747e&oe=5434C617&__gda__=1413811119_55884851b246ddb301725a0a78cacc84");
			}};; 
		e.comment = 1;
		e.like = 2;
		mInteriorItemArrayList.add(e);
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
}
