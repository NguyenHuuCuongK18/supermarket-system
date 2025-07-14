package project.prm392_oss.dto;

import androidx.room.Ignore;

import java.io.Serializable;

import project.prm392_oss.entity.CartItem;

public class CartItemDTO implements Serializable {
    private int cartId;
    private int productId;
    private int quantity;

    @Ignore
    private boolean isSelected;

    public CartItemDTO(int cartId, int productId, int quantity) {
        this.cartId = cartId;
        this.productId = productId;
        this.quantity = quantity;
        this.isSelected = false;
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }


    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public CartItem toCartItem() {
        CartItem cartItem = new CartItem();
        cartItem.setCartId(this.cartId);
        cartItem.setProductId(this.productId);
        cartItem.setQuantity(this.quantity);
        return cartItem;
    }

}
