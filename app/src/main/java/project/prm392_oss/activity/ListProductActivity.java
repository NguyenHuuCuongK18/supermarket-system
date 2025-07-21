package project.prm392_oss.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;

import androidx.appcompat.app.AppCompatActivity;
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


import java.util.List;

public class ListProductActivity extends AppCompatActivity {

    private ProductViewModel productViewModel;
    private CategoryViewModel categoryViewModel;
    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private Spinner spFilterCategory;
    private final java.util.List<Product> allProducts = new java.util.ArrayList<>();
    private java.util.List<Category> categoryOptions = new java.util.ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        recyclerView = findViewById(R.id.recyclerView);
        spFilterCategory = findViewById(R.id.spFilterCategory);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);

        categoryViewModel.getAllCategories().observe(this, categories -> {
            categoryOptions.clear();
            categoryOptions.add(new Category(-1, "All Categories"));
            if (categories != null) categoryOptions.addAll(categories);
            ArrayAdapter<Category> adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_item, categoryOptions);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spFilterCategory.setAdapter(adapter);
        });

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
            public void onNothingSelected(AdapterView<?> parent) { }
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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.nav_user_management) {
            startActivity(new Intent(ListProductActivity.this, ListUsersActivity.class));
            return true;
        } else if (item.getItemId() == R.id.nav_product_management) {
            return true;
        }
        if (item.getItemId() == R.id.view_profile) {
            Intent intent = new Intent(ListProductActivity.this, EditProfileActivity.class);
            startActivity(intent);
            return true;
        }
        if (item.getItemId() == R.id.logout) {
            SharedPreferences prefs = getSharedPreferences("USER_PREFS", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.remove("USER_ID");
            editor.apply();

            Intent intent = new Intent(ListProductActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            Toast.makeText(ListProductActivity.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
