package project.prm392_oss.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;
import project.prm392_oss.entity.User;

@Dao
public interface UserDAO {

    // Chèn người dùng mới
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(User user);

    // Cập nhật người dùng
    @Update
    void update(User user);

    // Xóa người dùng
    @Delete
    void delete(User user);

    // Lấy thông tin người dùng theo ID (LiveData giúp UI tự động cập nhật)
    @Query("SELECT * FROM user WHERE user_id = :id")
    LiveData<User> getUserById(int id);

    // Lấy danh sách tất cả người dùng (LiveData giúp UI tự động cập nhật)
    @Query("SELECT * FROM user ORDER BY user_id ASC")
    LiveData<List<User>> getAllUsers();

    @Query("SELECT * FROM user WHERE user_id = :id LIMIT 1")
    User getUserByIdWelcome(int id); // Trả về User thay vì LiveData<User>

    @Insert
    void registerUser(User user);

    @Query("SELECT * FROM user WHERE username = :username OR email = :email LIMIT 1")
    User getUserByUsernameOrEmail(String username, String email);

    @Query("SELECT * FROM User WHERE username = :username AND password = :password LIMIT 1")
    User getUserByUsernameAndPassword(String username, String password);

    @Query("SELECT * FROM user WHERE user_id = :id")
    User getUserByid(int id);

    @Query("UPDATE user SET username = :username, name = :fullName, email = :email, phone = :phone, address = :address WHERE user_id = :id")
    void updateUser(int id, String username, String fullName, String email, String phone, String address);

    @Query("SELECT * FROM user WHERE email = :userEmail LIMIT 1")
    User getUserByEmail(String userEmail);

    @Query("UPDATE user SET password = :newPassword WHERE email = :userEmail")
    void updatePassword(String userEmail, String newPassword);

    @Query("SELECT * FROM user WHERE (username = :input OR email = :input) AND password = :password LIMIT 1")
    User getUserByUsernameOrEmailAndPassword(String input, String password);





    @Query("SELECT * FROM user WHERE role_id = :roleId")
    LiveData<List<User>> getUsersByRoleId(int roleId);
    @Query("SELECT name FROM USER WHERE user_id = :userId")
    LiveData<String> getCustomerNameById(int userId);
}
