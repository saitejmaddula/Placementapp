package com.urbanrider.placement;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;


public class student_home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    NavigationView view;
    TextView t,r;
    static String student_regno="";
    HashMap<String,Object> ids;
    ArrayList<jobProfile>jobList;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_home);
        Toolbar toolbar = findViewById(R.id.student_home_toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.layout_student_drawerlayout);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_navigation_drawer,
                R.string.close_navigation_drawer);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        Fragment defaultFragment1 = new fragment_student_applied();
        FragmentManager fm1 = getSupportFragmentManager();
        FragmentTransaction ft1 = fm1.beginTransaction();
        ft1.replace(R.id.layout_student_frame, defaultFragment1);
        ft1.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft1.commit();

         view=findViewById(R.id.navview_student);
        view.setNavigationItemSelectedListener(this);
        view.getMenu().getItem(0).setChecked(true);
         t=view.getHeaderView(0).findViewById(R.id.tv_stmenu_letter);
         r=view.getHeaderView(0).findViewById(R.id.tv_stmenu_regno);
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Students")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                t.setText(""+dataSnapshot.child("email").getValue().toString().toUpperCase().charAt(0));
                r.setText(dataSnapshot.child("regno").getValue().toString());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        /////////////////////////////////////////
        ////////////////////////////////////////
        RelativeLayout layout =(RelativeLayout) view.getMenu().findItem(R.id.stmenu_new).getActionView();
        final TextView new_count=layout.findViewById(R.id.stmenu_new_counter);
        //get count of new jobs
        DatabaseReference d= FirebaseDatabase.getInstance().getReference("Students").child(FirebaseAuth.getInstance()
                .getCurrentUser().getUid());
        jobList=new ArrayList<>();
        d.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("applied_jobs")) {
                    ids = new HashMap<>();
                    for (DataSnapshot v : dataSnapshot.child("applied_jobs").getChildren()) {
                        ids.put(v.child("id").getValue().toString(), v.child("id").getValue().toString());
                    }
                    DatabaseReference f = FirebaseDatabase.getInstance().getReference("Company");
                    f.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            jobList.clear();
                            for (DataSnapshot c : dataSnapshot.getChildren()) {
                                if (c.hasChild("jobs")) {
                                    for (DataSnapshot a : c.child("jobs").getChildren()) {
                                        if (!ids.containsKey(a.child("id").getValue().toString())) {
                                            jobProfile jp = new jobProfile(a.child("company").getValue().toString(), a.child("role").getValue().toString(), a.child("ctc").getValue().toString(), a.child("cgpa").getValue().toString(), a.child("branch").getValue().toString(), a.child("reslink").getValue().toString(), Integer.parseInt(a.child("count").getValue().toString()), a.child("id").getValue().toString());
                                            jobList.add(jp);
                                        }
                                    }
                                }
                            }
                            if(jobList.size()>0)
                            {   new_count.setVisibility(View.VISIBLE);
                                new_count.setText(""+jobList.size());}
                                else {
                                    new_count.setVisibility(View.GONE);
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                else{
                    jobList=new ArrayList<>();
                    DatabaseReference g=FirebaseDatabase.getInstance().getReference("Company");
                    g.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            jobList.clear();
                            for(DataSnapshot s: dataSnapshot.getChildren())
                            {
                                if(s.hasChild("jobs"))
                                {
                                    for(DataSnapshot a:s.child("jobs").getChildren())
                                    {
                                        jobProfile jp = new jobProfile(a.child("company").getValue().toString(), a.child("role").getValue().toString(), a.child("ctc").getValue().toString(), a.child("cgpa").getValue().toString(), a.child("branch").getValue().toString(), a.child("reslink").getValue().toString(), Integer.parseInt(a.child("count").getValue().toString()), a.child("id").getValue().toString());
                                        jobList.add(jp);
                                    }
                                }
                            }
                            if(jobList.size()>0)
                            {   new_count.setVisibility(View.VISIBLE);
                                new_count.setText(""+jobList.size());}
                            else {
                                new_count.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }


            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }

            @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId())
        {
            case R.id.stmenu_applied:
                Fragment defaultFragment1 = new fragment_student_applied();
                FragmentManager fm1 = getSupportFragmentManager();
                FragmentTransaction ft1 = fm1.beginTransaction();
                ft1.replace(R.id.layout_student_frame, defaultFragment1);
                ft1.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft1.commit();
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.stmenu_new:
                Fragment defaultFragment = new fragment_student_new();
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.layout_student_frame, defaultFragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.commit();
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.stmenu_profile:
                Fragment defaultFragment3 = new fragment_student_profile();
                FragmentManager fm2 = getSupportFragmentManager();
                FragmentTransaction ft2 = fm2.beginTransaction();
                ft2.replace(R.id.layout_student_frame, defaultFragment3);
                ft2.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft2.commit();
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.stmenu_logout:
                FirebaseAuth.getInstance().signOut();
                Intent i=new Intent(getApplicationContext(),choose_user.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

    }
}
