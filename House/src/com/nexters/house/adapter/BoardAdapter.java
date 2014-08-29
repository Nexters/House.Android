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
import com.nexters.house.utils.*;

public class BoardAdapter extends BaseAdapter {
	private Context mContext;
	private MainActivity mMainActivity;
	private ArrayList<BoardEntity> mBoardItemArrayList;
	private LayoutInflater mLayoutInflater;
	private int resource;
	private CommonUtils mUtil;

	public BoardAdapter(Context context, ArrayList<BoardEntity> mBoardItemArrayList, int resource, MainActivity mainActivity) {
		mMainActivity = mainActivity;
		mContext = context;
		mUtil = new CommonUtils();
		this.mBoardItemArrayList = mBoardItemArrayList;
		this.mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.resource = resource;
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

	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		Holder holder = new Holder();
		//int minHeight = mUtil.dpToPx(mContext, 360);

		if (convertView == null || holder.position != position) {
			final View createView;
			
			if (position % 2 == 0) {
				if (convertView == null) {
					convertView = mLayoutInflater.inflate(
							R.layout.custom_view_board_left, parent, false);
				}
			} else {
				if (convertView == null) {
					convertView = mLayoutInflater.inflate(
							R.layout.custom_view_board_right, parent, false);
				}
			}
			
			createView = convertView;
			//createView = convertView = mLayoutInflater.inflate(resource, null);

			// find resource
			holder.position = position;
			
			holder.houseId = (TextView) convertView.findViewById(R.id.tv_house_id);
			
			
			holder.houseProfile = (ImageView) convertView.findViewById(R.id.house_profile);
			holder.boardCreatedTime = (TextView) convertView.findViewById(R.id.created_time);
			holder.boardCategory = (TextView) convertView.findViewById(R.id.board_category);
			holder.boardTitle = (TextView)convertView.findViewById(R.id.board_title);
			holder.boardContent = (TextView) convertView.findViewById(R.id.board_content);
			holder.boardLikes = (TextView)convertView.findViewById(R.id.board_likes_cnt);
			holder.boardReplies = (TextView)convertView.findViewById(R.id.board_reply_cnt);
			
			
			
			convertView.setTag(holder);
			
		} else {
			holder = (Holder) convertView.getTag();
		}

		// 여기에서 게시물의 사용자 아이디/ 카테고리/ 내용/ 이미지를 넣어줄거임.
		String id = mBoardItemArrayList.get(position).id;
		String title = mBoardItemArrayList.get(position).title;
		String category = mBoardItemArrayList.get(position).category;
		String content = mBoardItemArrayList.get(position).content;
		List<String> previewImage = mBoardItemArrayList.get(position).previewImageUrls;
		int nLike = mBoardItemArrayList.get(position).like;
		int nReply = mBoardItemArrayList.get(position).reply;
		
		
		holder.houseId.setText(id);
		holder.boardTitle.setText(title);
		holder.boardCategory.setText(category);
		holder.boardContent.setText(content);
		
		//이미지뷰에 url로 불러오는거 정리하기
		holder.boardLikes.setText(Integer.toString(nLike));
		holder.boardLikes.setText(Integer.toString(nReply));
		
		return convertView;
	}

	private class Holder {
		int position;
		ImageView houseProfile, previewImage1, previewImage2, previewImage3;
		TextView houseId, boardContent, boardCategory, boardCreatedTime, boardTitle;
		TextView boardLikes, boardReplies;
	}
	
	public void add(){
		BoardEntity b = new BoardEntity();
		
		b.id = "newId";
		
		b.category = "new Q&A";
		b.createdTime = "20분전";
		b.title = "우리 완성할 수 있을까?";
		b.content = "넥스터즈 인유어하우스팀에서 개발중인 하우스 어플리케이션입니다. 누르면 걍 디테일컨텐츠뷰로 가면될듯";
		
		b.like = 1;
		b.reply = 1;
		
		mBoardItemArrayList.add(b);
	}	
	
	
}
