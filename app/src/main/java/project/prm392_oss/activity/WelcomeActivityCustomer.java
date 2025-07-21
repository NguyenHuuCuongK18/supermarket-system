package project.prm392_oss.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import project.prm392_oss.activity.LoginActivity;

import project.prm392_oss.activity.BaseActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import project.prm392_oss.R;
import project.prm392_oss.adapter.CategoryAdapterCustomer;
import project.prm392_oss.database.AppDatabase;
import project.prm392_oss.entity.Cart;
import project.prm392_oss.activity.OrderHistoryActivityCustomer;
import project.prm392_oss.utils.manager.SessionManager;

public class WelcomeActivityCustomer extends BaseActivity {

    private TextView tvWelcome;
    private RecyclerView rvCategory;
    private Button btnViewOrders;
    private Button btnShopNow;
    private ImageView ivProfile;
    private CategoryAdapterCustomer categoryAdapter;
    private AppDatabase db;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_customer);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        // Ánh xạ View
        tvWelcome = findViewById(R.id.tvWelcome);
        rvCategory = findViewById(R.id.rvCategory);
        btnShopNow = findViewById(R.id.btnShopNow);
        ivProfile = findViewById(R.id.ivProfile);
        btnViewOrders = findViewById(R.id.btnViewOrders);

        db = AppDatabase.getInstance(this);
        categoryAdapter = new CategoryAdapterCustomer();
        rvCategory.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvCategory.setAdapter(categoryAdapter);

        // Lấy thông tin từ SharedPreferences
        loadUserInfo();
        loadCategories();

        btnShopNow.setOnClickListener(v -> {
            checkAndCreateCartIfNeeded();

            Intent intent = new Intent(WelcomeActivityCustomer.this, ProductListActivityCustomer.class);
            startActivity(intent);
        });


        ivProfile.setOnClickListener(v -> {
            androidx.appcompat.widget.PopupMenu popup = new androidx.appcompat.widget.PopupMenu(WelcomeActivityCustomer.this, ivProfile);
            popup.getMenuInflater().inflate(R.menu.menu_profile_popup, popup.getMenu());
            popup.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.action_view_profile) {
                    Intent intent = new Intent(WelcomeActivityCustomer.this, EditProfileActivity.class);
                    startActivity(intent);
                    return true;
                } else if (item.getItemId() == R.id.action_logout) {
                    performLogout();
                    return true;
                }
                return false;
            });
            popup.show();
        });

        btnViewOrders.setOnClickListener(v -> {
            Intent intent = new Intent(WelcomeActivityCustomer.this, OrderHistoryActivityCustomer.class);
            startActivity(intent);
        });

    }

    private void loadUserInfo() {
        SharedPreferences sharedPreferences = getSharedPreferences("USER_PREFS", MODE_PRIVATE);
        userId = sharedPreferences.getInt("USER_ID", -1);  // Lấy userId từ SharedPreferences

        if (userId != -1) {
            // Truy vấn dữ liệu người dùng từ cơ sở dữ liệu
            db.userDAO().getUserById(userId).observe(this, user -> {
                if (user != null) {
                    // Lấy tên người dùng từ cơ sở dữ liệu
                    String nameToShow = user.getName();
                    if (nameToShow == null || nameToShow.isEmpty()) {
                        nameToShow = user.getUsername();
                    }
                    tvWelcome.setText("Welcome, " + nameToShow + "!");
                } else {
                    tvWelcome.setText("Welcome, Guest!");
                }
            });
        } else {
            tvWelcome.setText("Welcome, Guest!");
        }
    }

    private void loadCategories() {
        db.categoryDAO().getAllCategories().observe(this, categories -> {
            if (categories != null) {
                categoryAdapter.setCategories(categories);
            }
        });
    }

    private void checkAndCreateCartIfNeeded() {
        new Thread(() -> {
            int cartId = db.cartDAO().getCartIdByUserId(userId);
            if (cartId <= 0) {
                createNewCart();
            }
        }).start();
    }

    private void createNewCart() {
        try {
            Cart newCart = new Cart();
            newCart.setUserId(userId);

            long cartId = db.cartDAO().insertCart(newCart);

            if (cartId > 0) {
                Log.d("WelcomeActivityCustomer", "New Cart created successfully with ID: " + cartId);
            } else {
                Log.e("WelcomeActivityCustomer", "Failed to create new cart");
            }
        } catch (Exception e) {
            Log.e("WelcomeActivityCustomer", "Failed to create new cart", e);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        } else if (item.getItemId() == R.id.action_view_profile) {
            startActivity(new Intent(WelcomeActivityCustomer.this, EditProfileActivity.class));
            return true;
        } else if (item.getItemId() == R.id.action_logout) {
            SessionManager.logout(WelcomeActivityCustomer.this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void performLogout() {
        SharedPreferences prefs = getSharedPreferences("USER_PREFS", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove("USER_ID");
        editor.apply();

        Intent intent = new Intent(WelcomeActivityCustomer.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        Toast.makeText(WelcomeActivityCustomer.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
    }
}
