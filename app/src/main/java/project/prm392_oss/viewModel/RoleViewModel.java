package project.prm392_oss.viewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import project.prm392_oss.entity.Role;
import project.prm392_oss.repository.RoleRepository;

import java.util.List;

public class RoleViewModel extends AndroidViewModel {

    private RoleRepository roleRepository;
    private LiveData<List<Role>> allRoles;

    public RoleViewModel(Application application) {
        super(application);
        roleRepository = new RoleRepository(application);
        allRoles = roleRepository.getAllRoles();
    }

    public LiveData<List<Role>> getAllRoles() {
        return allRoles;
    }
    public void insertRole(Role role) {
        roleRepository.insert(role);
    }
    public LiveData<List<Role>> getRolesForEmployees() {
        return roleRepository.getRolesForEmployees();
    }
}
