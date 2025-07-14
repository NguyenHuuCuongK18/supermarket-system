package project.prm392_oss.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import project.prm392_oss.R;
import project.prm392_oss.activity.UpdateSupplierActivity;
import project.prm392_oss.entity.Supplier;

public class EmployeeSupplierListAdapter extends RecyclerView.Adapter<EmployeeSupplierListAdapter.SupplierViewHolder> implements Filterable {
    private Context context;
    private List<Supplier> list;
    private List<Supplier> filteredSupplierList;
    public EmployeeSupplierListAdapter(Context context, List<Supplier> list) {
        this.context = context;
        this.list = list;
        this.filteredSupplierList = list;
    }
    @NonNull
    @Override
    public SupplierViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SupplierViewHolder(LayoutInflater.from(context).inflate(R.layout.employee_supplier_item, parent, false));
    }
    @Override
    public void onBindViewHolder(@NonNull SupplierViewHolder holder, int position) {
        Supplier s = filteredSupplierList.get(position);
        holder.supplier_name_tv.setText(s.getName());
        holder.supplier_phone_tv.setText("Phone: "+s.getPhone());
        holder.supplier_address_tv.setText("Address: "+s.getAddress());
        holder.supplier_update_btn.setOnClickListener(v -> {
            Intent intent = new Intent(context, UpdateSupplierActivity.class);
            intent.putExtra("supplierId", String.valueOf(s.getSupplier_id()));
            context.startActivity(intent);
        });
    }
    @Override
    public int getItemCount() {
        return filteredSupplierList.size();
    }
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charSequenceString = constraint.toString();
                if (charSequenceString.isEmpty()) {
                    filteredSupplierList = list;
                } else {
                    List<Supplier> filteredList = new ArrayList<>();
                    for (Supplier s : list) {
                        if (s.getName().toLowerCase().contains(charSequenceString.toLowerCase())) {
                            filteredList.add(s);
                        }
                        filteredSupplierList = filteredList;
                    }

                }
                FilterResults results = new FilterResults();
                results.values = filteredSupplierList;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredSupplierList = (List<Supplier>) results.values;
                notifyDataSetChanged();
            }
        };
    }
    class SupplierViewHolder extends RecyclerView.ViewHolder{
        TextView supplier_name_tv, supplier_phone_tv, supplier_address_tv;
        Button supplier_update_btn;
        public SupplierViewHolder(@NonNull View itemView) {
            super(itemView);
            supplier_name_tv = itemView.findViewById(R.id.supplier_name_tv);
            supplier_phone_tv = itemView.findViewById(R.id.supplier_phone_tv);
            supplier_address_tv = itemView.findViewById(R.id.supplier_address_tv);
            supplier_update_btn = itemView.findViewById(R.id.supplier_update_btn);
        }
    }
}