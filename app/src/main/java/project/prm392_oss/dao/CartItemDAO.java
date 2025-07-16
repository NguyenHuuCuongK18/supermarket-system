package project.prm392_oss.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import project.prm392_oss.dto.CartItemDTO;
import project.prm392_oss.entity.CartItem;
import project.prm392_oss.entity.Product;

@Dao
public interface CartItemDAO {
    @Insert
    void insertCartItem(CartItem cartItem);

    @Query("SELECT * FROM cart_item WHERE cart_id= :cartId")
    LiveData<List<CartItem>> getCartItems(int cartId);

    @Delete
    void deleteCartItem(CartItem cartItem);

    @Query("DELETE FROM cart_item WHERE cart_id = :cartId AND product_id = :productId")
    void deleteByCartIdAndProductId(int cartId, int productId);

    @Query("SELECT * FROM product WHERE product_id = :productId")
    Product getProductById(int productId);

    @Query("SELECT * FROM cart_item WHERE cart_id = :cartId AND product_id = :productId LIMIT 1")
    CartItem getCartItemByCartIdAndProductId(int cartId, int productId);

    @Update
    void updateCartItem(CartItem cartItem);

    @Query("SELECT ci.cart_id AS cartId, ci.product_id AS productId, ci.quantity " +
            "FROM cart_item ci " +
            "WHERE ci.cart_id = :cartId")
    List<CartItemDTO> getCartItemsByCartId(int cartId);



}
