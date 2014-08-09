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
	ListView mListView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_board, container, false);

		mListView = (ListView) v.findViewById(R.id.listview);

		Button b=(Button)v.findViewById(R.id.btn_write_board);
	    b.setText("쓰기");
	    b.bringToFront();
		b.setOnClickListener(new Button.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(getActivity(),TalkWriteActivity.class);
				
			 	startActivity(intent);
				
			}
		});
		
		return v;
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

		mListView.setAdapter(boardAdapter);
	}

}