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
		arrayList.add("111111111111111111111111111111");
		arrayList.add("2222222222222222222222222222222");
		arrayList.add("3333333333333333333333333333333333333333");
		arrayList.add("444444444444444444444444444444444444444444444");
		arrayList.add("555555555555555555555555555555555555555555555555555555555555555555555555555555555555555555555555555555555555555555555555555555");
		arrayList.add("66666666666666666666666666666666666666666666666666666666666666666");
		arrayList.add("77777777777777777777777777777777777777777777777777777777777777777");
		arrayList.add("8888888888888888888888888888888888888888888888888888888888888888888888888");

		BoardAdapter boardAdapter = new BoardAdapter(getActivity(), arrayList);

		listView.setAdapter(boardAdapter);
	}

}