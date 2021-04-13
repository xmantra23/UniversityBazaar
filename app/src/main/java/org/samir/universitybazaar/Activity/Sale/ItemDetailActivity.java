package org.samir.universitybazaar.Activity.Sale;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import org.samir.universitybazaar.Database.ExchangeDAO;
import org.samir.universitybazaar.Database.SellDAO;
import org.samir.universitybazaar.Database.UserSession;
import org.samir.universitybazaar.Models.Exchange;
import org.samir.universitybazaar.Models.Sell;
import org.samir.universitybazaar.Models.User;
import org.samir.universitybazaar.R;
import org.samir.universitybazaar.Utility.Constants;

import java.io.File;
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
    private int inType, inId;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private Button btnBuy;
    private RelativeLayout dayRelLayout;

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
            String memberId = user.getMemberId(); //get the logged in users memberId.
            Sell sell = dao.getSellById(sell_id); //get the sell with the provided sellId from the database.

            if (sell != null) { // found a sell with the provided Id.

                //If the user didn't create this sale then don't allow them to edit or delete this post.
//                if (!sell.getCreatorId().equals(memberId)) {
//                    txtEdit.setVisibility(View.GONE);
//                    txtDelete.setVisibility(View.GONE);
//                }

                // Check if we have write permission
                int permission = ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.READ_EXTERNAL_STORAGE);
                if (permission != PackageManager.PERMISSION_GRANTED) {
                    // We don't have permission so prompt the user
                    ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE,
                            REQUEST_EXTERNAL_STORAGE);
                }

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
                if(inType != 1){// 1 是代表sell类型
                    dayRelLayout.setVisibility(View.INVISIBLE);
                }
                // handle edit, delete  and add comment button clicks.
            }
        }

        btnBuy.setOnClickListener(v -> {
            if(inType==1){//sell
                ExchangeDAO exchangeDAO = new ExchangeDAO(this);
                SellDAO sellDAO = new SellDAO(this);

                Sell sell = sellDAO.getSellById(inId);

                Exchange exchange = new Exchange();
                exchange.setCustomerId(user.getId());
                exchange.setSellerId(Integer.parseInt(sell.getCreatorId()));
                Date date = new Date();
                DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                String createdDate = df.format(date);
                exchange.setExchangeDate(createdDate);
                exchange.setItemId(inId);
                exchange.setPrice(priceText.getText().toString());
                exchange.setType(String.valueOf(inType));
                boolean b = exchangeDAO.addExchange(exchange);
                if(b){
                    btnBuy.setEnabled(false);
                    Toast.makeText(this, "Success!", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(this, "Failure!", Toast.LENGTH_LONG).show();
                }
            }else if(inType==2){//loan

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
        dayRelLayout = findViewById(R.id.dayRelLayout);
    }


}