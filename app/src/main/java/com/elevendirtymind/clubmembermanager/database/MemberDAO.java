package com.elevendirtymind.clubmembermanager.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.elevendirtymind.clubmembermanager.model.Member;

import java.util.List;

@Dao
public interface MemberDAO {
    String SELECTALL = "SELECT * FROM members_table ORDER BY ho_ten;";
    String SPECIFICSELECT = "SELECT * FROM members_table WHERE ma_sinh_vien = :ma_sinh_vien;";

    @Insert
    void Insert(Member member);

    @Update
    void Update(Member member);

    @Delete
    void Delete(Member member);

    @Query(SELECTALL)
    LiveData<List<Member>> getAllMembers();

    @Query(SPECIFICSELECT)
    LiveData<Member> getSpecificMember(String ma_sinh_vien);
}
