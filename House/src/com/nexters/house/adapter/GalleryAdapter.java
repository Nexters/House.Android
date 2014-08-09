package com.nexters.house.adapter;

import java.util.*;

import android.content.*;
import android.util.*;
import android.view.*;
import android.widget.*;

import com.nexters.house.*;
import com.nexters.house.entity.CustomGallery;
import com.nostra13.universalimageloader.core.*;
import com.nostra13.universalimageloader.core.assist.*;

public class GalleryAdapter extends BaseAdapter {
	public static ArrayList<CustomGallery> customGalleries = new ArrayList<CustomGallery>();
	public static ArrayList<CustomGallery> customGalleriesChecked = new ArrayList<CustomGallery>();
	public static HashSet<String> customGalleriesSet = new HashSet<String>();
	public static int selectCnt = 0;
	public static ArrayList<CustomGallery> selectedData = null;
	
	private Context mContext;
	private LayoutInflater mInfalter;
	private ImageLoader mImageLoader;
	
	private boolean isActionMultiplePick;
	
	public GalleryAdapter(Context c, ImageLoader imageLoader) {
		mInfalter = (LayoutInflater) c
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mContext = c;
		this.mImageLoader = imageLoader;
		// clearCache();
	}

	public void toggleGallery(boolean toggle){
		if (toggle)
			selectedData = customGalleries;
		else
			selectedData = customGalleriesChecked;
	}
	
	@Override
	public int getCount() {
		return selectedData.size();
	}

	@Override
	public CustomGallery getItem(int position) {
		return customGalleries.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void setMultiplePick(boolean isMultiplePick) {
		this.isActionMultiplePick = isMultiplePick;
	}

	public void selectAll(boolean selection) {
		for (int i = 0; i < customGalleries.size(); i++) {
			customGalleries.get(i).isSeleted = selection;
		}
		notifyDataSetChanged();
	}

	public boolean isAllSelected() {
		boolean isAllSelected = true;

		for (int i = 0; i < customGalleries.size(); i++) {
			if (!customGalleries.get(i).isSeleted) {
				isAllSelected = false;
				break;
			}
		}
		return isAllSelected;
	}

	public boolean isAnySelected() {
		boolean isAnySelected = false;

		for (int i = 0; i < customGalleries.size(); i++) {
			if (customGalleries.get(i).isSeleted) {
				isAnySelected = true;
				break;
			}
		}
		return isAnySelected;
	}

	public ArrayList<CustomGallery> getSelected() {
		/*
		 * ArrayList<CustomGallery> customGalleriesT = new ArrayList<CustomGallery>();
		 * for (int i = 0; i < customGalleries.size(); i++) { if (customGalleries.get(i).isSeleted) {
		 * customGalleriesT.add(customGalleries.get(i)); } }
		 * 
		 * return customGalleriesT;
		 */
		return customGalleriesChecked;
	}

	public void addAll(ArrayList<CustomGallery> files) {
		try {
			for(CustomGallery cg : files){
				if(!customGalleriesSet.contains(cg.sdcardPath)){
					this.customGalleriesSet.add(cg.sdcardPath);
					this.customGalleries.add(cg);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		notifyDataSetChanged();
	}

	public void changeSelection(View v, int position) {
		if (customGalleries.get(position).isSeleted) {
			customGalleries.get(position).isSeleted = false;

			customGalleriesChecked.remove(customGalleries.get(position)); // 들어가있는거 빼기
			if (selectCnt != 0)
				selectCnt--; // 0이면 빼지마
		} else { // 체크안되있을때
			if (selectCnt < 10) { // 그리고 10보다 작을때만 넣어
				customGalleries.get(position).isSeleted = true;
				selectCnt++;
				customGalleriesChecked.add(customGalleries.get(position)); // 체크된거 넣기
			} else { // 10모다 크면
				Toast.makeText(mContext, "10개까지만", Toast.LENGTH_LONG).show();
			}
		}
		((ViewHolder) v.getTag()).imgQueueMultiSelected.setSelected(customGalleries
				.get(position).isSeleted);
	}

	public void deleteItem(View v, int position) {
		customGalleries.get(position).isSeleted = false;
		selectCnt--;
		customGalleriesChecked.remove(customGalleries.get(position));

		((ViewHolder) v.getTag()).imgQueueMultiSelected.setSelected(customGalleries
				.get(position).isSeleted);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;

		if (convertView == null) {
			convertView = mInfalter.inflate(R.layout.gallery_item, null);
			holder = new ViewHolder();
			holder.imgQueue = (ImageView) convertView
					.findViewById(R.id.imgQueue);

			holder.imgQueueMultiSelected = (ImageView) convertView
					.findViewById(R.id.imgQueueMultiSelected);

			if (isActionMultiplePick) {
				holder.imgQueueMultiSelected.setVisibility(View.VISIBLE);
			} else {
				holder.imgQueueMultiSelected.setVisibility(View.GONE);
			}

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.imgQueue.setTag(position);

		try {

			// mImageLoader.displayImage("file://" +
			// customGalleries.get(position).sdcardPath,

			mImageLoader.displayImage("file://" + selectedData.get(position).sdcardPath,

			holder.imgQueue, new SimpleImageLoadingListener() {
				@Override
				public void onLoadingStarted(String imageUri, View view) {
					holder.imgQueue.setImageResource(R.drawable.no_media);
					super.onLoadingStarted(imageUri, view);
				}
			});
			if (isActionMultiplePick) {
				holder.imgQueueMultiSelected
						.setSelected(selectedData.get(position).isSeleted);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
//		Log.e("testdadsfsfsdf", "fdgdfg123 : " + customGalleries.get(position).isSeleted
//				+ "");
		return convertView;
	}

	public class ViewHolder {
		ImageView imgQueue;
		ImageView imgQueueMultiSelected;
	}

	public void clearCache() {
		mImageLoader.clearDiscCache();
		mImageLoader.clearMemoryCache();
	}

	public void clear() {
		// 버튼 누를때마다 리스트 초기화 시켜줭
		customGalleries.clear();
		customGalleriesChecked.clear();
		// 숫자도 초기화
		selectCnt = 0;
		notifyDataSetChanged();
	}
}