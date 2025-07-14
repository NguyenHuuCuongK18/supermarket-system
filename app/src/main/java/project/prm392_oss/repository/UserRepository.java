package project.prm392_oss.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;
import java.util.List;

import project.prm392_oss.dao.RoleDAO;
import project.prm392_oss.dao.UserDAO;
import project.prm392_oss.database.AppDatabase;
import project.prm392_oss.entity.User;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UserRepository {
    private final UserDAO userDAO;
    private final RoleDAO roleDAO;
    private final LiveData<List<User>> allUsers;
    private final ExecutorService executorService;


    public UserRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        userDAO = db.userDAO();
        roleDAO = db.roleDAO();
        allUsers = userDAO.getAllUsers();
        executorService = Executors.newSingleThreadExecutor();
    }
    public LiveData<User> getUserById(int userId) {
        return userDAO.getUserById(userId);
    }
    public LiveData<List<User>> getAllUsers() {
        return allUsers;
    }

    public void insert(User user) {
        executorService.execute(() -> userDAO.insert(user));
    }

    public void update(User user) {
        executorService.execute(() -> userDAO.update(user));
    }

    public void delete(User user) {
        executorService.execute(() -> userDAO.delete(user));
    }

    public void registerUser(User user) {
        userDAO.registerUser(user);
    }

    public void isUserRegistered(String username, String email, UserCheckCallback callback) {
        Executors.newSingleThreadExecutor().execute(() -> {
            User user = userDAO.getUserByUsernameOrEmail(username, email);
            callback.onResult(user != null);
        });
    }


    public User getUserByUsernameAndPassword(String username, String password) {
        return userDAO.getUserByUsernameAndPassword(username, password);
    }

    public void loginUser(String usernameoremail, String password, LoginCallback callback) {
        Executors.newSingleThreadExecutor().execute(() -> {
            User user = userDAO.getUserByUsernameOrEmailAndPassword(usernameoremail, password);
            callback.onResult(user);
        });
    }

    // Interface callback để trả kết quả
    public interface LoginCallback {
        void onResult(User user);
    }

    public interface UserCheckCallback {
        void onResult(boolean isRegistered);
    }



    public void getRoleName(int roleId, RoleCallback callback) {
        Executors.newSingleThreadExecutor().execute(() -> {
            String roleName = roleDAO.getRoleNameById(roleId);
            callback.onResult(roleName);
        });
    }

    public interface RoleCallback {
        void onResult(String roleName);
    }

    

    public LiveData<User> getUserByid(int userId) {
        return userDAO.getUserById(userId);
    }
    public LiveData<String> getRoleName(int roleId) {
        return roleDAO.getRoleNameByid(roleId);
    }
    public LiveData<List<User>> getCustomers() {
        return userDAO.getUsersByRoleId(4);
    }

}
