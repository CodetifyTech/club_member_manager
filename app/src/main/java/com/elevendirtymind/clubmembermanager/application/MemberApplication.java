package com.elevendirtymind.clubmembermanager.application;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;

import com.elevendirtymind.clubmembermanager.firebase.RealTimeDatabaseRef;
import com.elevendirtymind.clubmembermanager.model.Member;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

public class MemberApplication extends Application {

    private static final String firebaseRealTimeDataaseUrl = "https://admin-030724-default-rtdb.firebaseio.com/";

    public MemberApplication() {

    }

    private FirebaseDatabase instance;

    public FirebaseDatabase getDatabaseInstance() {
        if (instance == null) {
            instance = FirebaseDatabase.getInstance(firebaseRealTimeDataaseUrl);
        }
        return instance;
    }

    private FirebaseStorage firebaseStorage;
    public FirebaseStorage getFireStorage(){
        if(firebaseStorage == null){
            firebaseStorage = FirebaseStorage.getInstance();
        }
        return firebaseStorage;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
        getDatabaseInstance();
        getFireStorage();
    }
}
