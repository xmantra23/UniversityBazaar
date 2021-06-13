package org.samir.universitybazaar.Activity.Sale;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.samir.universitybazaar.Activity.Loan.EditLoanActivity;
import org.samir.universitybazaar.Activity.Loan.LoanDetailActivity;
import org.samir.universitybazaar.Database.LoanDAO;
import org.samir.universitybazaar.Database.SellDAO;
import org.samir.universitybazaar.Models.Sell;
import org.samir.universitybazaar.R;
import org.samir.universitybazaar.Utility.Constants;

public class EditSaleActivity extends AppCompatActivity {
    private TextView edtTxtTitle, edtTxtDescription, edtTxtPrice;
    private Button btnCancel, btnEdit, btnImgUpload;
    private String imagePath;
    private int sell_id;
    private SellDAO se;
    private Sell sell;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_sell);

        sell_id = getIntent().getIntExtra(Constants.SELL_ID,-1); // get club id
        se = new SellDAO(EditSaleActivity.this);


        initViews();
        initTexts();
        handleListeners();
    }

    private void initViews(){
        edtTxtTitle = findViewById(R.id.edtTxtTitle);
        edtTxtDescription = findViewById(R.id.edtTxtDescription);
        edtTxtPrice = findViewById(R.id.edtTxtPrice);
        btnCancel = findViewById(R.id.btnCancel);
        btnEdit = findViewById(R.id.btnEdit);
        btnImgUpload = findViewById(R.id.btnImgUpload);
    }

    private void initTexts(){
        System.out.println("id:"+sell_id);
        if (sell_id>=0){

            sell = se.getSellById(sell_id);
            edtTxtTitle.setText(sell.getTitle());
            edtTxtDescription.setText(sell.getDescription());
            edtTxtPrice.setText(sell.getPrice());
            imagePath = sell.getImage();
        } else {
            Toast.makeText(EditSaleActivity.this, "Cannot find this sale", Toast.LENGTH_LONG).show();
        }
    }

    private void handleListeners() {
        //cancel edit and return back to LoanDetailActivity
        btnCancel.setOnClickListener(v->{
            Intent intent = new Intent(EditSaleActivity.this, SellDetailActivity.class);
            intent.putExtra(Constants.SELL_ID,sell_id);
            startActivity(intent);
        });
        //update the information of sale and return back to SellDetailActivity
        btnEdit.setOnClickListener(v->{
            System.out.println(imagePath);
            sell.setTitle(edtTxtTitle.getText().toString());
            sell.setDescription(edtTxtDescription.getText().toString());
            sell.setPrice(edtTxtPrice.getText().toString());
            sell.setImage(imagePath);
            boolean flag = se.updateSale(sell);
            if (flag){
                Toast.makeText(EditSaleActivity.this, "Save Sale Information Successfully!", Toast.LENGTH_LONG).show();
                //navigate back to the ClubActivity
                Intent intent = new Intent(EditSaleActivity.this,SellDetailActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra(Constants.SELL_ID,sell_id);
                startActivity(intent);
            }else {
                Toast.makeText(EditSaleActivity.this, "Save Sale Information Failed! Please Try again!", Toast.LENGTH_LONG).show();
            }
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
}
