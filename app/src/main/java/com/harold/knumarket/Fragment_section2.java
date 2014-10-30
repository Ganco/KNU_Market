package com.harold.knumarket;

import com.knumarket.harold.knu_market.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Fragment_section2 extends Fragment {
    public static final String ARG_SECTION_NUMBER = "section_number";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_section2, container, false);
        Bundle args = getArguments();
        TextView textView = (TextView) rootView.findViewById(R.id.text2);
        textView.setText("Section2");

        return  rootView;
    }
}
