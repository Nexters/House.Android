package com.nexters.house.adapter;

import java.util.*;

import android.content.*;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;

import com.daimajia.slider.library.*;
import com.daimajia.slider.library.SliderTypes.*;
import com.nexters.house.R;
import com.nexters.house.activity.*;
import com.nexters.house.entity.*;
import com.nexters.house.utils.*;

public class InteriorAdapter extends BaseAdapter implements OnClickListener {
	
	public static final int REQUEST_CONTENT_DETAIL_VIEW = 0;

	final String TAG = "MainListAdapter";

	public Context mContext;
	private ArrayList<InteriorEntity> mInteriorItemArrayList;
	private LayoutInflater mLayoutInflater;
	int resource;
	CommonUtils mUtil = new CommonUtils();

	public InteriorAdapter(Context context, ArrayList<InteriorEntity> mInteriorItemArrayList, int resource) {
		mContext = context;
		this.mInteriorItemArrayList = mInteriorItemArrayList;
		this.mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.resource = resource;
	}


	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mInteriorItemArrayList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@SuppressWarnings("static-access")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		Holder holder = new Holder();
		int minHeight = mUtil.pxToDp(mContext, 1000);
		//Log.d(TAG, "pxToDp"+ minHeight);

		if (convertView == null) {
			convertView = mLayoutInflater.inflate(resource, null);

			// find resource
			holder.tv_id = (TextView) convertView.findViewById(R.id.tv_id);
			holder.tv_category = (TextView) convertView.findViewById(R.id.tv_category);
			holder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
			holder.iv_image = (ImageView) convertView.findViewById(R.id.iv_image);
			holder.tv_badge = (TextView)convertView.findViewById(R.id.tv_total_badge);
			holder.tv_reply = (TextView)convertView.findViewById(R.id.tv_total_reply);
			holder.tv_share = (TextView)convertView.findViewById(R.id.tv_total_share);
			holder.tv_scrap = (TextView)convertView.findViewById(R.id.tv_total_scrap);
			
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
		String category = mInteriorItemArrayList.get(position).category;
		//List<String> image = mInteriorItemArrayList.get(position).image_urls;
		int nBadge = mInteriorItemArrayList.get(position).badge;
		int nReply = mInteriorItemArrayList.get(position).reply;
		int nShare = mInteriorItemArrayList.get(position).share;
		int nScrap = mInteriorItemArrayList.get(position).scrap;
		
		
		holder.tv_id.setText(id);
		holder.tv_content.setText(content);
		holder.tv_category.setText(category);
		holder.tv_badge.setText(Integer.toString(nBadge));
		holder.tv_reply.setText(Integer.toString(nReply));
		holder.tv_share.setText(Integer.toString(nShare));
		holder.tv_scrap.setText(Integer.toString(nScrap));
		
		// set click listener
		
		holder.tv_id.setOnClickListener(this);
		holder.tv_content.setOnClickListener(this);
		//holder.iv_image.setOnClickListener(this);
		holder.tv_badge.setOnClickListener(this);
		holder.tv_reply.setOnClickListener(this);
		holder.tv_share.setOnClickListener(this);
		holder.tv_scrap.setOnClickListener(this);

		return convertView;
	}

	private class Holder {
		ImageView iv_image;
		TextView tv_id, tv_content, tv_category;
		TextView tv_badge, tv_reply, tv_share, tv_scrap;
	}
	
	@SuppressWarnings("serial")
	public void add(){
		InteriorEntity e = new InteriorEntity();
		e.badge = 1;
		e.category = "new";
		e.content = "새로만들어진 리스트";
		e.id = "newId";
		e.image_urls = new ArrayList<String>(){{
			add("https://fbcdn-sphotos-a-a.akamaihd.net/hphotos-ak-xfp1/v/t1.0-9/10402950_570244953084785_2207844659246242948_n.jpg?oh=b19d78a504af3a54501e629f0383da87&oe=5448A4C6&__gda__=1413348687_974d19e8b5ddcf217cb99b77b0186685");
			add("https://fbcdn-sphotos-g-a.akamaihd.net/hphotos-ak-xfa1/t1.0-9/10487348_570244969751450_1480175892860352468_n.jpg");
			add("https://fbcdn-sphotos-f-a.akamaihd.net/hphotos-ak-xpa1/t1.0-9/1546376_570244983084782_3616217572638065925_n.jpg");
			add("https://fbcdn-sphotos-d-a.akamaihd.net/hphotos-ak-xpf1/t1.0-9/10487342_570244993084781_3890212537564580615_n.jpg");
			add("https://fbcdn-sphotos-b-a.akamaihd.net/hphotos-ak-xpa1/v/t1.0-9/10524374_570245013084779_7454008372005256632_n.jpg?oh=4761db9f33b72709585016c2649c747e&oe=5434C617&__gda__=1413811119_55884851b246ddb301725a0a78cacc84");
			}};
		e.reply = 1;
		e.scrap = 1;
		e.share = 1;
		
		mInteriorItemArrayList.add(e);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId()==R.id.tv_content|| v.getId()==R.id.tv_id /*|| v.getId()==R.id.iv_image */){
			Intent intent = new Intent(v.getContext(), ContentDetailActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			mContext.startActivity(intent);
		}
		
	}

	

}