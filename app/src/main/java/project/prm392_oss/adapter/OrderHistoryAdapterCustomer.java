package project.prm392_oss.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.widget.ArrayAdapter;

import java.util.List;

import project.prm392_oss.R;
import project.prm392_oss.entity.Order;

public class OrderHistoryAdapterCustomer extends ArrayAdapter<Order> {

    private Context context;
    private List<Order> orders;

    public OrderHistoryAdapterCustomer(@NonNull Context context, List<Order> orders) {
        super(context, R.layout.item_order_customer, orders);
        this.context = context;
        this.orders = orders;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_order_customer, parent, false);
        }

        Order order = orders.get(position);

        TextView tvOrderDate = convertView.findViewById(R.id.tv_order_date);
        TextView tvTotalAmount = convertView.findViewById(R.id.tv_total_amount);
        TextView tvStatus = convertView.findViewById(R.id.tv_status);

        tvOrderDate.setText("Ngày đặt: " + order.getOrder_date());
        tvTotalAmount.setText("Tổng tiền: " + order.getTotal_amount() + " $");
        tvStatus.setText("Trạng thái: " + order.getStatus());

        return convertView;
    }
}
