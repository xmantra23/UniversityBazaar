package org.samir.universitybazaar.Adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.samir.universitybazaar.Activity.Messages.NewMessageActivity;
import org.samir.universitybazaar.Models.Member;
import org.samir.universitybazaar.R;
import org.samir.universitybazaar.Utility.Constants;

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
        String memberName = members.get(position).getMemberName();
        String memberId = members.get(position).getMemberId();
        holder.txtMemberName.setText("Member Name: "+ memberName);
        holder.txtMemberId.setText("Member ID: " + memberId);

        //handle clicking on the individual member item in the search page.
        holder.parent.setOnClickListener(v->{
            Intent intent = new Intent(context, NewMessageActivity.class);
            //need to pass memberId and memberName  and message type to the NewMessageActivity
            intent.putExtra(Constants.MEMBER_ID,memberId);
            intent.putExtra(Constants.MEMBER_NAME,memberName);
            intent.putExtra(Constants.MESSAGE_TYPE,Constants.SINGLE_MESSAGE); //need to specify that we are trying to send a message to a single member.
            context.startActivity(intent);
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
