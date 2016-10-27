package com.androidcalc.ertai87.androidcalc.main;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidcalc.ertai87.androidcalc.R;

public class BinaryFragment extends Fragment {

	public void onAttach(Context context){
		super.onAttach(context);
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	        // Inflate the layout for this fragment
	        View root = inflater.inflate(R.layout.frag_bin, container, false);
	        return root;
	}
}