package org.samir.universitybazaar.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.samir.universitybazaar.Models.ClubNoticeComment;
import org.samir.universitybazaar.R;

import java.util.ArrayList;

public class ClubNoticeCommentAdapter extends RecyclerView.Adapter<ClubNoticeCommentAdapter.ViewHolder> {
    private ArrayList<ClubNoticeComment> noticeComments;
    private Context context;

    public ClubNoticeCommentAdapter(Context context){
        this.context = context;
        noticeComments = new ArrayList<>();
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
        holder.txtComment.setText(noticeComments.get(position).getCommentText());
        holder.txtUserName.setText(noticeComments.get(position).getCommentOwnerName());
        holder.txtCommentDate.setText(noticeComments.get(position).getCommentDate());
    }

    @Override
    public int getItemCount() {
        return noticeComments.size();
    }

    public void setNoticeComments(ArrayList<ClubNoticeComment> noticeComments){
        this.noticeComments = noticeComments;
        notifyDataSetChanged();
    }

    //helper class for the adapter. This class allows the adapter to access all the elements inside the item_comment.xml layout file.
    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView txtComment,txtUserName,txtCommentDate;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtComment = itemView.findViewById(R.id.txtAnnouncement);
            txtUserName = itemView.findViewById(R.id.txtUserName);
            txtCommentDate = itemView.findViewById(R.id.txtCommentDate);
        }
    }
}
