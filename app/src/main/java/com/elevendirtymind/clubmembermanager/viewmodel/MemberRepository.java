package com.elevendirtymind.clubmembermanager.viewmodel;

import android.app.Activity;
import android.app.Application;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.elevendirtymind.clubmembermanager.adapter.MemberRecyclerViewAdapter;
import com.elevendirtymind.clubmembermanager.application.MemberApplication;
import com.elevendirtymind.clubmembermanager.database.MemberDAO;
import com.elevendirtymind.clubmembermanager.database.MemberDatabase;
import com.elevendirtymind.clubmembermanager.firebase.RealTimeDatabaseRef;
import com.elevendirtymind.clubmembermanager.model.Member;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import org.checkerframework.checker.units.qual.N;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MemberRepository {
    private ExecutorService executorService;
    private MemberApplication application;
    private MemberViewModel memberViewModel;
    private MemberDAO dao;

    public MemberRepository(@NonNull Application application, @NonNull MemberViewModel memberViewModel) {
        MemberDatabase memberDatabase = MemberDatabase.getInstance(application);
        this.dao = memberDatabase.getDAO();
        this.application = new MemberApplication();
        this.memberViewModel = memberViewModel;
        executorService = Executors.newSingleThreadExecutor(); // Handler IN BACKGROUND
    }

    synchronized public void InsertMember(Member member) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    dao.Insert(member);
                    Log.i("TAGMAIN", "MemberRepository :: InsertMember() :: " + "Thêm thành viên xong!");
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.i("TAGMAIN", "MemberRepository :: InsertMember() :: " + e.getMessage());
                }
            }
        });
    }

    synchronized public void UpdateMember(Member member) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    dao.Update(member);
                    Log.i("TAGMAIN", "MemberRepository :: UpdateMember() ::" + "Thành viên được cập nhật dữ liệu");
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.i("TAGMAIN", "MemberRepository :: UpdateMember() :: " + e.getMessage());
                }
            }
        });
    }

    synchronized public void DeleteMember(Member member) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    dao.Delete(member);
                    application.getDatabaseInstance().getReference(RealTimeDatabaseRef.refMain)
                            .child(RealTimeDatabaseRef.refMainChildMember)
                            .child(member.getMaSinhVien()).removeValue();

                    Log.i("TAGMAIN", "MemberRepository :: DeleteMember() ::" + "Thành viên đã bị xóa đi tồn tại");

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.i("TAGMAIN", "MemberRepository :: DeleteMember() ::" + e.getMessage());
                }
            }
        });
    }

    private LiveData<List<Member>> memberListLiveData;

    synchronized public LiveData<List<Member>> getAllMembers() {
                try {
                    memberListLiveData = dao.getAllMembers();
                    if (memberListLiveData != null) {
                        List<Member> members = memberListLiveData.getValue();
                        if (members != null) {
                            Log.i("TAGMAIN", "MemberRepository :: getAllMembers() :: " + "Danh sách tồn tại! " + memberListLiveData.getValue().toString());
                        } else {
                            Log.i("TAGMAIN", "MemberRepository :: getAllMembers() :: " + "Danh sách không tồn tại! ");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.i("TAGMAIN", "MemberRepository :: getAllMembers() :: " + e.getMessage());
                }
        return memberListLiveData;
    }

    private LiveData<Member> memberLiveData;

    synchronized public LiveData<Member> getSpecificMember(String ma_sinh_vien) {
                try {
                    memberLiveData = dao.getSpecificMember(ma_sinh_vien);
                    if (memberLiveData != null) {
                        Member member = memberLiveData.getValue();
                        if (member != null) {
                            Log.i("TAGMAIN", "MemberRepository :: getSpecificMember(id) :: " + "Thành viên tồn tại! " + memberLiveData.getValue().toString());
                        } else {
                            Log.i("TAGMAIN", "MemberRepository :: getSpecificMember(id) :: " + "Thành viên không tồn tại! ");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.i("TAGMAIN", "MemberRepository :: getSpecificMember(id) :: " + e.getMessage());
                }
        return memberLiveData;
    }
}
