package org.samir.universitybazaar.Activity.pay;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.samir.universitybazaar.Activity.HomeActivity;
import org.samir.universitybazaar.Database.ExchangeDAO;
import org.samir.universitybazaar.Database.LoanDAO;
import org.samir.universitybazaar.Database.SellDAO;
import org.samir.universitybazaar.Database.UserSession;
import org.samir.universitybazaar.Models.Exchange;
import org.samir.universitybazaar.Models.Loan;
import org.samir.universitybazaar.Models.Sell;
import org.samir.universitybazaar.Models.User;
import org.samir.universitybazaar.R;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PayActivity extends AppCompatActivity {
    private int inType, inId;
    private String inPrice, inNum;
    TextView txtPrice, txtWarning;
    EditText edtCreditCardID, edtTxtAddr;
    Button btnConfirm;
    private SellDAO sellDAO;
    private LoanDAO loanDAO;
    private UserSession userSession;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_pay);

        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        inType = intent.getIntExtra("objType", 1);
        inId = intent.getIntExtra("objId", 1);
        inNum = intent.getStringExtra("objNum");
        inPrice = intent.getStringExtra("objPrice");

        initViews();
        txtPrice.setText("Total Price: $"+inPrice);


        sellDAO = new SellDAO(this);
        loanDAO = new LoanDAO(this);
        userSession = new UserSession(this);
        User user = userSession.isUserLoggedIn();

        btnConfirm.setOnClickListener(v -> {
            if(edtTxtAddr.getText().toString().length()<1||edtCreditCardID.getText().toString().length()<1){
                txtWarning.setText("Please enter all fields.");
                txtWarning.setVisibility(View.VISIBLE);
                return;
            }

            if(edtCreditCardID.getText().toString().length()!=16){
                txtWarning.setText("Please enter the correct Credit Card ID.");
                txtWarning.setVisibility(View.VISIBLE);
                return;
            }


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
            exchange.setPrice(inPrice);

            exchange.setType(String.valueOf(inType));
            boolean b = exchangeDAO.addExchange(exchange);
            if (b) {
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
            Intent intent1 = new Intent(this, HomeActivity.class);
            startActivity(intent1);
        });
    }

    private void initViews() {
        txtPrice = findViewById(R.id.txtPrice);
        edtCreditCardID = findViewById(R.id.edtCreditCardID);
        edtTxtAddr = findViewById(R.id.edtTxtAddr);
        btnConfirm = findViewById(R.id.btnConfirm);
        txtWarning = findViewById(R.id.txtWarning);
    }
}
