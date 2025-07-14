package project.prm392_oss.viewModel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import project.prm392_oss.entity.Category;
import project.prm392_oss.entity.User;
import project.prm392_oss.repository.CategoryRepository;
import project.prm392_oss.repository.UserRepository;

public class CategoryViewModel extends AndroidViewModel {
    private CategoryRepository categoryRepository;
    private final LiveData<List<Category>> allCategories;


    public CategoryViewModel(@NonNull Application application) {
        super(application);
        categoryRepository = new CategoryRepository(application);
        allCategories = categoryRepository.getAllCategories();
    }

    public LiveData<List<Category>> getAllCategories() {
        return allCategories;
    }

    public void insert(Category category) {
        categoryRepository.insert(category);
    }

    public LiveData<Category> getCategoryById(int categoryId) {
        return categoryRepository.getCategoryById(categoryId);
    }
}
