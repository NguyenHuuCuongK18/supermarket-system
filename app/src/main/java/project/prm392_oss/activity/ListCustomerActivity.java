package project.prm392_oss.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import project.prm392_oss.activity.BaseActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import project.prm392_oss.R;
import project.prm392_oss.entity.User;
import project.prm392_oss.viewModel.UserViewModel;
import project.prm392_oss.activity.ListUsersActivity;

import java.util.ArrayList;
import java.util.List;

public class ListCustomerActivity extends BaseActivity {

    private UserViewModel userViewModel;
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;

    private static final int VIEW_TYPE_HEADER = 0;
    private static final int VIEW_TYPE_ITEM = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_customer);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);  // Hiển thị nút quay lại
        getSupportActionBar().setDisplayShowHomeEnabled(true);  // Hiển thị biểu tượng home

        recyclerView = findViewById(R.id.recyclerViewCustomer);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.getCustomers().observe(this, users -> {
            List<User> customerList = filterCustomers(users);
            if (customerList != null && !customerList.isEmpty()) {
                userAdapter = new UserAdapter(customerList);
                recyclerView.setAdapter(userAdapter);
            } else {
                Toast.makeText(this, "No customers found", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (item.getItemId() == R.id.nav_user_management) {
            startActivity(new Intent(ListCustomerActivity.this, ListUsersActivity.class));
            return true;
        } else if (item.getItemId() == R.id.nav_product_management) {
            startActivity(new Intent(ListCustomerActivity.this, ListProductActivity.class));
            return true;
        } else if (item.getItemId() == R.id.nav_order_management) {
            startActivity(new Intent(ListCustomerActivity.this, ListOrdersActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private List<User> filterCustomers(List<User> users) {
        if (users == null) return null;
        List<User> filtered = new ArrayList<>();
        for (User user : users) {
            if (user.getRole_id() == 3) {
                filtered.add(user);
            }
        }
        return filtered;
    }

    public class UserAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private final List<User> customerList;

        public UserAdapter(List<User> customerList) {
            this.customerList = customerList;
        }

        @Override
        public int getItemViewType(int position) {
            return (position == 0) ? VIEW_TYPE_HEADER : VIEW_TYPE_ITEM;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if (viewType == VIEW_TYPE_HEADER) {
                View headerView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.activity_header_list_customer, parent, false);
                return new HeaderViewHolder(headerView);
            } else {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.activity_item_list_customer, parent, false);
                return new UserViewHolder(itemView);
            }
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof UserViewHolder) {
                User user = customerList.get(position - 1);
                ((UserViewHolder) holder).bind(user, position);
            }
        }

        @Override
        public int getItemCount() {
            return customerList.size() + 1;
        }

        public class HeaderViewHolder extends RecyclerView.ViewHolder {
            public HeaderViewHolder(@NonNull View itemView) {
                super(itemView);
            }
        }

        public class UserViewHolder extends RecyclerView.ViewHolder {

            TextView userIdTextView, userNameTextView, userPhoneTextView;
            Button ordersButton, viewButton;

            public UserViewHolder(@NonNull View itemView) {
                super(itemView);
                userIdTextView = itemView.findViewById(R.id.userIdTextView);
                userNameTextView = itemView.findViewById(R.id.userNameTextView);
                userPhoneTextView = itemView.findViewById(R.id.userPhoneTextView);
                ordersButton = itemView.findViewById(R.id.ordersButton);
                viewButton = itemView.findViewById(R.id.viewButton);
            }

            public void bind(User user, int position) {
                userIdTextView.setText(String.valueOf(position));
                userNameTextView.setText(user.getName());
                userPhoneTextView.setText(user.getPhone());

                ordersButton.setOnClickListener(v -> {
                    Intent intent = new Intent(ListCustomerActivity.this, CustomerDetailActivity.class);
                    intent.putExtra("userId", user.getUser_id());
                    startActivity(intent);
                });

                viewButton.setOnClickListener(v -> {
                    Intent intent = new Intent(ListCustomerActivity.this, CustomerDetailActivity.class);
                    intent.putExtra("userId", user.getUser_id());
                    startActivity(intent);
                });
            }
        }
    }
}
