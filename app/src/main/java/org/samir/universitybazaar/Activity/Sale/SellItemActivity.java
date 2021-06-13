package org.samir.universitybazaar.Activity.Sale;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.samir.universitybazaar.Activity.HomeActivity;
import org.samir.universitybazaar.Activity.Posts.CreatePostActivity;
import org.samir.universitybazaar.Authentication.LoginActivity;
import org.samir.universitybazaar.Database.DatabaseHelper;
import org.samir.universitybazaar.Database.SellDAO;
import org.samir.universitybazaar.Database.UserSession;
import org.samir.universitybazaar.Models.Post;
import org.samir.universitybazaar.Models.Profile;
import org.samir.universitybazaar.Models.Sell;
import org.samir.universitybazaar.Models.User;
import org.samir.universitybazaar.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SellItemActivity extends AppCompatActivity {

    private static final String TAG = "SELL_I";
    private EditText edtTxtTitle,edtTxtDescription;
    private Button btnCancel,btnPost;
    private UserSession userSession;
    private DatabaseHelper db;
    private Button btnImgUpload;
    private String imagePath;
    private EditText edtTxtPrice;
    private SellDAO sellDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_item);

        userSession = new UserSession(this);
        db = new DatabaseHelper(this);
        sellDAO = new SellDAO(this);
        initViews();
        handleListeners();
    }

    private void handleListeners() {
        //redirect back to HomePage if user clicks on cancel button.
        btnCancel.setOnClickListener(v->{
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        });

        btnImgUpload.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");// type of picture
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("outputX", 360);
            intent.putExtra("outputY", 360);
            intent.putExtra("scale", true);
            intent.putExtra("return-data", true);
            // intent.putExtra("outputFormat",
            // Bitmap.CompressFormat.JPEG.toString());
            intent.putExtra("noFaceDetection", true); // no face detection
            startActivityForResult(intent, 1);
        });

        btnPost.setOnClickListener(v->{
            User user = userSession.isUserLoggedIn(); //get current logged in user from the session.
            if(user != null){ //if user is logged in.

                //get all the details user typed in the user interface and then create and build a sell object.
                Sell sell = new Sell();
                sell.setTitle(edtTxtTitle.getText().toString());
                sell.setDescription(edtTxtDescription.getText().toString());
                sell.setImage(this.imagePath);
                sell.setPrice(edtTxtPrice.getText().toString());

                Profile profile = db.getProfile(user.getMemberId()); //get the profile of the current logged in user from the database.
                String creatorName = profile.getFullName();

                //if the user doesn't have a full name then construct a username as: User + last four of the member id.
                if(creatorName == null){
                    creatorName = "User " + user.getMemberId().substring(6);
                }

                sell.setCreatorName(creatorName); //set the name of the user who created this sale.
                sell.setCreatorId(user.getMemberId());//this will be used as the id to search for a given users sales in the database.

                //Getting the current system date and formating it to mm/dd/yyyy format.
                Date date = new Date();
                DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                String createdDate = df.format(date);

                sell.setCreatedDate(createdDate);//setting the current system date as the sale created date.

                //adding the new sale in the database.
                if(sellDAO.addSell(sell)){ //insert successful
                    //Redirect to HomeActivity
                    Intent intent = new Intent(SellItemActivity.this,HomeActivity.class);
                    startActivity(intent);
                    Toast.makeText(this, "Sale successfully created.", Toast.LENGTH_LONG).show();
                }else{ //insert failed
                    //Redirect to HomeActivity
                    Intent intent = new Intent(SellItemActivity.this,HomeActivity.class);
                    startActivity(intent);
                    Toast.makeText(this, "Error. Please try again.", Toast.LENGTH_LONG).show();
                }
            }else{ //user is not logged in. Redirect to the login page.
                Toast.makeText(this, "You are not logged in.", Toast.LENGTH_LONG).show();
                //Redirect to LoginActivity
                Intent intent = new Intent(SellItemActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                String path = this.handleImageOnKitKat(data);
                System.out.println(path);
                String s = path.substring(path.lastIndexOf("/")+1);
                System.out.println(s);
                btnImgUpload.setText(s);
                this.imagePath = path;
                break;
            default:
                break;
        }
    }

    private String handleImageOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();

        if (DocumentsContract.isDocumentUri(this, uri)) {
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                //Log.d(TAG, uri.toString());
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                //Log.d(TAG, uri.toString());
                Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            //Log.d(TAG, "content: " + uri.toString());
            imagePath = getImagePath(uri, null);
        }
        return imagePath;
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }

            cursor.close();
        }
        return path;
    }

    private void initViews() {
        edtTxtTitle = findViewById(R.id.edtTxtTitle);
        edtTxtDescription = findViewById(R.id.edtTxtDescription);
        btnCancel = findViewById(R.id.btnCancel);
        btnPost = findViewById(R.id.btnPost);
        btnImgUpload = findViewById(R.id.btnImgUpload);
        edtTxtPrice = findViewById(R.id.edtTxtPrice);
    }
}