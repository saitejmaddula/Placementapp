package com.urbanrider.placement;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;



public class student_apply_for_job extends AppCompatActivity {
private String company,role,ctc,cgpa,branch,isEligibile,jobid;
private TextView tvcompany,tvrole,tvctc,tvcgpa,tvbranch;
private Button apply;
String compid,compcount;
HashMap<String,Object> hashMap;
DatabaseReference f;
Toolbar tb;
    int count;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_student_apply_for_job);
        tb=findViewById(R.id.student_apply_new_toolbar);
        setSupportActionBar(tb);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.ic_arrow_back_black_24dp);
        upArrow.setColorFilter(getResources().getColor(R.color.blue2), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        tb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        tvcompany=findViewById(R.id.tv_info_company);
        tvrole=findViewById(R.id.tv_info_role);
        tvctc=findViewById(R.id.tv_info_ctc);
        tvcgpa=findViewById(R.id.tv_info_cgpa);
        tvbranch=findViewById(R.id.tv_info_branch);
        apply=findViewById(R.id.bt_student_applyfordrive);
        company=getIntent().getStringExtra("company");
        role=getIntent().getStringExtra("role");

        ctc=getIntent().getStringExtra("ctc");
        cgpa=getIntent().getStringExtra("cgpa");
        branch=getIntent().getStringExtra("branch");
        isEligibile=getIntent().getStringExtra("isEligible");
        jobid=getIntent().getStringExtra("jobid");
      ////////////////////////////////

        tvcompany.setText(company);
        tvrole.setText(role);
        tvctc.setText(ctc);
        tvcgpa.setText(cgpa);
        tvbranch.setText(branch);
        String onlyInfo=getIntent().getStringExtra("onlyInfo");
        if(onlyInfo.equals("yes"))
        {
            apply.setVisibility(View.GONE);
        }
        else if(isEligibile.equals("no"))
        {
            apply.setBackgroundColor(0x99FC440A);
            apply.setEnabled(false);
            apply.setText("Not Eligible");
            apply.setTextColor(getResources().getColor(R.color.orange));
        }
        else if(isEligibile.equals("yes"))
        {
            apply.setEnabled(true);
            apply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Company");
                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot s:dataSnapshot.getChildren())
                            {
                                if(s.child("name").getValue().toString().equals(company))
                                {
                                    compid=s.child("uid").getValue().toString();
                                    compcount=s.child("jobs").child(jobid).child("count").getValue().toString();
                                    count=Integer.parseInt(compcount);
                                    count+=1;
                                }
                            }
                            hashMap=new HashMap<>();
                            hashMap.put("count",count);
                            f=FirebaseDatabase.getInstance().getReference("Company").child(compid).child("jobs").child(jobid);
                            f.getRef().updateChildren(hashMap);
                            hashMap=new HashMap<>();
                            DatabaseReference d=FirebaseDatabase.getInstance().getReference("Students").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                    d.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            hashMap.clear();
                                            hashMap.put(FirebaseAuth.getInstance().getCurrentUser().getUid(),dataSnapshot.child("regno").getValue().toString());
                                            f=FirebaseDatabase.getInstance().getReference("Company").child(compid).child("jobs").child(jobid).child("applied_students");
                                            f.setValue(hashMap);
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });

                            f=FirebaseDatabase.getInstance().getReference("Students").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .child("applied_jobs").child(jobid);
                            hashMap=new HashMap<>();
                            hashMap.put("id",jobid);
                            hashMap.put("company",company);
                            hashMap.put("role",role);
                            hashMap.put("ctc",ctc);
                            hashMap.put("cgpa",cgpa);
                            hashMap.put("branch",branch);
                            f.setValue(hashMap);
                            Intent i=new Intent(student_apply_for_job.this,student_home.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
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
}
