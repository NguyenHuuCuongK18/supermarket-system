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
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import project.prm392_oss.R;
import project.prm392_oss.activity.UpdateOrderStatusActivity;
import project.prm392_oss.entity.Order;
import project.prm392_oss.entity.User;
import project.prm392_oss.viewModel.UserViewModel;

public class EmployeeListOrderAdapter extends RecyclerView.Adapter<EmployeeListOrderAdapter.OrderViewHolder> implements Filterable {
    private Context context;
    private List<Order> list;
    private List<Order> filteredOrderList;
    private UserViewModel userViewModel;
    private String searchText = "";

    public EmployeeListOrderAdapter(Context context, List<Order> list) {
        this.context = context;
        this.list = list;
        this.filteredOrderList = list;
        this.userViewModel = new ViewModelProvider((ViewModelStoreOwner) context).get(UserViewModel.class);
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OrderViewHolder(LayoutInflater.from(context).inflate(R.layout.employee_order_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order o = filteredOrderList.get(position);
        userViewModel.getUserById(o.getCustomer_id()).observe((LifecycleOwner) context, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (user != null) {
                    holder.order_customer_name_tv.setText(user.getName());
                    if (!searchText.isEmpty()) {
                        if (!user.getName().toLowerCase().contains(searchText.toLowerCase())) {
                            if (filteredOrderList.contains(o)) {
                                filteredOrderList.remove(o);
                                notifyDataSetChanged();
                            }
                        }
                    }
                }
            }
        });
        holder.order_total_tv.setText("Total: "+ o.getTotal_amount() + " $");
        holder.order_orderdate_tv.setText(o.getOrder_date());
        holder.update_order_btn.setOnClickListener(v -> {
            Intent intent = new Intent(context, UpdateOrderStatusActivity.class);
            intent.putExtra("orderId", String.valueOf(o.getOrder_id()));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return filteredOrderList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                searchText = constraint.toString();
                List<Order> filteredList = new ArrayList<>(list);
                if (searchText.isEmpty()) {
                    filteredList = list;
                }
                FilterResults results = new FilterResults();
                results.values = filteredList;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredOrderList = (List<Order>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView order_customer_name_tv, order_total_tv, order_orderdate_tv;
        Button update_order_btn;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            order_customer_name_tv = itemView.findViewById(R.id.order_customer_name_tv);
            order_total_tv = itemView.findViewById(R.id.order_total_tv);
            order_orderdate_tv = itemView.findViewById(R.id.order_orderdate_tv);
            update_order_btn = itemView.findViewById(R.id.update_order_btn);
        }
    }
}