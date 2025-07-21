package project.prm392_oss.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import project.prm392_oss.activity.BaseActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

import project.prm392_oss.R;
import project.prm392_oss.adapter.EmployeeCategoryAdapter;
import project.prm392_oss.entity.Category;
import project.prm392_oss.viewModel.CategoryViewModel;
import project.prm392_oss.utils.manager.SessionManager;

public class ListCategoriesActivity extends BaseActivity {
    private ListView list_categories_lv;
    private List<Category> list = new ArrayList<>();
    private EmployeeCategoryAdapter categoryAdapter;
    private CategoryViewModel categoryViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.employee_activity_list_categories);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Category list");

        list_categories_lv = findViewById(R.id.list_categories_lv);
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);


        categoryAdapter = new EmployeeCategoryAdapter(list, this);
        list_categories_lv.setAdapter(categoryAdapter);

        categoryViewModel.getAllCategories().observe(this, new Observer<List<Category>>() {
            @Override
            public void onChanged(List<Category> categories) {
                if (categories != null) {
                    list.clear();
                    list.addAll(categories);
                    categoryAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(ListCategoriesActivity.this, "There have no categories", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_of_employee, menu);
        return super.onCreateOptionsMenu(menu);
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        int id = item.getItemId();
        if(id == R.id.product_mgt_menu) {
            intent = new Intent(ListCategoriesActivity.this, ListProductActivity.class);
            startActivity(intent);
            return true;
        }
//        if (id ==R.id.supplier_mgt_menu){
//            intent = new Intent(ListCategoriesActivity.this, SupplierListActivity.class);
//            startActivity(intent);
//            return true;
//        }
        if (id == R.id.order_mgt_menu) {
            intent = new Intent(ListCategoriesActivity.this, ListOrdersActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.view_profile) {
            intent = new Intent(ListCategoriesActivity.this, EditProfileActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.logout) {
            SessionManager.logout(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}