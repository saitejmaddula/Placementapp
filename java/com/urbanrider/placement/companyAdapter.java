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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class companyAdapter extends RecyclerView.Adapter<companyAdapter.ViewHolder> implements Filterable {
    private Context mcontext;
    private List<cirCompanyUser> users;
    private List<cirCompanyUser> test;
    private boolean ischat;
    String lastMsg;
    int c;
    FirebaseUser firebaseUser;
    public  companyAdapter(Context mcontext,List<cirCompanyUser> users,boolean ischat)
    {
        this.mcontext=mcontext;
        this.users=users;
        this.ischat=ischat;
        test=new ArrayList<>(users);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(mcontext).inflate(R.layout.cir_company_item,viewGroup,false);
        return new companyAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final cirCompanyUser companyUser=users.get(i);

        viewHolder.letter.setText(""+companyUser.getName().charAt(0));
        viewHolder.name.setText(companyUser.getName());
        if(ischat)
        {
            lastMessage(companyUser.getUid(),viewHolder.last_msg,viewHolder.cnt);
            if(companyUser.getStatus().equals("online"))
            {
                viewHolder.on.setVisibility(View.VISIBLE);
                viewHolder.off.setVisibility(View.GONE);
            }
            else
            {
                viewHolder.on.setVisibility(View.GONE);
                viewHolder.off.setVisibility(View.VISIBLE);
            }
        }
        else
        {
            viewHolder.on.setVisibility(View.GONE);
            viewHolder.off.setVisibility(View.GONE);
            viewHolder.last_msg.setVisibility(View.GONE);
        }
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent a=new Intent(mcontext,cir_company_chat.class);
                a.putExtra("userid",companyUser.getUid());
                a.putExtra("name",companyUser.getName());
                mcontext.startActivity(a);
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
           List<cirCompanyUser> filteredList = new ArrayList<>();
           if(charSequence == null || charSequence.length() == 0)
           {
               filteredList.addAll(test);
           }
           else
           {
               String filterPattern = charSequence.toString().toLowerCase().trim();
               for(cirCompanyUser c:test)
               {
                   if(c.getName().toLowerCase().contains(filterPattern))
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

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView letter,name,cnt;
        public ImageView on,off;
        public TextView last_msg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            letter=itemView.findViewById(R.id.tv_cir_company_letter);
            name=itemView.findViewById(R.id.tv_cir_company_name);
            on=itemView.findViewById(R.id.online_indicator);
            off=itemView.findViewById(R.id.offline_indicator);
            last_msg=itemView.findViewById(R.id.tv_last_msg);
            cnt=itemView.findViewById(R.id.count);
        }
    }
    private void lastMessage(final String userid, final TextView last, final TextView tvc) {
        lastMsg="default";
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference dref= FirebaseDatabase.getInstance().getReference("Chats");
        dref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                c=0;
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){

                    Chat chat=new Chat(snapshot.child("sender").getValue().toString(),snapshot.child("receiver").getValue().toString(),snapshot.child("message").getValue().toString(),Boolean.parseBoolean(snapshot.child("isseen").getValue().toString()));
                    if((chat.getReceiver().equals(firebaseUser.getUid()) && chat.getSender().equals(userid)) || (chat.getReceiver().equals(userid) && chat.getSender().equals(firebaseUser.getUid())))
                    {
                        lastMsg=chat.getMessage();
                        if((chat.getReceiver().equals(firebaseUser.getUid()) && chat.getSender().equals(userid)))
                        {   if(!chat.isIsseen()) {
                                c++;
                            }
                        }
                    }

                }
                if(c>0){tvc.setText(""+c);tvc.setVisibility(View.VISIBLE);}
                else{tvc.setText("");tvc.setVisibility(View.GONE);}
                switch (lastMsg){
                    case "default":
                        last.setText("No messages yet. Start chat!");
                        break;
                     default:
                         last.setText(lastMsg);
                         break;
                }

                lastMsg="default";
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
