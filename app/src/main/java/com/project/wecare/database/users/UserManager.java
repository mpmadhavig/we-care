package com.project.wecare.database.users;

import android.content.Context;

import com.project.wecare.models.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class UserManager {
    private static User currentUser;
    private static final User anonymousUser = new User("EN");

    private static final String USER_DATA_FILE = "userdata.ser";

    // Singleton
    private static UserManager instance;

    private UserManager() {
    }

    public static UserManager getInstance() {
        if (instance == null) instance = new UserManager();
        return instance;
    }

    public static User getAnonymousUser() {
        return anonymousUser;
    }

    public boolean saveUser(Context context) {
        try {
            System.out.println("Saving User");
            // Save in the path returned by getFilesDir()
            FileOutputStream fos = context.openFileOutput(USER_DATA_FILE, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(currentUser);
            oos.close();
            fos.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public User getSavedUser(Context context) {
        try {
            FileInputStream fis = context.openFileInput(USER_DATA_FILE);
            ObjectInputStream ois = new ObjectInputStream(fis);
            currentUser = (User) ois.readObject();
            ois.close();
            fis.close();
        } catch (Exception e) {
            currentUser = anonymousUser;
            currentUser.setAuthenticated(false);
            saveUser(context);
        }
        return currentUser;
    }

    //upon success login set currentUser to the new login user
    public void completeLogin(JSONObject o, Context context) {
        try {
            currentUser = new User(
                    o.getString("name"),
                    o.getString("nic"),
                    o.getString("licenseNo"),
                    o.getString("contactNo"),
                    o.getString("address"),
                    o.getString("occupation"),
//                    o.getString("licenseExp"), // Todo: To be added
                    o.getString("preferredLocale"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public User getCurrentUser() {
        return currentUser;
    }


    public void logoutUser(Context context) {
        currentUser.setAuthenticated(false);
        saveUser(context);
    }

}
