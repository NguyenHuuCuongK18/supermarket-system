package project.prm392_oss.activity;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import project.prm392_oss.activity.BaseActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

import project.prm392_oss.R;
import project.prm392_oss.adapter.EmployeeCategorySpinnerAdapter;
import project.prm392_oss.adapter.EmployeeSupplierSpinnerAdapter;
import project.prm392_oss.entity.Category;
import project.prm392_oss.entity.Product;
import project.prm392_oss.entity.Supplier;
import project.prm392_oss.viewModel.CategoryViewModel;
import project.prm392_oss.viewModel.ProductViewModel;
import project.prm392_oss.viewModel.SupplierViewModel;
import project.prm392_oss.activity.ListProductActivity;

public class AddProductActivity extends BaseActivity {
    private EditText add_product_name_edt, add_product_quantity_edt, add_import_price_edt, add_sale_price_edt, add_product_description_tml, add_image_URL_edt;
    private TextView add_category_of_product_sp, add_supplier_of_product_sp;
    private Button add_product_btn, add_browse_image_btn;
    private ProductViewModel productViewModel;
    private CategoryViewModel categoryViewModel;
    private SupplierViewModel supplierViewModel;
    private Dialog dialog;
    private Product currentProduct;
    private int selectedCategoryId, selectedSupplierId;
    private static final int PICK_IMAGE_REQUEST = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.employee_activity_add_product);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add Product");

        add_product_name_edt = findViewById(R.id.add_product_name_edt);
        add_product_quantity_edt = findViewById(R.id.add_product_quantity_edt);
        add_import_price_edt = findViewById(R.id.add_import_price_edt);
        add_sale_price_edt = findViewById(R.id.add_sale_price_edt);
        add_product_description_tml = findViewById(R.id.add_product_description_tml);
        add_image_URL_edt = findViewById(R.id.add_image_URL_edt);
        add_category_of_product_sp = findViewById(R.id.add_category_of_product_sp);
        add_supplier_of_product_sp = findViewById(R.id.add_supplier_of_product_sp);
        add_product_btn = findViewById(R.id.add_product_btn);
        add_browse_image_btn = findViewById(R.id.add_browse_image_btn);

        productViewModel = new ProductViewModel(this.getApplication());
        categoryViewModel = new ViewModelProvider(AddProductActivity.this).get(CategoryViewModel.class);
        supplierViewModel = new ViewModelProvider(AddProductActivity.this).get(SupplierViewModel.class);
        loadSpinners();
        add_browse_image_btn.setOnClickListener(v -> {
            openImageChooser();
        });

        add_product_btn.setOnClickListener(v -> {
            if(validate()) {
                String name = add_product_name_edt.getText().toString();
                int quantity = Integer.parseInt(add_product_quantity_edt.getText().toString());
                int importPrice = Integer.parseInt(add_import_price_edt.getText().toString());
                int salePrice = Integer.parseInt(add_sale_price_edt.getText().toString());
                String image = add_image_URL_edt.getText().toString();
                String description = add_product_description_tml.getText().toString();
                try{
                    Product addProduct = new Product(name, description, importPrice, salePrice, quantity, selectedCategoryId, selectedSupplierId, image);
                    productViewModel.insert(addProduct);

                    productViewModel.getNewestProduct().observe(this, new Observer<Product>() {
                        @Override
                        public void onChanged(Product product) {
                            currentProduct = product;
                            Intent intent = new Intent(AddProductActivity.this, ListProductActivity.class);
                            intent.putExtra("category_id", String.valueOf(product.getCategory_id()));
                            startActivity(intent);
                            Toast.makeText(AddProductActivity.this, "Add product successfully", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
    private boolean validate() {
        String name = add_product_name_edt.getText().toString();
        String quantity = add_product_quantity_edt.getText().toString();
        String importPrice = add_import_price_edt.getText().toString();
        String salePrice = add_sale_price_edt.getText().toString();
        String image = add_image_URL_edt.getText().toString();
        String description = add_product_description_tml.getText().toString();
        if (name.isEmpty()) {
            Toast.makeText(this, "Please enter product name", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (validateInteger(quantity)) {
            Toast.makeText(this, "Please enter valid quantity", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (validateInteger(importPrice)) {
            Toast.makeText(this, "Please enter valid import price", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (validateInteger(salePrice)) {
            Toast.makeText(this, "Please enter valid sale price", Toast.LENGTH_SHORT).show();
        }
        if (selectedCategoryId == 0) {
            Toast.makeText(this, "Please select a category", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (selectedSupplierId == 0) {
            Toast.makeText(this, "Please select a supplier", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (image.isEmpty()) {
            Toast.makeText(this, "Please browse image URL", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (description.isEmpty()) {
            Toast.makeText(this, "Please enter product description", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    private boolean validateInteger(String number) {
        try {
            int a = Integer.parseInt(number);
            return a<0;
        } catch (NumberFormatException e) {
            return true;
        }
    }
    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            add_image_URL_edt.setText(imageUri.toString());
        }
    }
    private void loadSpinners() {
        categorySearchSpinner(currentProduct);
        supplierSearchSpinner(currentProduct);
    }

    private void categorySearchSpinner(Product product) {
        categoryViewModel.getAllCategories().observe(this, new Observer<List<Category>>() {
            @Override
            public void onChanged(List<Category> list) {
                EmployeeCategorySpinnerAdapter adapter = new EmployeeCategorySpinnerAdapter(list, AddProductActivity.this);

                add_category_of_product_sp.setOnClickListener(v -> {
                    dialog = new Dialog(AddProductActivity.this);
                    dialog.setContentView(R.layout.employee_dialog_searchable_spinner);
                    dialog.getWindow().setLayout(1000, 2000);

                    list.clear();
                    list.addAll(new ArrayList<>(adapter.getOriginalCategoryList()));

                    dialog.show();

                    EditText search_edt = dialog.findViewById(R.id.search_edt);
                    ListView category_lv = dialog.findViewById(R.id.spinner_item_lv);

                    // set adapter
                    category_lv.setAdapter(adapter);
                    search_edt.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            adapter.filter(s.toString());
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                        }
                    });

                    category_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Category category = (Category) adapter.getItem(position);
                            add_category_of_product_sp.setText(category.getName());
                            selectedCategoryId = category.getCategory_id();
                            dialog.dismiss();
                        }
                    });
                });
            }
        });
    }
    private void supplierSearchSpinner(Product product) {
        supplierViewModel.getAllSuppliers().observe(this, new Observer<List<Supplier>>() {
            @Override
            public void onChanged(List<Supplier> list) {
                EmployeeSupplierSpinnerAdapter adapter = new EmployeeSupplierSpinnerAdapter(list, AddProductActivity.this);

                add_supplier_of_product_sp.setOnClickListener(v -> {
                    dialog = new Dialog(AddProductActivity.this);
                    dialog.setContentView(R.layout.employee_dialog_searchable_spinner);
                    dialog.getWindow().setLayout(1000, 2000);

                    list.clear();
                    list.addAll(new ArrayList<>(adapter.originalSupplierList()));

                    dialog.show();

                    EditText search_edt = dialog.findViewById(R.id.search_edt);
                    ListView supplier_lv = dialog.findViewById(R.id.spinner_item_lv);

                    // set adapter
                    supplier_lv.setAdapter(adapter);
                    search_edt.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            adapter.filter(s.toString());
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                        }
                    });

                    supplier_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Supplier s = (Supplier) adapter.getItem(position);
                            add_supplier_of_product_sp.setText(s.getName());
                            selectedSupplierId = s.getSupplier_id();
                            dialog.dismiss();
                        }
                    });
                });
            }
        });
    }
}