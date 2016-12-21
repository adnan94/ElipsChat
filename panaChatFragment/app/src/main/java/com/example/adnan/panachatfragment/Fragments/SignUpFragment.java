package com.example.adnan.panachatfragment.Fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.adnan.panachatfragment.Activities.FacebookAuthActivity;
import com.example.adnan.panachatfragment.R;
import com.example.adnan.panachatfragment.Signatures.User;
import com.example.adnan.panachatfragment.UTils.Global;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.example.adnan.panachatfragment.R.drawable.user;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpFragment extends Fragment {

    private FirebaseAuth mAuth;
    EditText email;
    EditText password;
    EditText name;
    EditText lastName;
    FirebaseUser user;
    DatabaseReference fire;
    private FirebaseAuth.AuthStateListener mAuthListener;

    public SignUpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        mAuth = FirebaseAuth.getInstance();
        fire= FirebaseDatabase.getInstance().getReference();
        View v = inflater.inflate(R.layout.fragment_sign_up, container, false);
        email = (EditText) v.findViewById(R.id.editTextSignUpEmail);
        name = (EditText) v.findViewById(R.id.editTextSignUpFirstName);
        lastName = (EditText) v.findViewById(R.id.editTextSignUpLastName);

        password = (EditText) v.findViewById(R.id.editTextSignUpPassword);
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                user = firebaseAuth.getCurrentUser();

                if (user != null) {

                    Log.d("TAG", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d("TAG", "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
        ((Button) v.findViewById(R.id.signUp)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(getActivity(), "Currently not implemented", Toast.LENGTH_SHORT).show();
                mAuth.addAuthStateListener(mAuthListener);

                String mEmail=email.getText().toString();
                String mPassword=password.getText().toString();
                final String mFirstName=name.getText().toString();
                final String mLastName=lastName.getText().toString();
                if(TextUtils.isEmpty(mEmail)){
                    email.setError("Enter Email");
                }
                if(TextUtils.isEmpty(mPassword)){
                    password.setError("Enter Password");
                }
                if(TextUtils.isEmpty(mFirstName)){
                    name.setError("Enter First Name");
                }
                if(TextUtils.isEmpty(mLastName)){
                    lastName.setError("Enter LastName");
                }

                mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                        .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Log.d("TAG", "createUserWithEmail:onComplete:" + task.isSuccessful());

                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    Toast.makeText(getActivity(), R.string.auth_failed, Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(getActivity(), R.string.auth_sucess, Toast.LENGTH_SHORT).show();
                                    fire.child("AppData").child("BasicInfo").child(task.getResult().getUser().getUid()).setValue(new User(mFirstName+" "+mLastName, "N/A", "Available", "Not Available", "00000-0000", "Not Available", task.getResult().getUser().getUid()));

                                }

                                // ...
                            }
                        });

            }
        });
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
