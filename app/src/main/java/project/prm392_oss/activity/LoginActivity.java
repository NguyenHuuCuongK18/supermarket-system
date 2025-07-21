package project.prm392_oss.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import project.prm392_oss.activity.BaseActivity;

import project.prm392_oss.R;
import project.prm392_oss.repository.UserRepository;
import project.prm392_oss.activity.ListProductActivity;
import project.prm392_oss.activity.ListUsersActivity;
import project.prm392_oss.activity.ListOrdersActivity;
import project.prm392_oss.activity.WelcomeActivityCustomer;

public class LoginActivity extends BaseActivity {

    private EditText usernameOrEmailEditText, passwordEditText;
    private Button loginButton;
    TextView registerTextView, forgotPasswordTextView;
    private UserRepository userRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userRepository = new UserRepository(getApplication());

        usernameOrEmailEditText = findViewById(R.id.username_login); // Vẫn dùng ID cũ
        passwordEditText = findViewById(R.id.password_login);
        loginButton = findViewById(R.id.login_btn);
        registerTextView = findViewById(R.id.register);
        forgotPasswordTextView = findViewById(R.id.forgotpassword);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        forgotPasswordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
                startActivity(intent);
            }
        });

        registerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loginUser() {
        String usernameOrEmail = usernameOrEmailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (usernameOrEmail.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập tên đăng nhập hoặc email và mật khẩu!", Toast.LENGTH_SHORT).show();
            return;
        }

        userRepository.loginUser(usernameOrEmail, password, user -> runOnUiThread(() -> {
            if (user != null) {
                userRepository.getRoleName(user.getRole_id(), roleName -> runOnUiThread(() -> {
                    SharedPreferences prefs = getSharedPreferences("USER_PREFS", MODE_PRIVATE);
                    prefs.edit()
                            .putString("USER_ROLE", roleName)
                            .putInt("USER_ID", user.getUser_id())
                            .apply();

                    Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();

                    Intent intent = null;
                    if ("Customer".equals(roleName)) {
                        intent = new Intent(LoginActivity.this, WelcomeActivityCustomer.class);
                    } else if ("Manager".equals(roleName)) {
                        intent = new Intent(LoginActivity.this, ListUsersActivity.class);
                    } else if ("Employee".equals(roleName)) {
                        intent = new Intent(LoginActivity.this, ListOrdersActivity.class);
                    }
                    if (intent != null) {
                        intent.putExtra("USER_ID", user.getUser_id());
                        startActivity(intent);
                        finish();
                    }
                }));
            } else {
                Toast.makeText(this, "Tên đăng nhập, email hoặc mật khẩu không đúng!", Toast.LENGTH_SHORT).show();
            }
        }));
    }
}
