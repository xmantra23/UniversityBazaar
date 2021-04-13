package org.samir.universitybazaar.Activity.Sale;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.samir.universitybazaar.Database.DatabaseHelper;
import org.samir.universitybazaar.Database.SellDAO;
import org.samir.universitybazaar.Database.UserSession;
import org.samir.universitybazaar.Models.Sell;
import org.samir.universitybazaar.Models.User;
import org.samir.universitybazaar.R;
import org.samir.universitybazaar.Utility.Constants;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class SellDetailActivity extends AppCompatActivity {

    private TextView txtPostTitle,txtPostDescription,txtEdit, txtDelete, txtCreatorName, txtCreatedDate, priceText;
    private ImageView imageView;
    private int sell_id;
    private UserSession userSession;
    private SellDAO dao;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_detail);

        initViews();

        sell_id = getIntent().getIntExtra(Constants.SELL_ID,-1);
        dao = new SellDAO(this);
        userSession = new UserSession(this);

        User user = userSession.isUserLoggedIn();
        if(user != null){ //user is logged in.
            String memberId = user.getMemberId(); //get the logged in users memberId.
            Sell sell = dao.getSellById(sell_id); //get the post with the provided postId from the database.

            if(sell != null){ // found a post with the provided postId.

                //If the user didn't create this post then don't allow them to edit or delete this post.
                if(!sell.getCreatorId().equals(memberId)){
                    txtEdit.setVisibility(View.GONE);
                    txtDelete.setVisibility(View.GONE);
                }

                // Check if we have write permission
                int permission = ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.READ_EXTERNAL_STORAGE);
                if (permission != PackageManager.PERMISSION_GRANTED) {
                    // We don't have permission so prompt the user
                    ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE,
                            REQUEST_EXTERNAL_STORAGE);
                }

                //initialize the layout with all the data from the retrieved post.
                txtPostTitle.setText(sell.getTitle());
                Uri uri = Uri.fromFile(new File(sell.getImage()));
                imageView.setImageURI(uri);
                txtPostDescription.setText(sell.getDescription());
                txtCreatorName.setText("Posted by: " + sell.getCreatorName());
                txtCreatedDate.setText(sell.getCreatedDate());
                priceText.setText(sell.getPrice());

                // handle edit, delete  and add comment button clicks.
            }
        }
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
    }


}