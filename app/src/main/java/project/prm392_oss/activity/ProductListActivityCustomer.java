package project.prm392_oss.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import project.prm392_oss.R;
import project.prm392_oss.adapter.ProductAdapterCustomer;
import project.prm392_oss.database.DatabaseClient;
import project.prm392_oss.entity.Cart;
import project.prm392_oss.entity.Product;
import project.prm392_oss.utils.manager.CartManager;
import project.prm392_oss.viewModel.ProductViewModelCustomer;

public class ProductListActivityCustomer extends AppCompatActivity implements ProductAdapterCustomer.OnAddToCartClickListener, ProductAdapterCustomer.OnItemClickListener {

    private RecyclerView recyclerView;
    private ProductAdapterCustomer adapter;
    private ProductViewModelCustomer productViewModel;
    private ProgressBar progressBar;
    private TextView tvEmptyMessage;
    private ImageView ivCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list_customer);

        initViews();
        setupRecyclerView();
        loadProducts();
        setupCartButton();

        // Giả sử bạn lấy userId từ SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("USER_PREFS", MODE_PRIVATE);
        int userId = sharedPreferences.getInt("USER_ID", -1);

        if (userId != -1) {
            checkAndCreateCartIfNeeded(userId);
        } else {
            Toast.makeText(this, "Không tìm thấy thông tin người dùng", Toast.LENGTH_SHORT).show();
            finish();  // Hoặc chuyển về màn hình đăng nhập
        }
    }


    private void initViews() {
        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);
        tvEmptyMessage = findViewById(R.id.tvEmptyMessage);
        ivCart = findViewById(R.id.ivCart);

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    private void setupRecyclerView() {
        // Setup RecyclerView với GridLayoutManager
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        //Khởi tạo Adapter với đủ ba tham số
        adapter = new ProductAdapterCustomer(this, this, this);
        recyclerView.setAdapter(adapter);

        productViewModel = new ViewModelProvider(this).get(ProductViewModelCustomer.class);
    }

    private void loadProducts() {
        // Hiển thị ProgressBar khi tải dữ liệu
        progressBar.setVisibility(View.VISIBLE);
        tvEmptyMessage.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);

        // Lấy dữ liệu từ ViewModel
        productViewModel.getAllProducts().observe(this, products -> {
            progressBar.setVisibility(View.GONE);
            if (products != null && !products.isEmpty()) {
                adapter.setProductList(products);
                recyclerView.setVisibility(View.VISIBLE);
            } else {
                tvEmptyMessage.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setupCartButton() {
        ivCart.setOnClickListener(v -> {
            CartManager.getCartItems(this).observe(this, cartItems -> {
                if (cartItems == null || cartItems.isEmpty()) {
                    Toast.makeText(this, "Giỏ hàng trống!", Toast.LENGTH_SHORT).show();
                } else {
                    startActivity(new Intent(this, CartActivityCustomer.class));
                }
            });
        });
    }


    @Override
    public void onAddToCart(Product product) {
        if (product != null) {

            SharedPreferences sharedPreferences = getSharedPreferences("USER_PREFS", MODE_PRIVATE);
            int userId = sharedPreferences.getInt("USER_ID", -1);

            CartManager.addToCart(this, userId, product);
            Toast.makeText(this, product.getName() + " đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Không thể thêm sản phẩm vào giỏ hàng", Toast.LENGTH_SHORT).show();
        }
    }

    // Xử lý sự kiện khi nhấn vào sản phẩm
    @Override
    public void onItemClick(Product product) {
        if (product != null) {
            Log.d("ProductListActivity", "Product ID: " + product.getProduct_id()); // Kiểm tra ID
            Intent intent = new Intent(this, ProductDetailActivityCustomer.class);
            intent.putExtra("productId", product.getProduct_id());
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_product_list_customer, menu);
        return true;
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_view_cart) {
            startActivity(new Intent(this, CartActivityCustomer.class));
            return true;
        } else if (id == R.id.action_view_profile) {
            startActivity(new Intent(this, EditProfileActivity.class));
            return true;
        } else if (id == R.id.action_logout) {
            SharedPreferences prefs = getSharedPreferences("USER_PREFS", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.remove("USER_ID");
            editor.apply();

            Intent intent = new Intent(ProductListActivityCustomer.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            Toast.makeText(ProductListActivityCustomer.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void checkAndCreateCartIfNeeded(int userId) {
        new Thread(() -> {
            int cartId = DatabaseClient.getInstance(this).getAppDatabase()
                    .cartDAO().getCartIdByUserId(userId);

            if (cartId <= 0) {
                cartId = createNewCart(userId);

                if (cartId > 0) {
                    Log.d("ProductListActivity", "New Cart created successfully with ID: " + cartId);
                } else {
                    Log.e("ProductListActivity", "Failed to create new cart");
                    runOnUiThread(() -> {
                        Toast.makeText(this, "Không thể tạo giỏ hàng mới", Toast.LENGTH_SHORT).show();
                    });
                }
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
                Log.d("ProductListActivity", "Cart created successfully with ID: " + cartId);
                return (int) cartId;
            } else {
                Log.e("ProductListActivity", "Failed to insert new cart");
                return -1;
            }
        } catch (Exception e) {
            Log.e("ProductListActivity", "Failed to create new cart", e);
            return -1;
        }
    }


}
