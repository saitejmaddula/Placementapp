package com.urbanrider.placement;

import android.app.ActivityOptions;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Vibrator;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class choose_user extends AppCompatActivity {
ImageView student,cir,company,admin,drag;
TextView wel;
private int longClickDuration=50;
private boolean isLongPress=false;
    private Toolbar mTopToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_user);
        wel=findViewById(R.id.welcome);
        student=findViewById(R.id.student);
        cir=findViewById(R.id.cir);
        company=findViewById(R.id.company);
        drag=findViewById(R.id.drag);
        mTopToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(mTopToolbar);
        drag.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.drag));
      //  student.setOnLongClickListener(longClickListener);
        //cir.setOnLongClickListener(longClickListener);
        //company.setOnLongClickListener(longClickListener);
        student.setOnTouchListener(touchListener);
        cir.setOnTouchListener(touchListener);
        company.setOnTouchListener(touchListener);
        drag.setOnDragListener(dragListener);
    }
    /*View.OnLongClickListener longClickListener=new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            ClipData clipData= ClipData.newPlainText("","");
            View.DragShadowBuilder shadowBuilder=new View.DragShadowBuilder(v);
            v.startDrag(clipData,shadowBuilder,v,0);
            return true;
        }
    };*/
    View.OnTouchListener touchListener= new View.OnTouchListener() {
        @Override
        public boolean onTouch(final View v, MotionEvent motionEvent) {
            if(motionEvent.getAction()==MotionEvent.ACTION_DOWN){
                isLongPress=true;
                Handler handler=new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(isLongPress)
                        {
                           Vibrator vibrator=(Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
                           vibrator.vibrate(100);
                            ClipData clipData= ClipData.newPlainText("","");
                            View.DragShadowBuilder shadowBuilder=new View.DragShadowBuilder(v);
                            v.startDrag(clipData,shadowBuilder,v,0);
                        }
                    }
                },longClickDuration);
            }
            else if(motionEvent.getAction()==MotionEvent.ACTION_UP)
            {
                isLongPress=false;
            }
            return true;
        }
    };

    View.OnDragListener dragListener=new View.OnDragListener() {

        @Override
        public boolean onDrag(View v, DragEvent event) {
            int dragEvent=event.getAction();
            final View view=(View)event.getLocalState();
            Handler handler=new Handler();
            switch (dragEvent)
            {
                case DragEvent.ACTION_DRAG_ENTERED:
                    break;
                case DragEvent.ACTION_DRAG_EXITED:break;
                case DragEvent.ACTION_DROP:
                    switch (view.getId()) {
                        case R.id.student:   drag.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.graduate));
                        wel.setText("Student");
                        handler.postDelayed(new Runnable() {
                            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                            @Override
                            public void run() {
                                if(wel.getText().toString().equals("Student")) {
                                    Intent intent = new Intent(choose_user.this, login_student.class);
                                    Pair[] pairs = new Pair[1];
                                    pairs[0] = new Pair<View, String>(drag, "icon_transition");
                                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(choose_user.this, pairs);
                                    startActivity(intent, options.toBundle());
                                }
                            }
                        },320);
                        break;
                        case R.id.company:   drag.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.office));
                            wel.setText("Company");
                            handler.postDelayed(new Runnable() {
                                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                                @Override
                                public void run() {
                                    if(wel.getText().toString().equals("Company")) {
                                        Intent intent = new Intent(choose_user.this, company_login.class);
                                        Pair[] pairs = new Pair[1];
                                        pairs[0] = new Pair<View, String>(drag, "icon_transition");
                                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(choose_user.this, pairs);
                                        startActivity(intent, options.toBundle());
                                    }
                                }
                            },320);
                        break;
                        case R.id.cir:   drag.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.teamwork));
                            wel.setText("CIR");
                            handler.postDelayed(new Runnable() {
                                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                                @Override
                                public void run() {
                                    if(wel.getText().toString().equals("CIR")) {
                                        Intent intent = new Intent(choose_user.this, cir_login.class);
                                        Pair[] pairs = new Pair[1];
                                        pairs[0] = new Pair<View, String>(drag, "icon_transition");
                                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(choose_user.this, pairs);
                                        startActivity(intent, options.toBundle());
                                    }
                                }
                            },320);
                        break;

                    }
                    break;
            }
            return true;
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        drag.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.drag));
    }
}
