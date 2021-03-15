package org.samir.universitybazaar.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.samir.universitybazaar.Activity.Clubs.ClubPostActivity;
import org.samir.universitybazaar.Activity.Clubs.NoticeActivity;
import org.samir.universitybazaar.Models.ClubPost;
import org.samir.universitybazaar.R;
import org.samir.universitybazaar.Utility.Constants;

import java.util.ArrayList;
/**
 * @author samir shrestha
 * adapter for club posts items.Displays all the posts made by other users inside a clubs as a well formatted list.
 */

public class ClubPostsAdapter extends RecyclerView.Adapter<ClubPostsAdapter.ViewHolder>{
    private ArrayList<ClubPost> clubPosts;
    private Context context;

    public ClubPostsAdapter(Context context){
        this.context = context;
        clubPosts = new ArrayList<>();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate the item_club_post layout file. This layout file describes how individual posts inside clubs should look like.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_club_post,parent,false);
        return new ClubPostsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //sets all the fields in the club posts such as title, username and date.
        holder.txtTitle.setText(clubPosts.get(position).getTitle());
        holder.txtUserName.setText("Posted By: " + clubPosts.get(position).getCreatorName());
        holder.txtPostDate.setText(clubPosts.get(position).getCreatedDate());

        //handle user clicks to the post items within the clubs page.
        holder.parent.setOnClickListener(v->{
            Intent intent = new Intent(context, ClubPostActivity.class);
            intent.putExtra(Constants.CLUB_POST_ID,clubPosts.get(position).get_id()); //passing the post id
            intent.putExtra(Constants.OWNER_ID,clubPosts.get(position).getCreatorId());//passing the creator/owner id.
            intent.putExtra(Constants.CLUB_ADMIN_ID,clubPosts.get(position).getAdminId());//passing the club admin id
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return clubPosts.size();
    }

    public void setClubPosts(ArrayList<ClubPost> clubPosts){
        this.clubPosts = clubPosts;
        notifyDataSetChanged();
    }

    //helper class for the adapter. This class allows the adapter to access all the elements inside the item_club_post.xml layout file.
    public class ViewHolder extends RecyclerView.ViewHolder{
        private RelativeLayout parent;
        private TextView txtTitle,txtUserName,txtPostDate;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            parent = itemView.findViewById(R.id.parent);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtUserName = itemView.findViewById(R.id.txtUserName);
            txtPostDate= itemView.findViewById(R.id.txtPostDate);
        }
    }
}
