package project.prm392_oss.entity;

import static androidx.room.ForeignKey.CASCADE;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "PRODUCT",
        indices = {@Index(value = "category_id"), @Index(value = "supplier_id")}, // Thêm index vào khóa ngoại
        foreignKeys = {
                @ForeignKey(entity = Category.class,
                        parentColumns = "category_id",
                        childColumns = "category_id",
                        onDelete = CASCADE),
                @ForeignKey(entity = Supplier.class,
                        parentColumns = "supplier_id",
                        childColumns = "supplier_id",
                        onDelete = CASCADE)
        })
public class Product implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public int product_id;
    public String name;
    public String description;
    public int import_price;
    public int sale_price;
    public int stock_quantity;
    public int category_id;
    public int supplier_id;
    public String imageUrl;


    public Product() {
    }

    public Product( String name, String description, int import_price, int sale_price, int stock_quantity, int category_id, int supplier_id, String imageUrl) {
        this.name = name;
        this.description = description;
        this.import_price = import_price;
        this.sale_price = sale_price;
        this.stock_quantity = stock_quantity;
        this.category_id = category_id;
        this.supplier_id = supplier_id;
        this.imageUrl = imageUrl;
    }



    public Product(int product_id, String name, String description, int import_price, int sale_price, int stock_quantity, int category_id, int supplier_id, String imageUrl) {
        this.product_id = product_id;
        this.name = name;
        this.description = description;
        this.import_price = import_price;
        this.sale_price = sale_price;
        this.stock_quantity = stock_quantity;
        this.category_id = category_id;
        this.supplier_id = supplier_id;
        this.imageUrl = imageUrl;
    }



    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getImport_price() {
        return import_price;
    }

    public void setImport_price(int import_price) {
        this.import_price = import_price;
    }

    public int getSale_price() {
        return sale_price;
    }

    public void setSale_price(int sale_price) {
        this.sale_price = sale_price;
    }

    public int getStock_quantity() {
        return stock_quantity;
    }

    public void setStock_quantity(int stock_quantity) {
        this.stock_quantity = stock_quantity;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public int getSupplier_id() {
        return supplier_id;
    }

    public void setSupplier_id(int supplier_id) {
        this.supplier_id = supplier_id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}

