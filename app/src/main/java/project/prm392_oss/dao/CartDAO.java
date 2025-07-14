package project.prm392_oss.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import project.prm392_oss.dto.CartItemDTO;
import project.prm392_oss.entity.Cart;
import project.prm392_oss.entity.CartItem;

@Dao
public interface CartDAO {
    @Insert
    long insertCart(Cart cart);

    @Query("SELECT * FROM cart WHERE user_id = :userId")
    Cart getCartByUserId(long userId);

    @Delete
    void deleteCart(Cart cart);

    // Lấy thông tin cartItem theo userId và productId
    @Query("SELECT * FROM cart_item WHERE cart_id = :userId AND product_id = :productId LIMIT 1")
    CartItem getCartItemByProductId(int userId, long productId);

    // Lấy danh sách sản phẩm trong giỏ hàng thông qua JOIN
    @Query("SELECT ci.id AS id, ci.cart_id AS cartId, ci.product_id AS productId, ci.quantity AS quantity " +
            "FROM cart_item ci " +
            "JOIN cart c ON ci.cart_id = c.id " +
            "WHERE c.user_id = :userId")
    LiveData<List<CartItemDTO>> getCartItems(int userId);

    @Query("SELECT id FROM cart WHERE user_id = :userId LIMIT 1")
    int getCartIdByUserId(int userId);





}

