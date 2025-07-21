package project.prm392_oss.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.view.MenuItem;

import project.prm392_oss.activity.BaseActivity;

import project.prm392_oss.R;
import project.prm392_oss.entity.User;
import project.prm392_oss.repository.UserRepository;

public class RegisterActivity extends BaseActivity {
    private EditText usernameEditText, passwordEditText, confirmPasswordEditText, emailEditText, phoneEditText, fullNameEditText;
    private Button registerButton;
    private TextView loginTextView;
    private UserRepository userRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Register");
        }
        userRepository = new UserRepository(getApplication());


        usernameEditText = findViewById(R.id.username_reg);
        passwordEditText = findViewById(R.id.password_reg);
        confirmPasswordEditText = findViewById(R.id.confirm_password_reg);
        fullNameEditText = findViewById(R.id.full_name_reg);
        emailEditText = findViewById(R.id.email_reg);
        phoneEditText = findViewById(R.id.phone_reg);
        registerButton = findViewById(R.id.register_btn);
        loginTextView = findViewById(R.id.sign_in);


        registerButton.setOnClickListener(v -> registerUser());
        loginTextView.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }

    private void registerUser() {
        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();
        String fullName = fullNameEditText.getText().toString().trim();


        if (!validateInput(username, password, confirmPassword, email, phone, fullName)) {
            return;
        }


        userRepository.isUserRegistered(username, email, isRegistered -> runOnUiThread(() -> {
            if (isRegistered) {
                Toast.makeText(RegisterActivity.this, "Tài khoản đã tồn tại!", Toast.LENGTH_SHORT).show();
            } else {

                User newUser = new User(0, username, password, fullName, email, phone, "", 3);
                userRepository.insert(newUser);

                Toast.makeText(RegisterActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        }));
    }

    private boolean validateInput(String username, String password, String confirmPassword, String email, String phone, String fullName) {
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || email.isEmpty() || phone.isEmpty() || fullName.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Email không hợp lệ!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!Patterns.PHONE.matcher(phone).matches()) {
            Toast.makeText(this, "Số điện thoại không hợp lệ!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (password.length() < 6) {
            Toast.makeText(this, "Mật khẩu phải có ít nhất 6 ký tự!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Mật khẩu xác nhận không khớp!", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
