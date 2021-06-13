package org.samir.universitybazaar.Activity.Loan;

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

import org.samir.universitybazaar.Database.LoanDAO;
import org.samir.universitybazaar.Models.Loan;
import org.samir.universitybazaar.R;
import org.samir.universitybazaar.Utility.Constants;

public class EditLoanActivity extends AppCompatActivity {
    private TextView edtTxtTitle, edtTxtDescription, edtTxtPrice;
    private Button btnCancel, btnEdit, btnImgUpload;
    private LoanDAO lo;
    private Loan loan;
    private int loan_id;
    private String imagePath;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_loan);

        loan_id = getIntent().getIntExtra(Constants.LOAN_ID,-1); // get club id
        lo = new LoanDAO(EditLoanActivity.this);


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
        System.out.println("id:"+loan_id);
        if (loan_id>=0){

            loan = lo.getLoanById(loan_id);
            edtTxtTitle.setText(loan.getTitle());
            edtTxtDescription.setText(loan.getDescription());
            edtTxtPrice.setText(loan.getPrice());
            imagePath = loan.getImage();
        } else {
            Toast.makeText(EditLoanActivity.this, "Cannot find this loan", Toast.LENGTH_LONG).show();
        }
    }

    private void handleListeners() {
        //cancel edit and return back to LoanDetailActivity
        btnCancel.setOnClickListener(v->{
            Intent intent = new Intent(EditLoanActivity.this, LoanDetailActivity.class);
            intent.putExtra(Constants.LOAN_ID,loan_id);
            startActivity(intent);
        });
        //update the information of club and return back to ClubActivity
        btnEdit.setOnClickListener(v->{
            System.out.println(imagePath);
            loan.setTitle(edtTxtTitle.getText().toString());
            loan.setDescription(edtTxtDescription.getText().toString());
            loan.setPrice(edtTxtPrice.getText().toString());
            loan.setImage(imagePath);
            boolean flag = lo.updateLoan(loan);
            if (flag){
                Toast.makeText(EditLoanActivity.this, "Save Loan Information Successfully!", Toast.LENGTH_LONG).show();
                //navigate back to the ClubActivity
                Intent intent = new Intent(EditLoanActivity.this,LoanDetailActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra(Constants.LOAN_ID,loan_id);
                startActivity(intent);
            }else {
                Toast.makeText(EditLoanActivity.this, "Save Loan Information Failed! Please Try again!", Toast.LENGTH_LONG).show();
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
