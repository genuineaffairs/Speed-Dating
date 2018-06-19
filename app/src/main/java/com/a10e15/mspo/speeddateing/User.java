package com.a10e15.mspo.speeddateing;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.IgnoreExtraProperties;


import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;
import static com.a10e15.mspo.speeddateing.SplashAct.db;
import static com.a10e15.mspo.speeddateing.SplashAct.mAuth;


/**
 * Created by justking100 on 2/13/2018.
 */
@IgnoreExtraProperties
public class User {

    public enum Status{
        inactive,
        active,
        offline

    }

    public String username;
    public String email;
    public String gender;
    public String dob;
    public String race;
    public String contactInfo;
    public Status status;
    private String uid;



    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String email, String userId) {
        this.username = username;
        this.email = email;
        this.uid = userId;
        this.status = Status.offline;
    }

public String getUid(){return uid;}
public void updateUser(){
    db.collection("users").document(this.getUid()).update(this.toMap());

}

    public void setUserStatus(Status st){
       status = st;
       updateUser();




    }

public static void getLoggedinUser( final SucFail getingUser){
    String uid = mAuth.getUid();
    if(null!=uid)
    getUser(mAuth.getUid(), new SucFail() {
        @Override
        public void onSuccess(Object ret) {
            getingUser.onSuccess(ret);
        }

        @Override
        public void onFailure(Exception ret) {
            getingUser.onFailure(ret);
        }
    });
    else
        getingUser.onFailure(new Exception("Not Logged in"));
}

public static void getUser(String uid, final SucFail getingUser){
    DocumentReference docRef = db.collection("users").document(uid);
    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
        @Override
        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document != null) {
                    User user = document.toObject(User.class);
                    Log.d(TAG, "DocumentSnapshot data: " + task.getResult().getData());
                    getingUser.onSuccess(user);
                } else {
                    Log.d(TAG, "No such document");
                    getingUser.onFailure(new Exception("No such document"));

                }
            } else {
                Log.d(TAG, "get failed with ", task.getException());
                getingUser.onFailure(task.getException());
            }
        }
    });
}
public static User writeNewUser(String userId, String name, String email) {
        User user = new User(name, email,userId);
        db.collection("users").document(userId).set(user);
        return user;
}


    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", username);
        result.put("email", email);
        result.put("gender", gender);
        result.put("dob", dob);
        result.put("race", race);
        result.put("contactInfo", contactInfo);
        result.put("status", status.name());


        return result;
    }

}