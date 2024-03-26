package com.elevendirtymind.clubmembermanager.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.elevendirtymind.clubmembermanager.R;
import com.elevendirtymind.clubmembermanager.clickhandler.MemberDetailsClickHandlers;
import com.elevendirtymind.clubmembermanager.databinding.ActivityMemberDetailsBinding;
import com.elevendirtymind.clubmembermanager.model.Member;
import com.elevendirtymind.clubmembermanager.viewmodel.MemberViewModel;

public class MemberDetailsActivity extends AppCompatActivity {

    private ActivityMemberDetailsBinding binding;
    private MemberDetailsClickHandlers memberDetailsClickHandlers;
    private MemberViewModel memberViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_member_details);
//        setContentView(R.layout.activity_member_details);

        Intent i = getIntent();
        String command = i.getStringExtra("command");
        memberDetailsClickHandlers = new MemberDetailsClickHandlers(this, binding, command);
        binding.setOnClickUpdateInfo(memberDetailsClickHandlers);
        binding.buttonUpdateInfo.setShadowLayer(7,7,7,R.color.black);
    }
}