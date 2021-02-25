package org.samir.universitybazaar.Profile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.samir.universitybazaar.Database.DatabaseHelper;
import org.samir.universitybazaar.Database.ProfileDAO;
import org.samir.universitybazaar.Database.UserSession;
import org.samir.universitybazaar.Models.Profile;
import org.samir.universitybazaar.Models.User;
import org.samir.universitybazaar.R;

public class EditProfileActivity extends AppCompatActivity {

    private static final String TAG = "edit Profile";
    private ImageView avatar;
    private EditText ediTxtName, ediTxtAddress, ediTxtPhone,ediTxtDOB;
    private TextView txtWarning;
    private Button saveProfile, cancelProfile, changeAvatar;
    private RadioGroup selectGender;
    private RadioButton ediTextGender;
    private DatabaseHelper databaseHelper;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        UserSession userSession = new UserSession(this); //gets the handle to the shared preferences
        user = userSession.isUserLoggedIn();

        initViews();
        initListeners();
    }


    private void initViews() {

        avatar = findViewById(R.id.avatar);
        ediTxtName = findViewById(R.id.ediTxtName);
        ediTxtAddress = findViewById(R.id.ediTxtAddress);
        ediTxtPhone = findViewById(R.id.ediTxtPhone);
        ediTxtDOB = findViewById(R.id.ediTxtDOB);

        txtWarning = findViewById(R.id.txtWarning);

        selectGender = findViewById(R.id.selectGender);
        selectGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                ediTextGender = radioGroup.findViewById(i);
            }
        });

        cancelProfile = findViewById(R.id.cancelProfile);
        saveProfile = findViewById(R.id.saveProfile);
        changeAvatar = findViewById(R.id.changeAvatar);

        ProfileDAO profileDAO = new ProfileDAO(EditProfileActivity.this);

        // I don't do the get data from database in thread,if need,please let me know
        if(user != null) {
            Profile profile = profileDAO.getProfile(user.getMemberId());
            String ediTxtName = null2String(profile.getFullName());
            String gender = null2String(profile.getGender());
            String ediTxtAddress = null2String(profile.getAddress());
            String ediTxtPhone = null2String(profile.getPhone());
            String ediTxtDOB = null2String(profile.getDob());
            String avatar = null2String(profile.getAvatar());

            Log.i(TAG, avatar);


            this.ediTxtName.setText(ediTxtName);
            // gender default setting only when it have chosen
            if(!gender.equals("")){
                String[] genderList = new String[]{"man","women","other"};
                for(int i=0;i<this.selectGender.getChildCount();i++){
                    this.ediTextGender =(RadioButton) this.selectGender.getChildAt(i);
                    if(gender.equals(genderList[i])){
                        this.ediTextGender.setChecked(true);
                        break;
                    }
                }
            }
            this.ediTxtAddress.setText(ediTxtAddress);
            this.ediTxtPhone.setText(ediTxtPhone);
            this.ediTxtDOB.setText(ediTxtDOB);


            //            if image name is not set then choose default else set image from database.
            if(avatar.equals("")){
                this.avatar.setImageResource(R.mipmap.girl); //default image.
            }else{
                setImage(avatar); //set image from database.
            }

        }
    }

    private void initListeners() {
        cancelProfile.setOnClickListener((view)->{handleCancelProfile();});
        saveProfile.setOnClickListener((view)->{handleSaveButton();});
        changeAvatar.setOnClickListener((view)->{handleChangeAvatar();});
    }

    private void handleSaveButton() {
        //get the input from user and trim all whitespaces.
        if(ediTextGender == null){
            txtWarning.setVisibility(View.VISIBLE);
            txtWarning.setText("Please enter all the fields");
        }
        else {
            String fullName = ediTxtName.getText().toString().trim();
            String gender = ediTextGender.getText().toString().trim();
            String address = ediTxtAddress.getText().toString().trim();
            String phone = ediTxtPhone.getText().toString().trim();
            String dob = ediTxtDOB.getText().toString().trim();

            //input validation
            if(fullName.equals("") || gender.equals("") || address.equals("") || phone.equals("") || dob.equals("")){
                txtWarning.setVisibility(View.VISIBLE);
                txtWarning.setText("Please enter all the fields");
            }else if(fullName.length() > 50){
                txtWarning.setVisibility(View.VISIBLE);
                txtWarning.setText("FullName must be at-most 50 chars long");
            }else if(address.length() < 10 ){
                txtWarning.setVisibility(View.VISIBLE);
                txtWarning.setText("Address must be at-least 10 chars long");
            }else if(address.length() > 100){
                txtWarning.setVisibility(View.VISIBLE);
                txtWarning.setText("Address must be at-most 100 chars long");
            }else if(!(phone.length() == 10)){
                txtWarning.setVisibility(View.VISIBLE);
                txtWarning.setText("Phone numbers must be 10 chars long");
            }else if(!isValidDate(dob)){
                txtWarning.setVisibility(View.VISIBLE);
                txtWarning.setText("Please provide a valid birthday");
            }else {
                //All the inputs are valid.
                txtWarning.setVisibility(View.GONE);

                //Pop a dialog to user to make confirmation of save operation
                AlertDialog.Builder normalDialog = new AlertDialog.Builder(EditProfileActivity.this);
                normalDialog.setTitle("Save Profile Confirm");
                normalDialog.setMessage("Are your sure you want to save profile ?");
                normalDialog.setPositiveButton("sure",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Profile profile = new Profile();
                                profile.setMemberId(user.getMemberId().toString().trim());
                                profile.setFullName(fullName);
                                profile.setGender(gender);
                                profile.setAddress(address);
                                profile.setPhone(phone);
                                profile.setDob(formatDate(dob));

                                ProfileDAO profileDAO = new ProfileDAO(EditProfileActivity.this);

                                EditProfileActivity.SaveProfile updateProfile = new EditProfileActivity.SaveProfile(profileDAO, profile);
                                Thread thread = new Thread(updateProfile);
                                thread.start(); //update background task started.


                                Toast.makeText(EditProfileActivity.this, "Save Profile Successfully!", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(EditProfileActivity.this, ViewProfileActivity.class);
                                startActivity(intent);
                            }
                        });
                normalDialog.setNegativeButton("cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(EditProfileActivity.this, "cancel", Toast.LENGTH_LONG).show();
                            }
                        });
                normalDialog.show();
        }



        }
    }

    private Boolean isValidDate(String date){
        String[] date_str = date.split("/");

        int month = Integer.parseInt(date_str[0]);
        int day = Integer.parseInt(date_str[1]);
        int year = Integer.parseInt(date_str[2]);

        if(year%4==0 && year%100 !=0){
            if(month == 2){
                if(day <= 29){
                    return true;
                }
            }
            else if(month == 1||month == 3|| month == 5|| month==7||month==8||month==10||month==12){
                if (day <= 31){
                    return true;
                }
            }
            else if(month == 4||month==6||month==9||month==11){
                if(day <= 30){
                    return true;
                }
            }
        }else{
            if(month == 2){
                if(day <= 28){
                    return true;
                }
            }
            else if(month == 1||month == 3|| month == 5|| month==7||month==8||month==10||month==12){
                if (day <= 31){
                    return true;
                }
            }
            else if(month == 4||month==6||month==9||month==11){
                if(day <= 30){
                    return true;
                }
            }
        }
        return false;
    }

    private String formatDate(String date){
        String[] date_str = date.split("/");
        return TextUtils.join("/",date_str);
    }

    private void handleCancelProfile(){
        Intent intent = new Intent(EditProfileActivity.this, ViewProfileActivity.class);
        startActivity(intent);
    }

    private void handleChangeAvatar(){
        Intent intent = new Intent(EditProfileActivity.this, AvatarChangeActivity.class);
        startActivity(intent);
    }

    private String null2String(String str){
        if(str == null){
            return "";
        }else{
            return str;
        }
    }

    //handles background process to save the profile
    private class SaveProfile implements Runnable{
        private ProfileDAO profileDAO;
        private Profile profile;
        public SaveProfile(ProfileDAO profileDAO,Profile profile){
            this.profileDAO = profileDAO;
            this.profile = profile;
        }

        @Override
        public void run() {
            if (!profileDAO.updateProfile(profile)) {
                //database operation err
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        txtWarning.setVisibility(View.VISIBLE);
                        txtWarning.setText("Couldn't update profile. Please try again");
                    }
                });
            }
        }

    }

    private void setImage(String avatar){
        switch (avatar){
            case "bear": this.avatar.setImageResource(R.mipmap.bear);
                break;
            case "beauty": this.avatar.setImageResource(R.mipmap.beauty);
                break;
            case "blue": this.avatar.setImageResource(R.mipmap.blue);
                break;
            case "cool": this.avatar.setImageResource(R.mipmap.cool);
                break;
            case "dog": this.avatar.setImageResource(R.mipmap.dog);
                break;
            case "duck": this.avatar.setImageResource(R.mipmap.duck);
                break;
            case "girl": this.avatar.setImageResource(R.mipmap.girl);
                break;
            case "purple": this.avatar.setImageResource(R.mipmap.purple);
                break;
            case "terrible": this.avatar.setImageResource(R.mipmap.terrible);
                break;
            default:
                break;
        }
    }

}
