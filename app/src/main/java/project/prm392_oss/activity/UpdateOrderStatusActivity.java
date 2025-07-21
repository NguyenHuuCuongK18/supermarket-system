package project.prm392_oss.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import project.prm392_oss.activity.BaseActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import project.prm392_oss.R;
import project.prm392_oss.adapter.EmployeeOrderDetailsAdapter;
import project.prm392_oss.entity.Order;
import project.prm392_oss.entity.OrderDetails;
import project.prm392_oss.entity.User;
import project.prm392_oss.viewModel.OrderDetailsViewModel;
import project.prm392_oss.viewModel.OrderViewModel;
import project.prm392_oss.viewModel.UserViewModel;

public class UpdateOrderStatusActivity extends BaseActivity {
    private TextView orderstatus_customer_name_tv, orderstatus_orderdate_tv,
            orderstatus_customer_email_tv, orderstatus_customer_phone_tv,
            orderstatus_customer_address_tv, order_total_amount_tv;
    private RecyclerView order_details_rv;
    private OrderDetailsViewModel orderDetailsViewModel;
    private OrderViewModel orderViewModel;
    private Spinner order_status_sp;
    private Button update_order_status_btn;
    private EmployeeOrderDetailsAdapter adapter;
    private List<OrderDetails> list = new ArrayList<>();
    private String selectedItem, orderStatus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.employee_activity_update_order_status);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Order Details");

        orderstatus_customer_name_tv = findViewById(R.id.orderstatus_customer_name_tv);
        orderstatus_orderdate_tv = findViewById(R.id.orderstatus_orderdate_tv);
        order_total_amount_tv = findViewById(R.id.order_total_amount_tv);
        orderstatus_customer_phone_tv = findViewById(R.id.orderstatus_customer_phone_tv);
        orderstatus_customer_email_tv = findViewById(R.id.orderstatus_customer_email_tv);
        orderstatus_customer_address_tv = findViewById(R.id.orderstatus_customer_address_tv);
        order_details_rv = findViewById(R.id.order_details_rv);
        order_status_sp = findViewById(R.id.order_status_sp);
        update_order_status_btn = findViewById(R.id.update_order_status_btn);

        Intent intent = getIntent();
        String orderID = intent.getStringExtra("orderId");

        //get order
        orderViewModel = new ViewModelProvider(this).get(OrderViewModel.class);
        orderViewModel.getOrderById(Integer.parseInt(orderID)).observe(this, new Observer<Order>() {
            @Override
            public void onChanged(Order order) {
                orderstatus_customer_name_tv.setText(order.getCustomer_name());
                orderstatus_customer_email_tv.setText(order.getCustomer_email());
                orderstatus_customer_phone_tv.setText(order.getCustomer_phone());
                orderstatus_customer_address_tv.setText(order.getCustomer_address());
                orderstatus_orderdate_tv.setText(order.getOrder_date());
                order_total_amount_tv.setText(order.getTotal_amount() + "$");

                orderStatus = order.getStatus();

                //get order details list
                orderDetailsViewModel = new ViewModelProvider(UpdateOrderStatusActivity.this).get(OrderDetailsViewModel.class);
                orderDetailsViewModel.getOrderDetailsByOrderId(Integer.parseInt(orderID)).observe(UpdateOrderStatusActivity.this, new Observer<List<OrderDetails>>() {
                    @Override
                    public void onChanged(List<OrderDetails> orderDetails) {
                        adapter = new EmployeeOrderDetailsAdapter(UpdateOrderStatusActivity.this, orderDetails);
                        order_details_rv.setAdapter(adapter);
                        order_details_rv.setLayoutManager(new LinearLayoutManager(UpdateOrderStatusActivity.this, LinearLayoutManager.VERTICAL, false));
                    }
                });

                setSpinnerType(orderStatus);
                order_status_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        selectedItem = parent.getItemAtPosition(position).toString();

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                update_order_status_btn.setOnClickListener(v -> {
                    orderViewModel.updateOrderStatus(order.getOrder_id(), selectedItem);
                    Toast.makeText(UpdateOrderStatusActivity.this, "Update Order Status Successfully", Toast.LENGTH_SHORT).show();
                    finish();
                });
            }
        });
    }
    private void setSpinnerType(String status) {
        String[] statusArray;
        switch (status) {
            case "Not delivered yet":
                statusArray = new String[]{"Not delivered yet", "In progress", "Delivered"};
                break;
            case "In progress":
                statusArray = new String[]{"In progress", "Delivered"};
                break;
            case "Delivered":
                statusArray = new String[]{"Delivered"};
                break;
            default:
                statusArray = new String[]{"Not delivered yet", "In progress", "Delivered"};
                break;
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, statusArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        order_status_sp.setAdapter(adapter);

        // Tìm vị trí của trạng thái từ database trong mảng
        int position = -1;
        for (int i = 0; i < statusArray.length; i++) {
            if (statusArray[i].equals(status)) {
                position = i;
                break;
            }
        }

        // Set vị trí được chọn cho spinner
        if (position != -1) {
            order_status_sp.setSelection(position);
        }
    }
}