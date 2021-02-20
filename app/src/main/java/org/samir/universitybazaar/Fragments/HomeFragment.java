package org.samir.universitybazaar.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.samir.universitybazaar.Activity.HomeActivity;
import org.samir.universitybazaar.Database.UserSession;
import org.samir.universitybazaar.Activity.MyPostsActivity;
import org.samir.universitybazaar.Profile.ViewProfileActivity;
import org.samir.universitybazaar.R;
import org.samir.universitybazaar.Utility.Constants;

/**
 * @author Samir Shrestha
 * @Description This class is basically the main content of the HomeActivity. It has the main content in the middle and includes a bottom
 * navigation bar in the bottom of the fragment. The main content in the middle navigates to other activities whereas the clicking on the
 * bottom navigation views should make the page stay on HomeActivity but only refresh the middle fragment part with new data.
 */
public class HomeFragment extends Fragment {
    private BottomNavigationView bottomNavigationView;
    private ImageView manageClubArrow,manageSubArrow,managePostArrow,manageSaleArrow,managePurchaseArrow,manageLoanArrow;
    private Button btnProfile,btnLogout;

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
        manageClubArrow.setOnClickListener(v -> {
            // TODO: 2/18/2021 handle club arrow pressed
        });
        manageSubArrow.setOnClickListener(v -> {
            // TODO: 2/18/2021 handle subscription arrow pressed
        });
        managePostArrow.setOnClickListener(v->{
            Intent intent = new Intent(getActivity(), MyPostsActivity.class);
            startActivity(intent);
        });
        manageSaleArrow.setOnClickListener(v->{
            // TODO: 2/18/2021 handle sale arrow pressed
        });
        managePurchaseArrow.setOnClickListener(v->{
            // TODO: 2/18/2021 handle purchase arrow pressed
        });
        manageLoanArrow.setOnClickListener(v->{
            // TODO: 2/18/2021 handle loan arrow pressed
        });

        btnProfile.setOnClickListener(v->{
            Intent intent = new Intent(getActivity(), ViewProfileActivity.class);
            startActivity(intent);
        });

        btnLogout.setOnClickListener(v->{
            UserSession userSession = new UserSession(getContext());
            userSession.signOutUser();
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
        btnProfile = view.findViewById(R.id.btnProfile);
        btnLogout = view.findViewById(R.id.btnLogout);
    }

    private void initBottomNavView(View view) {
        bottomNavigationView.setSelectedItemId(R.id.home);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch(item.getItemId()){
                case R.id.home:
                    //In homepage. No action required.
                    break;
                case R.id.post:
                    Intent intent = new Intent(getActivity(), HomeActivity.class);
                    intent.putExtra(Constants.ACTIVITY_NAME,"post");
                    startActivity(intent);
                    break;
                case R.id.group:
                    //TODO: 2/18/2021 redirect to all clubs activity
                    break;
                case R.id.market:
                    // TODO: 2/18/2021 redirect to the markeplace activity
                    break;
                default:
                    break;
            }
            return false;
        });
    }
}
