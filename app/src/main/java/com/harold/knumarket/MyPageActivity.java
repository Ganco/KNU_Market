package com.harold.knumarket;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.knumarket.harold.knu_market.R;

/**
 * Created by Gan on 2014-10-10.
 */
public class MyPageActivity extends Activity {

    public static final int REQUEST_CODE_MAIN = 1001;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);

        //asdf;



        // fdsafdsa;;
        //// insert button listener for Main
        Button btnMain = (Button) findViewById(R.id.button4);
        btnMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivityForResult(intent, REQUEST_CODE_MAIN);
            }
        });
        ////


        final EditText mEditText01 = (EditText) findViewById(R.id.profileTextEdit01);

        // 프로필 입력창이 3줄이상 넘어가지 않게 이벤트 처리
        mEditText01.addTextChangedListener(new TextWatcher()
        {
            String previousString = "";

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
                previousString= s.toString();
            }

            @Override
            public void afterTextChanged(Editable s)
            {
                if (mEditText01.getLineCount() >= 4)
                {
                    mEditText01.setText(previousString);
                    mEditText01.setSelection(mEditText01.length());
                }
            }
        });
    }
}
