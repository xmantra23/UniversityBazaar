package org.samir.universitybazaar.Database;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.samir.universitybazaar.Authentication.LoginActivity;
import org.samir.universitybazaar.Authentication.RegisterActivity;
import org.samir.universitybazaar.Models.User;

import java.lang.reflect.Type;

public class UserSession {
    private static final String TAG = "UserSession";
    private static final String SESSION_DATA = "sessionData";
    private static final String LOGGED_USER = "loggedUser";

    private Context context;

    public UserSession(Context context){
        this.context = context;
    }

    public boolean createUserSession(User user){
        Log.d(TAG,"create user session: adding user:" + user.toString());
        SharedPreferences sharedPreferences = context.getSharedPreferences(SESSION_DATA, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();
        editor.putString(LOGGED_USER,gson.toJson(user));
        editor.apply();
        return true;
    }

    public User isUserLoggedIn(){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SESSION_DATA,Context.MODE_PRIVATE);
        Gson gson = new Gson();
        Type type = new TypeToken<User>(){}.getType();
        return gson.fromJson(sharedPreferences.getString(LOGGED_USER,null),type);
    }

    public void signOutUser() {
        Log.d(TAG, "signOutUser: started");
        SharedPreferences sharedPreferences = context.getSharedPreferences(SESSION_DATA, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.remove(LOGGED_USER);
        editor.commit();
        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    public SharedPreferences getSession(){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SESSION_DATA, Context.MODE_PRIVATE);
        return sharedPreferences;
    }
}
