package project.prm392_oss.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import project.prm392_oss.R;
import project.prm392_oss.activity.ProductDetailActivityCustomer;
import project.prm392_oss.entity.Product;

public class ProductAdapterCustomer extends RecyclerView.Adapter<ProductAdapterCustomer.ProductViewHolder> {

    private List<Product> productList;
    private final Context context;
    private final OnAddToCartClickListener addToCartClickListener;
    private final OnItemClickListener itemClickListener;
    private final List<Product> cartItems = new ArrayList<>();

    // Interface để xử lý sự kiện Add to Cart
    public interface OnAddToCartClickListener {
        void onAddToCart(Product product);
    }

    // Interface để xử lý khi click vào sản phẩm
    public interface OnItemClickListener {
        void onItemClick(Product product);
    }

    public ProductAdapterCustomer(Context context, OnAddToCartClickListener addToCartClickListener, OnItemClickListener itemClickListener) {
        this.context = context;
        this.addToCartClickListener = addToCartClickListener;
        this.itemClickListener = itemClickListener;
        this.productList = new ArrayList<>();
    }

    public void setProductList(List<Product> products) {
        if (products == null) {
            this.productList = new ArrayList<>();
        } else {
            this.productList = new ArrayList<>(products);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product_customer, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        if (product == null) {
            return;
        }

        holder.textViewName.setText(product.getName());
        String formattedPrice = NumberFormat.getCurrencyInstance(new Locale("en", "US"))
                .format(product.getSale_price());
        holder.textViewPrice.setText(formattedPrice);
        holder.textViewStock.setText("Stock: " + product.getStock_quantity());

        // Tải ảnh từ URL bằng Picasso
        Picasso.get()
                .load(product.getImageUrl()) // Lấy URL ảnh từ product
                .placeholder(R.drawable.placeholder_image) // Ảnh hiển thị khi tải ảnh
                .error(R.drawable.placeholder_image) // Ảnh hiển thị khi tải thất bại
                .into(holder.ivProductImage);

        // 👉 Xử lý sự kiện khi nhấn vào sản phẩm
        if (itemClickListener != null) {
            holder.itemView.setOnClickListener(v -> itemClickListener.onItemClick(product));
        }

        // 👉 Xử lý sự kiện khi nhấn vào nút "Add to Cart"
        holder.btnAddToCart.setOnClickListener(v -> {
            if (addToCartClickListener != null) {
                addToCartClickListener.onAddToCart(product);
                cartItems.add(product);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public List<Product> getCartItems() {
        return cartItems;
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName, textViewPrice, textViewStock;
        Button btnAddToCart;
        ImageView ivProductImage;

        public ProductViewHolder(View itemView) {
            super(itemView);
            ivProductImage = itemView.findViewById(R.id.ivProductImage);
            textViewName = itemView.findViewById(R.id.tvProductName);
            textViewPrice = itemView.findViewById(R.id.tvProductPrice);
            textViewStock = itemView.findViewById(R.id.tvProductStock);
            btnAddToCart = itemView.findViewById(R.id.btnAddToCart);

        }
    }
}
