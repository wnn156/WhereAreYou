package com.example.jun.whereareyou.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jun.whereareyou.Data.User;
import com.example.jun.whereareyou.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SetFragment extends Fragment {

    private static User me;
    public static SetFragment newInstance(){
        Bundle args = new Bundle();

        SetFragment fragment = new SetFragment();
        args.putParcelable("User",me);
        fragment.setArguments(args);
        return fragment;
    }

    public SetFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        me = (User)getArguments().get("User");


        return inflater.inflate(R.layout.fragment_set, container, false);
    }

}
