package org.samir.universitybazaar.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import org.samir.universitybazaar.Activity.PostActivity;
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
        User user = session.isUserLoggedIn();
        if(user != null){
            String userId = user.getMemberId();
            if(userId.equals(clubs.get(position).getOwnerId())){
                //user owns this club. hide join button.
                holder.txtJoin.setVisibility(View.GONE);
            }else{
                //user is a regular user. show join button.
                holder.txtJoin.setVisibility(View.VISIBLE);
                holder.txtJoin.setOnClickListener(v->{
                    // TODO: 3/7/2021 add code for joining user.
                });
            }
        }

        //clicking on any post in the list should navigate to the ClubActivity page for that Club.
        holder.parent.setOnClickListener(v->{
//            Intent intent = new Intent(context, PostActivity.class);
//            //pass the postId to the PostActivity page. This will allow us to retrieve all the details of the clicked post inside the PostActivity page.
//            intent.putExtra(Constants.POST_ID,posts.get(position).getId());
//            context.startActivity(intent);
        });
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
