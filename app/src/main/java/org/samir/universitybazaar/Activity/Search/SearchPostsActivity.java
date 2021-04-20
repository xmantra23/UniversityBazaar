package org.samir.universitybazaar.Activity.Search;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

import org.samir.universitybazaar.Adapter.MyPostAdapter;
import org.samir.universitybazaar.R;

public class SearchPostsActivity extends AppCompatActivity {
    private EditText edtTxtSearch;
    private RecyclerView postRecView;
    private MyPostAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_posts);
        initViews();
    }

    private void initViews() {
        edtTxtSearch = findViewById(R.id.edtTxtSearch);
        postRecView = findViewById(R.id.postRecView);
        adapter = new MyPostAdapter(this);
    }

    public void onRadioButtonClicked(View view){
        boolean checked = ((RadioButton) view).isChecked();

        switch(view.getId()){
            case R.id.radioTitle:
                if(checked){
                    String searchTerm = edtTxtSearch.getText().toString();
                    Log.d("SearchPostActivity",searchTerm);
                }
                break;
            case R.id.radioDescription:
                if(checked){
                    String searchTerm = edtTxtSearch.getText().toString();
                    Log.d("SearchPostActivity",searchTerm);
                }
                break;
            case R.id.radioDate:
                if(checked){
                    String searchTerm = edtTxtSearch.getText().toString();
                    Log.d("SearchPostActivity",searchTerm);
                }
                break;
            case R.id.radioAuthor:
                if(checked){
                    String searchTerm = edtTxtSearch.getText().toString();
                    Log.d("SearchPostActivity",searchTerm);
                }
                break;
        }
    }
}