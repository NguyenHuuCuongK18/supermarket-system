package project.prm392_oss.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import project.prm392_oss.dao.SupplierDAO;
import project.prm392_oss.database.AppDatabase;
import project.prm392_oss.entity.Supplier;

public class SupplierRepository {
    private final SupplierDAO supplierDAO;
    public SupplierRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        supplierDAO = db.supplierDAO();
    }
    public void insert(Supplier supplier) { AppDatabase.databaseWriteExecutor.execute(() -> supplierDAO.insert(supplier)); }
    public void update(Supplier supplier) { AppDatabase.databaseWriteExecutor.execute(() -> supplierDAO.update(supplier)); }
    public void delete(Supplier supplier) { AppDatabase.databaseWriteExecutor.execute(() -> supplierDAO.delete(supplier)); }
    public LiveData<List<Supplier>> getAllSuppliers() { return supplierDAO.getAllSuppliers(); }
    public LiveData<Supplier> getSupplierById(int id) { return supplierDAO.getSupplierById(id); }
}
