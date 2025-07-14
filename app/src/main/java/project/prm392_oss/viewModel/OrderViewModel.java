package project.prm392_oss.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import project.prm392_oss.entity.Order;

import project.prm392_oss.entity.OrderDetails;
import project.prm392_oss.entity.OrderWithDetails;
import project.prm392_oss.entity.Product;
import project.prm392_oss.repository.OrderRepository;

public class OrderViewModel extends AndroidViewModel {

    private OrderRepository orderRepository;
    private final LiveData<List<Order>> allOrders;

    public OrderViewModel(@NonNull Application application) {
        super(application);
        orderRepository = new OrderRepository(application);
        allOrders = orderRepository.getAllOrders();
    }
    public LiveData<List<Order>> getAllOrders() {
        return allOrders;
    }

    public void insert(Order o) {
        orderRepository.insert(o);
    }
    public void update(Order o) {orderRepository.update(o);}

    public LiveData<Order> getOrderById(int id) {
        return orderRepository.getOrderById(id);
    }
    public void updateOrderStatus(int id, String status) {
        orderRepository.updateOrderStatus(id, status);
    }



    public LiveData<List<Order>> getOrdersByCustomerId(int customerId) {
        return orderRepository.getOrdersByCustomerId(customerId);
    }
    public LiveData<List<OrderDetails>> getOrderDetailsByOrderId(int orderId) {
        return orderRepository.getOrderDetailsByOrderId(orderId);
    }

    public LiveData<String> getCustomerNameById(int userId) {
        return orderRepository.getCustomerNameById(userId);
    }

    public LiveData<OrderWithDetails> getOrderWithDetails(int orderId) {
        return orderRepository.getOrderWithDetails(orderId);
    }
    public LiveData<Product> getProductById(int productId) {
        return orderRepository.getProductById(productId);
    }

}
