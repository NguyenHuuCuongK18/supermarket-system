package project.prm392_oss.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;
import project.prm392_oss.entity.OrderDetails;

@Dao
public interface OrderDetailsDAO {

    // Chèn chi tiết đơn hàng mới
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(OrderDetails orderDetails);

    // Cập nhật chi tiết đơn hàng
    @Update
    void update(OrderDetails orderDetails);

    // Xóa chi tiết đơn hàng
    @Delete
    void delete(OrderDetails orderDetails);

    // Lấy chi tiết đơn hàng theo ID (LiveData để tự động cập nhật UI)
    @Query("SELECT * FROM order_details WHERE order_details_id = :id")
    LiveData<OrderDetails> getOrderDetailsById(int id);

    // Lấy danh sách chi tiết đơn hàng theo ID đơn hàng
    @Query("SELECT * FROM order_details WHERE order_id = :orderId ORDER BY order_details_id DESC")
    LiveData<List<OrderDetails>> getOrderDetailsByOrderId(int orderId);

    // Lấy danh sách tất cả chi tiết đơn hàng
    @Query("SELECT * FROM order_details ORDER BY order_details_id DESC")
    LiveData<List<OrderDetails>> getAllOrderDetails();
}
