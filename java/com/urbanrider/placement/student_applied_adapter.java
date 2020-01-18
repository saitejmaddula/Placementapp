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

public class student_applied_adapter extends RecyclerView.Adapter<student_applied_adapter.ViewHolder> {
    private Context mcontext;
    private List<jobProfile> newJobs;
    jobProfile j;
    public  student_applied_adapter(Context mcontext,List<jobProfile> newJobs)
    {
        this.mcontext=mcontext;
        this.newJobs=newJobs;
    }

    @NonNull
    @Override
    public student_applied_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(mcontext).inflate(R.layout.student_applied_item,viewGroup,false);
        return new student_applied_adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final student_applied_adapter.ViewHolder viewHolder, final int i) {
        j=newJobs.get(i);
        viewHolder.letter2.setText(""+j.getCompany().charAt(0));
        viewHolder.name2.setText(j.getCompany());
        viewHolder.job2.setText(j.getRole());
        String id= FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference f= FirebaseDatabase.getInstance().getReference("Students").child(id);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                j=newJobs.get(i);
                Intent i=new Intent(mcontext,student_apply_for_job.class);
                i.putExtra("onlyInfo","yes");
                i.putExtra("company",j.getCompany());
                i.putExtra("jobid",j.getId());
                i.putExtra("role",j.getRole());
                // Toast.makeText(mcontext,j.getRole(),Toast.LENGTH_SHORT).show();
                i.putExtra("ctc",j.getCtc());
                i.putExtra("cgpa",j.getCgpa());
                i.putExtra("branch",j.getBranch());
                mcontext.startActivity(i);
            }
        });
        viewHolder.img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i1=new Intent(mcontext,student_applied_job_chat.class);
                j=newJobs.get(i);
                i1.putExtra("dstid",j.getId());
                mcontext.startActivity(i1);
            }
        });

    }

    @Override
    public int getItemCount() {
        return newJobs.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView letter2,name2,job2;
        public ImageView img2;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            letter2=itemView.findViewById(R.id.tv_student_applied_company_letter);
            name2=itemView.findViewById(R.id.tv_student_applied_company_name);
            job2=itemView.findViewById(R.id.tv_student_applied_job);
            img2=itemView.findViewById(R.id.iv_student_applied_chat);;
        }
    }

}
