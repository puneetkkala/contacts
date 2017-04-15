package com.kalap.contacts;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by puneetkkala on 16/04/17.
 */

public class DialerFragment extends Fragment implements View.OnClickListener {

    private TextView _1, _2, _3, _4, _5, _6, _7, _8, _9, _0, star, hash, phoneNumber;
    private ImageView call, backspace;
    private StringBuilder phoneNumberBuilder;

    public static DialerFragment newInstance() {

        Bundle args = new Bundle();

        DialerFragment fragment = new DialerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialer_fragment,container,false);
        phoneNumberBuilder = new StringBuilder("");
        _1 = (TextView) view.findViewById(R.id._1);
        _2 = (TextView) view.findViewById(R.id._2);
        _3 = (TextView) view.findViewById(R.id._3);
        _4 = (TextView) view.findViewById(R.id._4);
        _5 = (TextView) view.findViewById(R.id._5);
        _6 = (TextView) view.findViewById(R.id._6);
        _7 = (TextView) view.findViewById(R.id._7);
        _8 = (TextView) view.findViewById(R.id._8);
        _9 = (TextView) view.findViewById(R.id._9);
        _0 = (TextView) view.findViewById(R.id._0);
        star = (TextView) view.findViewById(R.id._star);
        hash = (TextView) view.findViewById(R.id._hash);
        call = (ImageView) view.findViewById(R.id._call);
        backspace = (ImageView) view.findViewById(R.id.backspace);
        phoneNumber = (TextView) view.findViewById(R.id.phone_number);
        _1.setOnClickListener(this);
        _2.setOnClickListener(this);
        _3.setOnClickListener(this);
        _4.setOnClickListener(this);
        _5.setOnClickListener(this);
        _6.setOnClickListener(this);
        _7.setOnClickListener(this);
        _8.setOnClickListener(this);
        _9.setOnClickListener(this);
        _0.setOnClickListener(this);
        star.setOnClickListener(this);
        hash.setOnClickListener(this);
        call.setOnClickListener(this);
        backspace.setOnClickListener(this);
        phoneNumber.setOnClickListener(this);
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_HIDDEN);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id._1: {
                phoneNumberBuilder.append("1");
                break;
            }
            case R.id._2: {
                phoneNumberBuilder.append("2");
                break;
            }
            case R.id._3: {
                phoneNumberBuilder.append("3");
                break;
            }
            case R.id._4: {
                phoneNumberBuilder.append("4");
                break;
            }
            case R.id._5: {
                phoneNumberBuilder.append("5");
                break;
            }
            case R.id._6: {
                phoneNumberBuilder.append("6");
                break;
            }
            case R.id._7: {
                phoneNumberBuilder.append("7");
                break;
            }
            case R.id._8: {
                phoneNumberBuilder.append("8");
                break;
            }
            case R.id._9: {
                phoneNumberBuilder.append("9");
                break;
            }
            case R.id._0: {
                phoneNumberBuilder.append("0");
                break;
            }
            case R.id._star: {
                phoneNumberBuilder.append("*");
                break;
            }
            case R.id._hash: {
                phoneNumberBuilder.append("#");
                break;
            }
            case R.id._call: {
                if (phoneNumberBuilder.length() > 0) {
                    String data = "tel:" + phoneNumberBuilder.toString();
                    Intent callIntent = new Intent(Intent.ACTION_CALL).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    callIntent.setData(Uri.parse("tel:"+data));
                    getActivity().startActivity(callIntent);
                }
                return;
            }
            case R.id.backspace: {
                if (phoneNumberBuilder.length() > 0) {
                    phoneNumberBuilder.deleteCharAt(phoneNumberBuilder.length() - 1);
                }
                break;
            }
        }
        phoneNumber.setText(phoneNumberBuilder);
    }
}
