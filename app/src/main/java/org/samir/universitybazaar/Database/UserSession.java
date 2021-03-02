package org.samir.universitybazaar.Database;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.samir.universitybazaar.Authentication.LoginActivity;
import org.samir.universitybazaar.Models.User;

import java.lang.reflect.Type;

/**
 * @author Samir Shrestha
 * @description This class is responsible for creating and maintaining a user session once the user logs in.
 * It uses shared preference to create a sessionData. loggedUser resides inside sessionData. sessionData is like a database.
 * if user is logged in the loggedUser will contain data about that user if not then loggedUser will be null.
 * We can use this to figure out if the user is logged in or not.
 */
public class UserSession {
    private static final String TAG = "UserSession"; //for debugging and logging.
    private static final String SESSION_DATA = "sessionData"; // Internal data store containing information about the current logged in user.
    private static final String LOGGED_USER = "loggedUser"; // Refers to the current logged in user.

    private Context context; //application context refers to the calling activity.

    public UserSession(Context context){
        this.context = context;
    }

    //saves user info to the shared preferences variable if login was successful.
    public boolean createUserSession(User user){
        Log.d(TAG,"create user session: adding user:" + user.toString());

        //create a shared preference name SESSION_DATA
        SharedPreferences sharedPreferences = context.getSharedPreferences(SESSION_DATA, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        //Save the current users data into the shared preference. User object is converted and saved as a JSON in the shared preferences.
        Gson gson = new Gson();
        editor.putString(LOGGED_USER,gson.toJson(user));
        editor.apply();
        //user data successfully saved to shared preferences so return true indicating that session creation is successful.
        return true;
    }

    //Check if there is a user logged in. If yes then return that user as a User object to the caller. If no then return null.
    public User isUserLoggedIn(){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SESSION_DATA,Context.MODE_PRIVATE);
        Gson gson = new Gson();
        Type type = new TypeToken<User>(){}.getType();
        return gson.fromJson(sharedPreferences.getString(LOGGED_USER,null),type);
    }

    //Destroy the login session by removing user object from the shared preferences.
    public void signOutUser() {
        Log.d(TAG, "signOutUser: started");
        SharedPreferences sharedPreferences = context.getSharedPreferences(SESSION_DATA, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.remove(LOGGED_USER);
        editor.commit();

        //navigate back to the Login page after the user is logged out.
        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    //Utility method for the application. Will provide a handle to the shared preference which can be used to access data about the current
    // logged in user.
    public SharedPreferences getSession(){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SESSION_DATA, Context.MODE_PRIVATE);
        return sharedPreferences;
    }
}
