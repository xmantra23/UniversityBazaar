package org.samir.universitybazaar.Adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.samir.universitybazaar.Models.Member;
import org.samir.universitybazaar.R;

import java.util.ArrayList;

/**
 * @author Samir Shrestha
 * @description This is an adapter class for using with the recycler view inside the search member activity
 *
 */
public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.ViewHolder>{
    private ArrayList<Member> members = new ArrayList<>(); //holds the members list
    private Context context; // refers to its parent activity which is the SearchMemberActivity

    public MemberAdapter(Context context){
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_member,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //initialize the fields in the item_member.xml layout file
        holder.txtMemberName.setText("Member Name: "+ members.get(position).getMemberName());
        holder.txtMemberId.setText("Member ID: " + members.get(position).getMemberId());

        //handle clicking on the member item
        holder.parent.setOnClickListener(v->{
            // TODO: 4/5/2021 navigate to the new message activity
        });
    }

    @Override
    public int getItemCount() {
        return members.size();
    }

    public  void setMembers(ArrayList<Member> members){
        this.members = members;
        notifyDataSetChanged();
    }

    //allows the adapter class to access the elements inside the layout file item_member.xml
    public class ViewHolder extends RecyclerView.ViewHolder{
        private RelativeLayout parent;
        private TextView txtMemberName, txtMemberId;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            parent = itemView.findViewById(R.id.parent);
            txtMemberName = itemView.findViewById(R.id.txtMemberName);
            txtMemberId = itemView.findViewById(R.id.txtMemberId);
        }
    }
}
