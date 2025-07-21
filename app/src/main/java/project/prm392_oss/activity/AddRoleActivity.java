package project.prm392_oss.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

import project.prm392_oss.activity.BaseActivity;
import androidx.lifecycle.ViewModelProvider;

import project.prm392_oss.R;
import project.prm392_oss.entity.Role;
import project.prm392_oss.viewModel.RoleViewModel;

public class AddRoleActivity extends BaseActivity {

    private EditText edtRoleName;
    private Button btnSaveRole;
    private RoleViewModel roleViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_role);

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        edtRoleName = findViewById(R.id.edtRoleName);
        btnSaveRole = findViewById(R.id.btnSaveRole);

        roleViewModel = new ViewModelProvider(this).get(RoleViewModel.class);

        btnSaveRole.setOnClickListener(v -> {
            String roleName = edtRoleName.getText().toString().trim();

            if (roleName.isEmpty()) {
                Toast.makeText(AddRoleActivity.this, "Please enter a role name", Toast.LENGTH_SHORT).show();
            } else {
                Role role = new Role(roleName);
                roleViewModel.insertRole(role);

                Toast.makeText(AddRoleActivity.this, "Role has been added", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
