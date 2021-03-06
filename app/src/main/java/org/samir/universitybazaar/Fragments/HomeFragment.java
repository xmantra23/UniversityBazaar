package org.samir.universitybazaar.Fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.samir.universitybazaar.Activity.HomeActivity;
import org.samir.universitybazaar.Activity.Clubs.MyClubsActivity;
import org.samir.universitybazaar.Activity.Clubs.MySubscriptionsActivity;
import org.samir.universitybazaar.Activity.Loan.MyLoanItemListActivity;
import org.samir.universitybazaar.Activity.Messages.MessageHomeActivity;
import org.samir.universitybazaar.Activity.Sale.MySaleItemListActivity;
import org.samir.universitybazaar.Adapter.AdvertisementAdapter;
import org.samir.universitybazaar.Database.MessageDAO;
import org.samir.universitybazaar.Database.UserSession;
import org.samir.universitybazaar.Activity.Posts.MyPostsActivity;
import org.samir.universitybazaar.Activity.Profile.ViewProfileActivity;
import org.samir.universitybazaar.Models.User;
import org.samir.universitybazaar.R;
import org.samir.universitybazaar.Utility.Constants;
import org.samir.universitybazaar.Utility.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Samir Shrestha
 * @Description This class is basically the main content of the HomeActivity. It has the main content in the middle and includes a bottom
 * navigation bar in the bottom of the fragment. The main content in the middle contains buttons to navigate to other activities,
 * whereas clicking on the bottom navigation icon should make the page stay on HomeActivity but only reload the middle fragment part with the
 * fragment corresponding to the bottom navigation icon that was pressed.
 */
public class HomeFragment extends Fragment {
    private BottomNavigationView bottomNavigationView;
    private ImageView manageClubArrow,manageSubArrow,managePostArrow,manageSaleArrow,managePurchaseArrow,manageLoanArrow;
    private TextView txtProfile,txtLogout,txtMessages;

    /**
     * @author YiFei Lu
     */
    private ViewPager advertisement;
    private LinearLayout point;
    private List<View> viewList;
    private AdvertisementAdapter adAdapter;
    private List<ImageView> pointList;
    private Object[][] picIds = {
            {R.mipmap.kfc,"https://www.kfc.com/"},
            {R.mipmap.sky,"https://thatgamecompany.com/sky/"},
            {R.mipmap.apple,"https://www.apple.com/"},
            {R.mipmap.piza,"https://www.pizzahut.com/index.php#/home"},
            {R.mipmap.advertisement,"null"}
    };

