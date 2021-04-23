package org.samir.universitybazaar.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import org.samir.universitybazaar.Activity.Search.SearchPostsActivity;
import org.samir.universitybazaar.Utility.Constants;
import org.samir.universitybazaar.Models.Post;
import org.samir.universitybazaar.Activity.Posts.PostActivity;
import org.samir.universitybazaar.R;

import java.util.ArrayList;

/**
 * @author Samir Shrestha
 * @description This is an adapter class meant to be used with posts recycler view. This recycler view basically lists all the posts.
 */
public class MyPostAdapter extends RecyclerView.Adapter<MyPostAdapter.ViewHolder> {


    private ArrayList<Post> posts = new ArrayList<>(); //this will hold all our post objects.
    private Context context; //parent activity of that will use this adapter. In our case it is MyPostActivity.
    private String activity_type;

    public MyPostAdapter(Context context){
        this.context = context;

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate item_post.xml layout file inside the MyPostActivity page.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        //set the data for all the fields in the item_post.xml layout file.
        holder.txtPostTitle.setText(posts.get(position).getTitle());
        holder.txtPostCategory.setText('(' + posts.get(position).getCategory() + ')');
        holder.txtUserName.setText("Posted by: " + posts.get(position).getCreatorName());
        holder.txtDate.setText(posts.get(position).getCreatedDate());

        //clicking on any post in the list should navigate to the PostActivity page for that post.
        holder.parent.setOnClickListener(v->{
            activity_type = context.getClass().getName();
            Intent intent = new Intent(context, PostActivity.class);
            //pass the postId to the PostActivity page. This will allow us to retrieve all the details of the clicked post inside the PostActivity page.
            intent.putExtra(Constants.POST_ID,posts.get(position).getId());
            intent.putExtra(Constants.ACTIVITY_NAME,activity_type);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return posts.size();

    }

    public void setPosts(ArrayList<Post> posts){
        this.posts = posts;
        notifyDataSetChanged();//update the UI anytime dataset changes.
    }

    //Helper class for MyPostAdapter. Allows the adapter class to access the view elements inside item_post.xml layout file.
    //This layout file defines how an individual post should look like in the recycler view.
    public class ViewHolder extends RecyclerView.ViewHolder{
        private MaterialCardView parent;
        private TextView txtPostTitle, txtPostCategory,txtUserName,txtDate;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            parent = itemView.findViewById(R.id.parent);
            txtPostTitle = itemView.findViewById(R.id.txtClubTitle);
            txtPostCategory = itemView.findViewById(R.id.txtPostCategory);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtUserName = itemView.findViewById(R.id.txtUserName);
        }
    }
}
