package com.elevendirtymind.clubmembermanager.viewmodel;

import androidx.annotation.NonNull;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.elevendirtymind.clubmembermanager.model.Member;

public class ModelMemberViewModel extends ViewModel {

    private Member member = new Member();
    private MutableLiveData<Member> memberMutableLiveData = new MutableLiveData<>();

    public void updateLiveMember(@NonNull Member member) {
        this.member = member;
        memberMutableLiveData.setValue(member);
    }

    public void updateLiveMember(@NonNull String maSinhVien, @NonNull String hoTen, @NonNull String sdt, @NonNull String queQuan, @NonNull String role, @NonNull String chuyenNganh, @NonNull String khoa, @NonNull String lop) {
        member.setMaSinhVien(maSinhVien);
        member.setHoTen(hoTen);
        member.setSdt(sdt);
        member.setQueQuan(queQuan);
        member.setRole(role);
        member.setLop(lop);
        member.setChuyenNganh(chuyenNganh);
        member.setKhoa(khoa);
        memberMutableLiveData.setValue(member);
    }

    public void updateMaSinhVien(CharSequence s, int start, int before, int count) {
        member.setMaSinhVien(String.valueOf(s));
        memberMutableLiveData.setValue(member);
    }

    public void updateHoTen(CharSequence s, int start, int before, int count) {
        member.setHoTen(String.valueOf(s));
        memberMutableLiveData.setValue(member);
    }

    public void updateSDT(CharSequence s, int start, int before, int count) {
        member.setSdt(String.valueOf(s));
        memberMutableLiveData.setValue(member);
    }

    public void updateQueQuan(CharSequence s, int start, int before, int count) {
        member.setQueQuan(String.valueOf(s));
        memberMutableLiveData.setValue(member);
    }

    public void updateRole(CharSequence s, int start, int before, int count) {
        member.setRole(String.valueOf(s));
        memberMutableLiveData.setValue(member);
    }

    public void updateChuyenNganh(CharSequence s, int start, int before, int count) {
        member.setChuyenNganh(String.valueOf(s));
        memberMutableLiveData.setValue(member);
    }

    public void updateKhoa(CharSequence s, int start, int before, int count) {
        member.setKhoa(String.valueOf(s));
        memberMutableLiveData.setValue(member);
    }

    public void updateLop(CharSequence s, int start, int before, int count) {
        member.setLop(String.valueOf(s));
        memberMutableLiveData.setValue(member);
    }

    public LiveData<Member> getLiveMember() {
        return memberMutableLiveData;
    }
}
