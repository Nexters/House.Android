package com.nexters.house.adapter;

import java.util.ArrayList;

import org.springframework.http.MediaType;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nexters.house.R;
import com.nexters.house.activity.AbstractAsyncFragmentActivity;
import com.nexters.house.core.SessionManager;
import com.nexters.house.entity.ReplyEntity;
import com.nexters.house.entity.reqcode.AP0009;
import com.nexters.house.handler.TransHandler;
import com.nexters.house.thread.DownloadImageTask;
import com.nexters.house.thread.PostMessageTask;

public class ReplyAdapter extends BaseAdapter{
	private AbstractAsyncFragmentActivity mAbstractAsyncFragmentActivity;
	
	private Context mContext;
	private ArrayList<ReplyEntity> mReplyItemList;
	private LayoutInflater mLayoutInflater;
	private int resource;
	private int refreshCnt;
	
	private TransHandler mHandler;
	private PostMessageTask mPostTask;
	
	private int brdType;
	private String usrId;
	
	public ReplyAdapter(AbstractAsyncFragmentActivity abstractAsyncFragmentActivity,
			ArrayList<ReplyEntity> mReplyItemList, int resource, int brdType) {
		this.brdType = brdType;
		this.mContext = abstractAsyncFragmentActivity.getApplicationContext();
		this.usrId = SessionManager.getInstance(mContext).getUserDetails().get(SessionManager.KEY_EMAIL);
		this.mAbstractAsyncFragmentActivity = abstractAsyncFragmentActivity;
		this.mReplyItemList = mReplyItemList;
		this.mLayoutInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.resource = resource;
		this.refreshCnt = 0;
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		Holder holder = null;
		
		if(convertView != null)
			holder = (Holder) convertView.getTag();
		if (convertView == null || holder.position != position || holder.refresh != refreshCnt) {
			final View createView;
			createView = convertView = mLayoutInflater.inflate(resource, null);
			holder = new Holder();
			
			// find resource
			holder.position = position;
			holder.houseProfile = (ImageView) convertView.findViewById(R.id.iv_user_profile_image);
			holder.name = (TextView) convertView.findViewById(R.id.tv_user_profile_name);
			holder.content = (TextView) convertView.findViewById(R.id.tv_content);
			holder.created = (TextView) convertView.findViewById(R.id.tv_created);
			holder.btnDelete = (Button) convertView.findViewById(R.id.btn_delete);
			holder.refresh = refreshCnt;
			
			final long no = mReplyItemList.get(position).no;
			String profileImg = mReplyItemList.get(position).profileImg;
			
			if(profileImg != null)
				new DownloadImageTask(holder.houseProfile).setCrop(true).execute(mContext.getString(R.string.base_uri) + profileImg);
			else
				holder.houseProfile.setImageResource(R.drawable.user_profile_image);
			
			holder.name.setText(mReplyItemList.get(position).name);
			holder.content.setText(mReplyItemList.get(position).content);
			holder.created.setText(mReplyItemList.get(position).created);

			if(!usrId.equals(mReplyItemList.get(position).id))
					holder.btnDelete.setVisibility(View.GONE);
			holder.btnDelete.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					deleteReply(no);
				}
			});
			convertView.setTag(holder);
		}
		return convertView;
	}
	
	public void deleteReply(long replyNo){
		if(mPostTask != null && mPostTask.getStatus() != mPostTask.getStatus().FINISHED)
			return ;
		
		AP0009 ap = new AP0009();
		ap.setType(brdType);
		ap.setCommentNo(replyNo);
		ap.setCommentId(usrId);
		
		mHandler.setOneTranData(ap);
		mPostTask = new PostMessageTask(mAbstractAsyncFragmentActivity, mHandler);
		mPostTask.execute(MediaType.APPLICATION_JSON);
	}
	
	private class Holder {
		int refresh;
		int position;
		ImageView houseProfile;
		TextView name;
		TextView content;
		TextView created;
		Button btnDelete;
	}

	@Override
	public int getCount() {
		return mReplyItemList.size();
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
	public void notifyDataSetChanged() {
		refreshCnt += 1;
		super.notifyDataSetChanged();
	}

	public void setHandler(TransHandler handler) {
		mHandler = handler;
	}
}