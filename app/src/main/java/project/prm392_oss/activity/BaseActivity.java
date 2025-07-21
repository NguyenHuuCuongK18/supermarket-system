package project.prm392_oss.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import project.prm392_oss.R;
import project.prm392_oss.utils.manager.SessionManager;

public class BaseActivity extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        } else if (item.getItemId() == R.id.action_view_profile) {
            startActivity(new Intent(this, EditProfileActivity.class));
            return true;
        } else if (item.getItemId() == R.id.action_logout) {
            SessionManager.logout(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}