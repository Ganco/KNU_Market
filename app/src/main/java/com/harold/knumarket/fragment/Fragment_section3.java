package com.harold.knumarket.fragment;

import com.harold.knumarket.categories.Category_Book;
import com.harold.knumarket.categories.Category_Fashion;
import com.harold.knumarket.categories.Category_ListView;
import com.harold.knumarket.categories.Category_Living;
import com.knumarket.harold.knu_market.R;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class Fragment_section3 extends Fragment {
    public static final String ARG_SECTION_NUMBER = "section_number";

    public static final int REQUEST_CODE_MAIN = 1001;

    private Button button1;
    private Button button2;
    private Button button3;
    private Button button4;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_section3, container, false);
        //Bundle args = getArguments();

/*
        button1 = (Button) rootView.findViewById(R.id.btn_f000);
        button1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getActivity(), Category_Book.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.putExtra("category_no", "000");
                startActivity(intent);
            }
        });

        button2 = (Button) rootView.findViewById(R.id.btn_f100);
        button2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getActivity(), Category_Living.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.putExtra("category_no", "100");
                startActivity(intent);
            }
        });

        button3 = (Button) rootView.findViewById(R.id.btn_f200);
        button3.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getActivity(), Category_Fashion.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.putExtra("category_no", "200");
                startActivity(intent);
            }
        });

        button4 = (Button) rootView.findViewById(R.id.btn_f300);
        button4.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getActivity(), Category_ListView.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.putExtra("category_no", "300");
                startActivity(intent);
            }
        });*/



        return  rootView;
    }

/*
    public void onClick(View v){

        int id = v.getId();
        Intent intent = null;

        switch (id){

            case R.id.btn_f000:
                intent = new Intent(getActivity(), Category_Book.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.putExtra("category_no", "000");
                startActivity(intent);
                break;

            case R.id.btn_f100:
                intent = new Intent(getActivity(), Category_Living.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.putExtra("category_no", "100");
                startActivity(intent);
                break;

            //// insert button listener for MYPAGE
            case R.id.btn_f200:
                intent = new Intent(getActivity(), Category_Fashion.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.putExtra("category_no", "200");
                startActivity(intent);
                break;

            case R.id.btn_f300:
                intent = new Intent(getActivity(), Category_ListView.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.putExtra("category_no", "300");
                startActivity(intent);
                break;
        }
    }
    //*/
}
