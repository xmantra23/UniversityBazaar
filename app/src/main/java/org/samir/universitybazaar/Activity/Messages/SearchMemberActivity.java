package org.samir.universitybazaar.Activity.Messages;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import org.samir.universitybazaar.Adapter.MemberAdapter;
import org.samir.universitybazaar.Database.MessageDAO;
import org.samir.universitybazaar.Models.Member;
import org.samir.universitybazaar.R;

import java.util.ArrayList;

public class SearchMemberActivity extends AppCompatActivity {

    private EditText edtTxtSearchMember;
    private RecyclerView membersRecView;
    private MemberAdapter memberAdapter;
    private ArrayList<Member> members;
    private MessageDAO db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_member);
        db = new MessageDAO(this);
        memberAdapter = new MemberAdapter(this); //initializing the adapter for the recycler view that's inside this activity.
        members = new ArrayList<>(); // this will hold the list of the members for each matched search in the search bar.

        initializeUI(); //initializes the UI for the layout display.
        handleSearch(); //handles the search functionality when users types in the search bar.
    }

    private void handleSearch() {
        edtTxtSearchMember.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String name = s.toString(); //get the string that the user typed in the search bar.

                //get the list of members that match this name and reset the recycler view that lists all the members.
                members = db.getMembersByName(name);
                if(members != null){
                    memberAdapter.setMembers(members);
                    membersRecView.setAdapter(memberAdapter);
                    membersRecView.setLayoutManager(new LinearLayoutManager(SearchMemberActivity.this,RecyclerView.VERTICAL,true));
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //not implemented. don't need this.
            }

            @Override
            public void afterTextChanged(Editable s) {
                //not implemented. don't need this.
            }
        });
    }

    private void initializeUI() {
        edtTxtSearchMember = findViewById(R.id.edtTxtSearchMember);
        membersRecView = findViewById(R.id.membersRecView);

        //load the recycler view before the user start typing.
        members = db.getMembersByName("");
        if(members != null){
            memberAdapter.setMembers(members);
            membersRecView.setAdapter(memberAdapter);
            membersRecView.setLayoutManager(new LinearLayoutManager(SearchMemberActivity.this,RecyclerView.VERTICAL,true));
        }

    }
}