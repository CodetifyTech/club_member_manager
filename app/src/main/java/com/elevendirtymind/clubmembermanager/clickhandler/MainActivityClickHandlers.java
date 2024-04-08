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
        Log.i("TAGMAIN", "MainActivityClickHandlers() :: CLICK HANDLER MAIN ACTIVITY GENERATED");
    }

    public void onClickAddNewMember(@NonNull View view) {

        Intent i = new Intent(activity, MemberDetailsActivity.class);
        i.putExtra("command", "INSERT");
        activity.startActivity(i);
        Log.i("TAGMAIN", "onClickAddNewMember() :: ADD NEW BUTTON CLICKED");
    }

    public void onClickMemberItem(@NonNull Member member, @NonNull int ClickPosition) {
        Log.i("TAGMAIN", "onClickMember() :: Member Clicked: " + member.toString());
        Log.i("TAGMAIN", "onClickMember() :: Member Position: " + ClickPosition);
        Intent i = new Intent(activity, MemberDetailsActivity.class);
        i.putExtra("id", member.getMaSinhVien());
        i.putExtra("command", "UPDATE");
        activity.startActivity(i);
        Toast.makeText(activity, "MEMBER DETAILS", Toast.LENGTH_SHORT).show();
    }


    public void onClickExport(@NonNull View view) {
        Export.exportTest(activity, memberApplication, memberList);
        Log.i("TAGMAIN", "onClickExport() :: Export Clicked");
    }
}
