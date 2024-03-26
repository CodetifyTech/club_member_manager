package com.elevendirtymind.clubmembermanager.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.elevendirtymind.clubmembermanager.databinding.MemberItemBinding;
import com.elevendirtymind.clubmembermanager.model.Member;

import java.util.ArrayList;
import java.util.List;

public class MemberRecyclerViewAdapter extends RecyclerView.Adapter<MemberRecyclerViewAdapter.MemberViewHolder> {
    private List<Member> members = new ArrayList<>();

    /**
     * OnClick Settings
     */
    public interface OnClickMemberListInterface {
        void onClickMember(@NonNull Member member, @NonNull int ClickPosition);
    }

    OnClickMemberListInterface onClickMember;

    public MemberRecyclerViewAdapter(@NonNull OnClickMemberListInterface onClickMember) {
        this.onClickMember = onClickMember;
    }

    @NonNull
    public List<Member> getMembers() {
        return members;
    }

    public void setMembers(@NonNull List<Member> members) {
        this.members = members;
    }

    @NonNull
    @Override
    public MemberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MemberItemBinding binding = MemberItemBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        Log.i("BECHJ", "MemberRecyclerViewAdapter :: onCreateViewHolder() :: " + "Inflate Member Item View Successful");
        return new MemberViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MemberViewHolder holder, int position) {
        Member member = members.get(position);
        holder.setIndex(position);
        Log.i("BECHJ", "MemberRecyclerViewAdapter :: onBindViewHolder() :: " + member.toString());
        Log.i("BECHJ", "MemberRecyclerViewAdapter :: onBindViewHolder() :: " + member.getHoTen() + " index=\'" + holder.getIndex() + "\'");
        holder.binding.setMember(member);
        holder.binding.memberView.setOnClickListener(v -> {
            onClickMember.onClickMember(member, holder.getIndex());
        });
    }

    @Override
    public int getItemCount() {
        return members == null ? 0 : members.size();
    }

    public class MemberViewHolder extends RecyclerView.ViewHolder {
        private MemberItemBinding binding;
        private int index;

        public MemberViewHolder(@NonNull MemberItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @NonNull
        public int getIndex() {
            return index;
        }

        public void setIndex(@NonNull int index) {
            this.index = index;
        }

        @NonNull
        public MemberItemBinding getBinding() {
            return binding;
        }

    }
}
