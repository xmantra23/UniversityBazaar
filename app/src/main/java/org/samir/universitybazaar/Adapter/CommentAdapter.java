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

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    private ArrayList<Comment> comments;

    private Context context;

    public CommentAdapter(Context context){
        this.context = context;
        comments = new ArrayList<>();

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtComment.setText(comments.get(position).getCommentText());
        holder.txtUserName.setText(comments.get(position).getCommentOwnerName());
        holder.txtCommentDate.setText(comments.get(position).getCommentDate());
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public void setComments(ArrayList<Comment> comments){
        this.comments = comments;
        notifyDataSetChanged();
    }

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