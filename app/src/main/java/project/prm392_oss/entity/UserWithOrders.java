package project.prm392_oss.entity;


import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class UserWithOrders {
    @Embedded
    public User user;

    @Relation(
            parentColumn = "user_id",
            entityColumn = "customer_id"
    )
    public List<Order> orders;
}
