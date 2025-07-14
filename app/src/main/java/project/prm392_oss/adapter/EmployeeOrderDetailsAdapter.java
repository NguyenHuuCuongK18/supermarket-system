package project.prm392_oss.adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import project.prm392_oss.R;
import project.prm392_oss.entity.OrderDetails;
import project.prm392_oss.viewModel.ProductViewModel;

public class EmployeeOrderDetailsAdapter extends RecyclerView.Adapter<EmployeeOrderDetailsAdapter.OrderDetailsViewHolder>{
    private Context context;
    private List<OrderDetails> list;
    private ProductViewModel productViewModel;

    public EmployeeOrderDetailsAdapter(Context context, List<OrderDetails> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public OrderDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OrderDetailsViewHolder(LayoutInflater.from(context).inflate(R.layout.employee_order_details_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OrderDetailsViewHolder holder, int position) {
        OrderDetails od = list.get(position);
        productViewModel = new ViewModelProvider((ViewModelStoreOwner) context).get(ProductViewModel.class);
        productViewModel.getProductById(od.getProduct_id()).observe((LifecycleOwner) context, product -> {
            //display image
            String imagePath = product.getImageUrl();
            Log.d("OrderDetailsAdapter", "ImagePath: " + imagePath);
            if (imagePath != null && !imagePath.isEmpty()) {
                if (imagePath.startsWith("http://") || imagePath.startsWith("https://")) {
                    Picasso.get().load(imagePath).into(holder.order_details_img);
                } else if (imagePath.startsWith("content://")) {
                    Picasso.get().load(Uri.parse(imagePath)).into(holder.order_details_img);
                } else {
                    holder.order_details_img.setImageResource(R.drawable.img);
                }
            } else {
                holder.order_details_img.setImageResource(R.drawable.img);
            }

            holder.product_name_tv.setText(product.getName()+"   ");
            holder.product_price_tv.setText("Unit price: "+product.getSale_price()+" $");
            holder.product_quantity_tv.setText("x" + od.getQuantity()+"    ");
            holder.product_total_tv.setText("Total: " + String.valueOf(product.getSale_price() * od.getQuantity()) + " $");
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class OrderDetailsViewHolder extends RecyclerView.ViewHolder {
        TextView product_name_tv, product_quantity_tv, product_price_tv, product_total_tv;
        ImageView order_details_img;

        public OrderDetailsViewHolder(@NonNull View itemView) {
            super(itemView);
            product_name_tv = itemView.findViewById(R.id.order_details_product_name_tv);
            product_quantity_tv = itemView.findViewById(R.id.order_details_quantity_tv);
            product_price_tv = itemView.findViewById(R.id.order_details_unit_price_tv);
            product_total_tv = itemView.findViewById(R.id.order_details_total_tv);
            order_details_img = itemView.findViewById(R.id.order_details_img);
        }
    }
}
