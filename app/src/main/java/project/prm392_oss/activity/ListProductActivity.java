package project.prm392_oss.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.Button;

import project.prm392_oss.activity.BaseActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import project.prm392_oss.R;
import project.prm392_oss.entity.Product;
import project.prm392_oss.entity.Category;
import project.prm392_oss.viewModel.ProductViewModel;
import project.prm392_oss.viewModel.CategoryViewModel;
import project.prm392_oss.adapter.ProductAdapter;
import project.prm392_oss.activity.ListUsersActivity;
import project.prm392_oss.activity.AddProductActivity;
import project.prm392_oss.utils.manager.SessionManager;

import java.util.List;

public class ListProductActivity extends BaseActivity {

    private ProductViewModel productViewModel;
    private CategoryViewModel categoryViewModel;
    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private Spinner spFilterCategory;
    private final java.util.List<Product> allProducts = new java.util.ArrayList<>();
    private java.util.List<Category> categoryOptions = new java.util.ArrayList<>();
    private Button btnAddProduct;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        recyclerView = findViewById(R.id.recyclerView);
        spFilterCategory = findViewById(R.id.spFilterCategory);
        btnAddProduct = findViewById(R.id.btnAddProduct);
        btnAddProduct.setOnClickListener(v ->
                startActivity(new Intent(ListProductActivity.this, AddProductActivity.class)));

        String role = getSharedPreferences("USER_PREFS", MODE_PRIVATE)
                .getString("USER_ROLE", "");
        if (!"Employee".equals(role)) {
            btnAddProduct.setVisibility(View.GONE);
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        productViewModel.getAllProducts().observe(this, products -> {
            allProducts.clear();
            if (products != null) allProducts.addAll(products);
            filterProducts();
        });

        spFilterCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, android.view.View view, int position, long id) {
                filterProducts();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void filterProducts() {
        if (productAdapter == null) {
            productAdapter = new ProductAdapter(this, new java.util.ArrayList<>());
            recyclerView.setAdapter(productAdapter);
        }

        Category selected = (Category) spFilterCategory.getSelectedItem();
        int selectedId = selected != null ? selected.getCategory_id() : -1;
        java.util.List<Product> filtered = new java.util.ArrayList<>();
        for (Product p : allProducts) {
            if (selectedId == -1 || p.getCategory_id() == selectedId) {
                filtered.add(p);
            }
        }
        productAdapter.updateData(filtered);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.nav_user_management) {
            startActivity(new Intent(ListProductActivity.this, ListUsersActivity.class));
            return true;
        } else if (item.getItemId() == R.id.nav_product_management) {
            return true;
        } else if (item.getItemId() == R.id.nav_supplier_management) {
            startActivity(new Intent(ListProductActivity.this, SupplierListActivity.class));
            return true;
        } else if (item.getItemId() == R.id.nav_order_management) {
            startActivity(new Intent(ListProductActivity.this, ListOrdersActivity.class));
            return true;
        } else if (item.getItemId() == R.id.view_profile) {
            Intent intent = new Intent(ListProductActivity.this, EditProfileActivity.class);
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.logout) {
            SessionManager.logout(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}