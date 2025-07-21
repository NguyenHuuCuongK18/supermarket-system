package project.prm392_oss.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.SharedPreferences;
import android.view.MenuItem;


import project.prm392_oss.activity.BaseActivity;

import com.squareup.picasso.Picasso;

import project.prm392_oss.R;
import project.prm392_oss.entity.Product;
import project.prm392_oss.utils.manager.CartManager;
import project.prm392_oss.database.AppDatabase;

public class ProductDetailActivityCustomer extends BaseActivity {

    private ImageView imgProduct;
    private TextView tvProductName, tvProductDescription, tvProductPrice, tvProductStock;
    private EditText etQuantity;
    private Button btnAddToCart;
    private Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail_customer);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Ánh xạ view từ layout
        imgProduct = findViewById(R.id.imgProduct);
        tvProductName = findViewById(R.id.tvProductName);
        tvProductDescription = findViewById(R.id.tvProductDescription);
        tvProductStock = findViewById(R.id.tvProductStock);
        tvProductPrice = findViewById(R.id.tvProductPrice);
//        etQuantity = findViewById(R.id.etQuantity);
        btnAddToCart = findViewById(R.id.btnAddToCart);

        // Lấy productId từ Intent để truy vấn dữ liệu từ CSDL
        int productId = getIntent().getIntExtra("productId", -1);
        Log.d("ProductDetailActivity", "Received productId: " + productId);

        if (productId != -1) {
            loadProductData(productId);
        } else {
            Toast.makeText(this, "Không tìm thấy sản phẩm", Toast.LENGTH_SHORT).show();
            finish(); // Đóng activity nếu không có productId hợp lệ
        }

        // Xử lý sự kiện khi nhấn "Thêm vào giỏ hàng"
        btnAddToCart.setOnClickListener(v -> {
            int quantity = getQuantity();
            if (quantity > 0) {
                addToCart(product, quantity);
            } else {
                Toast.makeText(this, "Vui lòng nhập số lượng hợp lệ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 🔥 Load dữ liệu sản phẩm từ CSDL
    private void loadProductData(int productId) {
        new Thread(() -> {
            AppDatabase db = AppDatabase.getInstance(this);
            product = db.productDAO().getProductById(productId);
            if (product != null) {
                runOnUiThread(() -> {
                    // Sử dụng Picasso để load ảnh từ URL
                    if (product.getImageUrl() != null && !product.getImageUrl().isEmpty()) {
                        Picasso.get()
                                .load(product.getImageUrl()) // Load từ URL
                                .placeholder(R.drawable.ic_launcher_background) // Ảnh tạm thời khi load
                                .error(R.drawable.ic_launcher_background) // Ảnh khi load lỗi
                                .into(imgProduct);
                    } else {
                        imgProduct.setImageResource(R.drawable.ic_launcher_background);
                    }

                    // Hiển thị thông tin sản phẩm
                    tvProductName.setText(product.getName() != null ? product.getName() : "Không có tên");
                    tvProductDescription.setText(product.getDescription() != null ? product.getDescription() : "Không có mô tả");
                    tvProductPrice.setText("Giá: $" + (product.getSale_price() != 0 ? product.getSale_price() : "N/A"));
                    tvProductStock.setText("Tồn kho: " + product.getStock_quantity());
                });
            } else {
                runOnUiThread(() -> {
                    Toast.makeText(this, "Không tìm thấy sản phẩm", Toast.LENGTH_SHORT).show();
                    finish(); // Đóng activity nếu không tìm thấy sản phẩm
                });
            }
        }).start();
    }

    // 🛒 Xử lý thêm sản phẩm vào giỏ hàng
    private void addToCart(Product product, int quantity) {
        if (product != null) {
            SharedPreferences prefs = getSharedPreferences("USER_PREFS", MODE_PRIVATE);
            int userId = prefs.getInt("USER_ID", -1);
            if (userId != -1) {
//                product.setStock_quantity(quantity);
                CartManager.addToCart(this, userId, product);
                Toast.makeText(this, product.getName() + " đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Không tìm thấy thông tin người dùng", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Không thể thêm sản phẩm vào giỏ hàng", Toast.LENGTH_SHORT).show();
        }
    }

    // ✅ Lấy số lượng từ EditText (tránh lỗi NumberFormatException)
    private int getQuantity() {
        try {
            return Integer.parseInt(etQuantity.getText().toString());
        } catch (NumberFormatException e) {
            return 0;
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
