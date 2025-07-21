package project.prm392_oss.utils.manager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import project.prm392_oss.activity.LoginActivity;

public class SessionManager {
    public static void logout(Activity activity) {
        SharedPreferences prefs = activity.getSharedPreferences("USER_PREFS", Context.MODE_PRIVATE);
        prefs.edit().clear().apply();

        Intent intent = new Intent(activity, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);
        Toast.makeText(activity, "Logged out successfully", Toast.LENGTH_SHORT).show();
    }
}