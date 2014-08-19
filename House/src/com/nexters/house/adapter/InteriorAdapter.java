package com.nexters.house.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.nexters.house.R;
import com.nexters.house.entity.InteriorEntity;
import com.nexters.house.fragment.ContentDetailFragment;
import com.nexters.house.utils.CommonUtils;

public class InteriorAdapter extends BaseAdapter implements OnClickListener{
	private Context mContext;
	private FragmentActivity mFragmentActivity;
	
	public static final int REQUEST_CONTENT_DETAIL_VIEW = 0;
	private final String TAG = "MainListAdapter";	
	private ArrayList<InteriorEntity> mInteriorItemArrayList;
	private LayoutInflater mLayoutInflater;
	private int resource;
	private CommonUtils mUtil;

	public InteriorAdapter(Context context, ArrayList<InteriorEntity> mInteriorItemArrayList, int resource, FragmentActivity fragmentActivity) {
		mFragmentActivity = fragmentActivity;
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
		int minHeight = mUtil.pxToDp(mContext, 1500);

		if (convertView == null) {
			convertView = mLayoutInflater.inflate(resource, null);

			// find resource
			holder.houseId = (TextView) convertView.findViewById(R.id.house_id);
//			holder.interiorCategory = (TextView) convertView.findViewById(R.id.interior_category);
			holder.interiorContent = (TextView) convertView.findViewById(R.id.interior_content);
			
			//holder.tv_contents = (LinearLayout) convertView.findViewById(R.id.tv_content);
			
			holder.houseProfile = (ImageView) convertView.findViewById(R.id.house_profile);
			holder.interiorLikes = (TextView)convertView.findViewById(R.id.interior_likes_cnt);
			holder.interiorReplies = (TextView)convertView.findViewById(R.id.interior_reply_cnt);
//			holder.interiorShares = (TextView)convertView.findViewById(R.id.interior_share_cnt);
//			holder.interiorScraps = (TextView)convertView.findViewById(R.id.interior_scrap_cnt);
//			
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
			convertView.setMinimumHeight(minHeight);
			//Log.d(TAG, "minHeight"+minHeight);

		} else {
			holder = (Holder) convertView.getTag();
		}

		// 여기에서 게시물의 사용자 아이디/ 카테고리/ 내용/ 이미지를 넣어줄거임.
		String id = mInteriorItemArrayList.get(position).id;
		String content = mInteriorItemArrayList.get(position).content;
//		String category = mInteriorItemArrayList.get(position).category;
		List<String> image = mInteriorItemArrayList.get(position).image_urls;
		int nBadge = mInteriorItemArrayList.get(position).badge;
		int nReply = mInteriorItemArrayList.get(position).reply;
//		int nShare = mInteriorItemArrayList.get(position).share;
//		int nScrap = mInteriorItemArrayList.get(position).scrap;
		
		
		holder.houseId.setText(id);
		holder.interiorContent.setText(content);
//		holder.interiorCategory.setText(category);
		holder.interiorLikes.setText(Integer.toString(nBadge));
		holder.interiorReplies.setText(Integer.toString(nReply));
//		holder.interiorShares.setText(Integer.toString(nShare));
//		holder.interiorScraps.setText(Integer.toString(nScrap));
		
		// set click listener
		
//		holder.tv_id.setOnClickListener(this);
		holder.interiorContent.setOnClickListener(this);
//		holder.houseProfile.setOnClickListener(this);
//		holder.interiorLikes.setOnClickListener(this);
//		holder.interiorReplies.setOnClickListener(this);
//		holder.tv_share.setOnClickListener(this);
//		holder.tv_scrap.setOnClickListener(this);

		return convertView;
	}

	private class Holder {
		ImageView houseProfile;
		LinearLayout tvContents;
		TextView houseId, interiorContent, interiorCategory;
		TextView interiorLikes, interiorReplies;
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
//		e.scrap = 1;
//		e.share = 1;
		
		mInteriorItemArrayList.add(e);
	}

	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.interior_content){
			Fragment newFragment = null;
			newFragment = new ContentDetailFragment();

			// replace fragment
			final FragmentTransaction transaction = mFragmentActivity.getSupportFragmentManager()
					.beginTransaction();
			transaction.replace(R.id.ll_fragment, newFragment);
			//뒤로가기 버튼누르면 앞의 프래그먼트 나오도록하는거 
			transaction.addToBackStack(null);
			// Commit the transaction
			transaction.commit();
		}
	}
}