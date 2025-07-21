package project.prm392_oss.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import project.prm392_oss.activity.BaseActivity;

import project.prm392_oss.R;
import project.prm392_oss.dao.UserDAO;
import project.prm392_oss.database.AppDatabase;
import project.prm392_oss.entity.User;

public class ChangePasswordActivity extends BaseActivity {

    private EditText etOldPassword, etNewPassword, etConfirmPassword;
    private Button btnChangePassword;
    private int userId;
    private AppDatabase db;
    private UserDAO userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_ChangePassword);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Change Password");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Khởi tạo Room Database
        db = AppDatabase.getInstance(this);
        userDao = db.userDAO();

        // Lấy USER_ID từ SharedPreferences
        SharedPreferences prefs = getSharedPreferences("USER_PREFS", MODE_PRIVATE);
        userId = prefs.getInt("USER_ID", -1);


        // Ánh xạ các view
        etOldPassword = findViewById(R.id.old_password);
        etNewPassword = findViewById(R.id.new_password);
        etConfirmPassword = findViewById(R.id.confirm_password);
        btnChangePassword = findViewById(R.id.change_password_btn);

        // Sự kiện đổi mật khẩu
        btnChangePassword.setOnClickListener(v -> changePassword());
    }

    private void changePassword() {
        String oldPassword = etOldPassword.getText().toString().trim();
        String newPassword = etNewPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        if (oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            Toast.makeText(this, "Mật khẩu xác nhận không khớp!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (newPassword.length() < 6) {
            Toast.makeText(this, "Mật khẩu mới phải có ít nhất 6 ký tự!", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(() -> {
            User user = userDao.getUserByid(userId);
            if (user == null) {
                runOnUiThread(() -> Toast.makeText(this, "Không tìm thấy tài khoản!", Toast.LENGTH_SHORT).show());
                return;
            }

            if (!user.getPassword().equals(oldPassword)) {
                runOnUiThread(() -> Toast.makeText(this, "Mật khẩu cũ không chính xác!", Toast.LENGTH_SHORT).show());
                return;
            }

            user.setPassword(newPassword);
            userDao.update(user);

            runOnUiThread(() -> {
                Toast.makeText(this, "Đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show();
                finish();
            });
        }).start();
    }
}
