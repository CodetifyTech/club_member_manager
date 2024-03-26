package com.elevendirtymind.clubmembermanager.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.elevendirtymind.clubmembermanager.R;
import com.elevendirtymind.clubmembermanager.adapter.MemberRecyclerViewAdapter;
import com.elevendirtymind.clubmembermanager.application.MemberApplication;
import com.elevendirtymind.clubmembermanager.clickhandler.MainActivityClickHandlers;
import com.elevendirtymind.clubmembermanager.database.MemberDatabase;
import com.elevendirtymind.clubmembermanager.databinding.ActivityMainBinding;
import com.elevendirtymind.clubmembermanager.firebase.RealTimeDatabaseRef;
import com.elevendirtymind.clubmembermanager.model.Member;
import com.elevendirtymind.clubmembermanager.viewmodel.MemberViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class MainActivity extends AppCompatActivity {

    /**
     * Binding
     */
    private ActivityMainBinding binding;

    /**
     * @param savedInstanceState If the activity is being re-initialized after
     * previously being shut down then this Bundle contains the data it most
     * recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     * Adapter
     */
    private MemberRecyclerViewAdapter memberRecyclerViewAdapter;
    /***
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     * Database
     */
    private MemberDatabase memberDatabase;
    private List<Member> memberList = new ArrayList<>();
    /**
     * @param savedInstanceState If the activity is being re-initialized after
     * previously being shut down then this Bundle contains the data it most
     * recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     * ClickHandler
     */
    private MainActivityClickHandlers mainActivityClickHandlers;

    /**
     * @param savedInstanceState If the activity is being re-initialized after
     * previously being shut down then this Bundle contains the data it most
     * recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     * ViewModel
     */
    private MemberViewModel memberViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         * The following line acting as the setContenView(R.layout.activity_main);
         */
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        /**
         * This line below can comment when have the above
         */
//        setContentView(R.layout.activity_main);
        /**
         * Set on Swipe
         */
        swipeToDeleteWithRedButton();
        /**
         * Click Handler
         */
        mainActivityClickHandlers = new MainActivityClickHandlers(this, binding, memberList);
        binding.setOnClickHandler(mainActivityClickHandlers);
        /**
         * RecyclerView
         */
        binding.memberRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.memberRecyclerView.setHasFixedSize(true);
        /**
         * Adapter
         */
        memberRecyclerViewAdapter = new MemberRecyclerViewAdapter(new MemberRecyclerViewAdapter.OnClickMemberListInterface() {
            @Override
            public void onClickMember(@NonNull Member member, @NonNull int ClickPosition) {
                mainActivityClickHandlers.onClickMemberItem(member, ClickPosition);
            }
        });

        /**
         * Database
         */
        memberDatabase = MemberDatabase.getInstance(this);
        /**
         * ViewModel
         */
        memberViewModel = new ViewModelProvider(this).get(MemberViewModel.class);
//        iniDat();

        memberViewModel.getAllMembers().observe(MainActivity.this, new Observer<List<Member>>() {
            @Override
            public void onChanged(List<Member> members) {
                memberList.clear();
                memberList.addAll(members);
                memberRecyclerViewAdapter.setMembers(memberList);
                memberRecyclerViewAdapter.notifyDataSetChanged();
            }
        });

        /**
         * Link adapter to recyclerview
         */
        binding.memberRecyclerView.setAdapter(memberRecyclerViewAdapter);

        /**
         * Ask permission to write file excel
         */
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request it
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
        }
    }

    private static final int REQUEST_CODE = 49;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with file operation
                File internalStorageDir = getFilesDir(); // Thư mục lưu trữ trên bộ nhớ trong
                Log.i("BECHJ", "MainActivity :: Permission Read Storage :: " + "Internal Storage Path: " + internalStorageDir.getAbsolutePath());
            } else {
                // Permission denied, handle accordingly
            }
        }
    }

//    private void iniDat() {
//        Member m1 = new Member(
//                "DTC20H4801030029",
//                "BE CHI KIEN",
//                "0979965784",
//                "BAC GIANG",
//                "BAN CHU NHIEM",
//                "KTPM",
//                "CNTT",
//                "KTPM - K19A");
//        Member member1 = new Member(
//                "DTC20H4801030028",
//                "TRAN DUY KHANH",
//                "0979965784",
//                "BAC GIANG",
//                "BAN CHU NHIEM",
//                "KTPM",
//                "CNTT",
//                "KTPM - K19A");
//        memberViewModel.InsertMember(m1);
//        memberViewModel.InsertMember(member1);
//    }

//    private void swipeToDelete() {
//         /*
//        Swipe to delete
//        new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT)
//        the first param `0` specify the drag and drop directions,0 is no drag and drop action is supported
//        the second param `ItemTouchHelper.LEFT` specify the swipe direction. In this case `to the left` supported
//         */
//        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
//            @Override
//            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
//                return false;
//            }
//
//            @Override
//            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
//                Member member = memberList.get(viewHolder.getAdapterPosition());
//                memberViewModel.DeleteMember(member);
//            }
//        }).attachToRecyclerView(binding.memberRecyclerView);
//    }

    private Member backupMember = null;

    private void swipeToDeleteWithRedButton() {
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                // Handle swipe to delete here
                int position = viewHolder.getAdapterPosition();
                // Backup the deleted item
                backupMember = memberList.get(position);
                // Remove the item from the RecyclerView
                memberList.remove(position);
                memberRecyclerViewAdapter.notifyItemRemoved(position);

                // Show a Snackbar with an "Undo" option
                Snackbar snackbar = Snackbar.make(binding.memberRecyclerView, "Member deleted", Snackbar.LENGTH_LONG)
                        .setAction("Undo", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Undo the delete action
                                memberList.add(position, backupMember);
                                memberRecyclerViewAdapter.notifyItemInserted(position);

                            }
                        }).setDuration(4000); // Have 5 second to undo the change
                snackbar.show();
                // Retrieve the item from the adapter
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (memberList.isEmpty()) {
                                memberViewModel.DeleteMember(backupMember);
                            } else if (!backupMember.equals(memberList.get(position))) {
                                memberViewModel.DeleteMember(backupMember);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.i("BECHJ", "MainActivity :: On Delete Member ::" + e.getMessage());
                        }

                    }
                }, 4000);

            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                // Draw the delete button when swiping
                View itemView = viewHolder.itemView;
                if (dX < 0) { // Swiping to the left
                    Paint paint = new Paint();
                    paint.setColor(Color.RED);
                    RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
                    c.drawRect(background, paint);
                    Drawable deleteIcon = ContextCompat.getDrawable(MainActivity.this, R.drawable.trash_can);
                    if (deleteIcon != null) {
                        int iconMargin = (itemView.getHeight() - deleteIcon.getIntrinsicHeight()) / 2;
                        int iconTop = itemView.getTop() + (itemView.getHeight() - deleteIcon.getIntrinsicHeight()) / 2;
                        int iconBottom = iconTop + deleteIcon.getIntrinsicHeight();
                        int iconLeft = itemView.getRight() - iconMargin - deleteIcon.getIntrinsicWidth();
                        int iconRight = itemView.getRight() - iconMargin;
                        deleteIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
                        deleteIcon.draw(c);
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(binding.memberRecyclerView);
    }
}