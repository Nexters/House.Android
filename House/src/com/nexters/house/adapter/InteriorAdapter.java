package com.nexters.house.adapter;

import java.util.*;

import android.app.*;
import android.content.*;
import android.view.*;
import android.view.animation.*;
import android.widget.*;

import com.daimajia.slider.library.*;
import com.daimajia.slider.library.SliderTypes.*;
import com.nexters.house.R;
import com.nexters.house.activity.*;
import com.nexters.house.entity.*;
import com.nexters.house.fragment.InteriorFragment;
import com.nexters.house.utils.*;

public class InteriorAdapter extends BaseAdapter {
	private Context mContext;
	private MainActivity mMainActivity;
	
	public static final int REQUEST_CONTENT_DETAIL_VIEW = 0;
	private final String TAG = "MainListAdapter";	
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

	@SuppressWarnings("static-access")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		Holder holder = new Holder();
		//int minHeight = mUtil.dpToPx(mContext, 360);

		if (convertView == null || holder.position != position) {
			final View createView;
			createView = convertView = mLayoutInflater.inflate(resource, null);

			// find resource
			holder.position = position;
			holder.houseId = (TextView) convertView.findViewById(R.id.house_id);
			holder.interiorContent = (TextView) convertView.findViewById(R.id.interior_content);
			
			holder.btnDown = (ImageView) convertView.findViewById(R.id.icon_down);
			holder.btnEdit = (ImageView) convertView.findViewById(R.id.icon_edit);
			holder.btnDelete = (ImageView) convertView.findViewById(R.id.icon_delete);
			
			holder.houseProfile = (ImageView) convertView.findViewById(R.id.house_profile);
			holder.interiorLikes = (TextView)convertView.findViewById(R.id.interior_likes_cnt);
			holder.interiorReplies = (TextView)convertView.findViewById(R.id.interior_reply_cnt);
			
			// set click listener
			View.OnClickListener convertOnClickListener = new View.OnClickListener() {
				Boolean clicked = false;
				@Override
				public void onClick(View v) {
					View rootView = createView;
					Animation showDown = AnimationUtils.loadAnimation(mContext, R.anim.show_down);
					Animation hideUp	= AnimationUtils.loadAnimation(mContext, R.anim.hide_up);
					
					ImageView btnEdit = (ImageView) rootView.findViewById(R.id.icon_edit);
					ImageView btnDelete = (ImageView) rootView.findViewById(R.id.icon_delete);
					
					switch(v.getId()){
					case R.id.icon_down :
						//렐러티브레이아웃이 이미지 슬라이더에 가려져서 버튼들이 가려지게됨. 그래서 레이아웃을 맨 위로 올려줌!
						RelativeLayout test = (RelativeLayout)rootView.findViewById(R.id.rl_test);
						test.bringToFront();

						if(!clicked){
							clicked = true;
							
							btnEdit.startAnimation(showDown);
							btnDelete.startAnimation(showDown);
							
						}else{
							clicked = false;
							
							btnEdit.startAnimation(hideUp);
							btnDelete.startAnimation(hideUp);							
						}
						
						//Log.d("Click Click", "down onClick : " + showDown + " : ");
						
						break;
					case R.id.interior_content :
						mMainActivity.changeFragment(MainActivity.FRAGMENT_DETAIL_INTERIOR);
						break;
						
					case R.id.icon_edit:
						
						Intent intent=new Intent(mContext,EditActivity.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						mContext.startActivity(intent);
						break;
						
					case R.id.icon_delete:						
						/*AlertDialog.Builder alt_bld = new AlertDialog.Builder(mContext);
					    alt_bld.setMessage("삭제하시겠습니까?").setCancelable(
					        false).setPositiveButton("예",
					        new DialogInterface.OnClickListener() {
					        public void onClick(DialogInterface dialog, int id) {
					        	
					        }
					        }).setNegativeButton("아니요",
					        new DialogInterface.OnClickListener() {
				        public void onClick(DialogInterface dialog, int id) {
					            // Action for 'NO' Button
					            dialog.cancel();
					        }
					        });
					    AlertDialog alert = alt_bld.create();
					    // Title for AlertDialog

					    alert.show();*/
					
						break;
					}
				}
			};
			
			holder.interiorContent.setOnClickListener(convertOnClickListener);
			holder.btnDown.setOnClickListener(convertOnClickListener);
			holder.btnEdit.setOnClickListener(convertOnClickListener);
			holder.btnDelete.setOnClickListener(convertOnClickListener);
			convertView.setTag(holder);
			
			SliderLayout slider = (SliderLayout) convertView.findViewById(R.id.slider);
			List<String> image_urls = mInteriorItemArrayList.get(position).image_urls;
			
			for (String url : image_urls) {
				TextSliderView textSliderView = new TextSliderView(
						convertView.getContext());
				// initialize a SliderLayout
				textSliderView.image(url);
				// .setScaleType(BaseSliderView.ScaleType.Fit);
				slider.addSlider(textSliderView);
			}
			// 리스트뷰안의 아이템 높이 설정하는 메소드
			//convertView.setMinimumHeight(minHeight);
			//Log.d(TAG, "minHeight"+minHeight);
		} else {
			holder = (Holder) convertView.getTag();
		}

		// 여기에서 게시물의 사용자 아이디/ 카테고리/ 내용/ 이미지를 넣어줄거임.
		String id = mInteriorItemArrayList.get(position).id;
		String content = mInteriorItemArrayList.get(position).content;
		List<String> image = mInteriorItemArrayList.get(position).image_urls;
		int nBadge = mInteriorItemArrayList.get(position).badge;
		int nReply = mInteriorItemArrayList.get(position).reply;
		
		
		holder.houseId.setText(id);
		holder.interiorContent.setText(content);
		holder.interiorLikes.setText(Integer.toString(nBadge));
		holder.interiorReplies.setText(Integer.toString(nReply));
		
		return convertView;
	}

