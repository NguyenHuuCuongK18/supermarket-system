package project.prm392_oss.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
import project.prm392_oss.entity.Feedback;

@Dao
public interface FeedbackDAO {

    // Chèn phản hồi mới
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Feedback feedback);

    // Cập nhật phản hồi
    @Update
    void update(Feedback feedback);

    // Xóa một phản hồi
    @Delete
    void delete(Feedback feedback);

    // Lấy phản hồi theo ID
    @Query("SELECT * FROM FEEDBACK WHERE feedback_id = :id")
    LiveData<Feedback> getFeedbackById(int id);

    // Lấy danh sách phản hồi theo sản phẩm (LiveData để cập nhật UI tự động)
    @Query("SELECT * FROM FEEDBACK WHERE product_id = :productId ORDER BY feedback_id DESC")
    LiveData<List<Feedback>> getFeedbackByProduct(int productId);

    // Lấy danh sách phản hồi theo khách hàng
    @Query("SELECT * FROM FEEDBACK WHERE customer_id = :customerId ORDER BY feedback_id DESC")
    LiveData<List<Feedback>> getFeedbackByCustomer(int customerId);

    // Lấy toàn bộ phản hồi
    @Query("SELECT * FROM FEEDBACK ORDER BY feedback_id DESC")
    LiveData<List<Feedback>> getAllFeedbacks();
}
