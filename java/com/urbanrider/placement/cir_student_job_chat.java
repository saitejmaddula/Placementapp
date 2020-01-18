package com.urbanrider.placement;

import android.content.Intent;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class cir_student_job_chat extends AppCompatActivity {
    ImageButton send;
    EditText textsend;
    messageAdapter mAdapter;
    List<Chat> chatList;
    RecyclerView recyclerView;
    FirebaseUser fuser;
    String dstid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cir_student_job_chat);
        dstid=getIntent().getStringExtra("dstid");
        recyclerView=findViewById(R.id.cir_student_job_chat_recview);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        Toolbar toolbar=findViewById(R.id.cir_student_job_chat_toolbar);
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
         fuser= FirebaseAuth.getInstance().getCurrentUser();
        if(fuser!=null) {
            readMessages("cir", dstid);
        }
        send=findViewById(R.id.bt_cir_student_job_chat_send);
        textsend=findViewById(R.id.et_cir_student_job_chat_message);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg=textsend.getText().toString().trim();
                if(!msg.equals(""))
                {
                    sendMessage("cir",dstid,msg);
                }
                textsend.setText("");
            }
        });

    }
    public void sendMessage(final String src, final String dst, final String message)
    {
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Chats");
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("sender",src);
        hashMap.put("receiver",dst);
        hashMap.put("message",message);
        hashMap.put("isseen",true);
        ref.push().setValue(hashMap);
    }
    public void readMessages(final String src, final String dst)
    {
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
                    if(ds.child("sender").getValue().toString().equals(src)&&ds.child("receiver").getValue().toString().equals(dst))
                    {
                        Chat chat=new Chat(ds.child("sender").getValue().toString(),ds.child("receiver").getValue().toString(),ds.child("message").getValue().toString(),Boolean.parseBoolean(ds.child("isseen").getValue().toString()));
                        chatList.add(chat);
                    }

                }
                mAdapter = new messageAdapter(cir_student_job_chat.this,chatList);

                recyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
