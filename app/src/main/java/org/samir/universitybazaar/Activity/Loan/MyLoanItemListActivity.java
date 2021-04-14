package org.samir.universitybazaar.Activity.Loan;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.samir.universitybazaar.Adapter.MyLoanItemListAdapter;
import org.samir.universitybazaar.Adapter.MySaleItemListAdapter;
import org.samir.universitybazaar.Database.DatabaseHelper;
import org.samir.universitybazaar.Database.LoanDAO;
import org.samir.universitybazaar.Database.SellDAO;
import org.samir.universitybazaar.Database.UserSession;
import org.samir.universitybazaar.Models.Loan;
import org.samir.universitybazaar.Models.Sell;
import org.samir.universitybazaar.Models.User;
import org.samir.universitybazaar.R;

import java.util.ArrayList;

public class MyLoanItemListActivity extends AppCompatActivity {
    private RecyclerView saleView;
    private MyLoanItemListAdapter adapter;
    private LoanDAO loanDAO;

    public MyLoanItemListActivity(){loanDAO = new LoanDAO(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_loan_item_list);

        saleView = findViewById(R.id.saleView);
        adapter = new MyLoanItemListAdapter(this);

        DatabaseHelper db = new DatabaseHelper(this);
        UserSession userSession = new UserSession(this);

        User user = userSession.isUserLoggedIn();
        if(user != null){ //user is logged in.
            ArrayList<Loan> loans = new ArrayList<>();
            loans = loanDAO.getLoanByMemberId(user.getMemberId()); //get all the loans whose creatorId matches the logged in users memberId.
            adapter.setLoans(loans); //initialize the recycler view with the retrieved loans from the database.
        }

        //populate the recycler view and display all the loans in the activity page using a linear layout.
        saleView.setAdapter(adapter);

        //reverseLayout true means the most recent loan will be in the top.
        saleView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,true));

    }
}
