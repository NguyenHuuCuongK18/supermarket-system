package project.prm392_oss.activity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import project.prm392_oss.activity.BaseActivity;
import androidx.lifecycle.ViewModelProvider;

import project.prm392_oss.R;
import project.prm392_oss.entity.Role;
import project.prm392_oss.entity.User;
import project.prm392_oss.viewModel.RoleViewModel;
import project.prm392_oss.viewModel.UserViewModel;

public class AddEmployeeActivity extends BaseActivity {

    private EditText edtUsername, edtPassword, edtName, edtEmail, edtPhone, edtAddress;
    private Spinner spRole;
    private Button btnSave;


    private UserViewModel userViewModel;
    private RoleViewModel roleViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_employee);

        if (getSupportActionBar() != null) {
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        edtName = findViewById(R.id.edtName);
        edtEmail = findViewById(R.id.edtEmail);
        edtPhone = findViewById(R.id.edtPhone);
        edtAddress = findViewById(R.id.edtAddress);
        spRole = findViewById(R.id.spRole);
        btnSave = findViewById(R.id.btnSave);


        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        roleViewModel = new ViewModelProvider(this).get(RoleViewModel.class);

        String currentRole = getSharedPreferences("USER_PREFS", MODE_PRIVATE)
                .getString("USER_ROLE", "");

        roleViewModel.getRolesForEmployees().observe(this, roles -> {
            java.util.List<Role> filtered = new java.util.ArrayList<>();
            for (Role r : roles) {
                if ("Manager".equals(currentRole)) {
                    if (!"Customer".equals(r.getName())) {
                        filtered.add(r);
                    }
                } else if ("Employee".equals(currentRole)) {
                    if ("Employee".equals(r.getName())) {
                        filtered.add(r);
                    }
                }
            }
            ArrayAdapter<Role> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, filtered);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spRole.setAdapter(adapter);
        });


        btnSave.setOnClickListener(v -> {
            String username = edtUsername.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();
            String name = edtName.getText().toString().trim();
            String email = edtEmail.getText().toString().trim();
            String phone = edtPhone.getText().toString().trim();
            String address = edtAddress.getText().toString().trim();

            Role selectedRole = (Role) spRole.getSelectedItem();
            int roleId = selectedRole != null ? selectedRole.getRole_id() : 0;

            if (username.isEmpty() || username.length() < 4) {
                edtUsername.setError("Tên đăng nhập phải có ít nhất 4 ký tự");
                edtUsername.requestFocus();
                return;
            }

            if (password.isEmpty() || password.length() < 6) {
                edtPassword.setError("Mật khẩu phải có ít nhất 6 ký tự");
                edtPassword.requestFocus();
                return;
            }

            if (name.isEmpty()) {
                edtName.setError("Vui lòng nhập họ tên");
                edtName.requestFocus();
                return;
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                edtEmail.setError("Email không hợp lệ");
                edtEmail.requestFocus();
                return;
            }

            if (!phone.matches("0\\d{9}")) {
                edtPhone.setError("Số điện thoại không hợp lệ (VD: 0123456789)");
                edtPhone.requestFocus();
                return;
            }

            if (address.isEmpty()) {
                edtAddress.setError("Vui lòng nhập địa chỉ");
                edtAddress.requestFocus();
                return;
            }

            User user = new User(0, username, password, name, email, phone, address, roleId);
            userViewModel.insert(user);

            Toast.makeText(this, "Nhân viên đã được thêm", Toast.LENGTH_SHORT).show();
            finish();
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
