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

import org.samir.universitybazaar.Activity.HomeActivity;
import org.samir.universitybazaar.Activity.pay.PayActivity;
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
            String price = priceText.getText().toString();
            if(inType==2){
                BigDecimal result = new BigDecimal(priceText.getText().toString()).multiply(new BigDecimal(edtTxtDay.getText().toString()));
                price = result.toString();
            }
            Intent payIntent = new Intent(this, PayActivity.class);
            //pass the sellId the ItemDetailActivity page. This will allow us to retrieve all the details of the clicked detail inside the ItemDetailActivity page.
            payIntent.putExtra("objId", inId);
            payIntent.putExtra("objType", inType);
            payIntent.putExtra("objNum", edtTxtDay.getText().toString());
            payIntent.putExtra("objPrice", price);

            startActivity(payIntent);
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