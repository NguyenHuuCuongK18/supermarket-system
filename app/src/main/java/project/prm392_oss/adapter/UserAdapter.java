package project.prm392_oss.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import project.prm392_oss.R;
import project.prm392_oss.activity.EmployeeDetailActivity;
import project.prm392_oss.activity.CustomerDetailActivity;
import project.prm392_oss.activity.ListUsersActivity;
import project.prm392_oss.entity.User;
import project.prm392_oss.viewModel.UserViewModel;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_HEADER = 0;
    private static final int VIEW_TYPE_ITEM = 1;
    private final List<User> userList;
    private final Context context;  // Context để sử dụng startActivity trong adapter
    private final UserViewModel userViewModel;  // ViewModel cho user

    public UserAdapter(List<User> userList, Context context, UserViewModel userViewModel) {
        this.userList = userList;
        this.context = context;
        this.userViewModel = userViewModel;  // Gán userViewModel
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? VIEW_TYPE_HEADER : VIEW_TYPE_ITEM; // First item is the header
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_HEADER) {
            View headerView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.activity_header_list_users, parent, false);
            return new HeaderViewHolder(headerView);
        } else {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.activity_item_list_users, parent, false);
            return new UserViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof UserViewHolder) {
            User user = userList.get(position - 1); // Subtract 1 for the header item
            ((UserViewHolder) holder).bind(user);
        }
    }

    @Override
    public int getItemCount() {
        return userList.size() + 1; // Adding 1 for the header
    }
    public void updateData(List<User> newUsers) {
        userList.clear();
        userList.addAll(newUsers);
        notifyDataSetChanged();
    }
    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        public HeaderViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {

        TextView userIdTextView, userNameTextView, userRoleTextView, userPhoneTextView;
        Button viewButton;

        public UserViewHolder(View itemView) {
            super(itemView);
            userIdTextView = itemView.findViewById(R.id.userIdTextView);
            userNameTextView = itemView.findViewById(R.id.userNameTextView);
            userRoleTextView = itemView.findViewById(R.id.userRoleTextView);
            userPhoneTextView = itemView.findViewById(R.id.userPhoneTextView);
            viewButton = itemView.findViewById(R.id.viewButton);
        }

        public void bind(User user) {
            userIdTextView.setText(String.valueOf(user.getUser_id()));
            userNameTextView.setText(user.getName());
            userPhoneTextView.setText(user.getPhone());

            userViewModel.getRoleName(user.getRole_id()).observe((ListUsersActivity) context, roleName -> {
                userRoleTextView.setText(roleName);
            });

            viewButton.setOnClickListener(v -> {
                Class<?> dest = (user.getRole_id() == 3)
                        ? CustomerDetailActivity.class
                        : EmployeeDetailActivity.class;
                Intent intent = new Intent(context, dest);
                intent.putExtra("userId", user.getUser_id());
                context.startActivity(intent);
            });
        }
    }
}
