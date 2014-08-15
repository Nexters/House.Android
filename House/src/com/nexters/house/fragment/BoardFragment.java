package com.nexters.house.fragment;

import java.util.*;

import android.content.*;
import android.os.*;
import android.support.v4.app.*;
import android.view.*;
import android.widget.*;

import com.nexters.house.*;
import com.nexters.house.activity.*;
import com.nexters.house.adapter.*;

public class BoardFragment extends Fragment {
	ListView mBoardList;
	Button mBtnWrite;
	View mView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_board, container, false);

		initResource();
		initEvent();
		return mView;
	}
	
	public void initResource(){
		mBoardList = (ListView) mView.findViewById(R.id.board_list);
		
		mBtnWrite = (Button)mView.findViewById(R.id.btn_write);
	    mBtnWrite.setText("쓰기");
	    mBtnWrite.bringToFront();
	}
	
	public void initEvent(){
		mBtnWrite.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(getActivity(),TalkWriteActivity.class);
			 	startActivity(intent);
			}
		});
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		ArrayList<String> arrayList = new ArrayList<String>();
		arrayList.add("content1");
		arrayList.add("content2");
		arrayList.add("content3");
		arrayList.add("content4");
		arrayList.add("content5");
		arrayList.add("content6");
		arrayList.add("content7");
		arrayList.add("content8");
		arrayList.add("content9");
		arrayList.add("content10");

		BoardAdapter boardAdapter = new BoardAdapter(getActivity(), arrayList);

		mBoardList.setAdapter(boardAdapter);
	}

}