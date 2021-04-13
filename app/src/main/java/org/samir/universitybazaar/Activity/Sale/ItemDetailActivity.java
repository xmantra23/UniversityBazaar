package org.samir.universitybazaar.Activity.Sale;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import org.samir.universitybazaar.Database.ExchangeDAO;
import org.samir.universitybazaar.Database.LoanDAO;
import org.samir.universitybazaar.Database.SellDAO;
import org.samir.universitybazaar.Database.UserSession;
import org.samir.universitybazaar.Models.Exchange;
import org.samir.universitybazaar.Models.Loan;
import org.samir.universitybazaar.Models.Sell;
import org.samir.universitybazaar.Models.User;
import org.samir.universitybazaar.R;
import org.samir.universitybazaar.Utility.Constants;

import java.io.File;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;

public class ItemDetailActivity extends AppCompatActivity {

    private TextView txtPostTitle, txtPostDescription, txtEdit, txtDelete, txtCreatorName, txtCreatedDate, priceText;
    private ImageView imageView;
    private int sell_id;
    private UserSession userSession;
    private SellDAO dao;
    private LoanDAO dao1;
    private int inType, inId;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private Button btnBuy;
    private RelativeLayout dayRelLayout;
    private SellDAO sellDAO;
    private LoanDAO loanDAO;
    private EditText edtTxtDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        inType = intent.getIntExtra("objType", 1);
        inId = intent.getIntExtra("objId", 1);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        initViews();

        sell_id = inId;
        dao = new SellDAO(this);
        userSession = new UserSession(this);

        User user = userSession.isUserLoggedIn();
        if (user != null) { //user is logged in.
            int permission = ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE);
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // We don't have permission so prompt the user
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE,
                        REQUEST_EXTERNAL_STORAGE);
            }

            String memberId = user.getMemberId(); //get the logged in users memberId.
            if (inType == 1) { // type sell
                Sell sell = dao.getSellById(sell_id); //get the sell with the provided sellId from the database.
                if (sell != null) { // found a sell with the provided Id.

                    //initialize the layout with all the data from the retrieved sale.
                    if (sell.getStatus().equals("sold out")) {
                        btnBuy.setEnabled(false);
                    }
                    txtPostTitle.setText(sell.getTitle());
                    Uri uri = Uri.fromFile(new File(sell.getImage()));
                    imageView.setImageURI(uri);
                    txtPostDescription.setText(sell.getDescription());
                    txtCreatorName.setText("Posted by: " + sell.getCreatorName());
                    txtCreatedDate.setText(sell.getCreatedDate());
                    priceText.setText(sell.getPrice());
                    dayRelLayout.setVisibility(View.GONE);
                }
                // handle edit, delete  and add comment button clicks.
            } else {
                dao1 = new LoanDAO(this);
                Loan loan = dao1.getLoanById(sell_id); //get the sell with the provided sellId from the database.
                if (loan != null) { // found a loan with the provided Id.

                    //initialize the layout with all the data from the retrieved sale.
                    if (loan.getStatus().equals("rent out")) {
                        btnBuy.setEnabled(false);
                    }
                    txtPostTitle.setText(loan.getTitle());
                    Uri uri = Uri.fromFile(new File(loan.getImage()));
                    imageView.setImageURI(uri);
                    txtPostDescription.setText(loan.getDescription());
                    txtCreatorName.setText("Posted by: " + loan.getCreatorName());
                    txtCreatedDate.setText(loan.getCreatedDate());
                    priceText.setText(loan.getPrice());
                }
            }
        }

        btnBuy.setOnClickListener(v -> {
            int sellerId = 1;
            if (inType == 1) {//sell
                sellDAO = new SellDAO(this);
                Sell sell = sellDAO.getSellById(inId);
                sellerId = Integer.parseInt(sell.getCreatorId());
            } else {
                loanDAO = new LoanDAO(this);
                Loan loan = loanDAO.getLoanById(inId);
                sellerId = Integer.parseInt(loan.getCreatorId());
            }
            ExchangeDAO exchangeDAO = new ExchangeDAO(this);

            Exchange exchange = new Exchange();
            exchange.setCustomerId(user.getId());
            exchange.setSellerId(sellerId);
            Date date = new Date();
            DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
            String createdDate = df.format(date);
            exchange.setExchangeDate(createdDate);
            exchange.setItemId(inId);
            exchange.setPrice(priceText.getText().toString());
            if(inType==2){
                BigDecimal result = new BigDecimal(priceText.getText().toString()).multiply(new BigDecimal(edtTxtDay.getText().toString()));
                exchange.setPrice(result.toString());
            }
            exchange.setType(String.valueOf(inType));
            boolean b = exchangeDAO.addExchange(exchange);
            if (b) {
                btnBuy.setEnabled(false);
                if (inType == 1) {
                    Sell updateSell = new Sell();
                    updateSell.set_id(inId);
                    updateSell.setStatus("sold out");
                    sellDAO.updateSellStatus(updateSell);
                } else {
                    Loan updateLoan = new Loan();
                    updateLoan.set_id(inId);
                    updateLoan.setStatus("rent out");
                    loanDAO.updateLoanStatus(updateLoan);
                }
                Toast.makeText(this, "Success!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Failure!", Toast.LENGTH_LONG).show();
            }

        });
    }

    private void initViews() {
        txtPostTitle = findViewById(R.id.txtPostTitle);
        txtPostDescription = findViewById(R.id.txtPostDescription);
        txtEdit = findViewById(R.id.txtEdit);
        txtDelete = findViewById(R.id.txtDelete);
        txtCreatorName = findViewById(R.id.txtCreatorName);
        txtCreatedDate = findViewById(R.id.txtCreatedDate);
        imageView = findViewById(R.id.imageView);
        priceText = findViewById(R.id.priceText);
        btnBuy = findViewById(R.id.btnBuy);
        edtTxtDay = findViewById(R.id.edtTxtDay);
        dayRelLayout = findViewById(R.id.dayRelLayout);
    }


}