package org.samir.universitybazaar.Activity.Loan;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import org.samir.universitybazaar.Activity.Clubs.ClubActivity;
import org.samir.universitybazaar.Activity.Clubs.EditClubActivity;
import org.samir.universitybazaar.Activity.Clubs.MyClubsActivity;
import org.samir.universitybazaar.Database.ClubDAO;
import org.samir.universitybazaar.Database.LoanDAO;
import org.samir.universitybazaar.Database.UserSession;
import org.samir.universitybazaar.Models.Loan;
import org.samir.universitybazaar.Models.User;
import org.samir.universitybazaar.R;
import org.samir.universitybazaar.Utility.Constants;

import java.io.File;

public class LoanDetailActivity extends AppCompatActivity {

    private TextView txtPostTitle,txtPostDescription,txtEdit, txtDelete, txtCreatorName, txtCreatedDate, priceText;
    private ImageView imageView;
    private int loan_id;
    private UserSession userSession;
    private LoanDAO dao;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_detail);

        initViews();

        loan_id = getIntent().getIntExtra(Constants.LOAN_ID,-1);
        dao = new LoanDAO(this);
        userSession = new UserSession(this);

        User user = userSession.isUserLoggedIn();
        if(user != null){ //user is logged in.
            String memberId = user.getMemberId(); //get the logged in users memberId.
            Loan loan = dao.getLoanById(loan_id); //get the loan with the provided loanId from the database.

            if(loan != null){ // found a loan with the provided loanId.

                //If the user didn't create this loan then don't allow them to edit or delete this loan.
                if(!loan.getCreatorId().equals(memberId)){
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

                //initialize the layout with all the data from the retrieved loan.
                txtPostTitle.setText(loan.getTitle());
                Uri uri = Uri.fromFile(new File(loan.getImage()));
                imageView.setImageURI(uri);
                txtPostDescription.setText(loan.getDescription());
                txtCreatorName.setText("Posted by: " + loan.getCreatorName());
                txtCreatedDate.setText(loan.getCreatedDate());
                priceText.setText(loan.getPrice());

                // handle edit, delete  and add comment button clicks.
                /**
                 * @author minyi lu
                 * @discription open edit loan item page
                 */
                txtEdit.setOnClickListener(v->{
                    System.out.println("edit");
                    Intent intent = new Intent(LoanDetailActivity.this, EditLoanActivity.class);
                    intent.putExtra(Constants.LOAN_ID,loan_id);
                    startActivity(intent);
                });
                /**
                 * @author minyi lu
                 * @discription delete loan
                 */
                txtDelete.setOnClickListener(v->{
                    handleDeleteLoan();

                });

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

    private void handleDeleteLoan(){
        System.out.println("delete");
        AlertDialog.Builder normalDialog = new AlertDialog.Builder(LoanDetailActivity.this);
        normalDialog.setTitle("Delete");
        normalDialog.setMessage("Are your sure you want to delete ?");
        //Delete the post and back
        normalDialog.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                LoanDAO lo = new LoanDAO(LoanDetailActivity.this);
                boolean flag = lo.deleteLoan(loan_id);
                if (flag){
                    Toast.makeText(LoanDetailActivity.this, "Delete Successfully!", Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(LoanDetailActivity.this, MyLoanItemListActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoanDetailActivity.this, "Delete failed!", Toast.LENGTH_LONG).show();
                }



            }
        });
        //Stay on the current page
        normalDialog.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(LoanDetailActivity.this, "Cancel", Toast.LENGTH_LONG).show();
                    }
                });
        normalDialog.show();
    }


}