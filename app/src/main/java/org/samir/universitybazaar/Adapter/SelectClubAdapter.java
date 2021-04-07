package org.samir.universitybazaar.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import org.samir.universitybazaar.Activity.Messages.NewMessageActivity;
import org.samir.universitybazaar.Models.Club;
import org.samir.universitybazaar.R;
import org.samir.universitybazaar.Utility.Constants;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;

public class SelectClubAdapter extends RecyclerView.Adapter<SelectClubAdapter.ViewHolder> {
    private ArrayList<Club> clubs = new ArrayList<>();
    private Context context;
    public SelectClubAdapter(Context context){
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_select_club,parent,false);
        return new SelectClubAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtClubName.setText(clubs.get(position).getTitle());

        holder.parent.setOnClickListener(v->{
            //only proceed to sending message if the clubs has at-least 1 member.
            if(clubs.get(position).getMemberCount() == 0){
                Toast.makeText(context, "This club has no members.", Toast.LENGTH_SHORT).show();
            }else{
                //navigate to new message activity and pass the clubid, clubname and type of message that we are trying to send.
                Intent intent = new Intent(context, NewMessageActivity.class);
                intent.putExtra(Constants.MEMBER_NAME,clubs.get(position).getTitle());
                //pass the club id as string
                String clubId = String.valueOf(clubs.get(position).get_id());
                intent.putExtra(Constants.MEMBER_ID,clubId);
                intent.putExtra(Constants.MESSAGE_TYPE,Constants.CLUB_MESSAGE);
                context.startActivity(intent);
            }

        });
    }

    @Override
    public int getItemCount() {
        return clubs.size();
    }

    public void setClubs(ArrayList<Club> clubs){
        this.clubs = clubs;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private MaterialCardView parent;
        private TextView txtClubName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            parent = itemView.findViewById(R.id.parent);
            txtClubName = itemView.findViewById(R.id.txtClubName);
        }
    }
}
