package project.prm392_oss.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import project.prm392_oss.R;
import project.prm392_oss.entity.User;
import project.prm392_oss.viewModel.UserViewModel;
import project.prm392_oss.adapter.UserAdapter;

import java.util.ArrayList;
import java.util.List;

public class ListEmployeeActivity extends AppCompatActivity {

    private UserViewModel userViewModel;
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private Button createEmployeeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_employee);

        // Check if ActionBar is not null before calling methods on it
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        recyclerView = findViewById(R.id.recyclerView);
        createEmployeeButton = findViewById(R.id.CreateEmployee);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize ViewModel
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        userViewModel.getAllUsers().observe(this, users -> {
            List<User> filteredUsers = new ArrayList<>();
            for (User user : users) {
                int roleId = user.getRole_id();
                if (roleId == 1 || roleId == 2 || roleId == 3) {
                    filteredUsers.add(user);
                }
            }

            // Initialize the adapter with context and ViewModel
            userAdapter = new UserAdapter(filteredUsers, ListEmployeeActivity.this, userViewModel);
            recyclerView.setAdapter(userAdapter);
        });

        createEmployeeButton.setOnClickListener(v -> {
            Intent intent = new Intent(ListEmployeeActivity.this, AddEmployeeActivity.class);
            startActivity(intent);
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
            onBackPressed();
            return true;
        }
        if (item.getItemId() == R.id.nav_employee_management) {
            startActivity(new Intent(ListEmployeeActivity.this, ListEmployeeActivity.class));
            return true;
        } else if (item.getItemId() == R.id.nav_customer_management) {
            startActivity(new Intent(ListEmployeeActivity.this, ListCustomerActivity.class));
            return true;
        } else if (item.getItemId() == R.id.nav_product_management) {
            startActivity(new Intent(ListEmployeeActivity.this, ListProductActivity.class));
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}
