package org.samir.universitybazaar.Activity.Search;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.samir.universitybazaar.Activity.HomeActivity;
import org.samir.universitybazaar.Adapter.AllItemsAdapter;
import org.samir.universitybazaar.Database.LoanDAO;
import org.samir.universitybazaar.Database.SellDAO;
import org.samir.universitybazaar.Database.UserSession;
import org.samir.universitybazaar.Models.Loan;
import org.samir.universitybazaar.Models.Sell;
import org.samir.universitybazaar.Models.User;
import org.samir.universitybazaar.R;

import java.util.ArrayList;

public class SearchMarketActivity extends AppCompatActivity {
    private EditText edtTxtSearch;
    private RecyclerView itemRecView;
    private AllItemsAdapter adapter;
    private UserSession session;
    private SellDAO sellDAO;
    private LoanDAO loanDAO;
    private RadioGroup searchTypeRadioGroup;
    private ArrayList<Sell> allSell;
    private ArrayList<Loan> allLoan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_market);

        session = new UserSession(this);
        User user = session.isUserLoggedIn();
        sellDAO = new SellDAO(this);
        loanDAO = new LoanDAO(this);
        initViews();

        if(user != null){
            initRecView();
        }
    }

    private void initViews() {
        edtTxtSearch = findViewById(R.id.edtTxtSearch);
        itemRecView = findViewById(R.id.itemRecView);
        adapter = new AllItemsAdapter(this);
        searchTypeRadioGroup = findViewById(R.id.searchTypeRadioGroup);
    }

    private void initRecView() {
        ArrayList<Object> objects = new ArrayList<>();
        allSell = sellDAO.getAllSells(); //get all the sells in the sells table from the database.
        allLoan = loanDAO.getAllLoans();
        objects.addAll(allSell);
        objects.addAll(allLoan);
        adapter.setObjs(objects);
        itemRecView.setAdapter(adapter);
        itemRecView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,true));
    }

    public void onRadioButtonClicked(View view){
        boolean checked = ((RadioButton) view).isChecked();

        switch(view.getId()){
            case R.id.radioTitle:
                if(checked){
                    edtTxtSearch.setHint("Enter title.");
                }
                break;
            case R.id.radioDescription:
                if(checked){
                    edtTxtSearch.setHint("Enter description.");
                }
                break;
            case R.id.radioDate:
                if(checked){
                    edtTxtSearch.setHint("Enter date (MM/DD/YYYY).");
                }
                break;
            case R.id.radioOwner:
                if(checked){
                    edtTxtSearch.setHint("Enter owner name.");
                }
                break;

        }
    }

    public void onSearchButtonClicked(View view){
        String searchTerm = edtTxtSearch.getText().toString().trim();
        switch(searchTypeRadioGroup.getCheckedRadioButtonId()){
            case R.id.radioTitle:
//                clubs = dao.getClubsByTitle(searchTerm);
                allSell = sellDAO.getSellsByTitle(searchTerm); //get all the sells in the sells table from the database.
                allLoan = loanDAO.getLoansByTitle(searchTerm);
                break;
            case R.id.radioDescription:
//                clubs = dao.getClubsByDescription(searchTerm);
                allSell = sellDAO.getSellsByDescription(searchTerm); //get all the sells in the sells table from the database.
                allLoan = loanDAO.getLoansByDescription(searchTerm);
                break;
            case R.id.radioDate:
//                clubs = dao.getClubsByDate(searchTerm);
                allSell = sellDAO.getSellsByDate(searchTerm); //get all the sells in the sells table from the database.
                allLoan = loanDAO.getLoansByDate(searchTerm);
                break;
            case R.id.radioOwner:
//                clubs = dao.getClubsByAdmin(searchTerm);
                allSell = sellDAO.getSellsByOwner(searchTerm); //get all the sells in the sells table from the database.
                allLoan = loanDAO.getLoansByOwner(searchTerm);
                break;
        }
        ArrayList<Object> objects = new ArrayList<>();
        if (allSell != null) objects.addAll(allSell);
        if (allLoan != null) objects.addAll(allLoan);
        if (objects.size() == 0) Toast.makeText(this, "Couldn't find any matches.", Toast.LENGTH_SHORT).show();
        adapter.setObjs(objects);
        itemRecView.setAdapter(adapter);
        itemRecView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,true));
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SearchMarketActivity.this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
