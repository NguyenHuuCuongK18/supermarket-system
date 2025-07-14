package project.prm392_oss.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;
import project.prm392_oss.entity.Role;

@Dao
public interface RoleDAO {

    // Chèn vai trò mới
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Role role);

    // Cập nhật vai trò
    @Update
    void update(Role role);

    // Xóa vai trò
    @Delete
    void delete(Role role);

    // Lấy vai trò theo ID (LiveData giúp UI tự động cập nhật)
    @Query("SELECT * FROM role WHERE role_id = :id")
    LiveData<Role> getRoleById(int id);

    // Lấy danh sách tất cả vai trò (LiveData giúp UI tự động cập nhật)
    @Query("SELECT * FROM role ORDER BY role_id ASC")
    LiveData<List<Role>> getAllRoles();



    @Query("SELECT name FROM Role WHERE role_id = :roleId")
    String getRoleNameById(int roleId);

    @Query("SELECT * FROM ROLE WHERE role_id IN (1,2,3)")
    LiveData<List<Role>> getRolesForEmployees();
    @Query("SELECT name FROM role WHERE role_id = :roleId")
    LiveData<String> getRoleNameByid(int roleId);


}
