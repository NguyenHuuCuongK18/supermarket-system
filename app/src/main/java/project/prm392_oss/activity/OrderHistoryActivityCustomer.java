package project.prm392_oss.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;
import android.view.MenuItem;

import project.prm392_oss.activity.BaseActivity;

import java.util.List;

import project.prm392_oss.R;
import project.prm392_oss.adapter.OrderHistoryAdapterCustomer;
import project.prm392_oss.database.DatabaseClient;
import project.prm392_oss.entity.Order;

public class OrderHistoryActivityCustomer extends BaseActivity {

    private ListView listViewOrders;
    private OrderHistoryAdapterCustomer adapter;
    private List<Order> orderList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history_customer);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        listViewOrders = findViewById(R.id.listViewOrders);

        loadOrderHistory();
    }

    private void loadOrderHistory() {
        new Thread(() -> {
            try {
                SharedPreferences prefs = getSharedPreferences("USER_PREFS", MODE_PRIVATE);
                int customerId = prefs.getInt("USER_ID", -1);
                orderList = DatabaseClient.getInstance(this).getAppDatabase()
                        .orderDAO()
                        .getOrdersByCustomerId(customerId);

                runOnUiThread(() -> {
                    if (orderList != null && !orderList.isEmpty()) {
                        adapter = new OrderHistoryAdapterCustomer(this, orderList);
                        listViewOrders.setAdapter(adapter);
                    } else {
                        Toast.makeText(this, "Không có đơn hàng nào!", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception e) {
                runOnUiThread(() ->
                        Toast.makeText(this, "Lỗi khi tải lịch sử đơn hàng!", Toast.LENGTH_SHORT).show());
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
