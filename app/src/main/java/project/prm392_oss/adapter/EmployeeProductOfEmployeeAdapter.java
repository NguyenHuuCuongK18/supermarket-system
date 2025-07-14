package project.prm392_oss.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import project.prm392_oss.R;
import project.prm392_oss.activity.UpdateProductActivity;
import project.prm392_oss.entity.Product;

public class EmployeeProductOfEmployeeAdapter extends BaseAdapter {
    private List<Product> list;
    private Context context;

    public EmployeeProductOfEmployeeAdapter(List<Product> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return list.get(position).getProduct_id();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ProductViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.employee_product_item, parent, false);
            holder = new ProductViewHolder();
            holder.product_name_tv = convertView.findViewById(R.id.product_name_tv);
            holder.import_price_tv = convertView.findViewById(R.id.import_price_tv);
            holder.sale_price_tv = convertView.findViewById(R.id.sale_price_tv);
            holder.product_quantity_tv = convertView.findViewById(R.id.product_quantity_tv);
            holder.product_img_of_lv = convertView.findViewById(R.id.product_img_of_lv);
            holder.update_product_btn = convertView.findViewById(R.id.update_product_btn);
            convertView.setTag(holder);
        } else {
            holder = (ProductViewHolder) convertView.getTag();
        }

        Product p = list.get(position);
        holder.product_name_tv.setText(p.getName());
        holder.import_price_tv.setText("Import Price: " + p.getImport_price());
        holder.sale_price_tv.setText("Sale Price: " + p.getSale_price());
        holder.product_quantity_tv.setText("Quantity: " + p.getStock_quantity());
        //display image
        String imagePath = p.getImageUrl();
        if (imagePath != null && !imagePath.isEmpty()) {
            if (imagePath.startsWith("http://") || imagePath.startsWith("https://")) {
                Picasso.get().load(imagePath).into(holder.product_img_of_lv);
            } else if (imagePath.startsWith("content://")) {
                 Picasso.get().load(Uri.parse(imagePath)).into(holder.product_img_of_lv);
            } else {
                holder.product_img_of_lv.setImageResource(R.drawable.ic_launcher_background);
            }
        } else {
            holder.product_img_of_lv.setImageResource(R.drawable.ic_launcher_background);
        }

        holder.update_product_btn.setOnClickListener(v -> {
            Intent intent = new Intent(context, UpdateProductActivity.class);
            intent.putExtra("product_id", String.valueOf(p.getProduct_id()));
            context.startActivity(intent);
        });
        return convertView;
    }

    static class ProductViewHolder {
        TextView product_name_tv, import_price_tv, sale_price_tv, product_quantity_tv;
        ImageView product_img_of_lv;
        Button update_product_btn;
    }
}
