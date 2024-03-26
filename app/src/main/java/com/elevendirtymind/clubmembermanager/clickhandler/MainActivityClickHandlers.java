package com.elevendirtymind.clubmembermanager.clickhandler;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.elevendirtymind.clubmembermanager.R;
import com.elevendirtymind.clubmembermanager.activity.MainActivity;
import com.elevendirtymind.clubmembermanager.activity.MemberDetailsActivity;
import com.elevendirtymind.clubmembermanager.application.MemberApplication;
import com.elevendirtymind.clubmembermanager.databinding.ActivityMainBinding;
import com.elevendirtymind.clubmembermanager.excel.Export;
import com.elevendirtymind.clubmembermanager.model.Member;

import java.util.List;

public class MainActivityClickHandlers {
    private Activity activity;
    private ActivityMainBinding binding;
    private List<Member> memberList;

    private MemberApplication memberApplication;
    public MainActivityClickHandlers(@NonNull Activity activity, @NonNull ActivityMainBinding binding, @NonNull List<Member> memberList) {
        this.activity = activity;
        this.binding = binding;
        this.memberList = memberList;
        this.memberApplication = new MemberApplication();
        Log.i("BECHJ", "MainActivityClickHandlers() :: CLICK HANDLER MAIN ACTIVITY GENERATED");
    }

    public void onClickAddNewMember(@NonNull View view) {
        binding.buttonAddNewMember.setBackgroundResource(R.drawable.simple_button_background_onclick);
        binding.buttonAddNewMember.setShadowLayer(0, 0, 0, R.color.white);
        binding.buttonAddNewMember.setTextColor(Color.BLACK);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.buttonAddNewMember.setBackgroundResource(R.drawable.simple_button_backgound);
                binding.buttonAddNewMember.setShadowLayer(7, 7, 7, R.color.black);
                binding.buttonAddNewMember.setTextColor(Color.WHITE);
                Intent i = new Intent(activity, MemberDetailsActivity.class);
                i.putExtra("command", "INSERT");
                activity.startActivity(i);
                Log.i("BECHJ", "onClickAddNewMember() :: ADD NEW BUTTON CLICKED");
            }
        }, 100);
    }

    public void onClickMemberItem(@NonNull Member member, @NonNull int ClickPosition) {
        Log.i("BECHJ", "onClickMember() :: Member Clicked: " + member.toString());
        Log.i("BECHJ", "onClickMember() :: Member Position: " + ClickPosition);
        Intent i = new Intent(activity, MemberDetailsActivity.class);
        i.putExtra("id", member.getMaSinhVien());
        i.putExtra("command", "UPDATE");
        activity.startActivity(i);
        Toast.makeText(activity, "MEMBER DETAILS", Toast.LENGTH_SHORT).show();
    }


    public void onClickExport(@NonNull View view){
        binding.buttonExport.setBackgroundResource(R.drawable.simple_button_background_onclick);
        binding.buttonExport.setShadowLayer(0, 0, 0, R.color.white);
        binding.buttonExport.setTextColor(Color.BLACK);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.buttonExport.setBackgroundResource(R.drawable.simple_button_backgound);
                binding.buttonExport.setShadowLayer(7, 7, 7, R.color.black);
                binding.buttonExport.setTextColor(Color.WHITE);
                Export.exportTest(activity,memberApplication,memberList);
                Log.i("BECHJ", "onClickExport() :: Export Clicked");
            }
        }, 100);
    }
}
