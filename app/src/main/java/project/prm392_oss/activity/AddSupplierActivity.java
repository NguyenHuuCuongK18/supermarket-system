package project.prm392_oss.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import project.prm392_oss.activity.BaseActivity;
import androidx.lifecycle.ViewModelProvider;

import java.util.regex.Pattern;

import project.prm392_oss.R;
import project.prm392_oss.entity.Supplier;
import project.prm392_oss.viewModel.SupplierViewModel;

public class AddSupplierActivity extends BaseActivity {
    private EditText add_supplier_name_edt, add_supplier_phone_edt, add_supplier_address_edt;
    private Button supplier_add_btn;
    private SupplierViewModel supplierViewModel;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.employee_activity_add_supplier);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add Supplier");

        add_supplier_name_edt = findViewById(R.id.add_supplier_name_edt);
        add_supplier_phone_edt = findViewById(R.id.add_supplier_phone_edt);
        add_supplier_address_edt = findViewById(R.id.add_supplier_address_edt);
        supplier_add_btn = findViewById(R.id.supplier_add_btn);

        supplier_add_btn.setOnClickListener(v -> {
            if (validate()) {
                String name = add_supplier_name_edt.getText().toString();
                String phone = add_supplier_phone_edt.getText().toString();
                String address = add_supplier_address_edt.getText().toString();
                supplierViewModel = new ViewModelProvider(this).get(SupplierViewModel.class);
                supplierViewModel.insert(new Supplier(name, phone, address));
                Toast.makeText(AddSupplierActivity.this, "Add successfully", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
    private boolean validate() {
        String name = add_supplier_name_edt.getText().toString();
        String phone = add_supplier_phone_edt.getText().toString();
        String address = add_supplier_address_edt.getText().toString();
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