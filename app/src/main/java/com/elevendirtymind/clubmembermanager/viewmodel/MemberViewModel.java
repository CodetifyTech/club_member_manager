package com.elevendirtymind.clubmembermanager.viewmodel;

import android.app.Activity;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.elevendirtymind.clubmembermanager.adapter.MemberRecyclerViewAdapter;
import com.elevendirtymind.clubmembermanager.application.MemberApplication;
import com.elevendirtymind.clubmembermanager.model.Member;

import java.util.List;

/**
 * Android View Model: Use when you need use context inside a ViewModel
 */
public class MemberViewModel extends AndroidViewModel {
    private MemberRepository memberRepository;
    /**
     * Live Data
     */
    private LiveData<List<Member>> memberListLiveData;
    private LiveData<Member> memberLiveData;

    public MemberViewModel(@NonNull Application application) {
        super(application);
        this.memberRepository = new MemberRepository(application, this);
    }

    public synchronized void InsertMember(Member member) {
        memberRepository.InsertMember(member);
    }

    public synchronized void UpdateMember(Member member) {
        memberRepository.UpdateMember(member);
    }

    public synchronized void DeleteMember(Member member) {
        memberRepository.DeleteMember(member);
    }

    public synchronized LiveData<List<Member>> getAllMembers() {
        memberListLiveData = memberRepository.getAllMembers();
        return memberListLiveData;
    }

    public synchronized LiveData<Member> getSpecificMember(String ma_sinh_vien) {
        memberLiveData = memberRepository.getSpecificMember(ma_sinh_vien);
        return memberLiveData;
    }
}
