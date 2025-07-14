package project.prm392_oss.database;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import project.prm392_oss.dao.*;
import project.prm392_oss.entity.*;

@Database(entities = {Category.class, Feedback.class, Order.class, OrderDetails.class,
        Product.class, Role.class, Supplier.class, User.class, Cart.class, CartItem.class},
        version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    // Khai báo DAO
    public abstract ProductDAO productDAO();
    public abstract SupplierDAO supplierDAO();
    public abstract CategoryDAO categoryDAO();
    public abstract FeedbackDAO feedbackDAO();
    public abstract OrderDAO orderDAO();
    public abstract OrderDetailsDAO orderDetailsDAO();
    public abstract RoleDAO roleDAO();
    public abstract UserDAO userDAO();

    public abstract CartDAO cartDAO();
    public abstract CartItemDAO cartItemDAO();

    // Singleton Database
    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "app_database_1")
                            .fallbackToDestructiveMigration()
                            .addCallback(new RoomDatabase.Callback() {
                                @Override
                                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                    super.onCreate(db);
                                    Log.d("Database", "Database created");
                                }
                            })
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
