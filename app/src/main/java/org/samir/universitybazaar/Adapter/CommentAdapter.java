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

import org.samir.universitybazaar.Activity.CommentReplyActivity;
import org.samir.universitybazaar.Activity.Profile.EditProfileActivity;
import org.samir.universitybazaar.Activity.Posts.PostActivity;
import org.samir.universitybazaar.Activity.Profile.ViewProfileActivity;
import org.samir.universitybazaar.Database.CommentDAO;
import org.samir.universitybazaar.Database.DatabaseHelper;
import org.samir.universitybazaar.Database.UserSession;
import org.samir.universitybazaar.Models.Comment;
import org.samir.universitybazaar.Models.Post;
import org.samir.universitybazaar.Models.User;
import org.samir.universitybazaar.R;
import org.samir.universitybazaar.Utility.Constants;

import java.util.ArrayList;

/**
 * @author Samir Shrestha
 * @description This is an adapter class meant to be used to initialize the recycler view for the comments.
 */
public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    private ArrayList<Comment> comments; //this will hold all the comments for a particular post.
    private User user;
    private DatabaseHelper db;
    private Context context; //activity that owns this adapter. This should be the PostActivity.
    private String creator_id;
    private int post_id;

    public CommentAdapter(Context context){
        this.context = context;
        comments = new ArrayList<>();

        db = new DatabaseHelper(context);

        UserSession userSession = new UserSession(context); //gets the handle to the shared preferences
        user = userSession.isUserLoggedIn();

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

        /**
         * @author Yifei Lu
         */
        //if there are no receiver,the reply and punctuation's visible should set false
        String receiverName = comments.get(position).getCommentReceiverName();
        if(receiverName!= null){
            holder.txtReplyName.setVisibility(View.VISIBLE);
            holder.txtReplyName.setText("Reply to " + receiverName + " : ");
        }

        //if user_id not match owner_id or creator_id,the delete should not visible
        if(this.creator_id.equals(String.valueOf(this.user.getMemberId()))){
            holder.btnDelete.setVisibility(View.VISIBLE);
        }else if(comments.get(position).getCommentOwnerId().equals(String.valueOf(this.user.getMemberId()))){
            holder.btnDelete.setVisibility(View.VISIBLE);
        }

        //clicking on "reply" it will jump to the reply page
        holder.btnReply.setOnClickListener(v->{
            Intent intent = new Intent(context, CommentReplyActivity.class);
            //pass the comment id to reply activity so the reply comment can get the comment then auto complete some field
            intent.putExtra(Constants.POST_ID,comments.get(position).getPostId());
            intent.putExtra(Constants.Comment_ID,comments.get(position).get_id());
            context.startActivity(intent);
        });

        //clicking on "delete" it will delete the comment/reply from database
        holder.btnDelete.setOnClickListener((view)->{handleDeleteComment(comments.get(position).get_id());});
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

    /**
     * @author Yifei Lu
     */
    public void setCreatorId(String creator_id){
        this.creator_id = creator_id;
        notifyDataSetChanged(); // refreshes the view anytime the data set changes.
    }
    /**
     * @author Yifei Lu
     */
    public void setPostId(int post_id){
        this.post_id = post_id;
        notifyDataSetChanged(); // refreshes the view anytime the data set changes.
    }

    //helper class for the adapter. This class allows the adapter to access all the elements inside the item_comment.xml layout file.
    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView txtComment,txtUserName,txtCommentDate,btnReply,btnDelete,txtReplyName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtComment = itemView.findViewById(R.id.txtAnnouncement);
            txtUserName = itemView.findViewById(R.id.txtUserName);
            txtCommentDate = itemView.findViewById(R.id.txtCommentDate);
            btnReply = itemView.findViewById(R.id.btnReply);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            txtReplyName = itemView.findViewById(R.id.txtReplyName);
        }
    }

    /**
     * @author Yifei Lu
     */
    private void handleDeleteComment(int comment_id){
        CommentDAO commentDAO = new CommentDAO(context);
        if(commentDAO.deleteComment(comment_id)){ //delete successful
            Toast.makeText(context, "Delete successfully.", Toast.LENGTH_LONG).show();
            Post post = db.getPostById(post_id);
            //use PostActivity's method to update the data
            PostActivity context = (PostActivity) this.context;
            context.initCommentAdapter(post);
            notifyDataSetChanged();
        }else{
            Toast.makeText(context, "Delete fail.", Toast.LENGTH_LONG).show();
        }
    }
}