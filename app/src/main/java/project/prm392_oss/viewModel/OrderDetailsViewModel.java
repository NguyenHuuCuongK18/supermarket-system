package project.prm392_oss.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import project.prm392_oss.entity.OrderDetails;
import project.prm392_oss.repository.OrderDetailsRepository;

public class OrderDetailsViewModel extends AndroidViewModel {
    private OrderDetailsRepository repository;
    private final LiveData<List<OrderDetails>> allOrderDetails;

    public OrderDetailsViewModel(@NonNull Application application) {
        super(application);
        repository = new OrderDetailsRepository(application);
        allOrderDetails = repository.getAllOrderDetails();
    }

    public LiveData<List<OrderDetails>> getAllOrderDetails() {
        return allOrderDetails;
    }
    public void update(OrderDetails orderDetails) {
        repository.update(orderDetails);
    }
    public LiveData<List<OrderDetails>> getOrderDetailsByOrderId(int orderId) {
        return repository.getOrderDetailsByOrderId(orderId);
    }
    public LiveData<OrderDetails> getOrderDetailsById(int orderId) {
        return repository.getOrderDetailsById(orderId);
    }
}
