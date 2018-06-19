package com.a10e15.mspo.speeddateing;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.firestore.FirebaseFirestore;

/**
 * Created by justking100 on 2/8/2018.
 */

public class SplashAct extends AppCompatActivity {
    public static FirebaseAuth mAuth;
    public static FirebaseFirestore db;
    public static User cuser;
    public static FirebaseUser user;
    private static final int REQUEST_LOGIN = 0;
    private static final String LOGGED_IN = "user_logged";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()==null){
                    Intent intent = new Intent(getApplicationContext(), LoginPg.class);
                    startActivityForResult(intent,REQUEST_LOGIN );

                }else {
                    user = firebaseAuth.getCurrentUser();
                }
            }
        });
        Intent intent = new Intent(getApplicationContext(), instantcall.class);
         db = FirebaseFirestore.getInstance();

        startActivity(intent);
        finish();
    }
    public static boolean logOut(){
        mAuth.signOut();
        return true;
    }

}
