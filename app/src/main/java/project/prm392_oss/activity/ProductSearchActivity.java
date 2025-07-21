package project.prm392_oss.activity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.SearchView;

import project.prm392_oss.activity.BaseActivity;
import androidx.lifecycle.ViewModelProvider;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import project.prm392_oss.R;
import project.prm392_oss.adapter.UserProductAdapter;
import project.prm392_oss.entity.Product;
import project.prm392_oss.viewModel.ProductViewModel;

public class ProductSearchActivity extends BaseActivity {

    private ProductViewModel viewModel;
    private UserProductAdapter adapter;
    private List<Product> productList = new ArrayList<>();
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_search);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        listView = findViewById(R.id.product_list);
        adapter = new UserProductAdapter(this, productList);
        listView.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(ProductViewModel.class);
        viewModel.getProductList().observe(this, products -> {
            productList.clear();
            productList.addAll(products);
            adapter.notifyDataSetChanged();
        });

        handleSearch();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void handleSearch() {
        SearchView searchView = findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                viewModel.searchProducts(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                viewModel.searchProducts(newText);
                return true;
            }
        });
    }
}