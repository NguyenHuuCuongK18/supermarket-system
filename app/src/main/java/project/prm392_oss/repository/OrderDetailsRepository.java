package project.prm392_oss.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import project.prm392_oss.dao.OrderDetailsDAO;
import project.prm392_oss.database.AppDatabase;
import project.prm392_oss.entity.OrderDetails;

public class OrderDetailsRepository {
    private final OrderDetailsDAO orderDetailsDAO;
    public OrderDetailsRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        orderDetailsDAO = db.orderDetailsDAO();
    }
    public void insert(OrderDetails orderDetails) { AppDatabase.databaseWriteExecutor.execute(() -> orderDetailsDAO.insert(orderDetails)); }
    public void update(OrderDetails orderDetails) { AppDatabase.databaseWriteExecutor.execute(() -> orderDetailsDAO.update(orderDetails)); }
    public void delete(OrderDetails orderDetails) { AppDatabase.databaseWriteExecutor.execute(() -> orderDetailsDAO.delete(orderDetails)); }
    public LiveData<List<OrderDetails>> getOrderDetailsByOrderId(int orderId) { return orderDetailsDAO.getOrderDetailsByOrderId(orderId); }
    public LiveData<List<OrderDetails>> getAllOrderDetails() { return orderDetailsDAO.getAllOrderDetails(); }
    public LiveData<OrderDetails> getOrderDetailsById(int id) { return orderDetailsDAO.getOrderDetailsById(id); }
}