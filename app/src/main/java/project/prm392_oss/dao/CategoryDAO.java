package project.prm392_oss.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Delete;

import java.util.List;
import project.prm392_oss.entity.Category;

@Dao
public interface CategoryDAO {

    // Thêm một danh mục
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Category category);

    // Cập nhật danh mục
    @Update
    void update(Category category);

    // Xóa danh mục
    @Delete
    void delete(Category category);

    // Xóa tất cả danh mục
    @Query("DELETE FROM CATEGORY")
    void deleteAll();

    // Lấy tất cả danh mục (Sử dụng LiveData để tự động cập nhật UI)

    @Query("SELECT * FROM CATEGORY ORDER BY category_id ASC")
    LiveData<List<Category>> getAllCategories();

    @Query("SELECT * FROM Category")
    List<Category> getAllCategoriesList();

    @Query("SELECT * FROM category WHERE category_id = :categoryId LIMIT 1")
    LiveData<Category> getCategoryById(int categoryId);
}
