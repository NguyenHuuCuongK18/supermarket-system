// app/src/main/java/project/prm392_oss/activity/ProductListActivityCustomer.java
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
import project.prm392_oss.activity.BaseActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import project.prm392_oss.R;
import project.prm392_oss.adapter.ProductAdapterCustomer;
import project.prm392_oss.database.DatabaseClient;
import project.prm392_oss.entity.Cart;
import project.prm392_oss.entity.Product;
import project.prm392_oss.utils.manager.CartManager;
import project.prm392_oss.utils.manager.SessionManager;
import project.prm392_oss.viewModel.ProductViewModelCustomer;
import project.prm392_oss.activity.ProductSearchActivity;


public class ProductListActivityCustomer extends BaseActivity
        implements ProductAdapterCustomer.OnAddToCartClickListener,
        ProductAdapterCustomer.OnItemClickListener {

    private RecyclerView recyclerView;
    private ProductAdapterCustomer adapter;
    private ProductViewModelCustomer productViewModel;
    private ProgressBar progressBar;
    private TextView tvEmptyMessage;
    private ImageView ivCart;
    private androidx.appcompat.widget.SearchView searchView;
    private final java.util.List<Product> allProducts = new java.util.ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list_customer);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        initViews();
        setupRecyclerView();
        loadProducts();
        setupSearch();
        setupCartButton();

        SharedPreferences sharedPreferences = getSharedPreferences("USER_PREFS", MODE_PRIVATE);
        int userId = sharedPreferences.getInt("USER_ID", -1);
        if (userId != -1) {
            checkAndCreateCartIfNeeded(userId);
        } else {
            Toast.makeText(this, "Không tìm thấy thông tin người dùng", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);
        tvEmptyMessage = findViewById(R.id.tvEmptyMessage);
        ivCart = findViewById(R.id.ivCart);
        searchView = findViewById(R.id.search_view);

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new ProductAdapterCustomer(this, this, this);
        recyclerView.setAdapter(adapter);
        productViewModel = new ViewModelProvider(this).get(ProductViewModelCustomer.class);
    }

    private void loadProducts() {
        progressBar.setVisibility(View.VISIBLE);
        tvEmptyMessage.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        productViewModel.getAllProducts().observe(this, products -> {
            progressBar.setVisibility(View.GONE);
            if (products != null && !products.isEmpty()) {
                allProducts.clear();
                allProducts.addAll(products);
                adapter.setProductList(products);
                recyclerView.setVisibility(View.VISIBLE);
            } else {
                tvEmptyMessage.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setupCartButton() {
        ivCart.setOnClickListener(v -> {
            SharedPreferences prefs = getSharedPreferences("USER_PREFS", MODE_PRIVATE);
            int userId = prefs.getInt("USER_ID", -1);
            CartManager.getCartItems(this, userId).observe(this, cartItems -> {
                if (cartItems == null || cartItems.isEmpty()) {
                    Toast.makeText(this, "Giỏ hàng trống!", Toast.LENGTH_SHORT).show();
                } else {
                    startActivity(new Intent(this, CartActivityCustomer.class));
                }
            });
        });
    }
    private void setupSearch() {
        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterProducts(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterProducts(newText);
                return true;
            }
        });
    }

    private void filterProducts(String query) {
        java.util.List<Product> filtered = new java.util.ArrayList<>();
        for (Product p : allProducts) {
            if (p.getName().toLowerCase().contains(query.toLowerCase())) {
                filtered.add(p);
            }
        }
        adapter.setProductList(filtered);
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

    @Override
    public void onItemClick(Product product) {
        if (product != null) {
            Log.d("ProductListActivity", "Product ID: " + product.getProduct_id());
            Intent intent = new Intent(this, ProductDetailActivityCustomer.class);
            intent.putExtra("productId", product.getProduct_id());
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_product_list_customer, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        } else if (id == R.id.action_view_cart) {
            startActivity(new Intent(this, CartActivityCustomer.class));
            return true;
        } else if (id == R.id.action_view_profile) {
            startActivity(new Intent(this, EditProfileActivity.class));
            return true;
        } else if (id == R.id.action_logout) {
            SessionManager.logout(this);
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
                    runOnUiThread(() ->
                            Toast.makeText(this, "Không thể tạo giỏ hàng mới", Toast.LENGTH_SHORT).show()
                    );
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