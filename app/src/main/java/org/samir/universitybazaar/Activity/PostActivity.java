package org.samir.universitybazaar.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.samir.universitybazaar.Adapter.CommentAdapter;
import org.samir.universitybazaar.Database.DatabaseHelper;
import org.samir.universitybazaar.Database.PostDAO;
import org.samir.universitybazaar.Database.UserSession;
import org.samir.universitybazaar.Fragments.AllPostsFragment;
import org.samir.universitybazaar.Models.Comment;
import org.samir.universitybazaar.Models.Post;
import org.samir.universitybazaar.Models.User;
import org.samir.universitybazaar.Profile.EditProfileActivity;
import org.samir.universitybazaar.Profile.ViewProfileActivity;
import org.samir.universitybazaar.R;
import org.samir.universitybazaar.Utility.Constants;

import java.util.ArrayList;

/**
 * @author Samir Shrestha
 * @Description This class displays a single post when a users clicks on a post from the list of post items.
 */
public class PostActivity extends AppCompatActivity {
    private TextView txtPostTitle,txtPostCategory,txtPostDescription,txtEdit,txtDelete,txtCreatorName,txtCreatedDate,txtAddComment;
    private TextView txtDeletePostWarning;
    private RecyclerView commentsRecView;
    private CommentAdapter adapter;
    private DatabaseHelper db;
    private UserSession userSession;
    private Post post;
    private int post_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        initViews();

        //get the postId of the post that the user clicked on before navigating to this activity.
        post_id = getIntent().getIntExtra(Constants.POST_ID,-1);


        db = new DatabaseHelper(this);
        userSession = new UserSession(this);

        User user = userSession.isUserLoggedIn();
        if(user != null){ //user is logged in.
            String memberId = user.getMemberId(); //get the logged in users memberId.
            post = db.getPostById(post_id); //get the post with the provided postId from the database.

            if(post != null){ // found a post with the provided postId.

                //If the user didn't create this post then don't allow them to edit or delete this post.
                if(!post.getCreatorId().equals(memberId)){
                    txtEdit.setVisibility(View.GONE);
                    txtDelete.setVisibility(View.GONE);
                }

                //initialize the layout with all the data from the retrieved post.
                txtPostTitle.setText(post.getTitle());
                txtPostCategory.setText('(' + post.getCategory() +')');
                txtPostDescription.setText(post.getDescription());
                txtCreatorName.setText("Posted by: " + post.getCreatorName());
                txtCreatedDate.setText(post.getCreatedDate());

                handleListeners(); // handle edit, delete  and add comment button clicks.


                //this is just for testing. Need to get actual comments from the database in the future.
                // for example like  we can do ArrayList<Comment> comments = db.getCommentsByPostId(postId);
                ArrayList<Comment> comments = new ArrayList<>();
                Comment comment1 = new Comment(1,post.getId(),"This is first test comment","Samir Shrestha","1000795680","08/12/2020");
                Comment comment2 = new Comment(2,post.getId(),"This is second test comment","Samir Shrestha","1000795680","08/12/2020");
                Comment comment3 = new Comment(3,post.getId(),"This is third test comment","Samir Shrestha","1000795680","08/12/2020");
                Comment comment4 = new Comment(4,post.getId(),"This is fourth test comment","Samir Shrestha","1000795680","08/12/2020");
                Comment comment5 = new Comment(5,post.getId(),"This is fifth test comment","Samir Shrestha","1000795680","08/12/2020");
                Comment comment6 = new Comment(5,post.getId(),"This is sixth test comment","Samir Shrestha","1000795680","08/12/2020");

                comments.add(comment1);
                comments.add(comment2);
                comments.add(comment3);
                comments.add(comment4);
                comments.add(comment5);
                comments.add(comment6);

                //initializing the recycler view which will display all the comments in this post.
                adapter = new CommentAdapter(this);
                adapter.setComments(comments);
                commentsRecView.setAdapter(adapter);
                commentsRecView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,true));

            }
        }

    }

    private void handleListeners() {
        txtEdit.setOnClickListener(v->handleUpdatePost()
        );
        
        txtDelete.setOnClickListener(v->handleDeletePost()
        );
        
        txtAddComment.setOnClickListener(v->{
            //need to create a new activity page which will allow a user to post a new comment and then afterwards navigate back to the post activity.
            // TODO: 2/20/2021 Navigate to PostCommentActivity 
        });
    }

    //initialize all the views in the activity_post.xml layout file.
    private void initViews() {
        txtPostTitle = findViewById(R.id.txtPostTitle);
        txtPostCategory = findViewById(R.id.txtPostCategory);
        txtPostDescription = findViewById(R.id.txtPostDescription);
        txtEdit = findViewById(R.id.txtEdit);
        txtDelete = findViewById(R.id.txtDelete);
        txtCreatorName = findViewById(R.id.txtCreatorName);
        txtCreatedDate = findViewById(R.id.txtCreatedDate);
        txtAddComment = findViewById(R.id.txtAddComment);
        commentsRecView = findViewById(R.id.commentsRecView);
        txtDeletePostWarning = findViewById(R.id.txtDeletePostWarning);
    }


    /**
     * @author Jingwen Ma
     * @Description To update post
     */
    private void handleUpdatePost(){
        Intent intent = new Intent(PostActivity.this, EditPostActivity.class);
        intent.putExtra(Constants.POST_ID,post_id);
        startActivity(intent);
    }

    /**
     * @author Jingwen Ma
     * @Description Delete post
     */
    private void handleDeletePost(){
        AlertDialog.Builder normalDialog = new AlertDialog.Builder(PostActivity.this);
        normalDialog.setTitle("Delete");
        normalDialog.setMessage("Are your sure you want to delete ?");
        //Delete the post and back
        normalDialog.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                PostDAO postDAO = new PostDAO(PostActivity.this);
                PostActivity.DeletePost deletePost = new PostActivity.DeletePost(postDAO, post_id);
                Thread thread = new Thread(deletePost);
                thread.start();
                Toast.makeText(PostActivity.this, "Delete Post Successfully!", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(PostActivity.this, HomeActivity.class);
                intent.putExtra(Constants.ACTIVITY_NAME,"post");
                startActivity(intent);

            }
        });
        //Stay on the current page
        normalDialog.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(PostActivity.this, "No", Toast.LENGTH_LONG).show();
                    }
                });
        normalDialog.show();
    }

    /**
     * @author Jingwen Ma
     * @Description Handles background process to delete the post
     */
    private class DeletePost implements Runnable{
        private PostDAO postDAO;
        private int post_id;
        public DeletePost(PostDAO postDAO,int post_id){
            this.postDAO = postDAO;
            this.post_id = post_id;
        }

        @Override
        public void run() {
            if (!postDAO.deletePost(post_id)) {
                //database operation err
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        txtDeletePostWarning.setVisibility(View.VISIBLE);
                        txtDeletePostWarning.setText("Delete post failed. Please try again!");
                    }
                });
            }
        }

    }

}