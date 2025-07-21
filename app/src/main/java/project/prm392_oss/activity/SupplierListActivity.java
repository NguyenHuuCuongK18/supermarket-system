package project.prm392_oss.activity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.SearchView;
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
import project.prm392_oss.adapter.EmployeeSupplierListAdapter;
import project.prm392_oss.entity.Supplier;
import project.prm392_oss.utils.manager.SessionManager;
import project.prm392_oss.viewModel.SupplierViewModel;

public class SupplierListActivity extends BaseActivity {
    private RecyclerView supplier_list_rv;
    private Button add_supplier_btn;
    private List<Supplier> list = new ArrayList<>();
    private SupplierViewModel supplierViewModel;
    private EmployeeSupplierListAdapter adapter;
    private SearchView search_supplier_sv;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.employee_activity_supplier_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Supplier List");

        supplier_list_rv = findViewById(R.id.supplier_list_rv);
        add_supplier_btn = findViewById(R.id.add_supplier_btn);
        search_supplier_sv = findViewById(R.id.search_supplier_sv);


        //display list suppliers
        supplierViewModel = new ViewModelProvider(this).get(SupplierViewModel.class);
        adapter = new EmployeeSupplierListAdapter(this, list);
        supplier_list_rv.setAdapter(adapter);
        supplier_list_rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        supplierViewModel.getAllSuppliers().observe(this, new Observer<List<Supplier>>() {
            @Override
            public void onChanged(List<Supplier> suppliers) {
                if (suppliers != null) {
                    list.clear();
                    list.addAll(suppliers);
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(SupplierListActivity.this, "There have no suppliers", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //add supplier
        add_supplier_btn.setOnClickListener(v -> {
            Intent intent = new Intent(SupplierListActivity.this, AddSupplierActivity.class);
            startActivity(intent);
        });

        search_supplier_sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            SessionManager.logout(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}