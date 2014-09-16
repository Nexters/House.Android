package com.nexters.house.adapter;

import java.lang.reflect.Array;
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

	
//	public static ArrayList<Integer> selectedPosition =new ArrayList<Integer>();
	public static int selectCnt = 0;
	public static boolean isShow = true;
	public static ArrayList<CustomGallery> selectedGarlleries = null;

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

	@Override
	public int getCount() {
		if (isShow)
			selectedGarlleries = customGalleries;
		else
			selectedGarlleries = customGalleriesChecked;
		return selectedGarlleries.size();
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
		 * ArrayList<CustomGallery> dataT = new ArrayList<CustomGallery>();
		 * for (int i = 0; i < data.size(); i++) { if (data.get(i).isSeleted) {
		 * dataT.add(data.get(i)); } }
		 * 
		 * return dataT;
		 */
		return customGalleriesChecked;
	}

	@SuppressWarnings("static-access")
	public void addAll(ArrayList<CustomGallery> files) {
		try {
			for (CustomGallery cg : files) {
				if (!customGalleriesSet.contains(cg.sdcardPath)) {
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
		if (customGalleries.get(position).isSeleted) {  //여기 버그..
			customGalleries.get(position).isSeleted = false;
			customGalleriesChecked.remove(customGalleries.get(position)); // 들어가있는거
			
			((ViewHolder) v.getTag()).txtNumSelected.setVisibility(View.GONE);
		
			
		/*	int deleteIndex=selectviewHolder.indexOf((ViewHolder) v.getTag()); //지울 인덱스
			((ViewHolder) v.getTag()).txtNumSelected.setVisibility(View.GONE); //안보이게
			selectviewHolder.remove(deleteIndex); //리스트에서 지워
			for(int i=0;i<selectviewHolder.size();i++){
				selectviewHolder.get(i).txtNumSelected.setVisibility(View.VISIBLE);
				selectviewHolder.get(i).txtNumSelected.setText(Integer.toString(i+1));
			}*/
		//	notifyDataSetChanged(); //일단
		
			
			if (selectCnt != 0)
				selectCnt--; // 0이면 빼지마
		} else { // 체크안되있을때
			if (selectCnt < 15) { // 그리고 15보다 작을때만 넣어
				customGalleries.get(position).isSeleted = true;
				selectCnt++;
				customGalleriesChecked.add(customGalleries.get(position)); // 체크된거
				
			
				
				// 넣기
				//글씨넣어
				
				((ViewHolder) v.getTag()).txtNumSelected.setVisibility(View.VISIBLE);
				((ViewHolder) v.getTag()).txtNumSelected.setText(Integer.toString(selectCnt));
				
		
			//	((ViewHolder) v.getTag()).txtNumSelected.setText(Integer.toString(selectCnt));
				
				
			} else { // 15보다 크면
				Toast.makeText(mContext, "15개까지만", Toast.LENGTH_LONG).show();
			}
		}
		((ViewHolder) v.getTag()).imgQueueMultiSelected
				.setSelected(customGalleries.get(position).isSeleted);
		
		
	//	notifyDataSetChanged();
		
	}

	public static void compareChecked(CustomGallery customGallery) {
		customGalleries.get(customGalleries.indexOf(customGallery)).isSeleted = false;
	}

	public void deleteItem(View v, int position) {
		customGalleries.get(position).isSeleted = false;
		selectCnt--;
		customGalleriesChecked.remove(customGalleries.get(position));

		((ViewHolder) v.getTag()).imgQueueMultiSelected
				.setSelected(customGalleries.get(position).isSeleted);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;

		if (isShow)
			selectedGarlleries = customGalleries;
		else
			selectedGarlleries = customGalleriesChecked;
		
		if (convertView == null) {
			convertView = mInfalter.inflate(R.layout.gallery_item, null);
			holder = new ViewHolder();
			holder.imgQueue = (ImageView) convertView
					.findViewById(R.id.imgQueue);

			holder.imgQueueMultiSelected = (ImageView) convertView
					.findViewById(R.id.imgQueueMultiSelected);
			holder.txtNumSelected=(TextView)convertView.findViewById(R.id.txtNumSelected);

			if (isActionMultiplePick) {
				holder.imgQueueMultiSelected.setVisibility(View.VISIBLE);
				holder.txtNumSelected.setVisibility(View.VISIBLE);
			} else {
				holder.imgQueueMultiSelected.setVisibility(View.GONE);
				holder.txtNumSelected.setVisibility(View.GONE);
			}

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.imgQueue.setTag(position);

		try {
			// imageLoader.displayImage("file://" +
			// data.get(position).sdcardPath,
			mImageLoader.displayImage("file://" + selectedGarlleries.get(position).sdcardPath,

			holder.imgQueue, new SimpleImageLoadingListener() {
				@Override
				public void onLoadingStarted(String imageUri, View view) {
					holder.imgQueue.setImageResource(R.drawable.no_media);
					super.onLoadingStarted(imageUri, view);
				}
			});
			if (isActionMultiplePick) {
				holder.imgQueueMultiSelected
						.setSelected(selectedGarlleries.get(position).isSeleted);
				holder.imgQueueMultiSelected
						.setBackgroundResource(R.drawable.checkbox1); // 이부분을
			
				if(selectedGarlleries.get(position).isSeleted){
					holder.txtNumSelected.setVisibility(View.VISIBLE);
					int index=customGalleriesChecked.indexOf(selectedGarlleries.get(position));
					holder.txtNumSelected.setText(Integer.toString(index+1));
					
				}
				else
					holder.txtNumSelected.setVisibility(View.GONE);
						
/*				if(selectedGarlleries.get(position).isSeleted){
					holder.txtNumSelected.setVisibility(View.VISIBLE);
			//		int index=customGalleriesChecked.indexOf(selectedGarlleries.get(position));
					holder.txtNumSelected.setText(Integer.toString(selectCnt));
					
				}
				else
					holder.txtNumSelected.setVisibility(View.GONE);
		//		holder.txtNumSelected.setText(Integer.toString(position));	// position에
																		// 따라
																		// 다른걸로!
*/			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Log.e("testdadsfsfsdf", "fdgdfg123 : "
				+ customGalleries.get(position).isSeleted + "");
		return convertView;
	}

	public class ViewHolder {
		ImageView imgQueue;
		ImageView imgQueueMultiSelected;
		TextView txtNumSelected;
	
		
	}

	public void clearCache() {
		mImageLoader.clearDiscCache();
		mImageLoader.clearMemoryCache();
	}

	public static void clear() {
		customGalleries.clear();
		customGalleriesSet.clear();
		customGalleriesChecked.clear();
	
		selectCnt = 0;
		// notifyDataSetChanged();
	}
}
