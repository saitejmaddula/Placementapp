package com.urbanrider.placement;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
TextView st;
FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        st=findViewById(R.id.start);
        user=FirebaseAuth.getInstance().getCurrentUser();


            st.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this, choose_user.class));
                }
            });

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(user!=null && user.getEmail().equals("cir@gmail.com"))
        {
            Intent i1=new Intent(MainActivity.this,cir_home.class);
            i1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i1);
            finish();
        }

        else if(user!=null && user.getEmail().equals("cisco@gmail.com")){
            Intent i1=new Intent(MainActivity.this,company_home.class);
            i1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i1);
            finish();
        }
        else if(user!=null && user.getEmail().contains("addepalli"))
        {
            Intent i1=new Intent(MainActivity.this,student_home.class);
            i1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i1);
            finish();
        }
    }
}
