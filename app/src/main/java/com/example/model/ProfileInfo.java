package com.example.model;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileInfo
{
    public static FirebaseUser firebaseUser;

    public static void setFirebaseUser(FirebaseUser firebaseUser) {
        ProfileInfo.firebaseUser = firebaseUser;
    }
}
