package project.prm392_oss.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import project.prm392_oss.dao.RoleDAO;
import project.prm392_oss.database.AppDatabase;
import project.prm392_oss.entity.Role;

public class RoleRepository {
    private final RoleDAO roleDAO;
    public RoleRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        roleDAO = db.roleDAO();
    }
    public void insert(Role role) { AppDatabase.databaseWriteExecutor.execute(() -> roleDAO.insert(role)); }
    public void update(Role role) { AppDatabase.databaseWriteExecutor.execute(() -> roleDAO.update(role)); }
    public void delete(Role role) { AppDatabase.databaseWriteExecutor.execute(() -> roleDAO.delete(role)); }
    public LiveData<List<Role>> getAllRoles() { return roleDAO.getAllRoles(); }
    public LiveData<List<Role>> getRolesForEmployees() {
        return roleDAO.getRolesForEmployees();  // Đảm bảo roleDAO đã có hàm này
    }

}