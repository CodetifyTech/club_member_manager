package com.elevendirtymind.clubmembermanager.database;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Entity;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.elevendirtymind.clubmembermanager.model.Member;

@Database(entities = {Member.class}, exportSchema = true, version = 1)
public abstract class MemberDatabase extends RoomDatabase {
    public abstract MemberDAO getDAO();
    private static MemberDatabase instance;

    public static synchronized MemberDatabase getInstance(@NonNull Context context){
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), MemberDatabase.class, "members_club_db")
                    .fallbackToDestructiveMigration().build();
            Log.i("BECHJ", "MemberDatabase :: getInstance() :: INSTANCE DATABASE GENERATED");
        }
        return instance;
    }

}
