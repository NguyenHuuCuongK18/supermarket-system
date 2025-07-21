package project.prm392_oss.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.Observer;

import com.bumptech.glide.Glide;

import project.prm392_oss.R;
import project.prm392_oss.entity.User;
import project.prm392_oss.viewModel.UserViewModel;
import project.prm392_oss.activity.ListUsersActivity;


public class EmployeeDetailActivity extends AppCompatActivity {

    private TextView tvId, tvName, tvEmail, tvPhone, tvRole;
    private ImageView ivProfile;
    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_detail);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        tvId = findViewById(R.id.tvId);
        tvName = findViewById(R.id.tvName);
        tvEmail = findViewById(R.id.tvEmail);
        tvPhone = findViewById(R.id.tvPhone);
        tvRole = findViewById(R.id.tvRole);
        ivProfile = findViewById(R.id.ivProfile);

        int userId = getIntent().getIntExtra("userId", -1);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);


        userViewModel.getUserById(userId).observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (user != null) {
                    tvId.setText("ID: " + user.getUser_id());
                    tvName.setText("Name: " + user.getName());
                    tvEmail.setText("Email: " + user.getEmail());
                    tvPhone.setText("Phone: " + user.getPhone());

                    userViewModel.getRoleName(user.getRole_id()).observe(EmployeeDetailActivity.this, new Observer<String>() {
                        @Override
                        public void onChanged(String roleName) {
                            tvRole.setText("Position: " + roleName);
                        }
                    });

                        ivProfile.setImageResource(R.drawable.defaultimage);
                }
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
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        if (item.getItemId() == R.id.nav_user_management) {
            startActivity(new Intent(EmployeeDetailActivity.this, ListUsersActivity.class));
            return true;
        } else if (item.getItemId() == R.id.nav_product_management) {
            startActivity(new Intent(EmployeeDetailActivity.this, ListProductActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
