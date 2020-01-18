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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class cir_company_chat extends AppCompatActivity {
FirebaseUser fuser;
DatabaseReference ref;
DatabaseReference ref2;
ImageButton send;
EditText textsend;
String dstuid;
messageAdapter mAdapter;
List<Chat> chatList;
RecyclerView recyclerView;
ValueEventListener seenListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cir_company_chat);
        ref2=FirebaseDatabase.getInstance().getReference("Chats");
        recyclerView=findViewById(R.id.cir_chat_recview);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        Intent i1=getIntent();
        String name=i1.getStringExtra("name");
        Toolbar toolbar=findViewById(R.id.cir_chat_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.ic_arrow_back_black_24dp);
        upArrow.setColorFilter(getResources().getColor(R.color.blue2), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(cir_company_chat.this,cir_home.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });




        send=findViewById(R.id.bt_cir_send);
        textsend=findViewById(R.id.et_cir_send);
         dstuid=i1.getStringExtra("userid");
        fuser= FirebaseAuth.getInstance().getCurrentUser();
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg=textsend.getText().toString().trim();
                if(!msg.equals(""))
                {
                    sendMessage(fuser.getUid(),dstuid,msg);
                }
                textsend.setText("");
            }
        });
        if(fuser!=null) {
            readMessages(fuser.getUid(), dstuid);
            seenMessage(dstuid);
        }
    }
    private void seenMessage(final String userid){

        seenListener=ref2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot d:dataSnapshot.getChildren())
                {
                    Chat chat=new Chat(d.child("sender").getValue().toString(),d.child("receiver").getValue().toString(),d.child("message").getValue().toString(),Boolean.parseBoolean(d.child("isseen").getValue().toString()));
                    if(chat.getReceiver().equals(fuser.getUid()) && chat.getSender().equals(userid)){
                        HashMap<String,Object> hashMap=new HashMap<>();
                        hashMap.put("isseen",true);
                        d.getRef().updateChildren(hashMap);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void sendMessage(String sender, final String receiver, String message)
    {
        ref= FirebaseDatabase.getInstance().getReference();
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("receiver",receiver);
        hashMap.put("message",message);
        hashMap.put("isseen",false);
        ref.child("Chats").push().setValue(hashMap);
    }

    private void readMessages(final String myid, final String userid)
    {
        chatList=new ArrayList<>();

        DatabaseReference d=FirebaseDatabase.getInstance().getReference("Chats");
        d.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatList.clear();
                for(DataSnapshot ds:dataSnapshot.getChildren())
                {
                    Chat chat=new Chat(ds.child("sender").getValue().toString(),ds.child("receiver").getValue().toString(),ds.child("message").getValue().toString(),Boolean.parseBoolean(ds.child("isseen").getValue().toString()));
                    if((chat.getReceiver().equals(myid)&& chat.getSender().equals(userid)) || (chat.getReceiver().equals(userid) && chat.getSender().equals(myid)))
                    {
                        chatList.add(chat);
                    }
                }
                mAdapter = new messageAdapter(cir_company_chat.this,chatList);
                recyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void status(String status)
    {
        ref= FirebaseDatabase.getInstance().getReference("cir");
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("status",status);
        ref.updateChildren(hashMap);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
            if(fuser!=null){
            ref2.addValueEventListener(seenListener);
        status("online");}

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(fuser!=null){
            ref2.removeEventListener(seenListener);
        status("offline");}
    }
    @Override
    protected void onStop() {
        super.onStop();
        if(fuser!=null){
        ref2.removeEventListener(seenListener);
        status("offline");}
    }

}
