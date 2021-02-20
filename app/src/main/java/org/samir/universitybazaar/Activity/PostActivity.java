package org.samir.universitybazaar.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
 * @Description This class displays a single post when users clicks on a post item from a list of post items.
 */
public class PostActivity extends AppCompatActivity {
    private TextView txtPostTitle,txtPostCategory,txtPostDescription,txtEdit,txtDelete,txtCreatorName,txtCreatedDate,txtAddComment;
    private RecyclerView commentsRecView;
    private DatabaseHelper db;
    private CommentAdapter adapter;
    private UserSession userSession;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        initViews();

        int post_id = getIntent().getIntExtra(Constants.POST_ID,-1);
        db = new DatabaseHelper(this);
        userSession = new UserSession(this);

        User user = userSession.isUserLoggedIn();
        if(user != null){
            String memberId = user.getMemberId();
            Post post = db.getPostById(post_id); //get the post from the database by the post id we got from the intent.

            if(post != null){

                if(!post.getCreatorId().equals(memberId)){
                    txtEdit.setVisibility(View.GONE);
                    txtDelete.setVisibility(View.GONE);
                }
                txtPostTitle.setText(post.getTitle());
                txtPostCategory.setText('(' + post.getCategory() +')');
                txtPostDescription.setText(post.getDescription());
                txtCreatorName.setText("Posted by: " + post.getCreatorName());
                txtCreatedDate.setText(post.getCreatedDate());
                handleListeners();


                //this is just for testing. Need to get actual comments from the database.
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
            // TODO: 2/20/2021 Navigate to PostCommentActivity 
        });
    }

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