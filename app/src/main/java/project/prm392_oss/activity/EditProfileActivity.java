package project.prm392_oss.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.view.MenuItem;
import androidx.annotation.NonNull;

import project.prm392_oss.activity.BaseActivity;

import project.prm392_oss.R;
import project.prm392_oss.dao.UserDAO;
import project.prm392_oss.database.AppDatabase;
import project.prm392_oss.entity.User;

public class EditProfileActivity extends BaseActivity {

    private EditText etUsername, etFullName, etEmail, etPhone, etAddress;
    private Button btnSave, changePasswordBtn;
    private int userId;
    private AppDatabase db;
    private UserDAO userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_EditProfile);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Edit Profile");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        db = AppDatabase.getInstance(this);
        userDao = db.userDAO();

        SharedPreferences prefs = getSharedPreferences("USER_PREFS", MODE_PRIVATE);
        userId = prefs.getInt("USER_ID", -1);

        etUsername = findViewById(R.id.username);
        etFullName = findViewById(R.id.full_name);
        etEmail = findViewById(R.id.email);
        etPhone = findViewById(R.id.phone);
        etAddress = findViewById(R.id.address);
        btnSave = findViewById(R.id.save_changes_btn);
        changePasswordBtn = findViewById(R.id.change_password_btn);

        if (userId != -1) {
            loadUserData(userId);
        } else {
            Toast.makeText(this, "Lỗi: Không tìm thấy User ID!", Toast.LENGTH_SHORT).show();
        }

        btnSave.setOnClickListener(v -> validateAndSaveUserData());
        changePasswordBtn.setOnClickListener(v -> {
            Intent intent = new Intent(EditProfileActivity.this, ChangePasswordActivity.class);
            startActivity(intent);
        });
    }

    private void loadUserData(int userId) {
        new Thread(() -> {
            User user = userDao.getUserByid(userId);
            runOnUiThread(() -> {
                if (user != null) {
                    etUsername.setText(user.getUsername());
                    etFullName.setText(user.getName());
                    etEmail.setText(user.getEmail());
                    etPhone.setText(user.getPhone());
                    etAddress.setText(user.getAddress());
                } else {
                    Toast.makeText(EditProfileActivity.this, "Không tìm thấy thông tin!", Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }

    private void validateAndSaveUserData() {
        String username = etUsername.getText().toString().trim();
        String fullName = etFullName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String address = etAddress.getText().toString().trim();

        if (!validateInputs(username, fullName, email, phone, address)) {
            return;
        }

        new Thread(() -> {
            User existingUser = userDao.getUserByid(userId);
            if (existingUser != null) {
                existingUser.setUsername(username);
                existingUser.setName(fullName);
                existingUser.setEmail(email);
                existingUser.setPhone(phone);
                existingUser.setAddress(address);

                userDao.update(existingUser);

                runOnUiThread(() -> {
                    Toast.makeText(EditProfileActivity.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                    loadUserData(userId);
                });
            }
        }).start();
    }

    private boolean validateInputs(String username, String fullName, String email, String phone, String address) {
        if (username.isEmpty() || username.length() < 3) {
            etUsername.setError("Tên tài khoản phải có ít nhất 3 ký tự!");
            etUsername.requestFocus();
            return false;
        }

        if (fullName.isEmpty()) {
            etFullName.setError("Hãy điền họ tên");
            etFullName.requestFocus();
            return false;
        }

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Email không hợp lệ!");
            etEmail.requestFocus();
            return false;
        }

        if (phone.isEmpty() || !phone.matches("[0-9]+") || phone.length() < 9 || phone.length() > 12) {
            etPhone.setError("Số điện thoại không hợp lệ!");
            etPhone.requestFocus();
            return false;
        }

        if (address.isEmpty()) {
            etAddress.setError("Hãy điền địa chỉ!");
            etAddress.requestFocus();
            return false;
        }

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
