package project.prm392_oss.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import project.prm392_oss.entity.Category;

public class EmployeeCategorySpinnerAdapter extends BaseAdapter {
    private List<Category> list;
    private Context context;
    private ArrayList<Category> originalCategoryList;

    public EmployeeCategorySpinnerAdapter(List<Category> list, Context context) {
        this.list = list;
        this.context = context;
        this.originalCategoryList = new ArrayList<>(list);
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
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_2, parent, false);
        }
        Category c = list.get(position);
        TextView name = convertView.findViewById(android.R.id.text1);
        name.setText(c.getName());
        return convertView;
    }
    public ArrayList<Category> getOriginalCategoryList() {
        return originalCategoryList;
    }

    public void filter(String searchText) {
        searchText = searchText.toLowerCase();
        list.clear();

        if (searchText.isEmpty()) {
            list.addAll(originalCategoryList);
        } else {
            for (Category category : originalCategoryList) {
                if (category.getName().toLowerCase().contains(searchText)) {
                    list.add(category);
                }
            }
        }
        notifyDataSetChanged();
    }
}
