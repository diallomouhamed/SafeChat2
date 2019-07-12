package com.diallo.safechat;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ConversationFragment extends Fragment {

    public static ConversationFragment newInstance(){
        ConversationFragment fragment = new ConversationFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_conversation , container, false);
        return view;
    }


}
