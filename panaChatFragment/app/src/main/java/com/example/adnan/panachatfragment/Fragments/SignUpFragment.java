package com.example.adnan.panachatfragment.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.adnan.panachatfragment.Activities.FacebookAuthActivity;
import com.example.adnan.panachatfragment.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpFragment extends Fragment {


    public SignUpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View v = inflater.inflate(R.layout.fragment_sign_up, container, false);

        ((Button)v.findViewById(R.id.signUp)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Currently not implemented", Toast.LENGTH_SHORT).show();
            }
        });
        return v;
    }

}
