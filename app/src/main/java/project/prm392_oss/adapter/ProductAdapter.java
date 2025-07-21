package project.prm392_oss.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import project.prm392_oss.R;
import project.prm392_oss.activity.UpdateProductActivity;
import project.prm392_oss.entity.Product;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<Product> productList;
    private final Context context;
    private static final int VIEW_TYPE_HEADER = 0;
    private static final int VIEW_TYPE_ITEM = 1;

    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? VIEW_TYPE_HEADER : VIEW_TYPE_ITEM;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_HEADER) {
            View headerView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_product_header, parent, false);
            return new HeaderViewHolder(headerView);
        } else {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_product, parent, false);
            return new ProductViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ProductViewHolder) {
            Product product = productList.get(position - 1); // Subtract 1 for the header item
            ((ProductViewHolder) holder).bind(product);
        }
        // No need to bind header, as it's static
    }

    @Override
    public int getItemCount() {
        return productList.size() + 1;
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        public HeaderViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView productIdTextView, productNameTextView, productQuantityTextView, productPriceTextView;
        ImageView productImageView;
        View btnEdit;

        public ProductViewHolder(View itemView) {
            super(itemView);
            productIdTextView = itemView.findViewById(R.id.productId);
            productNameTextView = itemView.findViewById(R.id.productName);
            productQuantityTextView = itemView.findViewById(R.id.productQuantity);
            productPriceTextView = itemView.findViewById(R.id.productPrice);
            productImageView = itemView.findViewById(R.id.productImage);
            btnEdit = itemView.findViewById(R.id.btnEdit);
        }

        public void bind(Product product) {
            productIdTextView.setText(String.valueOf(product.getProduct_id()));
            productNameTextView.setText(product.getName());
            productQuantityTextView.setText("" + product.getStock_quantity());
            productPriceTextView.setText("$" + product.getSale_price());

            if (product.getImageUrl() != null && !product.getImageUrl().isEmpty()) {
                Glide.with(itemView.getContext())
                        .load(product.getImageUrl())
                        .into(productImageView);
            } else {
                Glide.with(itemView.getContext())
                        .load(R.drawable.defaultimage)
                        .into(productImageView);
            }

            btnEdit.setOnClickListener(v -> {
                Intent intent = new Intent(context, UpdateProductActivity.class);
                intent.putExtra("product_id", String.valueOf(product.getProduct_id()));
                context.startActivity(intent);
            });
        }
    }

    public void updateData(List<Product> newProducts) {
        productList.clear();
        productList.addAll(newProducts);
        notifyDataSetChanged();
    }
}

