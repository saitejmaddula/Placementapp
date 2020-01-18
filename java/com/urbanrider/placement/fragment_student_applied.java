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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class fragment_student_applied extends Fragment {
    private RecyclerView recyclerView2;
    private student_applied_adapter adapter;
    private List<jobProfile> jobList;
    HashMap<String,String> ids;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_student_applied,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView2=view.findViewById(R.id.student_applied_recview);
        recyclerView2.setHasFixedSize(true);
        recyclerView2.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new student_applied_adapter(getContext(),jobList);
        getAppliedJobs();
    }
    private void getAppliedJobs() {
        jobList=new ArrayList<jobProfile>();
        DatabaseReference f= FirebaseDatabase.getInstance().getReference("Students")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("applied_jobs");
        f.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                jobList.clear();
                for(DataSnapshot v:dataSnapshot.getChildren())
                {
                    jobProfile jp=new jobProfile(v.child("company").getValue().toString(),v.child("role").getValue().toString(),v.child("ctc").getValue().toString(),v.child("cgpa").getValue().toString(),v.child("branch").getValue().toString(),"",0,v.child("id").getValue().toString());
                    jobList.add(jp);
                }
                adapter = new student_applied_adapter(getContext(),jobList);
                recyclerView2.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
