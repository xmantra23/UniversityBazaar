package org.samir.universitybazaar.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.samir.universitybazaar.Models.ClubPost;
import org.samir.universitybazaar.R;

import java.util.ArrayList;
//adapter for club posts items.
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
        private TextView txtTitle,txtUserName,txtPostDate;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtUserName = itemView.findViewById(R.id.txtUserName);
            txtPostDate= itemView.findViewById(R.id.txtPostDate);
        }
    }
}
