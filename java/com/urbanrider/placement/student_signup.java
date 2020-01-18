package com.urbanrider.placement;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.HashMap;

public class student_signup extends AppCompatActivity {
private TextView regno,email,dept,phone,cgpa,arrears,password;
private String regno1,email1,dept1,phone1,cgpa1,arrears1,password1;
private Button signup;
FirebaseAuth auth;
DatabaseReference ref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         ref = FirebaseDatabase.getInstance().getReference("Students");
        setContentView(R.layout.activity_student_signup);
        regno=findViewById(R.id.et_signup_regno);
        email=findViewById(R.id.et_signup_email);
        dept=findViewById(R.id.et_signup_dept);
        phone=findViewById(R.id.et_signup_phone);
        cgpa=findViewById(R.id.et_signup_cgpa);
        arrears=findViewById(R.id.et_signup_arrears);
        password=findViewById(R.id.et_signup_password);
        signup=findViewById(R.id.bt_signup_signup);
        auth=FirebaseAuth.getInstance();
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                regno1=regno.getText().toString().trim();
                email1=email.getText().toString().trim();
                dept1=dept.getText().toString().trim();
                phone1=phone.getText().toString().trim();
                cgpa1=cgpa.getText().toString().trim();
                arrears1=arrears.getText().toString().trim();
                password1=password.getText().toString().trim();
                //Toast.makeText(getApplicationContext(),regno1+password1,Toast.LENGTH_SHORT).show();
                auth.createUserWithEmailAndPassword(email1,password1)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                    final HashMap<String, Object> hashMap = new HashMap<>();
                                    hashMap.put("regno", regno1);
                                    hashMap.put("email", email1);
                                    hashMap.put("dept", dept1);
                                    hashMap.put("phone", phone1);
                                    hashMap.put("cgpa", cgpa1);
                                    hashMap.put("arrears", arrears1);
                                    hashMap.put("password", password1);
                                    FirebaseUser user=task.getResult().getUser();
                                    final String id = user.getUid();
                                    hashMap.put("id", id);
                                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            ref.child(id).setValue(hashMap);
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                    Intent intent = new Intent(student_signup.this, login_student.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }
                        });

            }
        });
    }
}
