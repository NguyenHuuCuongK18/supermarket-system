package project.prm392_oss.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import project.prm392_oss.activity.BaseActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.util.regex.Pattern;

import project.prm392_oss.R;
import project.prm392_oss.entity.Supplier;
import project.prm392_oss.viewModel.SupplierViewModel;

public class UpdateSupplierActivity extends BaseActivity {
    private EditText supplier_name_edt, supplier_phone_edt, supplier_address_edt;
    private Button supplier_update_btn;
    private SupplierViewModel supplierViewModel;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.employee_activity_update_supplier);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Update Supplier");

        supplier_name_edt = findViewById(R.id.supplier_name_edt);
        supplier_phone_edt = findViewById(R.id.supplier_phone_edt);
        supplier_address_edt = findViewById(R.id.supplier_address_edt);
        supplier_update_btn = findViewById(R.id.supplier_update_btn);


        Intent intent = getIntent();
        String supplierId = intent.getStringExtra("supplierId");
        supplierViewModel = new ViewModelProvider(this).get(SupplierViewModel.class);
        supplierViewModel.getSupplierById(Integer.parseInt(supplierId)).observe(this, new Observer<Supplier>() {
            @Override
            public void onChanged(Supplier s) {
//                currentProduct = p;
                supplier_name_edt.setText(s.getName());
                supplier_phone_edt.setText(s.getPhone());
                supplier_address_edt.setText(s.getAddress());
                //update product
                supplier_update_btn.setOnClickListener(v -> {
                    if (validate()) {
                        String name = supplier_name_edt.getText().toString();
                        String phone = supplier_phone_edt.getText().toString();
                        String address = supplier_address_edt.getText().toString();
                        supplierViewModel.update(new Supplier(name, phone, address));
                        Toast.makeText(UpdateSupplierActivity.this, "Update successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }
        });
    }
    private boolean validate() {
        String name = supplier_name_edt.getText().toString();
        String phone = supplier_phone_edt.getText().toString();
        String address = supplier_address_edt.getText().toString();
        if (name.isEmpty()) {
            Toast.makeText(this, "Name is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!Pattern.matches("^0[0-9]{9}$", phone)) {
            Toast.makeText(this, "Phone number is invalid", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (address.isEmpty()) {
            Toast.makeText(this, "Address is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}