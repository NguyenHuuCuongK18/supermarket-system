package project.prm392_oss.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import project.prm392_oss.R;
import project.prm392_oss.entity.User;
import project.prm392_oss.viewModel.UserViewModel;

public class CustomerDetailActivity extends AppCompatActivity {

    private TextView tvId, tvName, tvEmail, tvPhone, tvAddress, tvRole;
    private ImageView ivProfile;
    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Initialize the views
        tvId = findViewById(R.id.tvId);
        tvName = findViewById(R.id.tvName);
        tvEmail = findViewById(R.id.tvEmail);
        tvPhone = findViewById(R.id.tvPhone);
        tvAddress = findViewById(R.id.tvAddress);
        tvRole = findViewById(R.id.tvRole);
        ivProfile = findViewById(R.id.ivProfile);

        int userId = getIntent().getIntExtra("userId", -1);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        userViewModel.getUserById(userId).observe(this, user -> {
            if (user != null) {
                tvId.setText("ID: " + user.getUser_id());
                tvName.setText("Name: " + user.getName());
                tvEmail.setText("Email: " + user.getEmail());
                tvPhone.setText("Phone: " + user.getPhone());
                tvAddress.setText("Address: " + user.getAddress());
                ivProfile.setImageResource(R.drawable.defaultimage);

                userViewModel.getRoleName(user.getRole_id()).observe(this, roleName -> {
                    tvRole.setText("Role: " + roleName);
                });
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        if (id == R.id.nav_employee_management) {
            startActivity(new Intent(CustomerDetailActivity.this, ListEmployeeActivity.class));
            return true;
        } else if (id == R.id.nav_product_management) {
            startActivity(new Intent(CustomerDetailActivity.this, ListProductActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
