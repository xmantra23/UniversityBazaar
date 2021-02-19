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

import org.samir.universitybazaar.Database.UserSession;
import org.samir.universitybazaar.Profile.ViewProfileActivity;
import org.samir.universitybazaar.R;

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
            // TODO: 2/18/2021 handle post arrow pressed
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
                    //default is homepage
                    break;
                case R.id.post:
                    // TODO: 2/18/2021 redirect to all posts activity
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
