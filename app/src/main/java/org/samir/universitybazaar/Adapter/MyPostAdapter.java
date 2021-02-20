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

import org.samir.universitybazaar.Constants;
import org.samir.universitybazaar.Models.Post;
import org.samir.universitybazaar.PostActivity;
import org.samir.universitybazaar.R;

import java.util.ArrayList;

public class MyPostAdapter extends RecyclerView.Adapter<MyPostAdapter.ViewHolder> {

    private ArrayList<Post> posts = new ArrayList<>();
    private Context context;

    public MyPostAdapter(Context context){
        this.context = context;

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtPostTitle.setText(posts.get(position).getTitle());
        holder.txtPostCategory.setText('(' + posts.get(position).getCategory() + ')');
        holder.txtUserName.setText("Posted by: " + posts.get(position).getCreatorName());
        holder.txtDate.setText(posts.get(position).getCreatedDate());
        
        holder.parent.setOnClickListener(v->{
            Intent intent = new Intent(context, PostActivity.class);
            intent.putExtra(Constants.POST_ID,posts.get(position).getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return posts.size();

    }

    public void setPosts(ArrayList<Post> posts){
        this.posts = posts;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private MaterialCardView parent;
        private TextView txtPostTitle, txtPostCategory,txtUserName,txtDate;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            parent = itemView.findViewById(R.id.parent);
            txtPostTitle = itemView.findViewById(R.id.txtPostTitle);
            txtPostCategory = itemView.findViewById(R.id.txtPostCategory);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtUserName = itemView.findViewById(R.id.txtUserName);
        }
    }
}
