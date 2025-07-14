package project.prm392_oss.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import project.prm392_oss.dao.OrderDAO;
import project.prm392_oss.dao.OrderDetailsDAO;
import project.prm392_oss.dao.ProductDAO;
import project.prm392_oss.database.AppDatabase;
import project.prm392_oss.entity.Order;
import project.prm392_oss.entity.Supplier;

import project.prm392_oss.entity.OrderDetails;
import project.prm392_oss.dao.UserDAO;
import project.prm392_oss.entity.OrderWithDetails;
import project.prm392_oss.entity.Product;


public class OrderRepository {
    private final OrderDAO orderDAO;
    private final UserDAO userDAO;
    private final ProductDAO productDAO;

    private final OrderDetailsDAO orderDetailsDAO;
    public OrderRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        orderDAO = db.orderDAO();
        userDAO = db.userDAO();
        productDAO = db.productDAO();
        orderDetailsDAO = db.orderDetailsDAO();
    }
    public void insert(Order order) { AppDatabase.databaseWriteExecutor.execute(() -> orderDAO.insert(order)); }
    public void update(Order order) { AppDatabase.databaseWriteExecutor.execute(() -> orderDAO.update(order)); }
    public void delete(Order order) { AppDatabase.databaseWriteExecutor.execute(() -> orderDAO.delete(order)); }
    public LiveData<List<Order>> getAllOrders() { return orderDAO.getAllOrders(); }

    public LiveData<Order> getOrderById(int id) { return orderDAO.getOrderById(id); }
    public void updateOrderStatus(int id, String status) { AppDatabase.databaseWriteExecutor.execute(() -> orderDAO.updateOrderStatus(id, status)); }

    public LiveData<List<Order>> getOrdersByCustomerId(int customerId) {
        return orderDAO.getOrdersByCustomerIdLive(customerId);
    }
    public LiveData<List<OrderDetails>> getOrderDetailsByOrderId(int orderId) {
        return orderDetailsDAO.getOrderDetailsByOrderId(orderId);
    }

    public LiveData<String> getCustomerNameById(int userId) {
        return userDAO.getCustomerNameById(userId);
    }

    public LiveData<OrderWithDetails> getOrderWithDetails(int orderId) {
        return orderDAO.getOrderWithDetails(orderId);
    }
    public LiveData<Product> getProductById(int productId) {
        return productDAO.getProductByIdLive(productId);
    }
}

