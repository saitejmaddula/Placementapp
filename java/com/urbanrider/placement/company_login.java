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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class company_login extends AppCompatActivity {
    private EditText company_username,company_pass;
    private Button company_login;
    FirebaseAuth mAuth;
    private String username,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_login);
        company_username=findViewById(R.id.et_company_username);
        company_pass=findViewById(R.id.et_company_password);
        company_login=findViewById(R.id.bt_company_login);
        mAuth=FirebaseAuth.getInstance();
        company_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username=company_username.getText().toString().trim();
                password=company_pass.getText().toString().trim();
                FirebaseDatabase.getInstance().getReference().child("Company").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot d:dataSnapshot.getChildren())
                        {
                            if(d.child("email").getValue().toString().equals(company_username.getText().toString()))
                            {
                                mAuth.signInWithEmailAndPassword(username,password)
                                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull final Task<AuthResult> task) {

                                                    Intent intent = new Intent(company_login.this, company_home.class);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    startActivity(intent);


                                            }
                                        });
                                break;
                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });
    }
}
