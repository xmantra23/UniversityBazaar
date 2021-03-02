package org.samir.universitybazaar.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.samir.universitybazaar.Models.Comment;
import org.samir.universitybazaar.R;

import java.util.ArrayList;

/**
 * @author Samir Shrestha
 * @description This is an adapter class meant to be used to initialize the recycler view for the comments.
 */
public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    private ArrayList<Comment> comments; //this will hold all the comments for a particular post.

    private Context context; //activity that owns this adapter. This should be the PostActivity.

    public CommentAdapter(Context context){
        this.context = context;
        comments = new ArrayList<>();

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate the item_comment layout file. This layout file describes how individual comments should look like.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //sets all the fields in the comments such as comment, username and comment date.
        holder.txtComment.setText(comments.get(position).getCommentText());
        holder.txtUserName.setText(comments.get(position).getCommentOwnerName());
        holder.txtCommentDate.setText(comments.get(position).getCommentDate());
    }

    @Override
    public int getItemCount() {
        //return how many comments are there for a given post item.
        return comments.size();
    }

    public void setComments(ArrayList<Comment> comments){
        this.comments = comments;
        notifyDataSetChanged(); // refreshes the view anytime the data set changes.
    }

    //helper class for the adapter. This class allows the adapter to access all the elements inside the item_comment.xml layout file.
    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView txtComment,txtUserName,txtCommentDate;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtComment = itemView.findViewById(R.id.txtComment);
            txtUserName = itemView.findViewById(R.id.txtUserName);
            txtCommentDate = itemView.findViewById(R.id.txtCommentDate);
        }
    }
}