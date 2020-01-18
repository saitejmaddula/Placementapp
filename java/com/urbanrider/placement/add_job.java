package com.urbanrider.placement;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class add_job extends AppCompatActivity {
private TextInputLayout company,role,ctc,cgpa,reslink;
private CheckBox cse,ece,eee,mee,eie;
private Button save;
private String company1,role1,ctc1,cgpa1,reslink1,branch;
    String key="";
DatabaseReference f;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_job);
        save=findViewById(R.id.bt_job_save);
        company=findViewById(R.id.et_job_company);
        role=findViewById(R.id.et_job_role);
        ctc=findViewById(R.id.et_job_ctc);
        cgpa=findViewById(R.id.et_job_cgpa);
        cse=findViewById(R.id.cb_cse);
        ece=findViewById(R.id.cb_ece);
        eee=findViewById(R.id.cb_eee);
        eie=findViewById(R.id.cb_eie);
        mee=findViewById(R.id.cb_mee);
        reslink=findViewById(R.id.et_job_reslink);
        branch="";
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                company1=company.getEditText().getText().toString().trim();
                role1=role.getEditText().getText().toString().trim();
                ctc1=ctc.getEditText().getText().toString().trim();
                cgpa1=cgpa.getEditText().getText().toString().trim();
                reslink1=reslink.getEditText().getText().toString().trim();
                if(cse.isChecked()){branch+=cse.getText().toString()+",";}
                if(ece.isChecked()){branch+=ece.getText().toString()+",";}
                if(eee.isChecked()){branch+=eee.getText().toString()+",";}
                if(mee.isChecked()){branch+=mee.getText().toString()+",";}
                if(eie.isChecked()){branch+=eie.getText().toString()+",";}

                final HashMap<String,Object> hashMap=new HashMap<>();
                hashMap.put("company",company1);
                hashMap.put("role",role1);
                hashMap.put("ctc",ctc1);
                hashMap.put("cgpa",cgpa1);
                hashMap.put("branch",branch);
                hashMap.put("reslink",reslink1);
                hashMap.put("count",0);
                f= FirebaseDatabase.getInstance().getReference("Company");
                f.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot ds:dataSnapshot.getChildren())
                        {
                            if(company1.equals(ds.child("name").getValue().toString()))
                            {
                              key=ds.child("uid").getValue().toString();
                            }
                        }
                        String k=f.child(key).child("jobs").push().getKey();
                        hashMap.put("id",k);
                        f.child(key).child("jobs").child(k).setValue(hashMap);
                        Intent i=new Intent(add_job.this,cir_home.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }
}
