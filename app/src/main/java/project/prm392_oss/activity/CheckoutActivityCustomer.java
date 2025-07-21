package project.prm392_oss.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.view.MenuItem;


import project.prm392_oss.activity.BaseActivity;

import project.prm392_oss.R;
import project.prm392_oss.activity.WelcomeActivityCustomer;

public class CheckoutActivityCustomer extends BaseActivity {

    private TextView tvTotalAmount, tvShippingFee, tvFinalAmount;
    private Button btnCheckout;

    private double totalAmount;
    private final double SHIPPING_FEE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout_customer);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        tvShippingFee = findViewById(R.id.tvShippingFee);
        tvFinalAmount = findViewById(R.id.tvFinalAmount);
        btnCheckout = findViewById(R.id.btnCheckout);


        totalAmount = getIntent().getDoubleExtra("totalAmount", 0);
        updateAmount();
        btnCheckout.setOnClickListener(v -> handleCheckout());
    }

    private void updateAmount() {
        tvTotalAmount.setText(String.format("Tổng tiền: %,d $", (int) totalAmount));
        tvShippingFee.setText(String.format("Phí vận chuyển: %,d $", (int) SHIPPING_FEE));
        double finalAmount = totalAmount + SHIPPING_FEE;
        tvFinalAmount.setText(String.format("Thành tiền: %,d $", (int) finalAmount));
    }

    private void handleCheckout() {
        Toast.makeText(this, "Thanh toán thành công!", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, WelcomeActivityCustomer.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

