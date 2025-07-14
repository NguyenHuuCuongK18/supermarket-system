package project.prm392_oss.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;


import project.prm392_oss.dao.ProductDAO;
import project.prm392_oss.database.AppDatabase;
import project.prm392_oss.entity.Product;

public class ProductRepository {
    private final ProductDAO productDAO;
    public ProductRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        productDAO = db.productDAO();

    }
    public void insert(Product product) { AppDatabase.databaseWriteExecutor.execute(() -> productDAO.insert(product)); }
    public void update(Product product) { AppDatabase.databaseWriteExecutor.execute(() -> productDAO.update(product)); }
    public void delete(Product product) { AppDatabase.databaseWriteExecutor.execute(() -> productDAO.delete(product)); }
    public LiveData<List<Product>> getAllProducts() { return productDAO.getAllProducts(); }

    public LiveData<Product> getRandomProduct() {
        return productDAO.getRandomProduct();
    }


    // Trả về LiveData<List<Product>>
    public LiveData<List<Product>> getProductsByCategory(int categoryId) {
        return productDAO.getProductsByCategory(categoryId);
    }

    // Trả về LiveData<Product>
    public LiveData<Product> getProductById(int id) {
        return productDAO.getProductByIdLive(id);
    }


    public LiveData<List<Product>> searchProducts(String query) {
        MutableLiveData<List<Product>> results = new MutableLiveData<>();
        new Thread(() -> results.postValue(productDAO.searchProductsByName("%" + query + "%"))).start();
        return results;

    }
    public LiveData<Product> getNewestProduct(){
        return productDAO.getNewestProduct();
    }
}
