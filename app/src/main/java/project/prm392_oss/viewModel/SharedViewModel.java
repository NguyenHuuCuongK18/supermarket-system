package project.prm392_oss.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import project.prm392_oss.entity.Product;

public class SharedViewModel extends ViewModel {
    private final MutableLiveData<List<Product>> cartItems = new MutableLiveData<>(new ArrayList<>());

    public LiveData<List<Product>> getCartItems() {
        return cartItems;
    }

    public void addToCart(Product product) {
        List<Product> currentCart = cartItems.getValue();
        if (currentCart != null) {
            currentCart.add(product);
            cartItems.setValue(currentCart);
        }
    }

    public void removeFromCart(Product product) {
        List<Product> currentCart = cartItems.getValue();
        if (currentCart != null) {
            currentCart.remove(product);
            cartItems.setValue(currentCart);
        }
    }
}