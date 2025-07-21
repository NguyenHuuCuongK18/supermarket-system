package project.prm392_oss.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import project.prm392_oss.R;
import project.prm392_oss.entity.User;
import project.prm392_oss.viewModel.UserViewModel;
import project.prm392_oss.viewModel.RoleViewModel;
import project.prm392_oss.adapter.UserAdapter;
import project.prm392_oss.entity.Role;
import java.util.ArrayList;
import java.util.List;

public class ListEmployeeActivity extends AppCompatActivity {

    private UserViewModel userViewModel;
    private RoleViewModel roleViewModel;
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private Button createEmployeeButton;
    private Spinner spFilterRole;
    private final List<User> allUsers = new ArrayList<>();
    private List<Role> roleOptions = new ArrayList<>();

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
        spFilterRole = findViewById(R.id.spFilterRole);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize ViewModels
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        roleViewModel = new ViewModelProvider(this).get(RoleViewModel.class);

        roleViewModel.getRolesForEmployees().observe(this, roles -> {
            roleOptions.clear();
            roleOptions.add(new Role(-1, "All Roles"));
            roleOptions.addAll(roles);
            ArrayAdapter<Role> adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_item, roleOptions);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spFilterRole.setAdapter(adapter);
        });

        userViewModel.getAllUsers().observe(this, users -> {
            allUsers.clear();
            for (User user : users) {
                if (user.getRole_id() != 4) { // exclude customers
                    allUsers.add(user);
                }
            }
            filterUsers();
        });

        spFilterRole.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, android.view.View view, int position, long id) {
                filterUsers();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
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

    private void filterUsers() {
        if (userAdapter == null) {
            userAdapter = new UserAdapter(new ArrayList<>(), ListEmployeeActivity.this, userViewModel);
            recyclerView.setAdapter(userAdapter);
        }

        Role selectedRole = (Role) spFilterRole.getSelectedItem();
        int selectedRoleId = selectedRole != null ? selectedRole.getRole_id() : -1;
        List<User> filtered = new ArrayList<>();
        for (User user : allUsers) {
            if (selectedRoleId == -1 || user.getRole_id() == selectedRoleId) {
                filtered.add(user);
            }
        }
        userAdapter.updateData(filtered);
    }
}
