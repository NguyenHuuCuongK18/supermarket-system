package project.prm392_oss.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ListView;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import project.prm392_oss.R;
import project.prm392_oss.adapter.OrderHistoryAdapterCustomer;
import project.prm392_oss.database.DatabaseClient;
import project.prm392_oss.entity.Order;
import project.prm392_oss.entity.User;
import project.prm392_oss.activity.ListUsersActivity;
import project.prm392_oss.viewModel.UserViewModel;

public class CustomerDetailActivity extends AppCompatActivity {

    private TextView tvId, tvName, tvEmail, tvPhone, tvAddress, tvRole, tvTotalSpent;
    private ImageView ivProfile;
    private ListView lvOrders;
    private OrderHistoryAdapterCustomer orderAdapter;
    private UserViewModel userViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Initialize the views
        tvId = findViewById(R.id.tvId);
        tvName = findViewById(R.id.tvName);
        tvEmail = findViewById(R.id.tvEmail);
        tvPhone = findViewById(R.id.tvPhone);
        tvAddress = findViewById(R.id.tvAddress);
        tvRole = findViewById(R.id.tvRole);
        ivProfile = findViewById(R.id.ivProfile);
        lvOrders = findViewById(R.id.lvOrders);
        tvTotalSpent = findViewById(R.id.tvTotalSpent);

        int userId = getIntent().getIntExtra("userId", -1);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        userViewModel.getUserById(userId).observe(this, user -> {
            if (user != null) {
                tvId.setText("ID: " + user.getUser_id());
                tvName.setText("Name: " + user.getName());
                tvEmail.setText("Email: " + user.getEmail());
                tvPhone.setText("Phone: " + user.getPhone());
                tvAddress.setText("Address: " + user.getAddress());
                ivProfile.setImageResource(R.drawable.defaultimage);

                userViewModel.getRoleName(user.getRole_id()).observe(this, roleName -> {
                    tvRole.setText("Role: " + roleName);
                });
                loadCustomerOrders(user.getUser_id());

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        if (id == R.id.nav_user_management) {
            startActivity(new Intent(CustomerDetailActivity.this, ListUsersActivity.class));
            return true;
        } else if (id == R.id.nav_product_management) {
            startActivity(new Intent(CustomerDetailActivity.this, ListProductActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void loadCustomerOrders(int customerId) {
        new Thread(() -> {
            List<Order> orders = DatabaseClient.getInstance(this)
                    .getAppDatabase()
                    .orderDAO()
                    .getOrdersByCustomerId(customerId);

            runOnUiThread(() -> {
                if (orders != null && !orders.isEmpty()) {
                    orderAdapter = new OrderHistoryAdapterCustomer(this, orders);
                    lvOrders.setAdapter(orderAdapter);
                    setListViewHeightBasedOnChildren(lvOrders);
                    int total = 0;
                    for (Order o : orders) {
                        total += o.getTotal_amount();
                    }
                    tvTotalSpent.setText("Total Spent: $" + total);
                } else {
                    tvTotalSpent.setText("Total Spent: $0");
                }
            });
        }).start();
    }

    private void setListViewHeightBasedOnChildren(ListView listView) {
        if (listView.getAdapter() == null) return;
        int totalHeight = 0;
        for (int i = 0; i < listView.getAdapter().getCount(); i++) {
            android.view.View listItem = listView.getAdapter().getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        android.view.ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listView.getAdapter().getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
}
