package project.prm392_oss.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import project.prm392_oss.dao.CategoryDAO;
import project.prm392_oss.database.AppDatabase;
import project.prm392_oss.entity.Category;

public class CategoryRepository {
    private final CategoryDAO categoryDAO;

    public CategoryRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        categoryDAO = db.categoryDAO();
    }

    public void insert(Category category) {
        AppDatabase.databaseWriteExecutor.execute(() -> categoryDAO.insert(category));
    }

    public void update(Category category) {
        AppDatabase.databaseWriteExecutor.execute(() -> categoryDAO.update(category));
    }

    public void delete(Category category) {
        AppDatabase.databaseWriteExecutor.execute(() -> categoryDAO.delete(category));
    }

    public LiveData<List<Category>> getAllCategories() {
        return categoryDAO.getAllCategories();
    }
    public LiveData<Category> getCategoryById(int categoryId) {
        return categoryDAO.getCategoryById(categoryId);
    }
}
