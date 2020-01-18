package com.urbanrider.placement;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
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

public class company_home extends AppCompatActivity {
    FirebaseUser fuser1;
    DatabaseReference ref3,ref4;
    ImageButton send;
    EditText textsend;
    String dstuid1;
    messageAdapter mAdapter1;
    List<Chat> chatList1;
    RecyclerView recyclerView1;
    ValueEventListener seenListener2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_home);
        final String comp_dst_id = this.getResources().getString(R.string.company_msg_dst_id);
        ref4=FirebaseDatabase.getInstance().getReference("Chats");
        recyclerView1=findViewById(R.id.company_chat_recview);
        recyclerView1.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView1.setLayoutManager(linearLayoutManager);
        Toolbar mTopToolbar = (Toolbar) findViewById(R.id.company_chat_toolbar);
        setSupportActionBar(mTopToolbar);
        mTopToolbar.setTitle("CIR");
        send=findViewById(R.id.bt_company_send);
        textsend=findViewById(R.id.et_company_send);
        dstuid1=comp_dst_id;
        fuser1= FirebaseAuth.getInstance().getCurrentUser();
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg=textsend.getText().toString().trim();
                if(!msg.equals(""))
                {
                    sendMessage(fuser1.getUid(),dstuid1,msg);
                }
                textsend.setText("");
            }
        });
            if(fuser1!=null) {
                readMessages(fuser1.getUid(), dstuid1);
                seenMessage2(dstuid1);
            }
    }
    private void seenMessage2(final String userid2){

       seenListener2=ref4.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot d2:dataSnapshot.getChildren())
                {
                    Chat chat=new Chat(d2.child("sender").getValue().toString(),d2.child("receiver").getValue().toString(),d2.child("message").getValue().toString(),Boolean.parseBoolean(d2.child("isseen").getValue().toString()));
                    if(chat.getReceiver().equals(fuser1.getUid()) && chat.getSender().equals(userid2)){
                        HashMap<String,Object> hashMap=new HashMap<>();
                        hashMap.put("isseen",true);
                        d2.getRef().updateChildren(hashMap);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private void sendMessage(String sender,String receiver,String message) {

            ref3 = FirebaseDatabase.getInstance().getReference();
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("sender", sender);
            hashMap.put("receiver", receiver);
            hashMap.put("message", message);
            hashMap.put("isseen",false);
            ref3.child("Chats").push().setValue(hashMap);

    }
    private void readMessages(final String myid, final String userid)
    {
        chatList1=new ArrayList<>();

        DatabaseReference d=FirebaseDatabase.getInstance().getReference("Chats");
        d.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatList1.clear();
                for(DataSnapshot ds:dataSnapshot.getChildren())
                {
                    Chat chat=new Chat(ds.child("sender").getValue().toString(),ds.child("receiver").getValue().toString(),ds.child("message").getValue().toString(),Boolean.parseBoolean(ds.child("isseen").getValue().toString()));
                    if((chat.getReceiver().equals(myid)&& chat.getSender().equals(userid)) || (chat.getReceiver().equals(userid) && chat.getSender().equals(myid)))
                    {
                        chatList1.add(chat);
                    }
                }
                mAdapter1 = new messageAdapter(company_home.this,chatList1);
                recyclerView1.setAdapter(mAdapter1);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.comp_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.logout_comp) {

            FirebaseAuth.getInstance().signOut();
            Intent i=new Intent(company_home.this,choose_user.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void status(String status) {
        if (fuser1 != null) {
            ref3 = FirebaseDatabase.getInstance().getReference("Company").child(fuser1.getUid());
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("status", status);
            ref3.updateChildren(hashMap);
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if(fuser1!=null){
        ref4.addValueEventListener(seenListener2);
        status("online");}
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(fuser1!=null){
        ref4.removeEventListener(seenListener2);
        status("offline");}
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (fuser1!= null) {
            ref4.removeEventListener(seenListener2);
            status("offline");
        }
    }
}
