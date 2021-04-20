package org.samir.universitybazaar.Activity.Search;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.samir.universitybazaar.Adapter.MyPostAdapter;
import org.samir.universitybazaar.Database.PostDAO;
import org.samir.universitybazaar.Database.UserSession;
import org.samir.universitybazaar.Models.Post;
import org.samir.universitybazaar.Models.User;
import org.samir.universitybazaar.R;

import java.util.ArrayList;

public class SearchPostsActivity extends AppCompatActivity {
    private EditText edtTxtSearch;
    private RecyclerView postRecView;
    private Button btnSearch;
    private MyPostAdapter adapter;
    private UserSession session;
    private PostDAO dao;
    private RadioGroup searchTypeRadioGroup;
    private ArrayList<Post> posts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_posts);

        session = new UserSession(this);
        User user = session.isUserLoggedIn();
        dao = new PostDAO(this);
        initViews();

        if(user != null){
            initRecView();
        }
    }

    private void initViews() {
        edtTxtSearch = findViewById(R.id.edtTxtSearch);
        postRecView = findViewById(R.id.postRecView);
        btnSearch = findViewById(R.id.btnSearch);
        adapter = new MyPostAdapter(this);
        searchTypeRadioGroup = findViewById(R.id.searchTypeRadioGroup);
    }

    private void initRecView() {
        posts = dao.getAllPosts();
        adapter.setPosts(posts);
        postRecView.setAdapter(adapter);
        postRecView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,true));
    }



    public void onSearchButtonClicked(View view){
        String searchTerm = edtTxtSearch.getText().toString().trim();
        switch(searchTypeRadioGroup.getCheckedRadioButtonId()){
            case R.id.radioTitle:
                posts = dao.getPostByTitle(searchTerm);
                adapter.setPosts(posts);
                postRecView.setAdapter(adapter);
                postRecView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,true));
                break;
            case R.id.radioDescription:
                posts = dao.getPostByDescription(searchTerm);
                adapter.setPosts(posts);
                postRecView.setAdapter(adapter);
                postRecView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,true));
                break;
            case R.id.radioDate:
                posts = dao.getPostByDate(searchTerm);
                adapter.setPosts(posts);
                postRecView.setAdapter(adapter);
                postRecView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,true));
                break;
            case R.id.radioAuthor:
                posts = dao.getPostByAuthor(searchTerm);
                adapter.setPosts(posts);
                postRecView.setAdapter(adapter);
                postRecView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,true));
                break;
        }
    }

    public void onRadioButtonClicked(View view){
        boolean checked = ((RadioButton) view).isChecked();

        switch(view.getId()){
            case R.id.radioTitle:
                if(checked){
                    edtTxtSearch.setHint("Enter post title.");
                }
                break;
            case R.id.radioDescription:
                if(checked){
                    edtTxtSearch.setHint("Enter post description.");
                }
                break;
            case R.id.radioDate:
                if(checked){
                    edtTxtSearch.setHint("Enter post date.");
                }
                break;
            case R.id.radioAuthor:
                if(checked){
                    edtTxtSearch.setHint("Enter post author name.");
                }
                break;

        }
    }

}