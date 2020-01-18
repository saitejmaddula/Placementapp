package com.urbanrider.placement;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class jobAdapter extends RecyclerView.Adapter<jobAdapter.ViewHolder> implements Filterable {
    private Context mcontext;
    private List<jobProfile> users;
    private List<jobProfile> test;
    public  jobAdapter(Context mcontext,List<jobProfile> users)
    {
        this.mcontext=mcontext;
        this.users=users;
        test=new ArrayList<>(users);
    }

    @NonNull
    @Override
    public jobAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(mcontext).inflate(R.layout.cir_student_item,viewGroup,false);
        return new jobAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull jobAdapter.ViewHolder viewHolder, final int i) {
        jobProfile j=users.get(i);
        viewHolder.letter2.setText(""+j.getCompany().charAt(0));
        viewHolder.name2.setText(j.getCompany());
        viewHolder.role2.setText(j.getRole());
        viewHolder.cnt2.setText(""+j.getCount());
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jobProfile jp=users.get(i);
                Intent i=new Intent(mcontext,cir_student_edit_job.class);
                i.putExtra("compid",jp.getCompany());
                i.putExtra("jobid",jp.getId());
                i.putExtra("role",jp.getRole());
                i.putExtra("ctc",jp.getCtc());
                i.putExtra("cgpa",jp.getCgpa());
                i.putExtra("branch",jp.getBranch());
                i.putExtra("reslink",jp.getReslink());
                mcontext.startActivity(i);
            }
        });
        viewHolder.iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i1=new Intent(mcontext,cir_student_job_chat.class);
                jobProfile jp=users.get(i);
                i1.putExtra("dstid",jp.getId());
                mcontext.startActivity(i1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }
    private Filter exampleFilter= new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<jobProfile> filteredList = new ArrayList<>();
            if(charSequence == null || charSequence.length() == 0)
            {
                filteredList.addAll(test);
            }
            else
            {
                String filterPattern = charSequence.toString().toLowerCase().trim();
                for(jobProfile c:test)
                {
                    if(c.getCompany().toLowerCase().contains(filterPattern))
                    {
                        filteredList.add(c);
                    }
                }
            }
            FilterResults results=new FilterResults();
            results.values=filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            users.clear();
            users.addAll((List)filterResults.values);
            notifyDataSetChanged();
        }
    };

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView letter2,name2,cnt2;
        public TextView role2;
        ImageView iv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            letter2=itemView.findViewById(R.id.tv_cir_company_letter2);
            name2=itemView.findViewById(R.id.tv_cir_company_name2);
            role2=itemView.findViewById(R.id.tv_role2);
            cnt2=itemView.findViewById(R.id.count2);
            iv=itemView.findViewById(R.id.cir_student_job_chat);
        }

    }

}
