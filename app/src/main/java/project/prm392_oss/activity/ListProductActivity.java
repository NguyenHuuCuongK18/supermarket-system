package project.prm392_oss.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import project.prm392_oss.R;
import project.prm392_oss.entity.Product;
import project.prm392_oss.viewModel.ProductViewModel;
import project.prm392_oss.adapter.ProductAdapter;

import java.util.List;

public class ListProductActivity extends AppCompatActivity {

    private ProductViewModel productViewModel;
    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);
        productViewModel.getAllProducts().observe(this, products -> {
            productAdapter = new ProductAdapter(products);
            recyclerView.setAdapter(productAdapter);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.nav_employee_management) {
            startActivity(new Intent(ListProductActivity.this, ListEmployeeActivity.class));
            return true;
        } else if (item.getItemId() == R.id.nav_customer_management) {
            startActivity(new Intent(ListProductActivity.this, ListCustomerActivity.class));
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
