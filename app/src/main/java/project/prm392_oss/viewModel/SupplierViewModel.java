package project.prm392_oss.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import project.prm392_oss.entity.Category;
import project.prm392_oss.entity.Supplier;
import project.prm392_oss.repository.CategoryRepository;
import project.prm392_oss.repository.SupplierRepository;

public class SupplierViewModel extends AndroidViewModel {
    private SupplierRepository supplierRepository;
    private final LiveData<List<Supplier>> allSuppliers;

    public SupplierViewModel(@NonNull Application application) {
        super(application);
        supplierRepository = new SupplierRepository(application);
        allSuppliers = supplierRepository.getAllSuppliers();
    }
    public LiveData<List<Supplier>> getAllSuppliers() {
        return allSuppliers;
    }

    public void insert(Supplier supplier) {
        supplierRepository.insert(supplier);
    }
    public void update(Supplier supplier) {supplierRepository.update(supplier);}

    public LiveData<Supplier> getSupplierById(int id) {
        return supplierRepository.getSupplierById(id);
    }
}
