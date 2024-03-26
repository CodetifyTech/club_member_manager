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

    /***
     * TEST SUCCEED
     * @param
     */
//    public void testInstance(@NonNull String referenceCaseSensitive) {
//        DatabaseReference reference = instance.getReference(referenceCaseSensitive);
//        Member member = new Member(
//                "DTC20H4801030029",
//                "BE CHI KIEN",
//                "0979965784",
//                "BAC GIANG",
//                "BAN CHU NHIEM",
//                "KTPM",
//                "CNTT",
//                "KTPM - K19A");
//        reference.child(RealTimeDatabaseRef.refMainChildMember).child(member.getMaSinhVien()).setValue(member);
//        Member member1 = new Member(
//                "DTC20H4801030028",
//                "TRAN DUY KHANH",
//                "0979965784",
//                "BAC GIANG",
//                "BAN CHU NHIEM",
//                "KTPM",
//                "CNTT",
//                "KTPM - K19A");
//        reference.child(RealTimeDatabaseRef.refMainChildMember).child(member1.getMaSinhVien()).setValue(member1);
//    }

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
        getDatabaseInstance();
        getFireStorage();
//        testInstance(RealTimeDatabaseRef.refMain);
    }
}
