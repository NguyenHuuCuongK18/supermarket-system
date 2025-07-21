package project.prm392_oss.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.view.MenuItem;


import androidx.annotation.NonNull;
import project.prm392_oss.activity.BaseActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import project.prm392_oss.R;
import project.prm392_oss.entity.OrderDetails;
import project.prm392_oss.viewModel.OrderViewModel;

import java.util.concurrent.atomic.AtomicInteger;

public class OrderDetailActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private TextView tvOrderId, tvOrderDate, tvOrderStatus, tvTotalAmount;
    private OrderViewModel orderViewModel;
    private int orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        orderId = getIntent().getIntExtra("orderId", -1);
        if (orderId == -1) {
            Toast.makeText(this, "Order not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        recyclerView = findViewById(R.id.recyclerViewProductList);
        tvOrderId = findViewById(R.id.tvOrderId);
        tvOrderDate = findViewById(R.id.tvOrderDate);
        tvOrderStatus = findViewById(R.id.tvOrderStatus);
        tvTotalAmount = findViewById(R.id.tvTotalAmount);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        orderViewModel = new ViewModelProvider(this).get(OrderViewModel.class);

        orderViewModel.getOrderWithDetails(orderId).observe(this, orderWithDetails -> {
            if (orderWithDetails != null) {
                tvOrderId.setText("ID: " + orderWithDetails.order.getOrder_id());
                tvOrderDate.setText("Date: " + orderWithDetails.order.getOrder_date());
                tvOrderStatus.setText("Status: " + orderWithDetails.order.getStatus());

                // Initialize totalAmount to 0
                AtomicInteger totalAmount = new AtomicInteger(0);

                // Loop through order details to calculate the total amount
                for (OrderDetails details : orderWithDetails.orderDetails) {
                    orderViewModel.getProductById(details.getProduct_id()).observe(this, product -> {
                        if (product != null) {
                            // Calculate total amount
                            totalAmount.addAndGet(details.getQuantity() * product.getSale_price());
                        }
                    });
                }

                // Set the total amount
                tvTotalAmount.setText("Total Amount: " + totalAmount.get());

                // Set the adapter for the RecyclerView
                recyclerView.setAdapter(new OrderDetailAdapter(orderWithDetails.orderDetails));
            } else {
                Toast.makeText(this, "No products in this order", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.OrderDetailViewHolder> {

        private final List<OrderDetails> orderDetailsList;

        public OrderDetailAdapter(List<OrderDetails> orderDetailsList) {
            this.orderDetailsList = orderDetailsList;
        }

        @NonNull
        @Override
        public OrderDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_order_detail, parent, false);
            return new OrderDetailViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull OrderDetailViewHolder holder, int position) {
            OrderDetails details = orderDetailsList.get(position);
            holder.tvProductId.setText(String.valueOf(details.getProduct_id()));

            orderViewModel.getProductById(details.getProduct_id()).observe(OrderDetailActivity.this, product -> {
                if (product != null) {
                    holder.tvProductName.setText(product.getName());
                    holder.tvQuantity.setText(String.valueOf(details.getQuantity()));
                    holder.tvPrice.setText(String.valueOf(product.getSale_price()));
                }
            });
        }

        @Override
        public int getItemCount() {
            return orderDetailsList.size();
        }

        public class OrderDetailViewHolder extends RecyclerView.ViewHolder {
            TextView tvProductId, tvProductName, tvQuantity, tvPrice;

            public OrderDetailViewHolder(@NonNull View itemView) {
                super(itemView);
                tvProductId = itemView.findViewById(R.id.tvProductId);
                tvProductName = itemView.findViewById(R.id.tvProductName);
                tvQuantity = itemView.findViewById(R.id.tvQuantity);
                tvPrice = itemView.findViewById(R.id.tvPrice);
            }
        }
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
