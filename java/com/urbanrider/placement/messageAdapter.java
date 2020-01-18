package com.urbanrider.placement;

import android.content.Context;
import android.content.Intent;
import android.opengl.Visibility;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class messageAdapter extends RecyclerView.Adapter<messageAdapter.ViewHolder>{
    private Context mcontext;
    private List<Chat> chats;
    public static final int MSG_TYPE_LEFT=0;
    public static final int MSG_TYPE_RIGHT=1;
    FirebaseUser firebaseUser;

    public  messageAdapter(Context c,List<Chat> u)
    {
        mcontext=c;
        chats=u;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if(i==MSG_TYPE_RIGHT){
        View view= LayoutInflater.from(mcontext).inflate(R.layout.chat_item_right,viewGroup,false);
        return new messageAdapter.ViewHolder(view);}
        else{
            View view= LayoutInflater.from(mcontext).inflate(R.layout.chat_item_left,viewGroup,false);
            return new messageAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Chat ch=chats.get(i);
        viewHolder.msg.setText(ch.getMessage());
        if(i==chats.size()-1 && !ch.getSender().equals("cir"))
        {   if(ch.isseen)
                viewHolder.seen.setText("Read");
            else
                viewHolder.seen.setText("Delivered");
        }
        else {
            viewHolder.seen.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView msg;
        public TextView seen;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
           msg=itemView.findViewById(R.id.show_message);
            seen=itemView.findViewById(R.id.text_seen);
        }
    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        if(chats.get(position).getSender().equals(firebaseUser.getUid())||chats.get(position).getSender().equals("cir"))
            return MSG_TYPE_RIGHT;
        else
            return MSG_TYPE_LEFT;
    }
}
