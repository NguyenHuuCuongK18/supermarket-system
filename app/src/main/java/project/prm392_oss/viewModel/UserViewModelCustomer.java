package project.prm392_oss.viewModel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import project.prm392_oss.entity.User;
import project.prm392_oss.repository.UserRepository;
import java.util.List;

public class UserViewModelCustomer extends AndroidViewModel {
    private final UserRepository repository;
    private final LiveData<List<User>> allUsers;

    public UserViewModelCustomer(@NonNull Application application) {
        super(application);
        repository = new UserRepository(application);
        allUsers = repository.getAllUsers();
    }

    public LiveData<List<User>> getAllUsers() {
        return allUsers;
    }

    public void insert(User user) {
        repository.insert(user);
    }

    public void update(User user) {
        repository.update(user);
    }

    public void delete(User user) {
        repository.delete(user);
    }
}
