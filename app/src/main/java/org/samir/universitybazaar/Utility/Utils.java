package org.samir.universitybazaar.Utility;

import android.content.Context;

import org.samir.universitybazaar.Database.ClubDAO;
import org.samir.universitybazaar.Database.ProfileDAO;
import org.samir.universitybazaar.Database.UserSession;
import org.samir.universitybazaar.Models.Profile;
import org.samir.universitybazaar.Models.User;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Samir Shrestha
 * @description A utility class that contains various utility methods to use throughout the application.
 */
public class Utils {

    // returns the current system date in mm/dd/yyyy format as a string.
    public static String getCurrentDate(){
        //Getting the current system date and formatting it to mm/dd/yyyy format.
        Date date = new Date();
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        return df.format(date);
    }

    //check if a user owns a club or a post by checking their creatorId with the current logged in users memberId.
    public static boolean isOwner(String creatorId,String memberId){
        return creatorId.equals(memberId);
    }

    //return the current logged in user from the session. returns null if no user logged in
    public static User getLoggedInUser(Context context){
        UserSession session = new UserSession(context);
        User user = session.isUserLoggedIn();
        return user;
    }

    //get the full name of the current logged in user
    public static String getUserFullName(Context context){
        User user = getLoggedInUser(context);
        if(user != null){
            ProfileDAO profileDAO = new ProfileDAO(context);
            Profile profile = profileDAO.getProfile(user.getMemberId());
            return profile.getFullName();
        }
        return null;
    }

}
