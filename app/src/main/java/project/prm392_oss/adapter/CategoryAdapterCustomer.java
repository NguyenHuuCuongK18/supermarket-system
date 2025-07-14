package project.prm392_oss.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import project.prm392_oss.R;
import project.prm392_oss.entity.Category;

public class CategoryAdapterCustomer extends RecyclerView.Adapter<CategoryAdapterCustomer.ViewHolder> {

    private List<Category> categoryList = new ArrayList<>();

    // Cập nhật dữ liệu cho Adapter
    public void setCategories(List<Category> categories) {
        this.categoryList = categories;
        notifyDataSetChanged(); // Thông báo cho Adapter là dữ liệu đã thay đổi
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category_customer, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Category category = categoryList.get(position);
        holder.tvCategoryName.setText(category.getName());
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCategoryName;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCategoryName = itemView.findViewById(R.id.tvCategoryName);
        }
    }
}
