package com.nexters.house.adapter;

import java.util.*;

import android.content.*;
import android.util.*;
import android.view.*;
import android.view.animation.*;
import android.widget.*;

import com.daimajia.slider.library.*;
import com.daimajia.slider.library.SliderTypes.*;
import com.nexters.house.*;
import com.nexters.house.R;
import com.nexters.house.activity.*;
import com.nexters.house.adapter.InteriorAdapter.*;
import com.nexters.house.entity.*;
import com.nexters.house.handler.TransHandler.Handler;
import com.nexters.house.thread.DownloadImageTask;
import com.nexters.house.utils.*;

public class BoardAdapter extends BaseAdapter {
	private Context mContext;
	private MainActivity mMainActivity;
	private ArrayList<BoardEntity> mBoardItemArrayList;
	private LayoutInflater mLayoutInflater;
	private CommonUtils mUtil;

	
	public BoardAdapter(Context context,
			ArrayList<BoardEntity> mBoardItemArrayList,
			MainActivity mainActivity) {
		mMainActivity = mainActivity;
		mContext = context;
		mUtil = new CommonUtils();
		this.mBoardItemArrayList = mBoardItemArrayList;
		this.mLayoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		Holder holder = new Holder();
		// int minHeight = mUtil.dpToPx(mContext, 360);

		if (convertView == null || holder.position != position) {
			final View createView;

			if (position % 2 == 0)
				createView = convertView = mLayoutInflater.inflate(
						R.layout.custom_view_board_left, parent, false);
			else
				createView = convertView = mLayoutInflater.inflate(
						R.layout.custom_view_board_right, parent, false);

			// 여기에서 게시물의 사용자 아이디/ 카테고리/ 내용/ 이미지를 넣어줄거임.
			final long no = mBoardItemArrayList.get(position).no;
			String id = mBoardItemArrayList.get(position).id;
			String profileImg = mBoardItemArrayList.get(position).profileImg;
			String created = mBoardItemArrayList.get(position).created;
			String title = mBoardItemArrayList.get(position).subject;
			String category = mBoardItemArrayList.get(position).category;
			String content = mBoardItemArrayList.get(position).content;
			List<String> previewImage = mBoardItemArrayList.get(position).imageUrls;
			int nLike = mBoardItemArrayList.get(position).like;
			int nReply = mBoardItemArrayList.get(position).comment;
			
			// find resource
			holder.position = position;
			holder.houseId = (TextView) convertView
					.findViewById(R.id.tv_house_id);
			holder.houseProfile = (ImageView) convertView
					.findViewById(R.id.house_profile);
			holder.created = (TextView) convertView.findViewById(R.id.tv_created_time);
			holder.boardCategory = (TextView) convertView
					.findViewById(R.id.board_category);
			holder.boardTitle = (TextView) convertView
					.findViewById(R.id.board_title);
			holder.boardContent = (TextView) convertView
					.findViewById(R.id.board_content);
			holder.boardLikes = (TextView) convertView
					.findViewById(R.id.board_likes_cnt);
			holder.boardReplies = (TextView) convertView
					.findViewById(R.id.board_reply_cnt);
			holder.chatBackground = (LinearLayout) convertView
					.findViewById(R.id.chat_background);
			holder.previewImage = new ImageView[3];
			holder.previewImage[0] = (ImageView) convertView.findViewById(R.id.iv_preview_image_1);
			holder.previewImage[1] = (ImageView) convertView.findViewById(R.id.iv_preview_image_2);
			holder.previewImage[2] = (ImageView) convertView.findViewById(R.id.iv_preview_image_3);
			
			// set
			holder.houseId.setText(id);
			holder.houseProfile = null;
			holder.created.setText(created);
			holder.boardTitle.setText(title);
			holder.boardCategory.setText(category);
			holder.boardContent.setText(content);
			
			for(int i=0; i<previewImage.size() && i<3; i++)
				new DownloadImageTask(holder.previewImage[i]).execute(previewImage.get(i));
			
			// 이미지뷰에 url로 불러오는거 정리하기
			holder.boardLikes.setText(Integer.toString(nLike));
			holder.boardReplies.setText(Integer.toString(nReply));

			// set click listener
			View.OnClickListener convertOnClickListener = new View.OnClickListener() {
				Boolean clicked = false;
				@Override
				public void onClick(View v) {
					View rootView = createView;
					switch (v.getId()) {
					case R.id.chat_background:
						Intent intent = new Intent(mContext,ContentDetailActivity.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						intent.putExtra("brdNo", no);
						mContext.startActivity(intent);
						break;
					}
				}
			};
			holder.chatBackground.setOnClickListener(convertOnClickListener);
			convertView.setTag(holder);
		}
		return convertView;
	}

	private class Holder {
		int position;
		ImageView houseProfile;
		ImageView[] previewImage;
		TextView houseId, created, boardContent, boardCategory,
				boardTitle;
		TextView boardLikes, boardReplies;
		LinearLayout chatBackground;
	}

	public void clear() {
		mBoardItemArrayList.clear();
	}

	public void add(long brdNo, String brdId, String brdProfileImg, String brdCreated, String brdContent, String brdSubject, String brdCategory, 
			ArrayList<String> imgUrls, int brdLikeCnt, int brdCommentCnt) {
		BoardEntity b = new BoardEntity();
		b.no = brdNo;
		b.id = brdId;
		b.profileImg = brdProfileImg;
		b.category = brdCategory;
		b.created = brdCreated;
		b.subject = brdSubject;
		b.content = brdContent;
		b.like = brdLikeCnt;
		b.comment = brdCommentCnt;
		b.imageUrls = imgUrls;
		mBoardItemArrayList.add(b);
	}

	public void add() {
		BoardEntity b = new BoardEntity();

		b.id = "newId";
		b.category = "new Q&A";
		b.created = "20분전";
		b.subject = "우리 완성할 수 있을까?";
		b.content = "넥스터즈 인유어하우스팀에서 개발중인 하우스 어플리케이션입니다. 누르면 걍 디테일컨텐츠뷰로 가면될듯";
		b.like = 1;
		b.comment = 1;
		b.imageUrls = new ArrayList<String>() {
			{
				add("https://fbcdn-sphotos-a-a.akamaihd.net/hphotos-ak-xfp1/v/t1.0-9/10402950_570244953084785_2207844659246242948_n.jpg?oh=b19d78a504af3a54501e629f0383da87&oe=5448A4C6&__gda__=1413348687_974d19e8b5ddcf217cb99b77b0186685");
				add("https://fbcdn-sphotos-g-a.akamaihd.net/hphotos-ak-xfa1/t1.0-9/10487348_570244969751450_1480175892860352468_n.jpg");
				add("https://fbcdn-sphotos-f-a.akamaihd.net/hphotos-ak-xpa1/t1.0-9/1546376_570244983084782_3616217572638065925_n.jpg");
				add("https://fbcdn-sphotos-d-a.akamaihd.net/hphotos-ak-xpf1/t1.0-9/10487342_570244993084781_3890212537564580615_n.jpg");
				add("https://fbcdn-sphotos-b-a.akamaihd.net/hphotos-ak-xpa1/v/t1.0-9/10524374_570245013084779_7454008372005256632_n.jpg?oh=4761db9f33b72709585016c2649c747e&oe=5434C617&__gda__=1413811119_55884851b246ddb301725a0a78cacc84");
			}
		};
		mBoardItemArrayList.add(b);
	}

	@Override
	public int getCount() {
		return mBoardItemArrayList.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	public void setHandler(Handler handler) {
		
	}
}
