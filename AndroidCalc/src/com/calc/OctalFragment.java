package com.calc;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.Activity;

import com.calc.R;

public class OctalFragment extends Fragment{
	OctFragListener act;
	
	public interface OctFragListener{
		public void setOctButtons(View root);
	}
	
	public void onAttach(Activity a){
		super.onAttach(a);
		
		act = (OctFragListener)a;
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	        // Inflate the layout for this fragment
		
	        View root = inflater.inflate(R.layout.frag_oct, container, false);
	        act.setOctButtons(root);
	        return root;
	}
}