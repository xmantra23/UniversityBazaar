package org.samir.universitybazaar.Activity.Sale;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.samir.universitybazaar.Adapter.MyPostAdapter;
import org.samir.universitybazaar.Adapter.MySaleItemListAdapter;
import org.samir.universitybazaar.Database.DatabaseHelper;
import org.samir.universitybazaar.Database.SellDAO;
import org.samir.universitybazaar.Database.UserSession;
import org.samir.universitybazaar.Models.Post;
import org.samir.universitybazaar.Models.Sell;
import org.samir.universitybazaar.Models.User;
import org.samir.universitybazaar.R;

import java.util.ArrayList;

public class MySaleItemListActivity extends AppCompatActivity {
    private RecyclerView saleView;
    private MySaleItemListAdapter adapter;
    private SellDAO sellDAO;

    public MySaleItemListActivity(){sellDAO = new SellDAO(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_sell_item_list);

        saleView = findViewById(R.id.saleView);
        adapter = new MySaleItemListAdapter(this);

        DatabaseHelper db = new DatabaseHelper(this);
        UserSession userSession = new UserSession(this);

        User user = userSession.isUserLoggedIn();
        if(user != null){ //user is logged in.
            ArrayList<Sell> sells = new ArrayList<>();
            sells = sellDAO.getSellByMemberId(user.getMemberId()); //get all the sales whose creatorId matches the logged in users memberId.
            adapter.setSells(sells); //initialize the recycler view with the retrieved sales from the database.
        }

        //populate the recycler view and display all the sales in the activity page using a linear layout.
        saleView.setAdapter(adapter);

        //reverseLayout true means the most recent sale will be in the top.
        saleView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,true));

    }
}
