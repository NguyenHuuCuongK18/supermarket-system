package project.prm392_oss.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import project.prm392_oss.activity.BaseActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import project.prm392_oss.R;
import project.prm392_oss.adapter.CartAdapterCustomer;
import project.prm392_oss.dto.CartItemDTO;
import project.prm392_oss.database.DatabaseClient;
import project.prm392_oss.entity.Cart;
import project.prm392_oss.entity.CartItem;
import project.prm392_oss.viewModel.CartViewModel;

public class CartActivityCustomer extends BaseActivity {

    private RecyclerView recyclerView;
    private CartAdapterCustomer adapter;
    private TextView tvEmptyCartMessage;
    private Button btnCreateOrder;
    private CartViewModel cartViewModel;
    private Button btnBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_customer);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        recyclerView = findViewById(R.id.recyclerView);
        tvEmptyCartMessage = findViewById(R.id.tvEmptyCartMessage);
        btnCreateOrder = findViewById(R.id.btnCreateOrder);
        btnBack = findViewById(R.id.btnBack);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Lấy thông tin userId từ SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("USER_PREFS", MODE_PRIVATE);
        int userId = sharedPreferences.getInt("USER_ID", -1);

        if (userId != -1) {
            Log.d("CartActivity", "User ID: " + userId);
            loadCartItems(userId);
        } else {
            Toast.makeText(this, "Không tìm thấy thông tin người dùng", Toast.LENGTH_SHORT).show();
            finish();
        }
        btnCreateOrder.setOnClickListener(view -> createOrder());
        btnBack.setOnClickListener(v -> finish());
    }

    private void loadCartItems(int userId) {
        new Thread(() -> {
            int cartId = DatabaseClient.getInstance(this).getAppDatabase()
                    .cartDAO().getCartIdByUserId(userId);

            if (cartId <= 0) {
                // Nếu không có giỏ hàng, tạo giỏ hàng mới cho user
                cartId = createNewCart(userId);

                // Kiểm tra lại nếu đã tạo thành công
                if (cartId > 0) {
                    Log.d("CartActivity", "New Cart created successfully with ID: " + cartId);
                } else {
                    Log.e("CartActivity", "Failed to create new cart");
                    runOnUiThread(() -> {
                        Toast.makeText(this, "Không thể tạo giỏ hàng mới", Toast.LENGTH_SHORT).show();
                    });
                    return;
                }
            }

            if (cartId > 0) {
                // Lấy danh sách CartItem theo cartId
                List<CartItemDTO> cartItems = DatabaseClient.getInstance(this).getAppDatabase()
                        .cartItemDAO().getCartItemsByCartId(cartId);

                runOnUiThread(() -> {
                    if (cartItems != null && !cartItems.isEmpty()) {
                        if (adapter == null) {
                            adapter = new CartAdapterCustomer(this, cartItems);
                            recyclerView.setAdapter(adapter);
                        } else {
                            adapter.setCartItems(cartItems);
                            adapter.notifyDataSetChanged();
                        }
                        updateCartVisibility(cartItems);
                    } else {
                        updateCartVisibility(null);
                    }
                });
            } else {
                runOnUiThread(() -> updateCartVisibility(null));
            }
        }).start();
    }


    private int createNewCart(int userId) {
        try {
            Cart newCart = new Cart();
            newCart.setUserId(userId);

            long cartId = DatabaseClient.getInstance(this).getAppDatabase()
                    .cartDAO().insertCart(newCart);

            if (cartId > 0) {
                Log.d("CartActivity", "Cart created successfully with ID: " + cartId);
                return (int) cartId;
            } else {
                Log.e("CartActivity", "Failed to insert new cart");
                return -1;
            }
        } catch (Exception e) {
            Log.e("CartActivity", "Failed to create new cart", e);
            return -1;
        }
    }


    private void updateCartVisibility(List<CartItemDTO> cartItems) {
        if (cartItems == null || cartItems.isEmpty()) {
            tvEmptyCartMessage.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            btnCreateOrder.setVisibility(View.GONE);
        } else {
            tvEmptyCartMessage.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            btnCreateOrder.setVisibility(View.VISIBLE);
        }
    }


    private void createOrder() {
        if (adapter == null) return;

        List<CartItemDTO> selectedItems = adapter.getSelectedItems();
        if (selectedItems.isEmpty()) {
            Toast.makeText(this, "Vui lòng chọn ít nhất một sản phẩm!", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(() -> {
            double totalAmount = 0;

            for (CartItemDTO item : selectedItems) {
                double price = DatabaseClient.getInstance(this).getAppDatabase()
                        .productDAO().getPriceById(item.getProductId());
                totalAmount += price * item.getQuantity();
            }

            // Chuyển qua CreateOrderActivity
            Intent intent = new Intent(this, CreateOrderActivityCustomer.class);
            intent.putExtra("selectedItems", new ArrayList<>(selectedItems));
            intent.putExtra("totalAmount", totalAmount);
            startActivity(intent);
        }).start();
    }


    private void refreshCart(int userId) {
        loadCartItems(userId);
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