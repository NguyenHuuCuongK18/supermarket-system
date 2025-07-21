package project.prm392_oss.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import project.prm392_oss.activity.BaseActivity;
import androidx.recyclerview.widget.RecyclerView;

import project.prm392_oss.R;

public class HomePageActivity extends BaseActivity {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        ImageButton btnMenu = findViewById(R.id.btn_menu);

        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v);
            }
        });
    }

    private void showPopupMenu(View view) {
        PopupMenu popup = new PopupMenu(this, view);
        popup.getMenuInflater().inflate(R.menu.menu_main, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int itemId = item.getItemId(); // Lưu giá trị ID vào biến tạm

                if (itemId == R.id.action_view_profile) {
                    startActivity(new Intent(HomePageActivity.this, EditProfileActivity.class));
                    return true;
                } else if (itemId == R.id.action_logout) {
                    SharedPreferences prefs = getSharedPreferences("USER_PREFS", MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.remove("USER_ID");
                    editor.apply();

                    Intent intent = new Intent(HomePageActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    Toast.makeText(HomePageActivity.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
                    return true;
                }

                return false;
            }
        });


        popup.show();
    }

//    private Button btnCommonFeature, btnCustomerFeature, btnEmployeeFeature, btnManagerFeature;
//    private String userRole;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_home_page);
//
//        // Lấy role từ SharedPreferences
//        SharedPreferences prefs = getSharedPreferences("USER_PREFS", MODE_PRIVATE);
//        userRole = prefs.getString("USER_ROLE", "");
//
//        // Ánh xạ các button
//        btnCommonFeature = findViewById(R.id.btn_common_feature);
//        btnCustomerFeature = findViewById(R.id.btn_customer_feature);
//        btnEmployeeFeature = findViewById(R.id.btn_employee_feature);
//        btnManagerFeature = findViewById(R.id.btn_manager_feature);
//
//        // Thiết lập hiển thị quyền hạn
//        setupRolePermissions();
//
//        // Xử lý các chức năng chung
//        btnCommonFeature.setOnClickListener(v -> openCommonFeature());
//    }
//
//    private void setupRolePermissions() {
//        if (userRole.isEmpty()) {
//            Toast.makeText(this, "Không xác định được quyền!", Toast.LENGTH_SHORT).show();
//            finish();
//            return;
//        }
//
//        btnCustomerFeature.setVisibility(View.GONE);
//        btnEmployeeFeature.setVisibility(View.GONE);
//        btnManagerFeature.setVisibility(View.GONE);
//
//        switch (userRole) {
//            case "Admin":
//                btnCustomerFeature.setVisibility(View.VISIBLE);
//                btnCustomerFeature.setOnClickListener(v -> openCustomerFeature());
//                break;
//            case "Customer":
//                btnEmployeeFeature.setVisibility(View.VISIBLE);
//                btnEmployeeFeature.setOnClickListener(v -> openEmployeeFeature());
//                break;
//            case "Manager":
//                btnManagerFeature.setVisibility(View.VISIBLE);
//                btnManagerFeature.setOnClickListener(v -> openManagerFeature());
//                break;
//        }
//    }
//
//    private void openCommonFeature() {
//        Toast.makeText(this, "Chức năng chung!", Toast.LENGTH_SHORT).show();
//    }
//
//    private void openCustomerFeature() {
//        Toast.makeText(this, "Chức năng dành cho Khách hàng!", Toast.LENGTH_SHORT).show();
//    }
//
//    private void openEmployeeFeature() {
//        Toast.makeText(this, "Chức năng dành cho Nhân viên!", Toast.LENGTH_SHORT).show();
//    }
//
//    private void openManagerFeature() {
//        Toast.makeText(this, "Chức năng dành cho Quản lý!", Toast.LENGTH_SHORT).show();
//    }
}
