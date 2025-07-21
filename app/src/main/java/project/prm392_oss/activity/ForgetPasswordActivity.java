package project.prm392_oss.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import project.prm392_oss.activity.BaseActivity;
import android.view.MenuItem;

import javax.mail.MessagingException;
import java.util.Random;

import project.prm392_oss.R;
import project.prm392_oss.dao.UserDAO;
import project.prm392_oss.database.AppDatabase;
import project.prm392_oss.entity.User;

public class ForgetPasswordActivity extends BaseActivity {
    private EditText emailInput;
    private Button sendButton;
    private TextView backToSignIn;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        emailInput = findViewById(R.id.email_input);
        sendButton = findViewById(R.id.send_btn);
        backToSignIn = findViewById(R.id.back_to_signin);


        sendButton.setOnClickListener(view -> {
            String email = emailInput.getText().toString().trim();
            if (validateEmail(email)) {
                String newPassword = generateNewPassword();
                resetPassword(email);
            }
        });


        backToSignIn.setOnClickListener(view -> {
            startActivity(new Intent(ForgetPasswordActivity.this, LoginActivity.class));
            finish();
        });
    }


    private boolean validateEmail(String email) {
        if (email.isEmpty()) {
            emailInput.setError("Email cannot be empty");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailInput.setError("Invalid email format");
            return false;
        }
        return true;
    }


    private String generateNewPassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder password = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 8; i++) {
            password.append(chars.charAt(random.nextInt(chars.length())));
        }
        return password.toString();
    }

    public void resetPassword(String userEmail) {
        new Thread(() -> {
            AppDatabase db = AppDatabase.getInstance(context);
            UserDAO userDao = db.userDAO();

            User user = userDao.getUserByEmail(userEmail);

            if (user == null) {
                runOnUiThread(() -> Toast.makeText(this, "Email not found!", Toast.LENGTH_LONG).show());
                return;
            }

            String newPassword = generateNewPassword();
            userDao.updatePassword(userEmail, newPassword); // Cập nhật mật khẩu


            try {
                GMailSender sender = new GMailSender();
                sender.sendMail(userEmail, "Your New Password", "Your new password is: " + newPassword);
                runOnUiThread(() -> Toast.makeText(this, "Check your email for the new password!", Toast.LENGTH_LONG).show());
            } catch (MessagingException e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(this, "Failed to send email. Try again!", Toast.LENGTH_LONG).show());
            }
        }).start();
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
