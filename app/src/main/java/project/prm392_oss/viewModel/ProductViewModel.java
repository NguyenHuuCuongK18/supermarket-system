package project.prm392_oss.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.room.Room;

import java.util.List;

import project.prm392_oss.dao.ProductDAO;
import project.prm392_oss.database.AppDatabase;



import project.prm392_oss.entity.Product;
import project.prm392_oss.repository.ProductRepository;

public class ProductViewModel extends AndroidViewModel {
    private final ProductRepository productRepository;
    private final LiveData<List<Product>> allProducts;
    private ProductDAO productDao;
    private MutableLiveData<List<Product>> productList = new MutableLiveData<>();
    public ProductViewModel(@NonNull Application application) {
        super(application);
        productRepository = new ProductRepository(application);
        allProducts = productRepository.getAllProducts();
        AppDatabase db = AppDatabase.getInstance(application);
        productDao = db.productDAO();
    }
    public LiveData<List<Product>> getAllProducts() {
        return allProducts;
    }

    public LiveData<List<Product>> getProductsByCategory(int categoryId) {
        return productRepository.getProductsByCategory(categoryId);
    }

    public LiveData<List<Product>> getProductList() {
        return productList;
    }

    public LiveData<Product> getProductById(int id) {
        return productRepository.getProductById(id);
    }

    public void insert(Product product) {
        productRepository.insert(product);
    }

    public void update(Product product) {
        productRepository.update(product);
    }

    public void searchProducts(String query) {
        new Thread(() -> {
            List<Product> results = productDao.searchProductsByName("%" + query + "%");
            productList.postValue(results);
        }).start();
    }
    public LiveData<Product> getNewestProduct() {
        return productRepository.getNewestProduct();
    }

}
