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

import org.samir.universitybazaar.Activity.HomeActivity;
import org.samir.universitybazaar.Activity.PostActivity;
import org.samir.universitybazaar.Database.ClubDAO;
import org.samir.universitybazaar.Database.UserSession;
import org.samir.universitybazaar.Models.Club;
import org.samir.universitybazaar.Models.User;
import org.samir.universitybazaar.R;
import org.samir.universitybazaar.Utility.Constants;

import java.util.ArrayList;

public class ClubAdapter extends RecyclerView.Adapter<ClubAdapter.ViewHolder>{
    private ArrayList<Club> clubs = new ArrayList<>();
    private Context context;

    public ClubAdapter(Context context){
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate item_club.xml layout file inside the ClubActivity page.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_club,parent,false);
        return new ClubAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //set the data for all the fields in the item_club.xml layout file.
        holder.txtClubTitle.setText(clubs.get(position).getTitle());
        holder.txtShortDesc.setText(clubs.get(position).getShortDescription());
        holder.txtTotalMembers.setText("Total Members: " + clubs.get(position).getMemberCount());

        UserSession session = new UserSession(context);
        ClubDAO cb = new ClubDAO(context);
        User user = session.isUserLoggedIn();

        //show, leave, join or hide the text on the clubs CardView based on if the user is owner, subscribed or unsubscribed.
        if(user != null){
            String userId = user.getMemberId();
            boolean isMember = cb.verifyMembership(clubs.get(position).get_id(),userId); //check if the user is a member of this club.
            boolean isOwner = userId.equals(clubs.get(position).getOwnerId());
            if(isOwner){
                //user owns this club so  hide join button.
                holder.txtJoin.setVisibility(View.GONE);
            }else{
                //user is a regular user.
                if(isMember){
                    //user is already a member so display LEAVE instead of JOIN text.
                    holder.txtJoin.setText("LEAVE");
                    holder.txtJoin.setTextColor(context.getResources().getColor(R.color.red));
                    holder.txtJoin.setVisibility(View.VISIBLE);
                    holder.txtJoin.setOnClickListener(v->{
                        if(cb.removeMemberFromClub(clubs.get(position).get_id(),userId)){
                            //successfully removed member from club.
                            //now decrement member count by 1.
                            cb.decrementMemberCount(clubs.get(position));
                            Toast.makeText(context, "Unsubscribed Successfully", Toast.LENGTH_LONG).show();

                            //redirect to homepage
                            Intent intent = new Intent(context,HomeActivity.class);
                            context.startActivity(intent);
                        }else{
                            //couldn't remove member from club.
                            Toast.makeText(context, "Error. Couldn't Unsubscribe", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    //add a new member to club.
                    holder.txtJoin.setText("JOIN");
                    holder.txtJoin.setTextColor(context.getResources().getColor(R.color.blue));
                    holder.txtJoin.setVisibility(View.VISIBLE);
                    holder.txtJoin.setOnClickListener(v->{
                        if(cb.addMemberToClub(clubs.get(position).get_id(),userId)){
                            //successfully added member to a club.
                            //now increment member count by 1.
                            cb.incrementMemberCount(clubs.get(position));
                            Toast.makeText(context, "Subscribed Successfully", Toast.LENGTH_LONG).show();

                            //navigate to homepage
                            Intent intent = new Intent(context,HomeActivity.class);
                            context.startActivity(intent);

                        }else{
                            //couldn't add member to a club.
                            Toast.makeText(context, "Error. Couldn't Subscribe", Toast.LENGTH_SHORT).show();
                        }

                    });
                }
            }
            //clicking on any post in the list should navigate to the ClubActivity page for that Club but we must verify if the user is
            //already subscribed to the club or not. If the user is not subscribed then we cannot let the user visit that club.
            holder.parent.setOnClickListener(v->{
                if(isOwner || isMember){
                    // TODO: 3/8/2021 navigate to the clubs activity page.
                }else{
                    Toast.makeText(context, "You are not subscribed to this club.", Toast.LENGTH_LONG).show();
                }
            });
        }


    }

    @Override
    public int getItemCount() {
        return clubs.size();
    }

    public void setClubs(ArrayList<Club> clubs){
        this.clubs = clubs; //initializing all the clubs.
        notifyDataSetChanged();
    }

    //Helper class for ClubAdapter. Allows the adapter class to access the view elements inside item_club.xml layout file.
    //This layout file defines how an individual club should look like in the recycler view.
    public class ViewHolder extends RecyclerView.ViewHolder{
        private MaterialCardView parent;
        private TextView txtClubTitle, txtShortDesc,txtTotalMembers,txtJoin;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            parent = itemView.findViewById(R.id.parent);
            txtClubTitle = itemView.findViewById(R.id.txtClubTitle);
            txtShortDesc = itemView.findViewById(R.id.txtShortDesc);
            txtTotalMembers = itemView.findViewById(R.id.txtTotalMembers);
            txtJoin = itemView.findViewById(R.id.txtJoin);
        }
    }
}
