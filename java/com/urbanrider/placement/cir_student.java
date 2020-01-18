package com.urbanrider.placement;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class cir_student extends Fragment {
    public  cir_student()
    {}
    private RecyclerView recyclerView2;
    private jobAdapter adapter;
    private List<jobProfile> userList;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_cir_student, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FloatingActionButton add;
        add=view.findViewById(R.id.fab_add_job);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getContext(),add_job.class);
                startActivity(i);
            }
        });
        recyclerView2=view.findViewById(R.id.cir_student_recview);
        recyclerView2.setHasFixedSize(true);
        recyclerView2.setLayoutManager(new LinearLayoutManager(getContext()));
        userList=new ArrayList<jobProfile>();
        adapter = new jobAdapter(getContext(),userList);
        readJobs();

    }
    private  void readJobs()
    {
        DatabaseReference dref= FirebaseDatabase.getInstance().getReference("Company");
        dref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                for(DataSnapshot d:dataSnapshot.getChildren())
                {
                    if(d.hasChild("jobs"))
                    {
                        for(DataSnapshot dd:d.child("jobs").getChildren())
                        { //Toast.makeText(getContext(),dd.child("ctc").getValue().toString(),Toast.LENGTH_SHORT).show();
                            jobProfile c=new jobProfile(dd.child("company").getValue().toString(),dd.child("role").getValue().toString(),dd.child("ctc").getValue().toString(),dd.child("cgpa").getValue().toString(),dd.child("branch").getValue().toString(),dd.child("reslink").getValue().toString(),Integer.parseInt(dd.child("count").getValue().toString()),dd.child("id").getValue().toString());
                            userList.add(c);
                        }
                    }

                }
                adapter = new jobAdapter(getContext(),userList);
                recyclerView2.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.cir_menu, menu);
        MenuItem searchItem=menu.findItem(R.id.search);
        //SearchView searchView=(SearchView)searchItem.getActionView();
        android.support.v7.widget.SearchView searchView = (android.support.v7.widget.SearchView) searchItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setQueryHint("Search...");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.getFilter().filter(s);
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.logout) {
            FirebaseAuth.getInstance().signOut();
            Intent i=new Intent(getContext(),choose_user.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            return true;
        }
        else if( id == R.id.search)
        {
            //TransitionManager.beginDelayedTransition((ViewGroup) getActivity().findViewById(R.id.cir_home_toolbar));
            MenuItemCompat.expandActionView(item);

        }

        return super.onOptionsItemSelected(item);

    }
}
