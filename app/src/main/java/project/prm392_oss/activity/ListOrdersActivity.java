package project.prm392_oss.activity;

import android.os.Bundle;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import project.prm392_oss.R;
import project.prm392_oss.adapter.EmployeeListOrderAdapter;
import project.prm392_oss.entity.Order;
import project.prm392_oss.viewModel.OrderViewModel;

public class ListOrdersActivity extends AppCompatActivity {
    private SearchView search_order_sv;
    private RecyclerView order_list_rv;
    private List<Order> list = new ArrayList<>();
    private OrderViewModel orderViewModel;
    private EmployeeListOrderAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.employee_activity_list_orders);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Order List");

        search_order_sv = findViewById(R.id.search_order_sv);
        order_list_rv = findViewById(R.id.order_list_rv);

        //display order list
        orderViewModel = new ViewModelProvider(this).get(OrderViewModel.class);
        adapter = new EmployeeListOrderAdapter(this, list);
        order_list_rv.setAdapter(adapter);
        order_list_rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        orderViewModel.getAllOrders().observe(this, new Observer<List<Order>>() {
            @Override
            public void onChanged(List<Order> orders) {
                if (orders != null) {
                    list.clear();
                    list.addAll(orders);
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(ListOrdersActivity.this, "There have no orders", Toast.LENGTH_SHORT).show();
                }
            }
        });
        search_order_sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String queryString) {
                adapter.getFilter().filter(queryString);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String queryString) {
                adapter.getFilter().filter(queryString);
                return false;
            }
        });
    }
}