package com.duang.easyecard.Activity;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.duang.easyecard.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class LostAndFoundFragment extends Fragment {


    public LostAndFoundFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lost_and_found, container, false);
    }

}
