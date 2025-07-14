package project.prm392_oss.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;
import java.util.List;
import project.prm392_oss.entity.Order;
import project.prm392_oss.entity.OrderWithDetails;

@Dao
public interface OrderDAO {

    // Chèn đơn hàng mới
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Order order);

    // Cập nhật đơn hàng
    @Update
    void update(Order order);

    // Xóa đơn hàng
    @Delete
    void delete(Order order);

    // Lấy đơn hàng theo ID (LiveData giúp UI tự động cập nhật)
    @Query("SELECT * FROM `ORDER` WHERE order_id = :id")
    LiveData<Order> getOrderById(int id);

    // Lấy danh sách tất cả đơn hàng
    @Query("SELECT * FROM `ORDER` ORDER BY order_id DESC")
    LiveData<List<Order>> getAllOrders();

    // Lấy thông tin đơn hàng cùng chi tiết của nó
    @Transaction
    @Query("SELECT * FROM `ORDER` WHERE order_id = :id")
    LiveData<OrderWithDetails> getOrderWithDetails(int id);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertOrder(Order order);

    @Query("SELECT * FROM `ORDER` WHERE customer_id = :customerId")
    List<Order> getOrdersByCustomerId(int customerId);


    @Query("UPDATE `order` SET status = :status WHERE order_id = :id")
    void updateOrderStatus(int id, String status);

    @Query("SELECT * FROM `order` WHERE customer_id = :customerId")
    LiveData<List<Order>> getOrdersByCustomerIdLive(int customerId);
}
