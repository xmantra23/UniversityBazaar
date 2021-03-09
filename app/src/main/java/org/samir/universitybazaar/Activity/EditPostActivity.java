package org.samir.universitybazaar.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.samir.universitybazaar.Database.DatabaseHelper;
import org.samir.universitybazaar.Database.PostDAO;
import org.samir.universitybazaar.Database.UserSession;
import org.samir.universitybazaar.Models.Post;
import org.samir.universitybazaar.Models.User;
import org.samir.universitybazaar.R;
import org.samir.universitybazaar.Utility.Constants;

/**
 * @author Jingwen Ma
 * @Description This class edits the post which the user click.
 */
public class EditPostActivity extends AppCompatActivity {

    private static final String TAG = "edit Post";
    private EditText updateTxtTitle, updateTxtDesc;
    private Button btnSaveEditPost, btnCancelEditPost;
    private TextView txtUpdatePostWarning;
    private RadioGroup categoryRadioGroup;
    private RadioButton edtTxtCategory;
    private DatabaseHelper databaseHelper;
    private Post post;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);

        UserSession userSession = new UserSession(this); //gets the handle to the shared preferences
        user = userSession.isUserLoggedIn();

        initViews();
        initListeners();
    }

    private void initViews() {
        updateTxtTitle = findViewById(R.id.updateTxtTitle);
        updateTxtDesc = findViewById(R.id.updateTxtDesc);
        categoryRadioGroup = findViewById(R.id.categoryRadioGroup);
        txtUpdatePostWarning = findViewById(R.id.txtUpdatePostWarning);
        categoryRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                edtTxtCategory = radioGroup.findViewById(i);
            }
        });
        btnSaveEditPost = findViewById(R.id.btnSaveEditPost);
        btnCancelEditPost = findViewById(R.id.btnCancelEditPost);

        databaseHelper = new DatabaseHelper(this);
        int post_id = getIntent().getIntExtra(Constants.POST_ID,-1);
        //user is logged in.
        if(user != null) {
            //get the post by postId.
            post = databaseHelper.getPostById(post_id);
            if(post != null){

                String txtTitle = null2String(post.getTitle());
                String txtDesc = null2String(post.getDescription());
                String txtCategory = null2String(post.getCategory());

                this.updateTxtTitle.setText(txtTitle);
                this.updateTxtDesc.setText(txtDesc);
                if(!txtCategory.equals("")){
                    String[] categoryList = new String[]{"Job Posting","News","Other"};
                    for(int i=0;i<this.categoryRadioGroup.getChildCount();i++){
                        this.edtTxtCategory =(RadioButton) this.categoryRadioGroup.getChildAt(i);
                        if(txtCategory.equals(categoryList[i])){
                            this.edtTxtCategory.setChecked(true);
                            break;
                        }
                    }
                }
            }
        }


    }

    private void initListeners() {
        btnSaveEditPost.setOnClickListener((view)->{handleSaveEditPost();});
        btnCancelEditPost.setOnClickListener((view)->{handleCancelEditPost();});
    }
    private void handleSaveEditPost(){

        String title = updateTxtTitle.getText().toString().trim();
        String description = updateTxtDesc.getText().toString().trim();
        String category = edtTxtCategory.getText().toString().trim();

        //Validation of post info
        if(title.equals("")){
            txtUpdatePostWarning.setVisibility(View.VISIBLE);
            txtUpdatePostWarning.setText("Please enter the title");
        }else if(title.length() > 100){
            txtUpdatePostWarning.setVisibility(View.VISIBLE);
            txtUpdatePostWarning.setText("Title must be at-most 100 chars long");
        }else if(description.length() > 500){
            txtUpdatePostWarning.setVisibility(View.VISIBLE);
            txtUpdatePostWarning.setText("Description must be at-most 500 chars long");
        }else {//Post info are valid.

            txtUpdatePostWarning.setVisibility(View.GONE);

            //Pop confirmation box to make confirmation of update post.
            AlertDialog.Builder normalDialog = new AlertDialog.Builder(EditPostActivity.this);
            normalDialog.setTitle("Save Post");
            normalDialog.setMessage("Are your sure you want to save changes ?");
            //Save changes and back
            normalDialog.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    post.setTitle(title);
                    post.setDescription(description);
                    post.setCategory(category);
                    post.setId(post.getId());

                    PostDAO postDAO = new PostDAO(EditPostActivity.this);

                    EditPostActivity.UpdatePost updatePost = new EditPostActivity.UpdatePost(postDAO, post);
                    Thread thread = new Thread(updatePost);
                    thread.start();

                    Toast.makeText(EditPostActivity.this, "Save Post Successfully!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(EditPostActivity.this, PostActivity.class);
                    intent.putExtra(Constants.POST_ID,post.getId());
                    startActivity(intent);
                }
            });
            //Stay on the current page
            normalDialog.setNegativeButton("No",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(EditPostActivity.this, "No", Toast.LENGTH_LONG).show();
                        }
                    });
            normalDialog.show();

        }



    }

    private void handleCancelEditPost(){

        //Pop confirmation box to make confirmation of cancel the update
        AlertDialog.Builder normalDialog = new AlertDialog.Builder(EditPostActivity.this);
        normalDialog.setTitle("Cancel");
        normalDialog.setMessage("Are your sure you want to leave ?");
        //Cancel the update and back
        normalDialog.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(EditPostActivity.this, PostActivity.class);
                intent.putExtra(Constants.POST_ID,post.getId());
                startActivity(intent);
            }
        });
        //Stay on the current page
        normalDialog.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(EditPostActivity.this, "No", Toast.LENGTH_LONG).show();
                    }
                });
        normalDialog.show();
    }

    private class UpdatePost implements Runnable{
        private PostDAO postDAO;
        private Post post;
        public UpdatePost(PostDAO postDAO,Post post){
            this.postDAO = postDAO;
            this.post = post;
        }

        @Override
        public void run() {
            if (!postDAO.updatePost(post)) {
                //database operation err
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        txtUpdatePostWarning.setVisibility(View.VISIBLE);
                        txtUpdatePostWarning.setText("Update post failed. Please try again!");
                    }
                });
            }
        }

    }

    private String null2String(String str){
        if(str == null){
            return "";
        }else{
            return str;
        }
    }



}
