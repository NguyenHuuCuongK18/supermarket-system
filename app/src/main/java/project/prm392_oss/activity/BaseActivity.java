package project.prm392_oss.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.content.SharedPreferences;

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
    public boolean onPrepareOptionsMenu(Menu menu) {
        adjustMenuForRole(menu);
        return super.onPrepareOptionsMenu(menu);
    }

    protected void adjustMenuForRole(Menu menu) {
        SharedPreferences prefs = getSharedPreferences("USER_PREFS", MODE_PRIVATE);
        String role = prefs.getString("USER_ROLE", "");
        if (role == null) role = "";

        MenuItem userItem = menu.findItem(R.id.nav_user_management);
        MenuItem productItem = menu.findItem(R.id.nav_product_management);
        MenuItem orderItem = menu.findItem(R.id.nav_order_management);
        MenuItem supplierItem = menu.findItem(R.id.nav_supplier_management);

        if ("Manager".equals(role)) {
            if (userItem != null) userItem.setVisible(true);
            if (supplierItem != null) supplierItem.setVisible(true);
            if (productItem != null) productItem.setVisible(false);
            if (orderItem != null) orderItem.setVisible(false);
        } else if ("Employee".equals(role)) {
            if (userItem != null) userItem.setVisible(false);
            if (supplierItem != null) supplierItem.setVisible(false);
            if (productItem != null) productItem.setVisible(true);
            if (orderItem != null) orderItem.setVisible(true);
        } else {
            if (userItem != null) userItem.setVisible(false);
            if (supplierItem != null) supplierItem.setVisible(false);
            if (productItem != null) productItem.setVisible(false);
            if (orderItem != null) orderItem.setVisible(false);
        }
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