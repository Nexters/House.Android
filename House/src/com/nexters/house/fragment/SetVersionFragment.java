package com.nexters.house.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nexters.house.R;

public class SetVersionFragment extends Fragment {
    private Activity mActivity;

    public static SetVersionFragment newInstance() {
        SetVersionFragment fragment = new SetVersionFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
    	View v = inflater.inflate(R.layout.fragment_set_nickname, container, false);
    	initResource(v);
    	initEvent();
    	return v;
    }

    public void initResource(View v){
    	
    }
    
    public void initEvent(){
    	
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
