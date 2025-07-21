package project.prm392_oss.entity;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "CATEGORY")
public class Category {

    @PrimaryKey(autoGenerate = true)
    private int category_id;

    private String name;
    public Category() {
    }
    public Category(int category_id, String name) {
        this.category_id = category_id;
        this.name = name;
    }

    public int getCategory_id() {
        return category_id;
    }
    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}