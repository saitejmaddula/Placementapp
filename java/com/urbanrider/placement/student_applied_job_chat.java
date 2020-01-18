package com.urbanrider.placement;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class student_applied_job_chat extends AppCompatActivity {
RecyclerView recyclerView;
Toolbar toolbar;
String dstid;
List<Chat> chatList;
messageAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_applied_job_chat);
        recyclerView=findViewById(R.id.student_applied_job_chat_recview);
        toolbar=findViewById(R.id.student_applied_job_chat_toolbar);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.ic_arrow_back_black_24dp);
        upArrow.setColorFilter(getResources().getColor(R.color.blue2), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        dstid=getIntent().getStringExtra("dstid");


                chatList=new ArrayList<>();
                DatabaseReference d=FirebaseDatabase.getInstance().getReference("Chats");

                d.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        chatList.clear();
                        for(DataSnapshot ds:dataSnapshot.getChildren())
                        {

                    /*if((chat.getReceiver().equals(dst)&& chat.getSender().equals(src)) || (chat.getReceiver().equals(src)&& chat.getSender().equals(dst)))
                    {
                        chatList.add(chat);
                    }*/
                            if(ds.child("sender").getValue().toString().equals("cir")&&ds.child("receiver").getValue().toString().equals(dstid))
                            {
                                Chat chat=new Chat(ds.child("receiver").getValue().toString(),ds.child("sender").getValue().toString(),ds.child("message").getValue().toString(),Boolean.parseBoolean(ds.child("isseen").getValue().toString()));
                                chatList.add(chat);
                            }

                        }
                        mAdapter = new messageAdapter(student_applied_job_chat.this,chatList);

                        recyclerView.setAdapter(mAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }



}
