package com.elevendirtymind.clubmembermanager.clickhandler;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.elevendirtymind.clubmembermanager.R;
import com.elevendirtymind.clubmembermanager.databinding.ActivityMemberDetailsBinding;
import com.elevendirtymind.clubmembermanager.firebase.RealTimeDatabaseRef;
import com.elevendirtymind.clubmembermanager.model.Member;
import com.elevendirtymind.clubmembermanager.viewmodel.MemberViewModel;
import com.elevendirtymind.clubmembermanager.viewmodel.ModelMemberViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MemberDetailsClickHandlers {
    private Activity activity;
    private ActivityMemberDetailsBinding binding;
    private MemberViewModel memberViewModelRepos;
    private ModelMemberViewModel memberModelViewModel;
    private Member observedMember;
    private Member existingMemberInRoomDatabase;
    private String command;

    private DatabaseReference memberRef;

    public MemberDetailsClickHandlers(@NonNull Activity activity, @NonNull ActivityMemberDetailsBinding binding, @NonNull String command) {
        /**
         * Global settings
         */
        this.activity = activity;
        this.binding = binding;
        this.command = command;
        this.memberRef = memberRef = FirebaseDatabase.getInstance().getReference(RealTimeDatabaseRef.refMain).child(RealTimeDatabaseRef.refMainChildMember);
        /**
         * Init View Model
         */
        memberViewModelRepos = new ViewModelProvider((ViewModelStoreOwner) this.activity).get(MemberViewModel.class);
        memberModelViewModel = new ViewModelProvider((ViewModelStoreOwner) this.activity).get(ModelMemberViewModel.class);
        /**
         * Binding memberModelViewModel to the view
         */
        memberModelViewModel.getLiveMember().observe((LifecycleOwner) activity, new Observer<Member>() {
            @Override
            public void onChanged(Member member) {
                observedMember = member;
                binding.setMember(observedMember);
                memberViewModelRepos.getSpecificMember(observedMember.getMaSinhVien()).observe((LifecycleOwner) activity, new Observer<Member>() {
                    @Override
                    public void onChanged(Member member) {
                        existingMemberInRoomDatabase = member;
                        if (existingMemberInRoomDatabase != null) {
                            Log.i("TAGMAIN", "MemberDetailsClickHandlers :: memberModelViewModel.getLiveMember() :: Mã Thành Viên đã có trong danh sách -> " + existingMemberInRoomDatabase);
                        } else {
                            Log.i("TAGMAIN", "MemberDetailsClickHandlers :: memberModelViewModel.getLiveMember() :: Mã Thành Viên không tồn tại, có thể thêm danh sách -> " + existingMemberInRoomDatabase);
                        }
                    }
                });
                Log.i("TAGMAIN", "MemberDetailsClickHandlers :: observing :: " + observedMember.toString());
            }
        });
        binding.setMemberModelViewModel(memberModelViewModel);
        Intent i = this.activity.getIntent();
        if (command.equalsIgnoreCase("insert")) {
            /**
             * Set backend for insert action
             */
            binding.buttonUpdateInfo.setText("Thêm mới");
            binding.editTextHoTen.setEnabled(true);
            binding.editTextMaSinhVien.setEnabled(true);
            binding.editTextQueQuan.setEnabled(true);
            binding.editTextChucVu.setEnabled(true);
            binding.editTextChuyenNganh.setEnabled(true);
            binding.editTextKhoa.setEnabled(true);
            binding.editTextLop.setEnabled(true);
            binding.editTextSDT.setEnabled(true);
        } else if (command.equalsIgnoreCase("UPDATE")) {
            /**
             * Set backend for update action
             */
            String id = i.getStringExtra("id");
            this.memberViewModelRepos.getSpecificMember(id).observe((LifecycleOwner) this.activity, new Observer<Member>() {
                @Override
                public void onChanged(Member member) {
                    observedMember = member;
                    memberModelViewModel.updateLiveMember(observedMember);
                    binding.setMemberModelViewModel(memberModelViewModel);
                    Log.i("TAGMAIN", "MemberDetailsClickHandlers :: observing :: " + observedMember.toString());
                }
            });
        }

        Log.i("TAGMAIN", "MemberDetailsClickHandlers :: " + "CLICK HANDLER MEMBER DETAILS GENERATED");
    }

    public void onClickUpdate(@NonNull View view) {
        if (command.equalsIgnoreCase("insert")) {

            if (binding.buttonUpdateInfo.getText().toString().equalsIgnoreCase("Thêm mới")) {
                if (TextUtils.isEmpty(binding.editTextHoTen.getText().toString()) ||
                        TextUtils.isEmpty(binding.editTextMaSinhVien.getText().toString()) ||
                        TextUtils.isEmpty(binding.editTextQueQuan.getText().toString()) ||
                        TextUtils.isEmpty(binding.editTextChucVu.getText().toString()) ||
                        TextUtils.isEmpty(binding.editTextChuyenNganh.getText().toString()) ||
                        TextUtils.isEmpty(binding.editTextKhoa.getText().toString()) ||
                        TextUtils.isEmpty(binding.editTextLop.getText().toString()) ||
                        TextUtils.isEmpty(binding.editTextSDT.getText().toString())) {

                    // Display error message to the user
                    Toast.makeText(activity, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();

                } else {
                    /**
                     * Check if the database have this member (the one manager wanna add) exists or not
                     */
                    if (existingMemberInRoomDatabase == null) {
                        memberViewModelRepos.InsertMember(binding.getMember());
                        Toast.makeText(activity, "Thêm thành công", Toast.LENGTH_SHORT).show();
                        /**
                         * Add firebase
                         */
                        memberRef.child(observedMember.getMaSinhVien()).setValue(observedMember)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(activity, "Đã lưu thay đổi trên Firebase", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(activity, "Có lỗi khi lưu thay đổi trên Firebase", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else if (!binding.getMember().equals(existingMemberInRoomDatabase)) {
                        memberViewModelRepos.InsertMember(binding.getMember());
                        Toast.makeText(activity, "Thêm thành công", Toast.LENGTH_SHORT).show();
                        /**
                         * Add firebase
                         *
                         */
                        memberRef.child(observedMember.getMaSinhVien()).setValue(observedMember)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(activity, "Đã lưu thay đổi trên Firebase", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(activity, "Có lỗi khi lưu thay đổi trên Firebase", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        Toast.makeText(activity, "Thêm thất bại! Mã thành viên đã có trong danh sách", Toast.LENGTH_SHORT).show();
                    }
                    //Toast.makeText(activity, "SOON", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(activity, "Lỗi chương trình, vui lòng thoát ra vào lại!", Toast.LENGTH_LONG).show();
            }
        } else if (command.equalsIgnoreCase("update")) {
            if (binding.buttonUpdateInfo.getText().toString().equalsIgnoreCase("Xác nhận đổi")) {
                if (TextUtils.isEmpty(binding.editTextHoTen.getText().toString()) ||
                        TextUtils.isEmpty(binding.editTextQueQuan.getText().toString()) ||
                        TextUtils.isEmpty(binding.editTextChucVu.getText().toString()) ||
                        TextUtils.isEmpty(binding.editTextChuyenNganh.getText().toString()) ||
                        TextUtils.isEmpty(binding.editTextKhoa.getText().toString()) ||
                        TextUtils.isEmpty(binding.editTextLop.getText().toString()) ||
                        TextUtils.isEmpty(binding.editTextSDT.getText().toString())) {

                    // Display error message to the user
                    Toast.makeText(activity, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();

                } else {
                    /**
                     * Confirm the changes
                     */
                    memberViewModelRepos.UpdateMember(observedMember);
                    /**
                     * Update on firebase
                     */
                    DatabaseReference memberRef = FirebaseDatabase.getInstance().getReference(RealTimeDatabaseRef.refMain).child(RealTimeDatabaseRef.refMainChildMember).child(observedMember.getMaSinhVien());
                    memberRef.updateChildren(observedMember.toMap())
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(activity, "Đã lưu thay đổi trên Firebase", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(activity, "Có lỗi khi lưu thay đổi trên Firebase", Toast.LENGTH_SHORT).show();
                                }
                            }); // Assuming observedMember.toMap() converts the Member object to a Map
                    /**
                     * Disable the edit fields
                     */
                    binding.editTextHoTen.setEnabled(false);
                    binding.editTextQueQuan.setEnabled(false);
                    binding.editTextChucVu.setEnabled(false);
                    binding.editTextChuyenNganh.setEnabled(false);
                    binding.editTextKhoa.setEnabled(false);
                    binding.editTextLop.setEnabled(false);
                    binding.editTextSDT.setEnabled(false);
                    binding.buttonUpdateInfo.setText("Chỉnh Sửa Thông Tin");
                    Toast.makeText(activity, "Đã lưu thay đổi", Toast.LENGTH_SHORT).show();
                }
            } else {
                /**
                 * Enable the edit fields
                 */
                binding.editTextHoTen.setEnabled(true);
                binding.editTextQueQuan.setEnabled(true);
                binding.editTextChucVu.setEnabled(true);
                binding.editTextChuyenNganh.setEnabled(true);
                binding.editTextKhoa.setEnabled(true);
                binding.editTextLop.setEnabled(true);
                binding.editTextSDT.setEnabled(true);
                binding.buttonUpdateInfo.setText("Xác nhận đổi");
                Toast.makeText(activity, "Đã có thể chỉnh sửa các trường thông tin", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void onClickCancel(@NonNull View view) {
        Log.i("TAGMAIN", "MemberDetailsClickHandlers :: onClickCancel :: " + "EDIT MEMBER BUTTON BUTTON CLICKED");
        activity.finish();
    }
}
