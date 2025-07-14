package project.prm392_oss.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import project.prm392_oss.entity.Order;
import project.prm392_oss.entity.Product;

@Dao
public interface ProductDAO {

    // Cập nhật sản phẩm
    @Update
    void update(Product product);

    // Xóa đơn hàng
    @Delete
    void delete(Product product);
    @Insert
    void insert(Product product);

    @Query("SELECT * FROM PRODUCT")
    LiveData<List<Product>> getAllProducts(); // Dành cho LiveData

    @Query("SELECT * FROM PRODUCT")
    List<Product> getAllProductsList(); // Dành cho kiểm tra trực tiếp

    @Query("SELECT sale_price FROM Product WHERE product_id = :productId")
    double getPriceById(int productId);


    @Query("SELECT * FROM PRODUCT WHERE product_id = :productId")
    Product getProductById(int productId);


    @Query("SELECT * FROM PRODUCT ORDER BY RANDOM() LIMIT 1")
    LiveData<Product> getRandomProduct();
    // Lấy sản phẩm theo danh mục
    @Query("SELECT * FROM PRODUCT WHERE category_id = :category")
    LiveData<Product> getProductsByCategory(String category);


    // Tìm kiếm sản phẩm theo tên
    @Query("SELECT * FROM PRODUCT WHERE name LIKE '%' || :name || '%'")
    List<Product> searchProductsByName(String name);

    @Query("SELECT * FROM product WHERE category_id = :categoryId ORDER BY product_id ASC")
    LiveData<List<Product>> getProductsByCategory(int categoryId);

    @Query("SELECT * FROM product WHERE product_id = :id LIMIT 1")
    LiveData<Product> getProductByIdLive(int id);



    @Query("SELECT * FROM PRODUCT ORDER BY product_id DESC LIMIT 1")
    LiveData<Product> getNewestProduct();

}
