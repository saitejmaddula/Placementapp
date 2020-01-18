package com.urbanrider.placement;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class fragment_student_profile extends Fragment {
    private EditText newpass,oldpass;
    private Button passupdate;
    FirebaseUser user;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_student_profile_layout,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        newpass=view.findViewById(R.id.et_new_student_password);
        oldpass=view.findViewById(R.id.et_old_student_password);
        passupdate=view.findViewById(R.id.bt_student_updatepassword);
        user=FirebaseAuth.getInstance().getCurrentUser();
        passupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!oldpass.getText().toString().equals("") && !newpass.getText().toString().equals("") )
                {
                    AuthCredential credential= EmailAuthProvider.getCredential(user.getEmail(),oldpass.getText().toString().trim());
                   // Toast.makeText(getContext(),newpass.getText().toString(),Toast.LENGTH_SHORT).show();
                    final String newp=newpass.getText().toString().trim();
                    user.reauthenticate(credential)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        user.updatePassword(newp)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(getContext(), "Password update successful!!", Toast.LENGTH_SHORT).show();
                                                            FirebaseDatabase.getInstance().getReference().child("Students")
                                                                    .child(user.getUid()).child("password").setValue(newp);

                                                            oldpass.setText("");
                                                        }
                                                    }
                                                });
                                    }
                                }
                            });

                }

                newpass.setText("");
            }
        });
    }
}