	private class Holder {
		int position;
		ImageView houseProfile;
		LinearLayout tvContents;
		TextView houseId, interiorContent, interiorCategory;
		TextView interiorLikes, interiorReplies;
		ImageView btnDown, btnEdit, btnDelete;
	}
	
	@SuppressWarnings("serial")
	public void add(){
		InteriorEntity e = new InteriorEntity();
		e.badge = 1;
		e.category = "new";
		e.content = "넥스터즈 인유어하우스팀에서 개발중인 하우스 어플리케이션입니다. 누르면 걍 디테일컨텐츠뷰로 가면될듯";
		e.id = "newId";
		e.image_urls = new ArrayList<String>(){{
			add("https://fbcdn-sphotos-a-a.akamaihd.net/hphotos-ak-xfp1/v/t1.0-9/10402950_570244953084785_2207844659246242948_n.jpg?oh=b19d78a504af3a54501e629f0383da87&oe=5448A4C6&__gda__=1413348687_974d19e8b5ddcf217cb99b77b0186685");
			add("https://fbcdn-sphotos-g-a.akamaihd.net/hphotos-ak-xfa1/t1.0-9/10487348_570244969751450_1480175892860352468_n.jpg");
			add("https://fbcdn-sphotos-f-a.akamaihd.net/hphotos-ak-xpa1/t1.0-9/1546376_570244983084782_3616217572638065925_n.jpg");
			add("https://fbcdn-sphotos-d-a.akamaihd.net/hphotos-ak-xpf1/t1.0-9/10487342_570244993084781_3890212537564580615_n.jpg");
			add("https://fbcdn-sphotos-b-a.akamaihd.net/hphotos-ak-xpa1/v/t1.0-9/10524374_570245013084779_7454008372005256632_n.jpg?oh=4761db9f33b72709585016c2649c747e&oe=5434C617&__gda__=1413811119_55884851b246ddb301725a0a78cacc84");
			}};
		e.reply = 1;
		
		mInteriorItemArrayList.add(e);
	}
	
}