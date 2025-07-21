package project.prm392_oss.activity;

import static project.prm392_oss.R.*;

import android.annotation.SuppressLint;
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

public class UpdateProductActivity extends BaseActivity {
    private EditText product_name_edt, product_quantity_edt, import_price_edt, sale_price_edt, image_URL_edt, product_description_tml;
    private TextView category_of_product_sp, supplier_of_product_sp;
    private Button browse_image_btn, update_product_info_btn;
    private ProductViewModel productViewModel;
    private CategoryViewModel categoryViewModel;
    private SupplierViewModel supplierViewModel;
    private Dialog dialog;
    private Product currentProduct;
    private int selectedCategoryId = 1, selectedSupplierId;
    private static final int PICK_IMAGE_REQUEST = 1;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.employee_activity_update_product);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        product_name_edt = findViewById(R.id.product_name_edt);
        product_quantity_edt = findViewById(R.id.product_quantity_edt);
        import_price_edt = findViewById(R.id.import_price_edt);
        sale_price_edt = findViewById(R.id.sale_price_edt);
        image_URL_edt = findViewById(R.id.image_URL_edt);
        product_description_tml = findViewById(R.id.product_description_tml);
        category_of_product_sp = findViewById(R.id.category_of_product_sp);
        supplier_of_product_sp = findViewById(R.id.supplier_of_product_sp);
        browse_image_btn = findViewById(R.id.browse_image_btn);
        update_product_info_btn = findViewById(R.id.update_product_info_btn);

        //get Product need to update
        Intent intent = getIntent();
        String product_id = intent.getStringExtra("product_id");
        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);
        productViewModel.getProductById(Integer.parseInt(product_id)).observe(this, new Observer<Product>() {
            @Override
            public void onChanged(Product p) {
                currentProduct = p;
                product_name_edt.setText(p.getName());
                product_quantity_edt.setText(String.valueOf(p.getStock_quantity()));
                import_price_edt.setText(String.valueOf(p.getImport_price()));
                sale_price_edt.setText(String.valueOf(p.getSale_price()));
                image_URL_edt.setText(p.getImageUrl());
                product_description_tml.setText(p.getDescription());
                //set categoryID and supplierID
                selectedCategoryId = p.getCategory_id();
                selectedSupplierId = p.getSupplier_id();
                //set spinner
                categoryViewModel = new ViewModelProvider(UpdateProductActivity.this).get(CategoryViewModel.class);
                supplierViewModel = new ViewModelProvider(UpdateProductActivity.this).get(SupplierViewModel.class);
                loadSpinners();
                //set image
                browse_image_btn.setOnClickListener(v -> {
                    openImageChooser();
                });

                //update product
                update_product_info_btn.setOnClickListener(v -> {
                    if (validate()) {
                        String name = product_name_edt.getText().toString();
                        int quantity = Integer.parseInt(product_quantity_edt.getText().toString());
                        int importPrice = Integer.parseInt(import_price_edt.getText().toString());
                        int salePrice = Integer.parseInt(sale_price_edt.getText().toString());
                        String image = image_URL_edt.getText().toString();
                        String description = product_description_tml.getText().toString();
                        try{
                            Product updatedProduct = new Product(p.getProduct_id(), name, description, importPrice, salePrice, quantity, selectedCategoryId, selectedSupplierId, image);
                            productViewModel.update(updatedProduct);
                            Intent intent = new Intent(UpdateProductActivity.this, ListProductActivity.class);
                            intent.putExtra("category_id", String.valueOf(p.getCategory_id()));
                            startActivity(intent);
                            Toast.makeText(UpdateProductActivity.this, "Update product successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
            }
        });
    }
    private boolean validate() {
        String name = product_name_edt.getText().toString();
        String quantity = product_quantity_edt.getText().toString();
        String importPrice =import_price_edt.getText().toString();
        String salePrice =sale_price_edt.getText().toString();
        String image = image_URL_edt.getText().toString();
        String description = product_description_tml.getText().toString();
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
            image_URL_edt.setText(imageUri.toString());
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
                int currentCategoryId = product.getCategory_id();
                Category currentCategory = null;
                for (Category category : list) {
                    if (category.getCategory_id() == currentCategoryId) {
                        currentCategory = category;
                        break;
                    }
                }

                if (currentCategory != null) {
                    category_of_product_sp.setText(currentCategory.getName());
                }
                EmployeeCategorySpinnerAdapter adapter = new EmployeeCategorySpinnerAdapter(list, UpdateProductActivity.this);

                category_of_product_sp.setOnClickListener(v -> {
                    dialog = new Dialog(UpdateProductActivity.this);
                    dialog.setContentView(R.layout.employee_dialog_searchable_spinner);
                    dialog.getWindow().setLayout(1000, 2000);

                    list.clear();
                    list.addAll(new ArrayList<>(adapter.getOriginalCategoryList()));

                    dialog.show();

                    EditText search_edt = dialog.findViewById(R.id.search_edt);
                    ListView category_lv = dialog.findViewById(id.spinner_item_lv);

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
                            category_of_product_sp.setText(category.getName());
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
                int currentSupplierId = product.getSupplier_id();
                Supplier currentSupplier = null;
                for (Supplier supplier : list) {
                    if (supplier.getSupplier_id() == currentSupplierId) {
                        currentSupplier = supplier;
                        break;
                    }
                }
                if (currentSupplier != null) {
                    supplier_of_product_sp.setText(currentSupplier.getName());
                }
                EmployeeSupplierSpinnerAdapter adapter = new EmployeeSupplierSpinnerAdapter(list, UpdateProductActivity.this);

                supplier_of_product_sp.setOnClickListener(v -> {
                    dialog = new Dialog(UpdateProductActivity.this);
                    dialog.setContentView(R.layout.employee_dialog_searchable_spinner);
                    dialog.getWindow().setLayout(1000, 2000);

                    list.clear();
                    list.addAll(new ArrayList<>(adapter.originalSupplierList()));

                    dialog.show();

                    EditText search_edt = dialog.findViewById(R.id.search_edt);
                    ListView supplier_lv = dialog.findViewById(id.spinner_item_lv);

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
                            supplier_of_product_sp.setText(s.getName());
                            selectedSupplierId = s.getSupplier_id();
                            dialog.dismiss();
                        }
                    });
                });
            }
        });
    }

}