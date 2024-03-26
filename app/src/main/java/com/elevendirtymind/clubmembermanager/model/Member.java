package com.elevendirtymind.clubmembermanager.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.elevendirtymind.clubmembermanager.BR;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Entity(tableName = "members_table")
public class Member extends BaseObservable implements Serializable {
    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "ma_sinh_vien")
    private String maSinhVien; // mã sinh viên
    /**
     * Mã thành viên là mỗi clb tự quy định, mã sinh viên là nhà trường cấp
     */
    @NonNull
    @ColumnInfo(name = "ho_ten")
    private String hoTen; // Họ và tên
    @ColumnInfo(name = "so_dien_thoai")
    @NonNull
    private String sdt; // Số liên lạc
    @ColumnInfo(name = "que_quan")
    @NonNull
    private String queQuan; // Quê quán
    @NonNull
    @ColumnInfo(name = "chuc_vu_trong_clb")
    private String role; // Chức vụ / vai trò trong câu lạc bộ

    @ColumnInfo(name = "chuyen_nganh")
    @NonNull
    private String chuyenNganh; // chuyên ngành đã chọn
    @ColumnInfo(name = "khoa")
    @NonNull
    private String khoa; // Khoa đang học
    @ColumnInfo(name = "lop")
    @NonNull
    private String lop; // Lớp học đăng ký trên trường

    @Ignore
    public Member() {
        this.maSinhVien = "";
        this.hoTen = "";
        this.sdt = "";
        this.queQuan = "";
        this.role = "";
        this.chuyenNganh = "";
        this.khoa = "";
        this.lop = "";
    }

    public Member(@NonNull String maSinhVien, @NonNull String hoTen, @NonNull String sdt, @NonNull String queQuan, @NonNull String role, @NonNull String chuyenNganh, @NonNull String khoa, @NonNull String lop) {
        this.maSinhVien = maSinhVien;
        this.hoTen = hoTen;
        this.sdt = sdt;
        this.queQuan = queQuan;
        this.role = role;
        this.chuyenNganh = chuyenNganh;
        this.khoa = khoa;
        this.lop = lop;
    }

    @NonNull
    @Bindable
    public String getMaSinhVien() {
        return maSinhVien;
    }

    public void setMaSinhVien(@NonNull String maSinhVien) {
        this.maSinhVien = maSinhVien;
        notifyPropertyChanged(BR.maSinhVien);
    }

    @NonNull
    @Bindable
    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(@NonNull String hoTen) {
        this.hoTen = hoTen;
        notifyPropertyChanged(BR.hoTen);
    }

    @NonNull
    @Bindable
    public String getSdt() {
        return sdt;
    }

    public void setSdt(@NonNull String sdt) {
        this.sdt = sdt;
        notifyPropertyChanged(BR.sdt);
    }

    @NonNull
    @Bindable
    public String getQueQuan() {
        return queQuan;
    }

    public void setQueQuan(@NonNull String queQuan) {
        this.queQuan = queQuan;
        notifyPropertyChanged(BR.queQuan);
    }

    @NonNull
    @Bindable
    public String getRole() {
        return role;
    }

    public void setRole(@NonNull String role) {
        this.role = role;
        notifyPropertyChanged(BR.role);
    }

    @NonNull
    @Bindable
    public String getChuyenNganh() {
        return chuyenNganh;
    }

    public void setChuyenNganh(@NonNull String chuyenNganh) {
        this.chuyenNganh = chuyenNganh;
        notifyPropertyChanged(BR.chuyenNganh);
    }

    @NonNull
    @Bindable
    public String getKhoa() {
        return khoa;
    }

    public void setKhoa(@NonNull String khoa) {
        this.khoa = khoa;
        notifyPropertyChanged(BR.khoa);
    }

    @NonNull
    @Bindable
    public String getLop() {
        return lop;
    }

    public void setLop(@NonNull String lop) {
        this.lop = lop;
        notifyPropertyChanged(BR.lop);
    }

    @Ignore
    @Override
    public String toString() {
        return "Member{" +
                "maSinhVien='" + maSinhVien + '\'' +
                ", hoTen='" + hoTen + '\'' +
                ", sdt='" + sdt + '\'' +
                ", queQuan='" + queQuan + '\'' +
                ", role='" + role + '\'' +
                ", chuyenNganh='" + chuyenNganh + '\'' +
                ", khoa='" + khoa + '\'' +
                ", lop='" + lop + '\'' +
                '}';
    }

    @Ignore
    public boolean equals(@Nullable Member obj) {
        return this.maSinhVien.equals(obj.maSinhVien);
    }

    @Ignore
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("maSinhVien", maSinhVien);
        map.put("hoTen", hoTen);
        map.put("sdt", sdt);
        map.put("queQuan", queQuan);
        map.put("role", role);
        map.put("chuyenNganh", chuyenNganh);
        map.put("khoa", khoa);
        map.put("lop", lop);
        return map;
    }
}
