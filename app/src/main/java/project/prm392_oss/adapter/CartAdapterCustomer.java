package project.prm392_oss.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import project.prm392_oss.R;
import project.prm392_oss.dao.CartItemDAO;
import project.prm392_oss.database.AppDatabase;
import project.prm392_oss.dto.CartItemDTO;
import project.prm392_oss.entity.Product;

public class CartAdapterCustomer extends RecyclerView.Adapter<CartAdapterCustomer.CartViewHolder> {

    private final Context context;
    private List<CartItemDTO> cartItems;
    private final OnItemRemoveListener onItemRemoveListener;
    private final OnItemSelectListener onItemSelectListener;
    private final CartItemDAO cartItemDAO;

    public CartAdapterCustomer(Context context, List<CartItemDTO> cartItems,
                       OnItemRemoveListener onItemRemoveListener,
                       OnItemSelectListener onItemSelectListener,
                       CartItemDAO cartItemDAO) {
        this.context = context;
        this.cartItems = new ArrayList<>(cartItems);
        this.onItemRemoveListener = onItemRemoveListener;
        this.onItemSelectListener = onItemSelectListener;
        this.cartItemDAO = cartItemDAO;
    }

    public CartAdapterCustomer (Context context, List<CartItemDTO> cartItems) {
        this(context, cartItems, null, null, AppDatabase.getInstance(context).cartItemDAO());
    }



    public void setCartItems(List<CartItemDTO> newCartItems) {
        this.cartItems.clear();
        this.cartItems.addAll(newCartItems);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart_customer, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItemDTO cartItem = cartItems.get(position);


        Executors.newSingleThreadExecutor().execute(() -> {
            Product product = cartItemDAO.getProductById(cartItem.getProductId());

            ((android.app.Activity) context).runOnUiThread(() -> {
                if (product != null) {
                    holder.tvProductName.setText(product.getName());
                    holder.tvProductPrice.setText(String.format("%,.0f$", (double) product.getSale_price()));

                } else {
                    holder.tvProductName.setText("Sản phẩm không tồn tại");
                    holder.tvProductPrice.setText("0$");
                }
            });
        });

        // 👉 Hiển thị số lượng và trạng thái chọn của sản phẩm
        holder.tvQuantity.setText(String.valueOf(cartItem.getQuantity()));
        holder.cbSelectProduct.setChecked(cartItem.isSelected());

        // 👉 Xử lý chọn sản phẩm
        holder.cbSelectProduct.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (holder.getAdapterPosition() != RecyclerView.NO_POSITION) {
                cartItem.setSelected(isChecked);
                if (onItemSelectListener != null) {
                    onItemSelectListener.onItemSelected(cartItem);
                }
            }
        });

        // 👉 Tăng số lượng sản phẩm
        holder.btnIncrease.setOnClickListener(v -> {
            if (holder.getAdapterPosition() != RecyclerView.NO_POSITION) {
                cartItem.setQuantity(cartItem.getQuantity() + 1);
                notifyItemChanged(holder.getAdapterPosition());
            }
        });

        // 👉 Giảm số lượng sản phẩm (tối thiểu là 1)
        holder.btnDecrease.setOnClickListener(v -> {
            if (holder.getAdapterPosition() != RecyclerView.NO_POSITION && cartItem.getQuantity() > 1) {
                cartItem.setQuantity(cartItem.getQuantity() - 1);
                notifyItemChanged(holder.getAdapterPosition());
            } else {
                Toast.makeText(context, "Số lượng tối thiểu là 1", Toast.LENGTH_SHORT).show();
            }
        });

        // 👉 Xóa sản phẩm khỏi giỏ hàng
        holder.btnDelete.setOnClickListener(v -> {
            int currentPosition = holder.getAdapterPosition();
            if (currentPosition != RecyclerView.NO_POSITION) {
                CartItemDTO removeItem = cartItems.get(currentPosition);
                cartItems.remove(currentPosition);
                notifyItemRemoved(currentPosition);
                notifyItemRangeChanged(currentPosition, cartItems.size());

                Toast.makeText(context, "Sản phẩm đã được xóa", Toast.LENGTH_SHORT).show();
                Executors.newSingleThreadExecutor().execute(() ->
                        cartItemDAO.deleteByCartIdAndProductId(removeItem.getCartId(), removeItem.getProductId()));
                if (onItemRemoveListener != null) {
                    onItemRemoveListener.onItemRemoved(currentPosition);
                }
            }
        });
    }



    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    // ✅ Interface cho sự kiện xóa sản phẩm
    public interface OnItemRemoveListener {
        void onItemRemoved(int position);
    }

    // ✅ Interface cho sự kiện chọn sản phẩm
    public interface OnItemSelectListener {
        void onItemSelected(CartItemDTO cartItem);
    }

    // ✅ ViewHolder
    public static class CartViewHolder extends RecyclerView.ViewHolder {
        CheckBox cbSelectProduct;
        TextView tvProductName, tvProductPrice, tvQuantity;
        Button btnIncrease, btnDecrease;
        ImageButton btnDelete;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            cbSelectProduct = itemView.findViewById(R.id.cbSelectProduct);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            btnIncrease = itemView.findViewById(R.id.btnIncrease);
            btnDecrease = itemView.findViewById(R.id.btnDecrease);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }

    public List<CartItemDTO> getSelectedItems() {
        List<CartItemDTO> selectedItems = new ArrayList<>();
        for (CartItemDTO item : cartItems) {
            if (item.isSelected()) {
                selectedItems.add(item);
            }
        }
        return selectedItems;
    }

}
