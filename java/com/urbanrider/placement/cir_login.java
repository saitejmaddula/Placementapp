package com.urbanrider.placement;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class cir_login extends AppCompatActivity {
private EditText cir_username,cir_pass;
private Button cir_login;
FirebaseAuth mAuth;
private String username,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cir_login);
        cir_username=findViewById(R.id.et_cir_username);
        cir_pass=findViewById(R.id.et_cir_password);
        cir_login=findViewById(R.id.bt_cir_login);
        mAuth=FirebaseAuth.getInstance();
        cir_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username=cir_username.getText().toString().trim();
                password=cir_pass.getText().toString().trim();
               FirebaseDatabase.getInstance().getReference().child("cir").child("email").addListenerForSingleValueEvent(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                       if(dataSnapshot.getValue().toString().equals(username))
                       {

                           mAuth.signInWithEmailAndPassword(username,password)
                                   .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                       @Override
                                       public void onComplete(@NonNull Task<AuthResult> task) {

                                           Intent intent = new Intent(cir_login.this, cir_home.class);
                                           intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                           startActivity(intent);


                                       }
                                   });
                       }
                   }

                   @Override
                   public void onCancelled(@NonNull DatabaseError databaseError) {

                   }
               });


                            cir_username.setText("");
                            cir_pass.setText("");

                    }




        });

    }
}
