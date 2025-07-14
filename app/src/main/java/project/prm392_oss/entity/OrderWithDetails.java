package project.prm392_oss.entity;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class OrderWithDetails {

    @Embedded
    public Order order;

    @Relation(
            parentColumn = "order_id",
            entityColumn = "order_id"
    )
    public List<OrderDetails> orderDetails;

    // Getters and Setters
    public List<OrderDetails> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetails> orderDetails) {
        this.orderDetails = orderDetails;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
