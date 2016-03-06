package com.calc;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.Activity;

import com.calc.R;

public class DecimalFragment extends Fragment{
	DecFragListener act;
	
	public interface DecFragListener{
		public void setDecButtons(View root);
	}
	
	public void onAttach(Activity a){
		super.onAttach(a);
		
		act = (DecFragListener)a;
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	        // Inflate the layout for this fragment
		
	        View root = inflater.inflate(R.layout.frag_dec, container, false);
	        act.setDecButtons(root);
	        return root;
	}
}