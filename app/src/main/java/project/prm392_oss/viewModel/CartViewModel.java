package project.prm392_oss.viewModel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import project.prm392_oss.dto.CartItemDTO;
import project.prm392_oss.utils.manager.CartManager;

public class CartViewModel extends ViewModel {
    public LiveData<List<CartItemDTO>> getCartItems(Context context, int userId) {
        return CartManager.getCartItems(context, userId);
    }
}
