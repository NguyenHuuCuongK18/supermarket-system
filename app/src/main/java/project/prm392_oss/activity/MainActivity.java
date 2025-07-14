package project.prm392_oss.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import project.prm392_oss.R;

public class MainActivity extends AppCompatActivity {

    private Button btnViewProducts;
    private Button btnViewOrders;
    private Button btnLogin;
    private Button btnHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnViewProducts = findViewById(R.id.btn_view_products);
        btnViewOrders = findViewById(R.id.btn_view_orders);
        btnLogin = findViewById(R.id.btn_login); // Khởi tạo nút đăng nhập

        btnHome = findViewById(R.id.btn_home); // Khởi tạo nút đăng nhập

        // Mở ProductListActivity khi nhấn nút
        btnViewProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, WelcomeActivityCustomer.class);
                startActivity(intent);
            }
        });

        // Mở OrderHistoryActivity khi nhấn nút
        btnViewOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, OrderHistoryActivityCustomer.class);
                startActivity(intent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });


    }
}
