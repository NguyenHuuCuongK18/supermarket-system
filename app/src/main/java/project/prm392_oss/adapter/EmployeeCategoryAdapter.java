package project.prm392_oss.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import java.util.List;

import project.prm392_oss.activity.ListProductActivity;
import project.prm392_oss.R;
import project.prm392_oss.entity.Category;

public class EmployeeCategoryAdapter extends BaseAdapter {
    private List<Category> list;
    private Context context;

    public EmployeeCategoryAdapter(List<Category> list, Context context) {
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
        return list.get(position).getCategory_id();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CategoryHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.employee_category_item, parent, false);
            holder = new CategoryHolder();
            holder.category_item_btn = convertView.findViewById(R.id.category_item_btn);
            convertView.setTag(holder);
        } else {
            holder = (CategoryHolder) convertView.getTag();
        }
        Category c = list.get(position);
        holder.category_item_btn.setText(c.getName());

        holder.category_item_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ListProductActivity.class);
                intent.putExtra("category_id", String.valueOf(c.getCategory_id()));
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    static class CategoryHolder {
        Button category_item_btn;
    }
}