    //Timer that can help advertisement move itself
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            if(msg.what == 1){
                int current_item = advertisement.getCurrentItem();
                advertisement.setCurrentItem(current_item+1);
                handler.sendEmptyMessageDelayed(1,5000);
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);
        initViews(view);
        initBottomNavView(view);
        handleListeners(view);
        return view;
    }

    private void handleListeners(View view) {
        //navigate to myclubsactivity and show all clubs that the user has created.
        manageClubArrow.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), MyClubsActivity.class);
            startActivity(intent);
        });

        //navigate to MySubscriptionsActivity and show all the clubs that the user is subscribed to.
        manageSubArrow.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), MySubscriptionsActivity.class);
            startActivity(intent);
        });

        //navigate to mypostsactivity and show all posts that the user has created.
        managePostArrow.setOnClickListener(v->{
            Intent intent = new Intent(getActivity(), MyPostsActivity.class);
            startActivity(intent);
        });
        manageSaleArrow.setOnClickListener(v->{
            Intent intent = new Intent(getActivity(), MySaleItemListActivity.class);
            startActivity(intent);
            // TODO: 2/18/2021 handle sale arrow pressed
        });
        managePurchaseArrow.setOnClickListener(v->{
            // TODO: 2/18/2021 handle purchase arrow pressed
        });
        manageLoanArrow.setOnClickListener(v->{
            // TODO: 2/18/2021 handle loan arrow pressed
            Intent intent = new Intent(getActivity(), MyLoanItemListActivity.class);
            startActivity(intent);
        });

        //profile button was pressed. navigate to ViewProfileActivity
        txtProfile.setOnClickListener(v->{
            Intent intent = new Intent(getActivity(), ViewProfileActivity.class);
            startActivity(intent);
        });

        //logout button was pressed. Call the signOutUser() method in UserSession to destroy the session data about the current user.
        txtLogout.setOnClickListener(v->{
            UserSession userSession = new UserSession(getContext());
            userSession.signOutUser();
        });
        
        //handle messages
        txtMessages.setOnClickListener(v->{
            Intent intent = new Intent(getActivity(), MessageHomeActivity.class);
            startActivity(intent);
        });
    }

    private void initViews(View view) {
        bottomNavigationView = view.findViewById(R.id.bottomNavView);
        manageClubArrow = view.findViewById(R.id.manageClubArrow);
        manageSubArrow = view.findViewById(R.id.manageSubArrow);
        managePostArrow = view.findViewById(R.id.managePostArrow);
        manageSaleArrow = view.findViewById(R.id.manageSaleArrow);
        managePurchaseArrow = view.findViewById(R.id.managePurchaseArrow);
        manageLoanArrow = view.findViewById(R.id.manageLoanArrow);
        txtProfile = view.findViewById(R.id.txtProfile);
        txtLogout = view.findViewById(R.id.txtLogout);
        txtMessages = view.findViewById(R.id.txtMessages);
        advertisement = view.findViewById(R.id.advertisement);
        point = view.findViewById(R.id.point);

        /**
         * @author Samir Shrestha
         */
        //get total number of unread message for this users and show it in the parenthesis inside the message text in the Home UI.
        MessageDAO db = new MessageDAO(getContext());
        User user = Utils.getLoggedInUser(getContext());
        if(user != null && db != null){
            int unreadMessageCount = db.getUnreadMessagesCount(user.getMemberId());
            txtMessages.setText("MESSAGES (" + unreadMessageCount + ")");
        }

        /**
         * @author:YIfei Lu
         * */
        //init the point that index which picture is showing
        pointList = new ArrayList<>();
        //init the Viewpager's views
        viewList = new ArrayList<>();
        for(int i=0;i<picIds.length;i++){
            View adView = LayoutInflater.from(getActivity()).inflate(R.layout.item_advertisement,null);
            ImageView iv = adView.findViewById(R.id.item_advertisement);
            iv.setImageResource((Integer) picIds[i][0]);
            String website = (String) picIds[i][1];
            iv.setOnClickListener((ImageView)->{handleImageListener(website);});
            viewList.add(adView);
            //point
            ImageView pointIv = new ImageView(getActivity());
            LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0,0,20,0);
            pointIv.setLayoutParams(lp);
            pointIv.setImageResource(R.mipmap.no);
            pointList.add(pointIv);
            point.addView(pointIv);
        }
        pointList.get(0).setImageResource(R.mipmap.yes);//set the first point as select

        adAdapter = new AdvertisementAdapter(viewList);
        advertisement.setAdapter(adAdapter);

        handler.sendEmptyMessageDelayed(1,5000);
        setVPListener();
    }

    //handles the bottom navigation view icon presses.
    private void initBottomNavView(View view) {
        bottomNavigationView.setSelectedItemId(R.id.home); //highlight the home icon in the bottom navigation view.

        //all cases will redirect to home activity but we are providing information about which icon was pressed before starting the intent.
        // for example if home icon is pressed navigate to home activity but also provide "home" as the activity name in the intent.
        //if group icon is pressed then navigate to home activity but send "group" as the activity name.
        //This will be used later in the HomeActivity to load the appropriate fragment in the body of the activity based on the activity name.
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Intent intent = new Intent(getActivity(), HomeActivity.class);
            switch(item.getItemId()){
                case R.id.home:
                    //In homepage. No action required.
                    break;
                case R.id.post:
                    //Navigate to HomeActivity but send "post" as the activity name so that AllPostsFragment can be loaded into the HomeActivity.
                    intent.putExtra(Constants.ACTIVITY_NAME,"post");
                    startActivity(intent);
                    break;
                case R.id.group:
                    //Navigate to HomeActivity but send "group" as the activity name so that AllClubsFragment can be loaded into the HomeActivity.
                    intent.putExtra(Constants.ACTIVITY_NAME,"group");
                    startActivity(intent);
                    break;
                case R.id.market:
                    intent.putExtra(Constants.ACTIVITY_NAME,"item");
                    startActivity(intent);
                    break;
                default:
                    break;
            }
            return false;
        });
    }

    /**
     * @author :Yifei Lu
     * */
    private void setVPListener(){
        //point listener
        advertisement.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                for(int i=0;i<pointList.size();i++){
                    pointList.get(i).setImageResource(R.mipmap.no);
                }
                pointList.get(position%pointList.size()).setImageResource(R.mipmap.yes);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void handleImageListener(String url){
        //the ImageView's Listener that can link to the url provided
        if(!url.equals("null")){
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }

    }
}
