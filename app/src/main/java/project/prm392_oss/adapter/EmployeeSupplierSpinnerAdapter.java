package project.prm392_oss.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import project.prm392_oss.entity.Supplier;

public class EmployeeSupplierSpinnerAdapter extends BaseAdapter {
    private List<Supplier> list;
    private Context context;
    private ArrayList<Supplier> originalSupplierList;

    public EmployeeSupplierSpinnerAdapter(List<Supplier> list, Context context) {
        this.list = list;
        this.context = context;
        this.originalSupplierList = new ArrayList<>(list);
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
        return list.get(position).getSupplier_id();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_2, parent, false);
        }
        Supplier c = list.get(position);
        TextView name = convertView.findViewById(android.R.id.text1);
        name.setText(c.getName());
        return convertView;
    }
    public ArrayList<Supplier> originalSupplierList() {
        return originalSupplierList;
    }

    public void filter(String searchText) {
        searchText = searchText.toLowerCase();
        list.clear();

        if (searchText.isEmpty()) {
            list.addAll(originalSupplierList);
        } else {
            for (Supplier supplier : originalSupplierList) {
                if (supplier.getName().toLowerCase().contains(searchText)) {
                    list.add(supplier);
                }
            }
        }
        notifyDataSetChanged();
    }
}
