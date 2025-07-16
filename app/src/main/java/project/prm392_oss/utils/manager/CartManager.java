package project.prm392_oss.utils.manager;


import android.content.Context;

import androidx.lifecycle.LiveData;

import java.util.List;

import project.prm392_oss.database.AppDatabase;
import project.prm392_oss.dto.CartItemDTO;
import project.prm392_oss.entity.Cart;
import project.prm392_oss.entity.CartItem;
import project.prm392_oss.entity.Product;

public class CartManager {

    public static void addToCart(Context context, int userId, Product product) {
        AppDatabase db = AppDatabase.getInstance(context);
        new Thread(() -> {
            // Lấy Cart theo userId (nếu chưa có thì tạo mới)
            Cart cart = db.cartDAO().getCartByUserId(userId);
            if (cart == null) {
                cart = new Cart(userId);
                long cartId = db.cartDAO().insertCart(cart);
                cart.setId((int) cartId);
            }

            CartItem existingItem = db.cartItemDAO().getCartItemByCartIdAndProductId(cart.getId(), product.getProduct_id());

            if (existingItem != null) {
                existingItem.setQuantity(existingItem.getQuantity() + 1);
                db.cartItemDAO().updateCartItem(existingItem);
            } else {
                CartItem cartItem = new CartItem(cart.getId(), product.getProduct_id(), 1);
                db.cartItemDAO().insertCartItem(cartItem);
            }
        }).start();
    }


    public static LiveData<List<CartItemDTO>> getCartItems(Context context, int userId) {
        AppDatabase db = AppDatabase.getInstance(context);
        return db.cartDAO().getCartItems(userId);
    }


}



