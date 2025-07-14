package project.prm392_oss.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "SUPPLIER")
public class Supplier {
    @PrimaryKey(autoGenerate = true)
    public int supplier_id;

    public String name;
    public String phone;
    public String address;


    public Supplier(String name, String phone, String address) {
        this.name = name;
        this.phone = phone;
        this.address = address;
    }
    public int getSupplier_id() {
        return supplier_id;
    }

    public void setSupplier_id(int supplier_id) {
        this.supplier_id = supplier_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
