package com.urbanrider.placement;

import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class cir_student_edit_job extends AppCompatActivity {
private TextInputLayout edit_company,edit_role,edit_ctc,edit_cgpa,edit_reslink;
    private CheckBox etcse,etece,eteee,etmee,eteie;
    private Button etsave,etedit;
    private String etcompany1,etrole1,etctc1,etcgpa1,etreslink1,etbranch;
    DatabaseReference d;
    String old_company,id="",old_role,old_ctc,old_cgpa,old_reslink,old_branch,cid="";
    String temp="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cir_student_edit_job);
        edit_cgpa=findViewById(R.id.et_job_cgpa2);
        edit_company=findViewById(R.id.et_job_company2);
        edit_role=findViewById(R.id.et_job_role2);
        edit_ctc=findViewById(R.id.et_job_ctc2);
        edit_reslink=findViewById(R.id.et_job_reslink2);
        etsave=findViewById(R.id.bt_job_save2);
        etedit=findViewById(R.id.bt_job_edit2);
        etcse=findViewById(R.id.cb_cse2);
        etece=findViewById(R.id.cb_ece2);
        eteee=findViewById(R.id.cb_eee2);
        eteie=findViewById(R.id.cb_eie2);
        etmee=findViewById(R.id.cb_mee2);
        Disable();
         old_company=getIntent().getStringExtra("compid");
         id=getIntent().getStringExtra("jobid");
         old_role=getIntent().getStringExtra("role");
         old_ctc=getIntent().getStringExtra("ctc");
         old_cgpa=getIntent().getStringExtra("cgpa");
         old_reslink=getIntent().getStringExtra("reslink");
         old_branch=getIntent().getStringExtra("branch");
         cid=getIntent().getStringExtra("cid");
         edit_company.getEditText().setText(old_company);
        edit_role.getEditText().setText(old_role);
        edit_ctc.getEditText().setText(old_ctc);
        edit_cgpa.getEditText().setText(old_cgpa);
        edit_reslink.getEditText().setText(old_reslink);
        if(old_branch.contains("CSE")){etcse.setChecked(true);}
        if(old_branch.contains("ECE")){etece.setChecked(true);}
        if(old_branch.contains("EEE")){eteee.setChecked(true);}
        if(old_branch.contains("EIE")){eteie.setChecked(true);}
        if(old_branch.contains("MEE")){etmee.setChecked(true);}
        etedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Enable();
            }
        });
        etsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Disable();
                etcompany1=etcgpa1=etctc1=etreslink1=etbranch=etrole1="";
                etcompany1=edit_company.getEditText().getText().toString().trim();
                etrole1=edit_role.getEditText().getText().toString().trim();
                etctc1=edit_ctc.getEditText().getText().toString().trim();
                etcgpa1=edit_cgpa.getEditText().getText().toString().trim();
                etreslink1=edit_reslink.getEditText().getText().toString().trim();
                if(etcse.isChecked()){etbranch+=etcse.getText().toString()+",";}
                if(etece.isChecked()){etbranch+=etece.getText().toString()+",";}
                if(eteee.isChecked()){etbranch+=eteee.getText().toString()+",";}
                if(etmee.isChecked()){etbranch+=etmee.getText().toString()+",";}
                if(eteie.isChecked()){etbranch+=eteie.getText().toString()+",";}
                final HashMap<String,Object> hashMap=new HashMap<>();
                hashMap.put("company",etcompany1);
                hashMap.put("role",etrole1);
                hashMap.put("ctc",etctc1);
                hashMap.put("cgpa",etcgpa1);
                hashMap.put("branch",etbranch);
                hashMap.put("reslink",etreslink1);
              //  Toast.makeText(getApplicationContext(),id,Toast.LENGTH_SHORT).show();
                d= FirebaseDatabase.getInstance().getReference("Company");
                d.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot d:dataSnapshot.getChildren())
                        {
                            if(d.child("name").getValue().toString().equals(old_company))
                            {
                                temp=d.child("uid").getValue().toString();
                            }
                        }
                        DatabaseReference f=FirebaseDatabase.getInstance().getReference("Company").child(temp).child("jobs").child(id);
                        f.updateChildren(hashMap);
                        onBackPressed();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }
    private void Enable()
    {
        edit_company.setEnabled(true);
        edit_role.setEnabled(true);
        edit_ctc.setEnabled(true);
        edit_cgpa.setEnabled(true);
        edit_reslink.setEnabled(true);
        etcse.setEnabled(true);
        etece.setEnabled(true);
        eteee.setEnabled(true);
        eteie.setEnabled(true);
        etmee.setEnabled(true);
    }
    private void Disable()
    {
        edit_company.setEnabled(false);
        edit_role.setEnabled(false);
        edit_ctc.setEnabled(false);
        edit_cgpa.setEnabled(false);
        edit_reslink.setEnabled(false);
        etcse.setEnabled(false);
        etece.setEnabled(false);
        eteee.setEnabled(false);
        eteie.setEnabled(false);
        etmee.setEnabled(false);
    }


}
