package com.urbanrider.placement;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class login_student extends AppCompatActivity {
TextView signup;
FirebaseAuth auth;
private EditText stu_username,stu_password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_student);
       Button b=findViewById(R.id.bt_login);
       signup=findViewById(R.id.tv_student_signup);
       stu_username=findViewById(R.id.et_stu_username);
       stu_password=findViewById(R.id.et_stu_password);
       auth=FirebaseAuth.getInstance();
       b.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               FirebaseDatabase.getInstance().getReference().child("Students").addListenerForSingleValueEvent(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                       for(DataSnapshot d:dataSnapshot.getChildren())
                       {
                           if(d.child("email").getValue().toString().equals(stu_username.getText().toString()) && d.child("password").getValue().toString().equals(stu_password.getText().toString()))
                           {
                               auth.signInWithEmailAndPassword(stu_username.getText().toString().trim(),stu_password.getText().toString().trim())
                                       .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                           @Override
                                           public void onComplete(@NonNull final Task<AuthResult> task) {
                                               if (task.isSuccessful()) {
                                                   Intent i = new Intent(login_student.this, student_home.class);
                                                   i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                   startActivity(i);

                                               }

                                           }
                                       });
                               break;
                           }
                       }

                       stu_username.setText("");
                       stu_password.setText("");

                   }

                   @Override
                   public void onCancelled(@NonNull DatabaseError databaseError) {

                   }
               });

           }
       });
       signup.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent i= new Intent(login_student.this,student_signup.class);
               startActivity(i);
           }
       });
    }
}
