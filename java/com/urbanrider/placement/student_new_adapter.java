package com.urbanrider.placement;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class student_new_adapter extends RecyclerView.Adapter<student_new_adapter.ViewHolder> {
    private Context mcontext;
    private List<jobProfile> newJobs;
    jobProfile j;
    public  student_new_adapter(Context mcontext,List<jobProfile> newJobs)
    {
        this.mcontext=mcontext;
        this.newJobs=newJobs;
    }

    @NonNull
    @Override
    public student_new_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(mcontext).inflate(R.layout.student_new_item,viewGroup,false);
        return new student_new_adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final student_new_adapter.ViewHolder viewHolder, final int i) {
         j=newJobs.get(i);
        viewHolder.letter.setText(""+j.getCompany().charAt(0));
        viewHolder.name.setText(j.getCompany());
        viewHolder.job.setText(j.getRole());
        String id= FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference f2= FirebaseDatabase.getInstance().getReference("Students").child(id);
        f2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                j=newJobs.get(i);
                double myCgpa=Double.parseDouble(dataSnapshot.child("cgpa").getValue().toString());
                double reqCgpa=Double.parseDouble(j.getCgpa());
                if(myCgpa<reqCgpa)
                {
                    viewHolder.img.setImageDrawable(ContextCompat.getDrawable(mcontext,R.drawable.not_eligible));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                j=newJobs.get(i);
                Intent i=new Intent(mcontext,student_apply_for_job.class);
                if(viewHolder.img.getDrawable().getConstantState() == mcontext.getResources().getDrawable(R.drawable.eligible).getConstantState())
                {
                    i.putExtra("isEligible","yes");
                }
                else
                {
                    i.putExtra("isEligible","no");
                }
                i.putExtra("company",j.getCompany());
                i.putExtra("jobid",j.getId());
                i.putExtra("role",j.getRole());
               // Toast.makeText(mcontext,j.getRole(),Toast.LENGTH_SHORT).show();
                i.putExtra("ctc",j.getCtc());
                i.putExtra("cgpa",j.getCgpa());
                i.putExtra("branch",j.getBranch());
                i.putExtra("onlyInfo","no");
                mcontext.startActivity(i);
            }
        });
        
    }

    @Override
    public int getItemCount() {
        return newJobs.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView letter,name,job;
        public ImageView img;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            letter=itemView.findViewById(R.id.tv_student_new_company_letter);
            name=itemView.findViewById(R.id.tv_student_new_company_name);
            job=itemView.findViewById(R.id.tv_student_new_job);
            img=itemView.findViewById(R.id.iv_student_eligible);;
        }
    }

}
