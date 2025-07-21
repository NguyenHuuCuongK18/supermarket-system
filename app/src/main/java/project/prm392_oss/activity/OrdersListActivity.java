package project.prm392_oss.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import project.prm392_oss.activity.BaseActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import project.prm392_oss.R;
import project.prm392_oss.entity.Order;
import project.prm392_oss.viewModel.OrderViewModel;

public class OrdersListActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private OrderViewModel orderViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_list);

//        if (getSupportActionBar() != null) {
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        }

        int customerId = getIntent().getIntExtra("customerId", -1);
        if (customerId == -1) {
            Toast.makeText(this, "Không tìm thấy khách hàng!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        recyclerView = findViewById(R.id.recyclerViewOrders);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        orderViewModel = new ViewModelProvider(this).get(OrderViewModel.class);

        orderViewModel.getOrdersByCustomerId(customerId).observe(this, orders -> {
            if (orders == null || orders.isEmpty()) {
                Toast.makeText(this, "Khách hàng chưa có đơn hàng nào.", Toast.LENGTH_SHORT).show();
            } else {
                recyclerView.setAdapter(new OrderAdapter(orders));
            }
        });
    }

    class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
        private final List<Order> orders;

        public OrderAdapter(List<Order> orders) {
            this.orders = orders;
        }

        @NonNull
        @Override
        public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_order, parent, false);
            return new OrderViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
            Order order = orders.get(position);
            holder.tvOrderId.setText("Mã đơn: #" + order.getOrder_id());
            holder.tvOrderDate.setText("Ngày đặt: " + order.getOrder_date());
            holder.tvStatus.setText("Trạng thái: " + order.getStatus());
            holder.tvTotal.setText("Tổng tiền: " + order.getTotal_amount() + "₫");

            holder.btnViewDetail.setOnClickListener(v -> {
                Intent intent = new Intent(OrdersListActivity.this, OrderDetailActivity.class);
                intent.putExtra("orderId", order.getOrder_id());
                startActivity(intent);
            });
        }

        @Override
        public int getItemCount() {
            return orders.size();
        }

        class OrderViewHolder extends RecyclerView.ViewHolder {
            TextView tvOrderId, tvOrderDate, tvStatus, tvTotal;
            Button btnViewDetail;

            public OrderViewHolder(@NonNull View itemView) {
                super(itemView);
                tvOrderId = itemView.findViewById(R.id.tvOrderId);
                tvOrderDate = itemView.findViewById(R.id.tvOrderDate);
                tvStatus = itemView.findViewById(R.id.tvOrderStatus);
                tvTotal = itemView.findViewById(R.id.tvOrderTotal);
                btnViewDetail = itemView.findViewById(R.id.btnViewDetail);
            }
        }
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == android.R.id.home) {
//            finish();
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
}
