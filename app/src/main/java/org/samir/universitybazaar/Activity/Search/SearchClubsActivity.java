package org.samir.universitybazaar.Activity.Search;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.samir.universitybazaar.Adapter.ClubAdapter;
import org.samir.universitybazaar.Adapter.MyPostAdapter;
import org.samir.universitybazaar.Database.ClubDAO;
import org.samir.universitybazaar.Database.PostDAO;
import org.samir.universitybazaar.Database.UserSession;
import org.samir.universitybazaar.Models.Club;
import org.samir.universitybazaar.Models.Post;
import org.samir.universitybazaar.Models.User;
import org.samir.universitybazaar.R;

import java.util.ArrayList;

public class SearchClubsActivity extends AppCompatActivity {
    private EditText edtTxtSearch;
    private RecyclerView clubRecView;
    private ClubAdapter adapter;
    private UserSession session;
    private ClubDAO dao;
    private RadioGroup searchTypeRadioGroup;
    private ArrayList<Club> clubs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_clubs);

        session = new UserSession(this);
        User user = session.isUserLoggedIn();
        dao = new ClubDAO(this);
        initViews();

        if(user != null){
            initRecView();
        }
    }

    private void initViews() {
        edtTxtSearch = findViewById(R.id.edtTxtSearch);
        clubRecView = findViewById(R.id.clubRecView);
        adapter = new ClubAdapter(this);
        searchTypeRadioGroup = findViewById(R.id.searchTypeRadioGroup);
    }

    private void initRecView() {
        clubs = dao.getAllClubs();
        adapter.setClubs(clubs);
        clubRecView.setAdapter(adapter);
        clubRecView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,true));
    }

    public void onSearchButtonClicked(View view){
        String searchTerm = edtTxtSearch.getText().toString().trim();
        switch(searchTypeRadioGroup.getCheckedRadioButtonId()){
            case R.id.radioTitle:
                clubs = dao.getClubsByTitle(searchTerm);
                break;
            case R.id.radioDescription:
                clubs = dao.getClubsByDescription(searchTerm);
                break;
            case R.id.radioDate:
                clubs = dao.getClubsByDate(searchTerm);
                break;
            case R.id.radioAdmin:
                clubs = dao.getClubsByAdmin(searchTerm);
                break;
        }

        if(clubs != null){
            if(clubs.size() == 0)
                Toast.makeText(this, "Couldn't find any matches.", Toast.LENGTH_SHORT).show();
        }
        adapter.setClubs(clubs);
        clubRecView.setAdapter(adapter);
        clubRecView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,true));
    }

    public void onRadioButtonClicked(View view){
        boolean checked = ((RadioButton) view).isChecked();

        switch(view.getId()){
            case R.id.radioTitle:
                if(checked){
                    edtTxtSearch.setHint("Enter club title.");
                }
                break;
            case R.id.radioDescription:
                if(checked){
                    edtTxtSearch.setHint("Enter club description.");
                }
                break;
            case R.id.radioDate:
                if(checked){
                    edtTxtSearch.setHint("Enter club date.");
                }
                break;
            case R.id.radioAdmin:
                if(checked){
                    edtTxtSearch.setHint("Enter club admin name.");
                }
                break;

        }
    }
}