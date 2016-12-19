package com.example.adnan.panachatfragment.Fragments;


import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.capricorn.ArcMenu;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.adnan.panachatfragment.R;
import com.example.adnan.panachatfragment.UTils.FireBaseHandler;
import com.example.adnan.panachatfragment.UTils.Global;
import com.example.adnan.panachatfragment.Signatures.User;
import com.example.adnan.panachatfragment.UTils.Service;
import com.firebase.client.Firebase;
import com.google.firebase.database.DatabaseReference;


/**
 * A simple {@link Fragment} subclass.
 */
public class ContactsInformation extends Fragment {
    EditText email, contact, date;
    DatabaseReference fire;
    Button update;
ArcMenu arcMenu;

    public ContactsInformation() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        fire = Service.fire;

        View v = inflater.inflate(R.layout.fragment_contacts_information, container, false);
        email = (EditText) v.findViewById(R.id.editTextInfoEmail);
        contact = (EditText) v.findViewById(R.id.editTextInfoContact);
        date = (EditText) v.findViewById(R.id.editTextInfoDateOfBirth);
        update = (Button) v.findViewById(R.id.updateButton);
        arcMenu = (ArcMenu) getActivity().findViewById(R.id.arc_menu);
        arcMenu.setVisibility(View.GONE);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email.getText().length() != 0 && contact.getText().length() >= 11 && date.getText().length() != 0) {
                    DatabaseReference ref = fire.child("AppData").child("BasicInfo").child(Global.uid);

                    fire.child("AppData").child("BasicInfo").child(Global.uid).setValue(new User(Global.name, Global.picUrl, Global.status, email.getText().toString(), contact.getText().toString(), date.getText().toString(), Global.uid));
                    Global.email = email.getText().toString();
                    Global.contact = contact.getText().toString();
                    Global.birthday = date.getText().toString();
                    contact.setText("");
                    email.setText("");
                    date.setText("");
                } else {

                    YoYo.with(Techniques.Wobble)
                            .duration(700)
                            .playOn(getActivity().findViewById(R.id.editTextInfoContact));


                    YoYo.with(Techniques.Wobble)
                            .duration(700)
                            .playOn(getActivity().findViewById(R.id.editTextInfoEmail));

                    YoYo.with(Techniques.Wobble)
                            .duration(700)
                            .playOn(getActivity().findViewById(R.id.editTextInfoDateOfBirth));
                    if(email.getText().length() == 0){
                        email.setError("Plz Enter Email ");
                    }if(contact.getText().length() == 0){
                        contact.setError("Plz Enter Contact");
                    }if(date.getText().length() == 0){
                       date.setError("Plz Enter Your Birthday");
                    }

                }
            }
        });
        return v;
//    }

    }

    @Override
    public void onStart() {
        super.onStart();
        Global.infoFlag = true;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Global.infoFlag = false;
        arcMenu.setVisibility(View.VISIBLE);

    }
}
