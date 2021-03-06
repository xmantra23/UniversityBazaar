package org.samir.universitybazaar.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.samir.universitybazaar.Adapter.CommentAdapter;
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
 * @Description This class displays a single post when a users clicks on a post from the list of post items.
 */
public class PostActivity extends AppCompatActivity {
    private TextView txtPostTitle,txtPostCategory,txtPostDescription,txtEdit,txtDelete,txtCreatorName,txtCreatedDate,txtAddComment;
    private RecyclerView commentsRecView;
    private CommentAdapter adapter;
    private DatabaseHelper db;
    private UserSession userSession;
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
            Post post = db.getPostById(post_id); //get the post with the provided postId from the database.

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
                comments = db.getCommentsByPostId(post_id); //get comments by post_id

                //initializing the recycler view which will display all the comments in this post.
                adapter = new CommentAdapter(this);
                adapter.setComments(comments);
                commentsRecView.setAdapter(adapter);
                commentsRecView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,true));

            }
        }

    }

    private void handleListeners() {
        txtEdit.setOnClickListener(v->{
            // TODO: 2/20/2021 Navigate to EditPostActivity. 
        });
        
        txtDelete.setOnClickListener(v->{
            // TODO: 2/20/2021 Delete this post from database, show confirmation toast and navigate to MyPostsActivity
        });
        
        txtAddComment.setOnClickListener(v->{
            //redirect to activity_comment
            Intent intent = new Intent(PostActivity.this,CommentActivity.class);
            intent.putExtra(Constants.POST_ID, post_id);
            startActivity(intent);
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
    }
}