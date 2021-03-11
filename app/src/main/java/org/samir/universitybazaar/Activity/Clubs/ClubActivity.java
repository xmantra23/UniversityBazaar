package org.samir.universitybazaar.Activity.Clubs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.samir.universitybazaar.Activity.HomeActivity;
import org.samir.universitybazaar.Activity.MainActivity;
import org.samir.universitybazaar.Adapter.ClubNoticeAdapter;
import org.samir.universitybazaar.Adapter.ClubPostsAdapter;
import org.samir.universitybazaar.Database.ClubDAO;
import org.samir.universitybazaar.Database.UserSession;
import org.samir.universitybazaar.Models.Club;
import org.samir.universitybazaar.Models.ClubNotice;
import org.samir.universitybazaar.Models.ClubPost;
import org.samir.universitybazaar.Models.User;
import org.samir.universitybazaar.R;
import org.samir.universitybazaar.Utility.Constants;
import org.samir.universitybazaar.Utility.Utils;

import java.util.ArrayList;

/**
 * @author samir shrestha
 * displays the clubs details with all the announcements and posts.
 */
public class ClubActivity extends AppCompatActivity {
    private TextView txtClubTitle,txtClubDescription,txtCreatorName,txtCreatedDate;
    private TextView txtPost,txtEdit,txtDelete;
    private TextView txtViewAllNotice,txtViewAllPosts;
    private RecyclerView noticeRecView,postRecView;
    private ClubNoticeAdapter clubNoticeAdapter;
    private ClubPostsAdapter clubPostsAdapter;
    private ClubDAO cb;
    private int clubId;

    private UserSession session;
    private User user;
    private boolean isOwner = false; // flag to check if user owns this club.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club);
        initializeViews(); //initialize all the elements in activity_club.xml

        session = new UserSession(this); //get the current session data.
        user = session.isUserLoggedIn(); //get the logged in user.
        if(user != null){ //check if user is logged in before doing anything.
            Intent intent = getIntent(); //receiving the intent from the caller activity.
            if(intent != null){
                clubId = intent.getIntExtra(Constants.CLUB_ID,-1);//get the clubId from the calling intent.
                if(clubId != -1){//checking for valid clubId
                    cb = new ClubDAO(this);
                    Club club = cb.getClubById(clubId); //get the club with the clubId from the database.
                    if(club != null){
                        initializeUI(club); //set all the text fields in the club UI.
                        isOwner = Utils.isOwner(club.getOwnerId(),user.getMemberId()); //is true if user owns this club
                    }

                }
            }
            //only owner of the club can edit or delete this club.
            if(!isOwner){
                txtEdit.setVisibility(View.GONE);
                txtDelete.setVisibility(View.GONE);
            }

        }
        handleListeners();// handle all listeners for the text button the activity.
        handleRecViews(); //populate announcements and posts inside clubs activity.

    }

    private void handleListeners() {
        //navigate to ClubNoticesActivity which displays all the announcements for this club.
        txtViewAllNotice.setOnClickListener(v->{
            Intent intent = new Intent(ClubActivity.this, ClubNoticesActivity.class);
            intent.putExtra(Constants.CLUB_ID,clubId);
            startActivity(intent);
        });

        //navigate to ClubPostsActivity which displays all the member posts made within a club.
        txtViewAllPosts.setOnClickListener(v->{
            Intent intent = new Intent(ClubActivity.this, ClubPostsActivity.class);
            intent.putExtra(Constants.CLUB_ID,clubId);
            startActivity(intent);
        });
    }

    private void handleRecViews() {
        //initializing the recycler view which will display all the announcements in this clubs.
        clubNoticeAdapter = new ClubNoticeAdapter(this);
        clubPostsAdapter = new ClubPostsAdapter(this);

        //get the top three notices for this club and display it in the clubs activity.
        ArrayList<ClubNotice> notices = cb.getClubNotices(clubId);
        if(notices != null){
            clubNoticeAdapter.setNotices(getTopThreeNotices(notices));
            noticeRecView.setAdapter(clubNoticeAdapter);
            noticeRecView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,true));
        }

        //get the top three posts for this club and display it in the clubs activity.
        ArrayList<ClubPost> clubPosts = cb.getClubPosts(clubId);
        if(clubPosts != null){
            clubPostsAdapter.setClubPosts(getTopThreePosts(clubPosts));
            postRecView.setAdapter(clubPostsAdapter);
            postRecView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,true));
        }

    }

    //gets the latest three values added to the notices arraylist.
    private ArrayList<ClubNotice> getTopThreeNotices(ArrayList <ClubNotice> notices){
        int totalNotices = notices.size();
        if(totalNotices < 3){
            return notices;
        }else{
            ArrayList<ClubNotice> topThreeNotices = new ArrayList<>();
            for(int i = totalNotices - 3; i <totalNotices ; i++){
                topThreeNotices.add(notices.get(i));
            }
            return topThreeNotices;
        }
    }

    //gets the latest three values added to the clubPosts arraylist.
    private ArrayList<ClubPost> getTopThreePosts(ArrayList <ClubPost> clubPosts){
        int totalPosts = clubPosts.size();
        if(totalPosts < 3){
            return clubPosts;
        }else{
            ArrayList<ClubPost> topThreePosts = new ArrayList<>();
            for(int i = totalPosts - 3; i <totalPosts ; i++){
                topThreePosts.add(clubPosts.get(i));
            }
            return topThreePosts;
        }
    }

    private void initializeUI(Club club) {
        txtClubTitle.setText(club.getTitle());
        txtClubDescription.setText(club.getLongDescription());
        txtCreatorName.setText("Admin: " + club.getOwnerName());
        txtCreatedDate.setText("Formed: " + club.getCreatedDate());
        txtPost.setOnClickListener(v->{
            if(isOwner){
                Intent intent = new Intent(ClubActivity.this,CreateClubNoticeActivity.class);
                intent.putExtra(Constants.CLUB_ID,club.get_id());
                startActivity(intent);
            }else{
                Intent intent = new Intent(ClubActivity.this, PostInClubActivity.class);
                intent.putExtra(Constants.CLUB_ID,club.get_id());
                startActivity(intent);
            }
        });
        txtEdit.setOnClickListener(v->{
            //// TODO: 3/8/2021 handle editing post. only admin should be allowed to edit
        });
        txtDelete.setOnClickListener(v->{
            // TODO: 3/8/2021 handle delete post. only admin should be allowed to delete.
        });

    }

    private void initializeViews() {
        txtClubTitle = findViewById(R.id.txtClubTitle);
        txtClubDescription = findViewById(R.id.txtClubDescription);
        txtCreatorName = findViewById(R.id.txtCreatorName);
        txtCreatedDate = findViewById(R.id.txtCreatedDate);
        txtPost = findViewById(R.id.txtPost);
        txtEdit = findViewById(R.id.txtEdit);
        txtDelete = findViewById(R.id.txtDelete);
        txtViewAllNotice = findViewById(R.id.txtViewAllNotice);
        txtViewAllPosts = findViewById(R.id.txtViewAllPosts);
        noticeRecView = findViewById(R.id.noticeRecView);
        postRecView = findViewById(R.id.postsRecView);
    }

    @Override
    public void onBackPressed() {
        //navigate to HomeActivity if the user presses back button.
        Intent intent = new Intent(ClubActivity.this, HomeActivity.class);
        intent.putExtra(Constants.ACTIVITY_NAME,Constants.GROUP);
        startActivity(intent);
    }
}
