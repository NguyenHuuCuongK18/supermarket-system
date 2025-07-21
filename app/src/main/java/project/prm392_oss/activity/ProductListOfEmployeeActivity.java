package project.prm392_oss.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import project.prm392_oss.activity.BaseActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

import project.prm392_oss.R;
import project.prm392_oss.adapter.EmployeeProductOfEmployeeAdapter;
import project.prm392_oss.entity.Product;
import project.prm392_oss.viewModel.CategoryViewModel;
import project.prm392_oss.viewModel.ProductViewModel;

public class ProductListOfEmployeeActivity extends BaseActivity {
    private TextView category_of_product_tv;
    private CategoryViewModel categoryViewModel;
    private ListView list_products_lv;
    private EmployeeProductOfEmployeeAdapter adapter;
    private ProductViewModel productViewModel;
    private List<Product> list = new ArrayList<>();
    private static final int UPDATE_PRODUCT_REQUEST = 1;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.employee_activity_product_list_of_employee);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        category_of_product_tv = findViewById(R.id.category_of_product_tv);
        list_products_lv = findViewById(R.id.list_products_lv);

        //lay ra productname
        Intent intent = getIntent();
        String categoryId = intent.getStringExtra("category_id");

        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);

        adapter = new EmployeeProductOfEmployeeAdapter(list, this);
        list_products_lv.setAdapter(adapter);

        categoryViewModel.getCategoryById(Integer.parseInt(categoryId)).observe(this, category -> {
            if (category != null) {
                category_of_product_tv.setText(category.getName());
                Log.d("Category", "Category found: " + category.getName());
                //lít products
                productViewModel.getProductsByCategory(category.getCategory_id()).observe(this, new Observer<List<Product>>() {
                    @Override
                    public void onChanged(List<Product> products) {
                        if(products != null) {
                            list.clear();
                            list.addAll(products);
                            adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(ProductListOfEmployeeActivity.this, "There have no products of " + category.getName(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else {
                Log.d("Category", "Category not found");
            }
        });


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == UPDATE_PRODUCT_REQUEST && resultCode == RESULT_OK) {
            if (data != null) {
                String categoryId = data.getStringExtra("category_id");
                if (categoryId != null && !categoryId.isEmpty()) {
                    try {
                        int categoryIdInt = Integer.parseInt(categoryId);

                        // Sử dụng categoryIdInt để cập nhật dữ liệu trong ProductListOfEmployeeActivity
                        categoryViewModel.getCategoryById(categoryIdInt).observe(this, category -> {
                            if (category != null) {
                                category_of_product_tv.setText(category.getName());
                                Log.d("Category", "Category found: " + category.getName());
                                productViewModel.getProductsByCategory(category.getCategory_id()).observe(this, new Observer<List<Product>>() {
                                    @Override
                                    public void onChanged(List<Product> products) {
                                        if(products != null) {
                                            list.clear();
                                            list.addAll(products);
                                            adapter.notifyDataSetChanged();
                                        } else {
                                            Toast.makeText(ProductListOfEmployeeActivity.this, "There have no products of " + category.getName(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            } else {
                                Log.d("Category", "Category not found");
                            }
                        });
                    } catch (NumberFormatException e) {
                        Log.e("ProductListOfEmployeeActivity", "Invalid category ID returned", e);
                    }
                }
            }
        }
    }
}