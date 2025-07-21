package project.prm392_oss.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.view.MenuItem;


import project.prm392_oss.activity.BaseActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import project.prm392_oss.R;
import project.prm392_oss.database.DatabaseClient;
import project.prm392_oss.dto.CartItemDTO;
import project.prm392_oss.entity.CartItem;
import project.prm392_oss.entity.Order;
import project.prm392_oss.entity.Product;
import project.prm392_oss.entity.OrderDetails;


public class CreateOrderActivityCustomer extends BaseActivity {

    private EditText etCustomerName;
    private EditText etPhoneNumber;
    private EditText etEmail;
    private EditText etAddress;
    private TextView tvTotalAmount;
    private Button btnCheckout;
    private TextView tvProductAmount;
    private TextView tvShippingFee;

    private List<CartItemDTO> selectedItems;
    private project.prm392_oss.entity.User currentUser;
    private double totalAmount = 0;
    private final double SHIPPING_FEE = 1; // Phí ship cố định

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_order_customer);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Khởi tạo các thành phần giao diện
        etCustomerName = findViewById(R.id.etCustomerName);
        etEmail = findViewById(R.id.etEmail);
        etEmail.setEnabled(false); // display user's email but do not allow editing
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        etAddress = findViewById(R.id.etAddress);
        tvProductAmount = findViewById(R.id.tvProductAmount);
        tvShippingFee = findViewById(R.id.tvShippingFee);
        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        btnCheckout = findViewById(R.id.btnCheckout);

        //  Kiểm tra dữ liệu từ Intent để tránh `NullPointerException`
        Intent intent = getIntent();
        if (intent != null) {
            selectedItems = (List<CartItemDTO>) intent.getSerializableExtra("selectedItems");
            totalAmount = intent.getDoubleExtra("totalAmount", 0);
        }

        if (selectedItems == null || totalAmount == 0) {
            Toast.makeText(this, "Dữ liệu không hợp lệ!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }


        tvProductAmount.setText(String.format("Giá sản phẩm: %.0f $", totalAmount));


        double shippingFee = SHIPPING_FEE;
        tvShippingFee.setText(String.format("Phí ship: %.0f $", shippingFee));


        double finalTotalAmount = totalAmount + shippingFee;
        tvTotalAmount.setText(String.format("Tổng cộng: %.0f $", finalTotalAmount));
        int userId = getSharedPreferences("USER_PREFS", MODE_PRIVATE)
                .getInt("USER_ID", -1);
        new Thread(() -> {
            currentUser = DatabaseClient.getInstance(this).getAppDatabase()
                    .userDAO().getUserByid(userId);
            if (currentUser != null) {
                runOnUiThread(() -> {
                    etCustomerName.setText(currentUser.getName());
                    etEmail.setText(currentUser.getEmail());
                    etAddress.setText(currentUser.getAddress());
                    etPhoneNumber.setText(currentUser.getPhone());
                });
            }
        }).start();

        btnCheckout.setOnClickListener(v -> checkoutOrder());
    }

    private void checkoutOrder() {
        // Kiểm tra null trước khi truy cập `EditText`
        if (etCustomerName == null || etPhoneNumber == null || etAddress == null || etEmail == null) {
            Toast.makeText(this, "Có lỗi xảy ra. Vui lòng thử lại!", Toast.LENGTH_SHORT).show();
            return;
        }

        String customerName = etCustomerName.getText().toString().trim();
        String phoneNumber = etPhoneNumber.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        // Always use the email from the logged in profile
        String email = currentUser != null ? currentUser.getEmail() : "";
        // Kiểm tra dữ liệu nhập vào
        if (customerName.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập tên khách hàng!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (phoneNumber.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập số điện thoại!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (address.isEmpty() && currentUser != null) {
            address = currentUser.getAddress();
        }

        saveOrderToDatabase(customerName, phoneNumber, email, address, totalAmount + SHIPPING_FEE);
        String finalEmail = email;
        String finalAddress = address;
        new Thread(() -> {
            try {
                if (selectedItems != null) {
                    for (CartItemDTO item : selectedItems) {
                        DatabaseClient.getInstance(this).getAppDatabase()
                                .cartItemDAO()
                                .deleteByCartIdAndProductId(item.getCartId(), item.getProductId());

                        // Giảm số lượng tồn kho của sản phẩm
                        Product product = DatabaseClient.getInstance(this).getAppDatabase()
                                .productDAO().getProductById(item.getProductId());
                        if (product != null) {
                            int newStock = product.getStock_quantity() - item.getQuantity();
                            if (newStock < 0) newStock = 0;
                            product.setStock_quantity(newStock);
                            DatabaseClient.getInstance(this).getAppDatabase()
                                    .productDAO().update(product);
                        }
                    }                }

                runOnUiThread(() -> {
                    Toast.makeText(this, "Thanh toán thành công!", Toast.LENGTH_SHORT).show();

                    // Chuyển sang màn hình kết quả
                    handleCreateOrder(customerName, phoneNumber, finalEmail, finalAddress);
                });
            } catch (Exception e) {
                runOnUiThread(() ->
                        Toast.makeText(this, "Có lỗi xảy ra khi xử lý đơn hàng!", Toast.LENGTH_SHORT).show()
                );
            }
        }).start();
    }

    private void handleCreateOrder(String customerName, String phoneNumber, String email, String address) {        // Tạo đơn hàng thành công
        Toast.makeText(this, "Đơn hàng đã được tạo!", Toast.LENGTH_SHORT).show();

        // Chuyển dữ liệu sang CheckoutActivity
        Intent intent = new Intent(this, CheckoutActivityCustomer.class);
        intent.putExtra("customerName", customerName);
        intent.putExtra("phoneNumber", phoneNumber);
        intent.putExtra("address", address);
        intent.putExtra("email", email);
        intent.putExtra("totalAmount", totalAmount);
        startActivity(intent);

        //Đóng `CreateOrderActivity`
        finish();
    }

    private void saveOrderToDatabase(String customerName, String phoneNumber, String email, String address, double totalAmount) {
        new Thread(() -> {
            try {
                Order order = new Order();
                int userId = getSharedPreferences("USER_PREFS", MODE_PRIVATE)
                        .getInt("USER_ID", -1);
                order.setCustomer_name(customerName);
                order.setCustomer_email(email);
                order.setCustomer_phone(phoneNumber);
                order.setCustomer_address(address);
                order.setCustomer_id(userId);                order.setOrder_date(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date()));
                order.setTotal_amount((int) totalAmount);
                order.setStatus("Pending"); // Đặt trạng thái mặc định là "Pending"

                //  Lưu vào cơ sở dữ liệu
                long orderId = DatabaseClient.getInstance(this).getAppDatabase()
                        .orderDAO().insertOrder(order);

                if (orderId > 0) {
                    // insert order details for each selected item
                    if (selectedItems != null) {
                        for (CartItemDTO item : selectedItems) {
                            OrderDetails details = new OrderDetails();
                            details.setOrder_id((int) orderId);
                            details.setProduct_id(item.getProductId());
                            details.setQuantity(item.getQuantity());
                            DatabaseClient.getInstance(this).getAppDatabase()
                                    .orderDetailsDAO().insert(details);
                        }
                    }
                    runOnUiThread(() -> Toast.makeText(this, "Đơn hàng đã được lưu!", Toast.LENGTH_SHORT).show());
                } else {
                    runOnUiThread(() -> Toast.makeText(this, "Lưu đơn hàng thất bại!", Toast.LENGTH_SHORT).show());
                }
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(this, "Lỗi khi lưu đơn hàng!", Toast.LENGTH_SHORT).show());
            }
        }).start();
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
