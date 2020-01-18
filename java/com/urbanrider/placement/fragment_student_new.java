package com.urbanrider.placement;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class fragment_student_new extends Fragment {
    private RecyclerView recyclerView2;
    private student_new_adapter adapter;
    private List<jobProfile> jobList;
    HashMap<String,String> ids;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_student_new,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView2=view.findViewById(R.id.student_new_recview);
        recyclerView2.setHasFixedSize(true);
        recyclerView2.setLayoutManager(new LinearLayoutManager(getContext()));
        jobList=new ArrayList<jobProfile>();
        adapter = new student_new_adapter(getContext(),jobList);
        getJobs();

    }
    private void getJobs()
    {
        DatabaseReference d= FirebaseDatabase.getInstance().getReference("Students").child(FirebaseAuth.getInstance()
                                .getCurrentUser().getUid());
        d.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("applied_jobs"))
                {
                     ids=new HashMap<>();
                    for(DataSnapshot v:dataSnapshot.child("applied_jobs").getChildren())
                    {
                        ids.put(v.child("id").getValue().toString(),v.child("id").getValue().toString());
                    }
                    DatabaseReference f=FirebaseDatabase.getInstance().getReference("Company");
                    f.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            jobList.clear();
                            for(DataSnapshot c:dataSnapshot.getChildren())
                            {
                                if(c.hasChild("jobs"))
                                {
                                    for(DataSnapshot a:c.child("jobs").getChildren())
                                    {
                                        if(!ids.containsKey(a.child("id").getValue().toString()))
                                        {
                                            jobProfile jp=new jobProfile(a.child("company").getValue().toString(),a.child("role").getValue().toString(),a.child("ctc").getValue().toString(),a.child("cgpa").getValue().toString(),a.child("branch").getValue().toString(),a.child("reslink").getValue().toString(),Integer.parseInt(a.child("count").getValue().toString()),a.child("id").getValue().toString());
                                            jobList.add(jp);
                                        }
                                    }
                                }
                            }
                            adapter = new student_new_adapter(getContext(),jobList);
                            recyclerView2.setAdapter(adapter);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                else
                {

                    DatabaseReference f=FirebaseDatabase.getInstance().getReference("Company");
                    f.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            jobList.clear();
                            for(DataSnapshot c:dataSnapshot.getChildren())
                            {
                                if(c.hasChild("jobs"))
                                {
                                    for(DataSnapshot a:c.child("jobs").getChildren())
                                    {

                                            jobProfile jp=new jobProfile(a.child("company").getValue().toString(),a.child("role").getValue().toString(),a.child("ctc").getValue().toString(),a.child("cgpa").getValue().toString(),a.child("branch").getValue().toString(),a.child("reslink").getValue().toString(),Integer.parseInt(a.child("count").getValue().toString()),a.child("id").getValue().toString());
                                            jobList.add(jp);

                                    }
                                }
                            }
                            adapter = new student_new_adapter(getContext(),jobList);
                            recyclerView2.setAdapter(adapter);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
              //  jobProfile j=new jobProfile("Cisco","IT","9","9.5","CSE","asd",0,"as");
                //jobList.add(j);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
