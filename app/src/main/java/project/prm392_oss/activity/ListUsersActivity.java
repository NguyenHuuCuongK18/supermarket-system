package project.prm392_oss.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.view.View;


import project.prm392_oss.activity.BaseActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import project.prm392_oss.R;
import project.prm392_oss.entity.User;
import project.prm392_oss.utils.manager.SessionManager;
import project.prm392_oss.viewModel.UserViewModel;
import project.prm392_oss.viewModel.RoleViewModel;
import project.prm392_oss.adapter.UserAdapter;
import project.prm392_oss.entity.Role;
import java.util.ArrayList;
import java.util.List;
import project.prm392_oss.activity.SupplierListActivity;

public class ListUsersActivity extends BaseActivity {

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
        setContentView(R.layout.activity_list_users);

        // Action bar should not show back button on first screen
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
        }

        recyclerView = findViewById(R.id.recyclerView);
        createEmployeeButton = findViewById(R.id.CreateEmployee);
        spFilterRole = findViewById(R.id.spFilterRole);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        String currentRole = getSharedPreferences("USER_PREFS", MODE_PRIVATE)
                .getString("USER_ROLE", "");
        if (!"Manager".equals(currentRole)) {
            createEmployeeButton.setVisibility(View.GONE);
        }

        // Initialize ViewModels
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        roleViewModel = new ViewModelProvider(this).get(RoleViewModel.class);

        roleViewModel.getAllRoles().observe(this, roles -> {
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
            if (users != null) {
                allUsers.addAll(users);
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
            Intent intent = new Intent(ListUsersActivity.this, AddEmployeeActivity.class);
            startActivity(intent);
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == android.R.id.home) {
//            onBackPressed();
//            return true;
//        }
        if (item.getItemId() == R.id.nav_user_management) {
            startActivity(new Intent(ListUsersActivity.this, ListUsersActivity.class));
            return true;
        } else if (item.getItemId() == R.id.nav_product_management) {
            startActivity(new Intent(ListUsersActivity.this, ListProductActivity.class));
            return true;
        }
        else if (item.getItemId() == R.id.nav_supplier_management) {
            startActivity(new Intent(ListUsersActivity.this, SupplierListActivity.class));
            return true;
        } else if (item.getItemId() == R.id.nav_order_management) {
            startActivity(new Intent(ListUsersActivity.this, ListOrdersActivity.class));
            return true;
        } else if (item.getItemId() == R.id.view_profile) {
            Intent intent = new Intent(ListUsersActivity.this, EditProfileActivity.class);
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.logout) {
            SessionManager.logout(this);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void filterUsers() {
        if (userAdapter == null) {
            userAdapter = new UserAdapter(new ArrayList<>(), ListUsersActivity.this, userViewModel);
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
