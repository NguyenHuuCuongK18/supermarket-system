package project.prm392_oss.viewModel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import project.prm392_oss.database.AppDatabase;
import project.prm392_oss.entity.CartItem;
import project.prm392_oss.entity.Product;

public class ProductViewModelCustomer extends AndroidViewModel {
    private final MutableLiveData<List<Product>> productList = new MutableLiveData<>();
    private final MutableLiveData<List<CartItem>> cartItems = new MutableLiveData<>(new ArrayList<>());
    private final AppDatabase database;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public ProductViewModelCustomer(@NonNull Application application) {
        super(application);
        database = AppDatabase.getInstance(application);
        loadProducts(); // Gọi để cập nhật dữ liệu ngay khi khởi tạo
    }

    public LiveData<List<Product>> getAllProducts() {
        return productList;
    }

    public LiveData<List<CartItem>> getCartItems() {
        return cartItems;
    }

    //  Thêm sản phẩm vào giỏ hàng với số lượng ban đầu là 1
    public void addToCart(Product product) {
        executorService.execute(() -> {
            List<CartItem> currentCart = cartItems.getValue();
            if (currentCart == null) {
                currentCart = new ArrayList<>();
            }

            boolean found = false;
            for (CartItem item : currentCart) {
                if (item.getProductId() == product.getProduct_id()) {
                    // Tăng số lượng nếu sản phẩm đã tồn tại trong giỏ hàng
                    item.setQuantity(item.getQuantity() + 1);
                    found = true;
                    break;
                }
            }

            // Nếu sản phẩm chưa có trong giỏ hàng, thêm mới với số lượng là 1
            if (!found) {
                currentCart.add(new CartItem(
                        0, // ID tự động tăng trong database
                        product.getProduct_id(),
                        1 // Số lượng ban đầu là 1
                ));
            }

            // Cập nhật LiveData
            cartItems.postValue(currentCart);
        });
    }

    // Tăng số lượng sản phẩm trong giỏ hàng
    public void increaseCartItemQuantity(int productId) {
        executorService.execute(() -> {
            List<CartItem> currentCart = cartItems.getValue();
            if (currentCart != null) {
                for (CartItem item : currentCart) {
                    if (item.getProductId() == productId) {
                        item.setQuantity(item.getQuantity() + 1);
                        break;
                    }
                }
                cartItems.postValue(currentCart);
            }
        });
    }

    // ✅ Giảm số lượng sản phẩm trong giỏ hàng
    public void decreaseCartItemQuantity(int productId) {
        executorService.execute(() -> {
            List<CartItem> currentCart = cartItems.getValue();
            if (currentCart != null) {
                CartItem toRemove = null;
                for (CartItem item : currentCart) {
                    if (item.getProductId() == productId) {
                        if (item.getQuantity() > 1) {
                            item.setQuantity(item.getQuantity() - 1);
                        } else {
                            // Nếu số lượng giảm về 0 thì xóa sản phẩm khỏi giỏ hàng
                            toRemove = item;
                        }
                        break;
                    }
                }
                if (toRemove != null) {
                    currentCart.remove(toRemove);
                }
                cartItems.postValue(currentCart);
            }
        });
    }

    // ✅ Xóa sản phẩm khỏi giỏ hàng
    public void removeCartItem(int productId) {
        executorService.execute(() -> {
            List<CartItem> currentCart = cartItems.getValue();
            if (currentCart != null) {
                currentCart.removeIf(item -> item.getProductId() == productId);
                cartItems.postValue(currentCart);
            }
        });
    }

    // Nạp danh sách sản phẩm từ cơ sở dữ liệu
    private void loadProducts() {
        executorService.execute(() -> {
            try {
                List<Product> products = database.productDAO().getAllProductsList();
                if (products != null && !products.isEmpty()) {
                    for (Product product : products) {
                        Log.d("ProductViewModel", "Product ID: " + product.getProduct_id() + ", Name: " + product.getName());
                    }
                    productList.postValue(products);
                } else {
                    Log.d("ProductViewModel", "No products found in database.");
                    productList.postValue(new ArrayList<>()); // Gửi danh sách rỗng để tránh lỗi NullPointerException
                }
            } catch (Exception e) {
                Log.e("ProductViewModel", "Error loading products", e);
            }
        });
    }
}
