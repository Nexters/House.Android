package com.nexters.house.fragment;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.nexters.house.R;
import com.nexters.house.adapter.BoardAdapter;

public class BoardFragment extends Fragment {
	ListView listView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.fragment_board, container, false);

		listView = (ListView) v.findViewById(R.id.listview);

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

		listView.setAdapter(boardAdapter);
	}

}