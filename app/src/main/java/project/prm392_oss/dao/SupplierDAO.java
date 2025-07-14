package project.prm392_oss.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;
import project.prm392_oss.entity.Supplier;

@Dao
public interface SupplierDAO {

    // Chèn nhà cung cấp mới
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Supplier supplier);

    // Cập nhật nhà cung cấp
    @Update
    void update(Supplier supplier);

    // Xóa nhà cung cấp
    @Delete
    void delete(Supplier supplier);

    // Lấy nhà cung cấp theo ID (LiveData giúp UI tự động cập nhật)
    @Query("SELECT * FROM supplier WHERE supplier_id = :id")
    LiveData<Supplier> getSupplierById(int id);

    @Query("SELECT * FROM SUPPLIER")
        LiveData<List<Supplier>> getAllSuppliers(); // 🔹 Trả về LiveData

        @Query("SELECT * FROM SUPPLIER")
        List<Supplier> getAllSuppliersSync(); // 🔥 Thêm phương thức này để lấy dữ liệu đồng bộ
    }

